/**
 * Name........: GraphicsFunctionsTest
 * Description.: Tests for the utility class 'GraphicsFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.graphics;

import com.kasisoft.lgpl.libs.common.io.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

import java.awt.image.*;

/**
 * Tests for the utility class 'GraphicsFunctions'.
 */
@Test(groups="all")
public class GraphicsFunctionsTest implements FilenameFilter {
  
  private File     images;
  private File[]   inputfiles;
  
  @BeforeSuite
  public void setup() {
    images          = new File( "testdata/images" );
    Assert.assertTrue( images.isDirectory() );
    inputfiles      = images.listFiles( this );
    Assert.assertNotNull( inputfiles );
    Assert.assertTrue( inputfiles.length > 0 );
  }
  
  @DataProvider(name="createOutputFormats")
  public Object[][] createOutputFormats() {
    List<PictureFormat> formats = new ArrayList<PictureFormat>();
    for( PictureFormat format : PictureFormat.values() ) {
      if( format.isRasterFormat() ) {
        formats.add( format );
      }
    }
    Object[][]      result  = new Object[ formats.size() ][1];
    for( int i = 0; i < formats.size(); i++ ) {
      result[i][0] = formats.get(i);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public boolean accept( File dir, String name ) {
    return name.startsWith( "sample" );
  }

  @Test(dataProvider="createOutputFormats")
  public void convert( PictureFormat outformat ) {
    File tempdir = IoFunctions.newTempFile();
    tempdir.mkdirs();
    for( File inputfile : inputfiles ) {
      BufferedImage image     = GraphicsFunctions.readImage( inputfile );
      Assert.assertNotNull( image );
      File          destfile  = new File( tempdir, inputfile.getName() + "." + outformat.getMimeType().getSuffices().get(0) );
      boolean       success   = GraphicsFunctions.writeImage( destfile, outformat, image );
      Assert.assertTrue( success );
    }
  }
  
} /* ENDCLASS */
