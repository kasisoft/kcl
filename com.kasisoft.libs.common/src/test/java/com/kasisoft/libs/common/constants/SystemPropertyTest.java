/**
 * Name........: SystemPropertyTest
 * Description.: Tests for the enumeration 'SystemProperty'.
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
 * Tests for the enumeration 'SystemProperty'.
 */
@Test(groups="all")
public class SystemPropertyTest {

  private Properties   systemproperties;
  private Properties   noproperties;
  
  @BeforeTest
  public void setup() {
    noproperties      = new Properties();
    systemproperties  = new Properties();
    systemproperties.setProperty( "java.class.path"             , "C:/tools.jar"    );
    systemproperties.setProperty( "java.class.version"          , "49"              );
    systemproperties.setProperty( "java.endorsed.dirs"          , "C:/endorsed"     );
    systemproperties.setProperty( "java.ext.dirs"               , "C:/ext"          );
    systemproperties.setProperty( "file.encoding"               , "UTF-8"           );
    systemproperties.setProperty( "file.separator"              , "/"               );
    systemproperties.setProperty( "java.home"                   , "/"               );
    systemproperties.setProperty( "java.version"                , "1.4"             );
    systemproperties.setProperty( "line.separator"              , "\r\n"            );
    systemproperties.setProperty( "os.name"                     , "Windows 7"       );
    systemproperties.setProperty( "java.library.path"           , "C:/WINDOWS"      );
    systemproperties.setProperty( "java.runtime.version"        , "2.0"             );
    systemproperties.setProperty( "java.specification.version"  , "2"               );
    systemproperties.setProperty( "java.io.tmpdir"              , "C:/tmp"          );
    systemproperties.setProperty( "user.dir"                    , "C:/kasimir"      );
    systemproperties.setProperty( "user.home"                   , "C:/kasimir2"     );
  }
  
  @DataProvider(name="createValues")
  public Object[][] createValues() {
    SystemProperty[] values = SystemProperty.values();
    Object[][]       result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i] };
    }
    return result;
  }
  
  @Test(dataProvider="createValues")
  public void checkAvailableValues( SystemProperty property ) {
    String result   = property.getValue( systemproperties, false );
    Assert.assertNotNull( result );
    String expected = systemproperties.getProperty( property.getKey() ).replace( '\\', '/' ).replace( '/', File.separatorChar );
    Assert.assertEquals( property.getValue( systemproperties ), expected );
  }

  @Test(dataProvider="createValues")
  public void checkMissingValues( SystemProperty property ) {
    String result = property.getValue( noproperties, false );
    Assert.assertNull( result );
  }

} /* ENDCLASS */
