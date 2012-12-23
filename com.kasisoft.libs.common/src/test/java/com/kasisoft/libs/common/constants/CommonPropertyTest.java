/**
 * Name........: CommonPropertyTest
 * Description.: Tests for the enumeration 'CommonProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

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
    properties.setProperty( "com.kasisoft.libs.common#DEBUG"       , "true"     );
    properties.setProperty( "com.kasisoft.libs.common#IORETRIES"   , "20"       );
    properties.setProperty( "com.kasisoft.libs.common#SLEEP"       , "1000"     );
    properties.setProperty( "com.kasisoft.libs.common#BUFFERCOUNT" , "8192"     );
    properties.setProperty( "com.kasisoft.libs.common#TEMPDIR"     , "D:/temp"  );
  }
  
  @DataProvider(name="createProperties")
  public Object[][] createProperties() {
    CommonProperty[] properties = CommonProperty.values();
    Object[][]       result     = new Object[ properties.length ][];
    for( int i = 0; i < properties.length; i++ ) {
      result[i] = new Object[] { properties[i] };
    }
    return result;
  }
  
  @Test
  public void checkMissingProperties() {
    Boolean debug       = CommonProperty.Debug.getValue( noproperties, false );
    Assert.assertEquals( debug, Boolean.FALSE );
    Integer ioretries   = CommonProperty.IoRetries.getValue( noproperties, false );
    Assert.assertEquals( ioretries, Integer.valueOf(5) );
    Integer sleep       = CommonProperty.Sleep.getValue( noproperties, false );
    Assert.assertEquals( sleep, Integer.valueOf(100) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( noproperties, false );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( noproperties, false );
    Assert.assertEquals( tempdir, new File( SystemProperty.TempDir.getValue() ) );
  }

  @Test
  public void checkAvailableProperties() {
    Boolean debug       = CommonProperty.Debug.getValue( properties, false );
    Assert.assertEquals( debug, Boolean.TRUE );
    Integer ioretries   = CommonProperty.IoRetries.getValue( properties, false );
    Assert.assertEquals( ioretries, Integer.valueOf(20) );
    Integer sleep       = CommonProperty.Sleep.getValue( properties, false );
    Assert.assertEquals( sleep, Integer.valueOf(1000) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( properties, false );
    Assert.assertEquals( buffersize, Integer.valueOf(8192) );
    File    tempdir     = CommonProperty.TempDir.getValue( properties, false );
    Assert.assertEquals( tempdir, new File( "D:/temp".replace( '/', File.separatorChar ) ) );
  }

  @Test(dataProvider="createProperties", expectedExceptions={ClassCastException.class})
  public void invalidUsedProperties( CommonProperty property ) {
    @SuppressWarnings("unused")
    Float floatvalue = property.getValue();
    Assert.fail( "This part should never be executed." );
  }

} /* ENDCLASS */
