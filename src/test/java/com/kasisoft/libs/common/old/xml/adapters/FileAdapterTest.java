package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( File value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
