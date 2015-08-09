package com.kasisoft.libs.common.graphics;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.sys.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.test.framework.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.awt.image.*;

import java.io.*;

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
