package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.old.test.framework.*;

import org.testng.annotations.*;

import java.util.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the type 'PathAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PathAdapterTest {

  PathAdapter adapter = new PathAdapter( ";", false );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    String path = Utilities.getTestdataDir().getAbsolutePath().replace( '\\', '/' );
    return new Object[][] {
      { null                                   , null                                             },
      { String.format( "%s\\http.xsd" , path ) , asList( Utilities.getTestdataDir( "http.xsd" ) ) },
      { String.format( "%s/http.xsd"  , path ) , asList( Utilities.getTestdataDir( "http.xsd" ) ) },
      { String.format( "%s\\bibo.txt" , path ) , asList( Utilities.getTestdataDir( "bibo.txt" ) ) },
      { String.format( "%s/bibo.txt"  , path ) , asList( Utilities.getTestdataDir( "bibo.txt" ) ) },
      { 
        String.format( "%s\\http.xsd;%s/http.xsd;%s\\bibo.txt;%s/bibo.txt", path, path, path, path ), 
        asList( 
          Utilities.getTestdataDir( "http.xsd" ),
          Utilities.getTestdataDir( "http.xsd" ),
          Utilities.getTestdataDir( "bibo.txt" ), 
          Utilities.getTestdataDir( "bibo.txt" )
        ) 
      },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    String path = Utilities.getTestdataDir().getAbsolutePath().replace( '\\', '/' );
    return new Object[][] {
      { null                                       , null               },
      { asList( Utilities.getTestdataDir( "http.xsd" ) ) , path + "/http.xsd" },
      { asList( Utilities.getTestdataDir( "bibo.txt" ) ) , path + "/bibo.txt" },
      { asList( Utilities.getTestdataDir( "bibo.txt" ),  Utilities.getTestdataDir( "http.xsd" ) ), String.format( "%s/bibo.txt;%s/http.xsd", path, path ) },
    };
  }
  
  private List<File> asList( File ... files ) {
    ArrayList<File> result = new ArrayList<>();
    if( files != null ) {
      for( int i = 0; i < files.length; i++ ) {
        result.add( files[i] );
      }
    }
    return result;
  }
  

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, List<File> expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( List<File> value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
