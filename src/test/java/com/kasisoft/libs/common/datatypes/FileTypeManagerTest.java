package com.kasisoft.libs.common.datatypes;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Files;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'FileTypeManager'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileTypeManagerTest extends AbstractTestCase {

  FileTypeManager   manager;
  
  @BeforeClass
  public void setup() {
    manager = new FileTypeManager();
  }
  
  @DataProvider(name = "identifyData")
  public Object[][] identifyData() {
    return new Object[][] {
      {"file0", "application/gzip"           },
      {"file1", "application/pdf"            },
      {"file2", "image/png"                  },
      {"file3", "image/bmp"                  },
      {"file4", "image/gif"                  },
      {"file5", "image/jpeg"                 },
      {"file6", "application/x-bzip"         },
      {"file7", "application/zip"            },
      {"file8", "application/x-7z-compressed"},
      {"file9", "application/java-vm"        }
    };
  }
  
  @Test(dataProvider = "identifyData", groups = "all")
  public void identify(String resource, String mime) throws Exception {
    
    var file     = getResource(resource);
    var content  = Files.readAllBytes(file);
    assertNotNull(content);
    
    var filetype = manager.identify(content);
    if (mime == null) {
      assertNull(filetype);
    } else {
      assertNotNull(filetype);
      assertThat(filetype.getContentType().getMimeType(), is(mime));
    }
    
  }
  
} /* ENDCLASS */
