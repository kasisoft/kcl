/**
 * Name........: PathAdapterTest
 * Description.: Tests for the type 'PathAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the type 'PathAdapter'.
 */
public class PathAdapterTest {

  private PathAdapter adapter = new PathAdapter( ";", false );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null                 , null                            },
      { "testdata\\http.xsd" , asList( new File( "testdata/http.xsd" ) ) },
      { "testdata/http.xsd"  , asList( new File( "testdata/http.xsd" ) ) },
      { "testdata\\bibo.txt" , asList( new File( "testdata/bibo.txt" ) ) },
      { "testdata/bibo.txt"  , asList( new File( "testdata/bibo.txt" ) ) },
      { 
        "testdata\\http.xsd;testdata/http.xsd;testdata\\bibo.txt;testdata/bibo.txt", 
        asList( 
          new File( "testdata/http.xsd" ),
          new File( "testdata/http.xsd" ),
          new File( "testdata/bibo.txt" ), 
          new File( "testdata/bibo.txt" )
        ) 
      },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                       , null               },
      { asList( new File( "testdata/http.xsd"  ) ) , "testdata/http.xsd" },
      { asList( new File( "testdata\\bibo.txt" ) ) , "testdata/bibo.txt" },
      { asList( new File( "testdata\\bibo.txt" ),  new File( "testdata/http.xsd" ) ), "testdata/bibo.txt;testdata/http.xsd" },
    };
  }
  
  private List<File> asList( File ... files ) {
    ArrayList<File> result = new ArrayList<File>();
    if( files != null ) {
      for( int i = 0; i < files.length; i++ ) {
        result.add( files[i] );
      }
    }
    return result;
  }
  

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, List<File> expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( List<File> value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
