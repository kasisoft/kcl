package com.kasisoft.libs.common.old.graphics;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.sys.SystemInfo;
import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FilenameFilter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the utility class 'GraphicsFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GraphicsFunctionsTest implements FilenameFilter {
  
  File     images;
  File[]   inputfiles;
  
  @BeforeTest
  public void setup() {
    images          = Utilities.getTestdataDir( "images" );
    assertTrue( images.isDirectory() );
    inputfiles      = images.listFiles( this );
    assertThat( inputfiles, is( notNullValue() ) );
    assertTrue( inputfiles.length > 0 );
  }
  
  @DataProvider(name="createOutputFormats")
  public Object[][] createOutputFormats() {
    List<PictureFormat> formats = new ArrayList<>();
    for( PictureFormat format : PictureFormat.values() ) {
      if( format.isRasterFormat() ) {
        if( (format != PictureFormat.Jpeg) || (! SystemInfo.getRunningOS().isUnixLike()) ) {
          formats.add( format );
        }
      }
    }
    Object[][] result  = new Object[ formats.size() ][1];
    for( int i = 0; i < formats.size(); i++ ) {
      result[i][0] = formats.get(i);
    }
    return result;
  }

  @Override
  public boolean accept( File dir, String name ) {
    return name.startsWith( "sample" );
  }

  @Test(dataProvider="createOutputFormats", groups="all")
  public void convert( PictureFormat outformat ) {
    File tempdir = IoFunctions.newTempFile();
    tempdir.mkdirs();
    assertTrue( tempdir.isDirectory() );
    for( File inputfile : inputfiles ) {
      BufferedImage image     = GraphicsFunctions.readImage( inputfile );
      assertThat( image, is( notNullValue() ) );
      File          destfile  = new File( tempdir, inputfile.getName() + "." + outformat.getMimeType().getSuffices().get(0) );
      GraphicsFunctions.writeImage( destfile, outformat, image );
      assertTrue( destfile.isFile() );
    }
  }
  
} /* ENDCLASS */