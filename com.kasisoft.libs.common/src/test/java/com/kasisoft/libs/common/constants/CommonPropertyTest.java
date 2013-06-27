/**
 * Name........: CommonPropertyTest
 * Description.: Tests for the enumeration 'CommonProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the enumeration 'CommonProperty'.
 */
@Test(groups="all")
public class CommonPropertyTest {

  private Properties   properties;
  private Properties   noproperties;
  
  @BeforeSuite
  public void setup() {
    noproperties  = new Properties();
    properties    = new Properties();
    properties.setProperty( "com.kasisoft.libs.common#IORETRIES"   , "20"       );
    properties.setProperty( "com.kasisoft.libs.common#BUFFERCOUNT" , "8192"     );
    properties.setProperty( "com.kasisoft.libs.common#TEMPDIR"     , "D:/temp"  );
  }
  
  @DataProvider(name="createProperties")
  public Object[][] createProperties() {
    Object[][] result = new Object[][] {
      { CommonProperty.Application },
      { CommonProperty.BufferCount },
      { CommonProperty.IoRetries   },
      { CommonProperty.TempDir     }
    };
    return result;
  }

  @DataProvider(name="createInvalidProperties")
  public Object[][] createInvalidProperties() {
    Object[][] result = new Object[][] {
      { CommonProperty.BufferCount },
      { CommonProperty.IoRetries   },
      { CommonProperty.TempDir     }
    };
    return result;
  }

  @Test
  public void checkMissingProperties() {
    Integer ioretries   = CommonProperty.IoRetries.getValue( noproperties );
    Assert.assertEquals( ioretries, Integer.valueOf(5) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( noproperties );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( noproperties );
    Assert.assertEquals( tempdir, new File( SystemProperty.TempDir.getValue() ) );
  }

  @Test
  public void checkAvailableProperties() {
    Integer ioretries   = CommonProperty.IoRetries.getValue( properties );
    Assert.assertEquals( ioretries, Integer.valueOf(20) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( properties );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( properties );
    Assert.assertEquals( tempdir, new File( "D:/temp".replace( '/', File.separatorChar ) ) );
  }

  @Test(dataProvider="createInvalidProperties", expectedExceptions={ClassCastException.class})
  public void invalidUsedProperties( TypedProperty<Float> property ) {
    @SuppressWarnings("unused")
    Float floatvalue = property.getValue();
    Assert.fail( "This part should never be executed." );
  }

} /* ENDCLASS */
