/**
 * Name........: FileAdapterTest
 * Description.: Tests for the type 'FileAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Tests for the type 'FileAdapter'.
 */
public class FileAdapterTest {

  private FileAdapter adapter = new FileAdapter();

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null                 , null                            },
      { "testdata\\http.xsd" , new File( "testdata/http.xsd" ) },
      { "testdata/http.xsd"  , new File( "testdata/http.xsd" ) },
      { "testdata\\bibo.txt" , new File( "testdata/bibo.txt" ) },
      { "testdata/bibo.txt"  , new File( "testdata/bibo.txt" ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                            , null               },
      { new File( "testdata/http.xsd" ) , "testdata/http.xsd" },
      { new File( "testdata/bibo.txt" ) , "testdata/bibo.txt" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, File expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( File value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
