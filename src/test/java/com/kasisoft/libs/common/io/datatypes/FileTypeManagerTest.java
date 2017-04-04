package com.kasisoft.libs.common.io.datatypes;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import java.net.*;

/**
 * Tests for the class 'FileTypeManager'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileTypeManagerTest {

  FileTypeManager   manager;
  
  @BeforeClass
  public void setup() {
    manager = new FileTypeManager();
  }
  
  @DataProvider(name="identifyData")
  public Object[][] identifyData() {
    return new Object[][] {
      { "/files/file0", "application/gzip"                },
      { "/files/file1", "application/pdf"                 },
      { "/files/file2", "image/png"                       },
      { "/files/file3", "image/bmp"                       },
      { "/files/file4", "image/gif"                       },
      { "/files/file5", "image/jpeg"                      },
      { "/files/file6", "application/x-bzip"              },
      { "/files/file7", "application/zip"                 },
      { "/files/file8", "application/x-7z-compressed"     },
    };
  }
  
  @Test(dataProvider="identifyData", groups="all")
  public void identify( String resource, String mime ) {
    
    URL    url         = getClass().getResource( resource );
    assertThat( url, is( notNullValue() ) );
    
    byte[] fullcontent = IoFunctions.loadBytes( url, null );
    assertThat( fullcontent, is( notNullValue() ) );
    
    FileType filetype = manager.identify( fullcontent );
    if( mime == null ) {
      assertThat( filetype, is( nullValue()) );
    } else {
      assertThat( filetype, is( notNullValue() ) );
      assertThat( filetype.getMimeType(), is( mime ) );
    }
    
  }
  
} /* ENDCLASS */
