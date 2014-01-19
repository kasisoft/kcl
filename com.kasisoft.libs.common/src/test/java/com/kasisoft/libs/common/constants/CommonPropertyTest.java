/**
 * Name........: CommonPropertyTest
 * Description.: Tests for the enumeration 'CommonProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the enumeration 'CommonProperty'.
 */
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
      { CommonProperty.BufferCount },
      { CommonProperty.IoRetries   },
      { CommonProperty.TempDir     }
    };
    return result;
  }

  @Test(groups="all")
  public void checkMissingProperties() {
    Integer ioretries   = CommonProperty.IoRetries.getValue( noproperties );
    Assert.assertEquals( ioretries, Integer.valueOf(5) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( noproperties );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( noproperties );
    Assert.assertEquals( tempdir, SysProperty.TempDir.getValue( System.getProperties() ) );
  }

  @Test(groups="all")
  public void checkAvailableProperties() {
    Integer ioretries   = CommonProperty.IoRetries.getValue( properties );
    Assert.assertEquals( ioretries, Integer.valueOf(20) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( properties );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( properties );
    Assert.assertEquals( tempdir, new File( "D:/temp".replace( '/', File.separatorChar ) ) );
  }

  @Test(dataProvider="createProperties", expectedExceptions={ClassCastException.class}, groups="all")
  public void invalidUsedProperties( SimpleProperty<Float> property ) {
    @SuppressWarnings("unused")
    Float floatvalue = property.getValue( System.getProperties() );
    Assert.fail( "This part should never be executed." );
  }

} /* ENDCLASS */
