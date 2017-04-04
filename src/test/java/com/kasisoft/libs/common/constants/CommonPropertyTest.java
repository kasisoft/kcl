package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.config.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the enumeration 'CommonProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonPropertyTest {

  Properties   properties;
  Properties   noproperties;
  
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
    assertThat( ioretries, is( Integer.valueOf(5) ) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( noproperties );
    assertThat( buffersize, is( Integer.valueOf(8192) ) );
    File    tempdir     = CommonProperty.TempDir.getValue( noproperties );
    assertThat( tempdir, is( SysProperty.TempDir.getValue( System.getProperties() ) ) );
  }

  @Test(groups="all")
  public void checkAvailableProperties() {
    Integer ioretries   = CommonProperty.IoRetries.getValue( properties );
    assertThat( ioretries, is( Integer.valueOf(20) ) );
    Integer buffersize  = CommonProperty.BufferCount.getValue( properties );
    assertThat( buffersize, is( Integer.valueOf(8192) ) );
    File    tempdir     = CommonProperty.TempDir.getValue( properties );
    assertThat( tempdir, is( new File( "D:/temp".replace( '/', File.separatorChar ) ) ) );
  }

  @Test(dataProvider="createProperties", expectedExceptions={ClassCastException.class}, groups="all")
  public void invalidUsedProperties( SimpleProperty<Float> property ) {
    @SuppressWarnings("unused")
    Float floatvalue = property.getValue( System.getProperties() );
    fail( "This part should never be executed." );
  }

} /* ENDCLASS */
