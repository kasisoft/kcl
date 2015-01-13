package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'FileAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAdapterTest {

  FileAdapter adapter = new FileAdapter();

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    String path = Utilities.getTestdataDir().getAbsolutePath().replace( '\\', '/' );
    return new Object[][] {
      { null                                   , null                                   },
      { String.format( "%s\\http.xsd" , path ) , Utilities.getTestdataDir( "http.xsd" ) },
      { String.format( "%s/http.xsd"  , path ) , Utilities.getTestdataDir( "http.xsd" ) },
      { String.format( "%s\\bibo.txt" , path ) , Utilities.getTestdataDir( "bibo.txt" ) },
      { String.format( "%s/bibo.txt"  , path ) , Utilities.getTestdataDir( "bibo.txt" ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    String path = Utilities.getTestdataDir().getAbsolutePath().replace( '\\', '/' );
    return new Object[][] {
      { null                                   , null                                 },
      { Utilities.getTestdataDir( "http.xsd" ) , String.format( "%s/http.xsd", path ) },
      { Utilities.getTestdataDir( "bibo.txt" ) , String.format( "%s/bibo.txt", path ) },
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
