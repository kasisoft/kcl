/**
 * Name........: SysPropertyTest
 * Description.: Tests for the enumeration 'SysProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the enumeration 'SysProperty'.
 */
public class SysPropertyTest {

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
    systemproperties.setProperty( "java.home"                   , "C:/"             );
    systemproperties.setProperty( "java.version"                , "1.4"             );
    systemproperties.setProperty( "os.name"                     , "Windows 7"       );
    systemproperties.setProperty( "line.separator"              , "\r\n"            );
    systemproperties.setProperty( "java.library.path"           , "C:/WINDOWS"      );
    systemproperties.setProperty( "java.runtime.version"        , "1.7.0_b17ea"     );
    systemproperties.setProperty( "java.specification.version"  , "1.7"             );
    systemproperties.setProperty( "java.io.tmpdir"              , "C:/tmp"          );
    systemproperties.setProperty( "user.dir"                    , "C:/kasimir"      );
    systemproperties.setProperty( "user.home"                   , "C:/kasimir2"     );
  }
  
  @DataProvider(name="createValues")
  public Object[][] createValues() throws Exception {
    Object[][]       result = new Object[][] {
      { SysProperty.ClassPath             , asList( new File( "C:/tools.jar" ) ) },
      { SysProperty.Path                  , asList( new File( "C:/WINDOWS"   ) ) },
      { SysProperty.EndorsedDirs          , asList( new File( "C:/endorsed"  ) ) }, 
      { SysProperty.ExtDirs               , asList( new File( "C:/ext"       ) ) }, 
      { SysProperty.RuntimeVersion        , new Version( 1, 7, 0, "b17ea" )      },
      { SysProperty.SpecificationVersion  , new Version( 1, 7 )                  },
      { SysProperty.JavaVersion           , new Version( 1, 4 )                  }, 
      { SysProperty.JavaHome              , new File( "C:/"         )            }, 
      { SysProperty.TempDir               , new File( "C:/tmp"      )            },
      { SysProperty.UserDir               , new File( "C:/kasimir"  )            },
      { SysProperty.UserHome              , new File( "C:/kasimir2" )            },
      { SysProperty.ClassVersion          , Double.valueOf(49)                   },
      { SysProperty.OsName                , "Windows 7"                          }, 
      { SysProperty.LineSeparator         , "\r\n"                               }, 
    };
    return result;
  }
  
  private <T> List<T> asList( T ... elements ) {
    List<T> result = new ArrayList<T>();
    for( int i = 0; i < elements.length; i++ ) {
      result.add( elements[i] );
    }
    return result;
  }
  
  @Test(dataProvider="createValues", groups="all")
  public void checkAvailableValues( SimpleProperty property, Object expected ) {
    Assert.assertEquals( property.getValue( systemproperties ), expected );
  }

  @Test(dataProvider="createValues", groups="all")
  public void checkMissingValues( SimpleProperty property, Object ignored ) {
    Object result = property.getValue( noproperties );
    Assert.assertNull( result );
  }

} /* ENDCLASS */
