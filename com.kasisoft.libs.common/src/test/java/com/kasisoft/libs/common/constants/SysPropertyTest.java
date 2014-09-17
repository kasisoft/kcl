package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the enumeration 'SysProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SysPropertyTest {

  private Properties   systemproperties;
  private Properties   noproperties;
  
  private File         toolsjar;
  private File         endorseddir;
  private File         extdir;
  private File         root;
  private File         sysdir;
  private File         tempdir;
  private File         userdir;
  private File         homedir;
    
  
  @BeforeTest
  public void setup() throws Exception {

    toolsjar    = new File( "/tools.jar" ) . getCanonicalFile();
    endorseddir = new File( "/endorsed"  ) . getCanonicalFile();
    extdir      = new File( "/ext"       ) . getCanonicalFile();
    root        = new File( "/"          ) . getCanonicalFile();
    sysdir      = new File( "/WINDOWS"   ) . getCanonicalFile();
    tempdir     = new File( "/tmp"       ) . getCanonicalFile();
    userdir     = new File( "/kasimir"   ) . getCanonicalFile();
    homedir     = new File( "/kasimir2"  ) . getCanonicalFile();

    noproperties      = new Properties();
    systemproperties  = new Properties();
    systemproperties.setProperty( "java.class.path"             , toolsjar    . getAbsolutePath() );
    systemproperties.setProperty( "java.endorsed.dirs"          , endorseddir . getAbsolutePath() );
    systemproperties.setProperty( "java.ext.dirs"               , extdir      . getAbsolutePath() );
    systemproperties.setProperty( "java.home"                   , root        . getAbsolutePath() );
    systemproperties.setProperty( "java.library.path"           , sysdir      . getAbsolutePath() );
    systemproperties.setProperty( "java.io.tmpdir"              , tempdir     . getAbsolutePath() );
    systemproperties.setProperty( "user.dir"                    , userdir     . getAbsolutePath() );
    systemproperties.setProperty( "user.home"                   , homedir     . getAbsolutePath() );
    systemproperties.setProperty( "java.class.version"          , "49"              );
    systemproperties.setProperty( "java.version"                , "1.4"             );
    systemproperties.setProperty( "os.name"                     , "Windows 7"       );
    systemproperties.setProperty( "line.separator"              , "\r\n"            );
    systemproperties.setProperty( "java.runtime.version"        , "1.7.0_b17ea"     );
    systemproperties.setProperty( "java.specification.version"  , "1.7"             );
    
  }
  
  @DataProvider(name="createValues")
  public Object[][] createValues() throws Exception {
    Object[][]       result = new Object[][] {
      { SysProperty.ClassPath             , asList( toolsjar    )           },
      { SysProperty.Path                  , asList( sysdir      )           },
      { SysProperty.EndorsedDirs          , asList( endorseddir )           }, 
      { SysProperty.ExtDirs               , asList( extdir      )           }, 
      { SysProperty.RuntimeVersion        , new Version( 1, 7, 0, "b17ea" ) },
      { SysProperty.SpecificationVersion  , new Version( 1, 7 )             },
      { SysProperty.JavaVersion           , new Version( 1, 4 )             }, 
      { SysProperty.JavaHome              , root                            }, 
      { SysProperty.TempDir               , tempdir                         },
      { SysProperty.UserDir               , userdir                         },
      { SysProperty.UserHome              , homedir                         },
      { SysProperty.ClassVersion          , Double.valueOf(49)              },
      { SysProperty.OsName                , "Windows 7"                     }, 
      { SysProperty.LineSeparator         , "\r\n"                          }, 
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
