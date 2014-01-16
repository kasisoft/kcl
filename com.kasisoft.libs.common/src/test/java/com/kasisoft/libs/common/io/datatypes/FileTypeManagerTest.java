/**
 * Name........: FileTypeManagerTest
 * Description.: Tests for the class 'FileTypeManager'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.io.*;

import org.testng.annotations.*;

import org.testng.*;

import java.net.*;

/**
 * Tests for the class 'FileTypeManager'.
 */
public class FileTypeManagerTest {

  private FileTypeManager   manager;
  
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
    Assert.assertNotNull( url );
    
    byte[] fullcontent = IoFunctions.loadBytes( url, null );
    Assert.assertNotNull( fullcontent );
    
    FileType filetype = manager.identify( fullcontent );
    if( mime == null ) {
      Assert.assertNull( filetype );
    } else {
      Assert.assertNotNull( filetype );
      Assert.assertEquals( filetype.getMimeType(), mime );
    }
    
  }
  
} /* ENDCLASS */
