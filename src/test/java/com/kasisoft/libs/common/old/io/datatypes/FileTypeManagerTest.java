package com.kasisoft.libs.common.old.io.datatypes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import com.kasisoft.libs.common.old.io.IoFunctions;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
    String resource = "/" + FileTypeManagerTest.class.getName().replace('.', '/') + ".class"; 
    return new Object[][] {
      { "/old/files/file0", "application/gzip"                },
      { "/old/files/file1", "application/pdf"                 },
      { "/old/files/file2", "image/png"                       },
      { "/old/files/file3", "image/bmp"                       },
      { "/old/files/file4", "image/gif"                       },
      { "/old/files/file5", "image/jpeg"                      },
      { "/old/files/file6", "application/x-bzip"              },
      { "/old/files/file7", "application/zip"                 },
      { "/old/files/file8", "application/x-7z-compressed"     },
      { resource          , "application/java-vm"             }
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
