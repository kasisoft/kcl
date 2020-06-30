package com.kasisoft.libs.common.graphics;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GraphicsFunctionsTest extends AbstractTestCase {
  
  Path          images;
  List<Path>    inputfiles;
  int           idx;
  
  @BeforeSuite
  public void setup() {
    
    idx    = 0;
    images = getResource("images");
    assertTrue(Files.isDirectory(images));
    
    inputfiles = Arrays.asList(images.toFile().list()).stream()
      .map(images::resolve)
      .collect(Collectors.toList())
      ;
    
    assertFalse(inputfiles.isEmpty());
    
  }

  @DataProvider(name = "data_readImage__writeImage")
  public Object[][] data_readImage__writeImage() {
    var formats = PictureFormat.values();
    var result  = new ArrayList<Object[]>();
    for (var inputfile : inputfiles) {
      for (var format : formats) {
        if (format.isRasterFormat()) {
          result.add(new Object[] {inputfile, format});
        }
      }
    }
    return result.toArray(new Object[result.size()][2]);
  }

  @Test(groups = "all", dataProvider = "data_readImage__writeImage")
  public void readImage__writeImage(Path imageFile, PictureFormat outformat) {
    var image    = GraphicsFunctions.readImage(imageFile);
    assertNotNull(image);
    var tempPath = getTempPath(String.format("image_%d.%s", idx++, outformat.getSuffix()));
    GraphicsFunctions.writeImage(tempPath, image, outformat);
    assertTrue(Files.isRegularFile(tempPath));
  }
  
} /* ENDCLASS */
