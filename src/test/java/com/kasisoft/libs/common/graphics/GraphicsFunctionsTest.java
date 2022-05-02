package com.kasisoft.libs.common.graphics;

import static com.kasisoft.libs.common.testsupport.ExtendedAsserts.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.functional.*;

import org.testng.annotations.*;

import javax.swing.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;
import java.util.List;

import java.net.*;

import java.awt.*;
import java.awt.image.*;

import java.nio.file.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GraphicsFunctionsTest extends AbstractTestCase {
  
  private Path          images;
  private List<Path>    inputfiles;
  private int           idx;
  
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

  private Path getTempPath(PictureFormat fmt) {
    return getTempPath(String.format("image_%d.%s", idx++, fmt.getSuffix()));
  }
  
  private <R> Object[][] data_readImage__writeImages(Function<Path, R> toResource) {
    var formats = PictureFormat.values();
    var result  = new ArrayList<Object[]>();
    for (var inputfile : inputfiles) {
      for (var format : formats) {
        if (format.isRasterFormat()) {
          result.add(new Object[] {toResource.apply(inputfile), format});
        }
      }
    }
    return result.toArray(new Object[result.size()][2]);
  }
  
  private <R> void readImage_writeImage(R source, PictureFormat outformat, Function<R, BufferedImage> loader, Function<Path, R> toDest, TriConsumer<R, BufferedImage, PictureFormat> saver) {
    var image = loader.apply(source);
    assertNotNull(image);
    var tempPath = getTempPath(outformat);
    saver.accept(toDest.apply(tempPath), image, outformat);
    assertTrue(Files.isRegularFile(tempPath));
  }

  @DataProvider(name = "data_readImage__writeImage__Path")
  public Object[][] data_readImage__writeImage__Path() {
    return this.data_readImage__writeImages(Function.identity());
  }

  @Test(groups = "all", dataProvider = "data_readImage__writeImage__Path")
  public void readImage__writeImagePath(Path imageFile, PictureFormat outformat) {
    readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, Function.identity(), GraphicsFunctions::writeImage);
  }
  
  @DataProvider(name = "data_readImage__writeImage__File")
  public Object[][] data_readImage__writeImage__File() {
    return this.data_readImage__writeImages($ -> $.toFile());
  }

  @Test(groups = "all", dataProvider = "data_readImage__writeImage__File")
  public void readImage__writeImageFile(File imageFile, PictureFormat outformat) {
    readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, $ -> $.toFile(), GraphicsFunctions::writeImage);
  }

  @DataProvider(name = "data_readImage__writeImage__URI")
  public Object[][] data_readImage__writeImage__URI() {
    return this.data_readImage__writeImages($ -> $.toFile().toURI());
  }

  @Test(groups = "all", dataProvider = "data_readImage__writeImage__URI")
  public void readImage__writeImageURI(URI imageFile, PictureFormat outformat) {
    readImage_writeImage(imageFile, outformat, GraphicsFunctions::readImage, $ -> $.toFile().toURI(), GraphicsFunctions::writeImage);
  }

  private JComponent createJComponent() {
    var frame    = new JFrame();
    var panel    = new JPanel();
    frame.getContentPane().add(panel);
    frame.setVisible(true);
    panel.setSize(200, 200);
    panel.getGraphics().drawOval(100, 100, 30, 30);
    frame.setVisible(false);
    return panel;
  }
  
  private <R> void writeImage__JComponent(PictureFormat outformat, Function<Path, R> toDest, TriConsumer<R, JComponent, PictureFormat> saver) {
    var tempPath = getTempPath(outformat);
    saver.accept(toDest.apply(tempPath), createJComponent(), outformat);
    assertTrue(Files.isRegularFile(tempPath));
  }

  @Test(groups = "all")
  public void writeImage__JComponent__Path() {
    for (var fmt : PictureFormat.rasterFormatValues()) {
      writeImage__JComponent(fmt, Function.identity(), GraphicsFunctions::writeImage);
    }
  }

  @Test(groups = "all")
  public void writeImage__JComponent__File() {
    for (var fmt : PictureFormat.rasterFormatValues()) {
      writeImage__JComponent(fmt, $ -> $.toFile(), GraphicsFunctions::writeImage);
    }
  }

  @Test(groups = "all")
  public void writeImage__JComponent__URI() {
    for (var fmt : PictureFormat.rasterFormatValues()) {
      writeImage__JComponent(fmt, $ -> $.toFile().toURI(), GraphicsFunctions::writeImage);
    }
  }

  @Test(groups = "all", expectedExceptions = KclException.class )
  public void writeImage__JComponent__UnsupportedFormat() {
    writeImage__JComponent(PictureFormat.Svg, Function.identity(), GraphicsFunctions::writeImage);
  }

  @Test(groups = "all")
  public void writeImage__JComponent__OutputStream() {
    for (var fmt : PictureFormat.rasterFormatValues()) {
      var tempPath = getTempPath(fmt);
      IoFunctions.forOutputStreamDo(tempPath, $ -> GraphicsFunctions.writeImage($, createJComponent(), fmt));
      assertTrue(Files.isRegularFile(tempPath));
    }
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void writeImage__JComponent__OutputStream__UnsupportedFormat() {
    var tempPath = getTempPath(String.format("image_%d.%s", idx++, PictureFormat.Svg.getSuffix()));
    IoFunctions.forOutputStreamDo(tempPath, $ -> GraphicsFunctions.writeImage($, createJComponent(), PictureFormat.Svg));
    assertTrue(Files.isRegularFile(tempPath));
  }

  @Test(groups = "all")
  public void applyWatermark() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-1.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void applyWatermark__Padding() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, 20);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-5.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void applyWatermark__Positioning() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Right, Alignment.Bottom);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-2.png"));
    assertImages(img1, expected);
    
  }


  @Test(groups = "all")
  public void applyWatermark__PositioningCentered() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center, Alignment.Middle);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-4.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void applyWatermark__SimpleAlignment1() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center, 0);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-4.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void applyWatermark__SimpleAlignment2() {
    
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var mark        = GraphicsFunctions.readImage(getResource("mark.png"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Center);
    
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-4.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void applyWatermark__LargerThanOriginWillBeScaledDown() {
    
    var img1        = GraphicsFunctions.readImage(getResource("mark.png"));
    var mark        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    GraphicsFunctions.applyWatermark(img1, mark, 0.4f, Alignment.Right, Alignment.Bottom);
   
    var expected    = GraphicsFunctions.readImage(getResource("watermarked-3.png"));
    assertImages(img1, expected);
    
  }

  @Test(groups = "all")
  public void scaleImage() {
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var scaled      = GraphicsFunctions.scaleImage(img1, img1.getWidth()*2, img1.getHeight()*2);
    var expected    = GraphicsFunctions.readImage(getResource("scaled-1.png"));
    assertImages(scaled, expected);
  }

  @Test(groups = "all")
  public void scaleImage__Image() {
    var img1        = GraphicsFunctions.readImage(getResource("images/sample.bmp"));
    var scaled      = GraphicsFunctions.scaleImage((Image) img1, img1.getWidth()*2, img1.getHeight()*2);
    var expected    = GraphicsFunctions.readImage(getResource("scaled-1.png"));
    assertImages(scaled, expected);
  }

  @Test(groups = "all")
  public void readImage__InputStream() {
    for (var file : inputfiles) {
      var image = IoFunctions.forInputStream(file, GraphicsFunctions::readImage);
      assertNotNull(image);
    }
  }

  @SuppressWarnings("deprecation")
  @Test(groups = "all")
  public void readImage__URL() throws Exception {
    for (var file : inputfiles) {
      var image = GraphicsFunctions.readImage(file.toFile().toURL());
      assertNotNull(image);
    }
  }

} /* ENDCLASS */
