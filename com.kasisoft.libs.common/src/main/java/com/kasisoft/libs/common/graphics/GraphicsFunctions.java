package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.ui.*;

import com.kasisoft.libs.common.io.*;

import lombok.*;

import javax.swing.*;

import javax.imageio.*;

import java.net.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.nio.file.*;

import java.io.*;

/**
 * Several utility functions related to graphical operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GraphicsFunctions {

  /**
   * Prevent instantiation.
   */
  private GraphicsFunctions() {
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param file   The resource which has to be loaded. Must be a valid file.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull File file ) {
    return IoFunctions.forInputStream( file, GraphicsFunctions::readImage );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param url   The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull URL url ) {
    return IoFunctions.forInputStream( url, GraphicsFunctions::readImage );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param uri   The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull URI uri ) {
    return IoFunctions.forInputStream( uri, GraphicsFunctions::readImage );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param path   The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull Path path ) {
    return IoFunctions.forInputStream( path, GraphicsFunctions::readImage );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param path   The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull String path ) {
    return IoFunctions.forInputStream( path, GraphicsFunctions::readImage );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param instream   The InputStream which provides the content. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static BufferedImage readImage( @NonNull InputStream instream ) {
    try {
      return ImageIO.read( instream );
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }
  
  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param url         The URL which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull URL url, @NonNull PictureFormat format, @NonNull JComponent component ) {
    IoFunctions.forOutputStreamDo( url, outstream -> writeImage( outstream, format, component ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param uri         The URI which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull URI uri, @NonNull PictureFormat format, @NonNull JComponent component ) {
    IoFunctions.forOutputStreamDo( uri, outstream -> writeImage( outstream, format, component ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param path        The path which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull Path path, @NonNull PictureFormat format, @NonNull JComponent component ) {
    IoFunctions.forOutputStreamDo( path, outstream -> writeImage( outstream, format, component ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param file        The File which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull File file, @NonNull PictureFormat format, @NonNull JComponent component ) {
    IoFunctions.forOutputStreamDo( file, outstream -> writeImage( outstream, format, component ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param path        The path which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull String path, @NonNull PictureFormat format, @NonNull JComponent component ) {
    IoFunctions.forOutputStreamDo( path, outstream -> writeImage( outstream, format, component ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param outstream   The OutputStream which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull OutputStream outstream, @NonNull PictureFormat format, @NonNull JComponent component ) {
    writeImage( outstream, format, SwingFunctions.createImage( component ) );
  }
  
  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param url         The URL which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull URL url, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    IoFunctions.forOutputStreamDo( url, outstream -> writeImage( outstream, format, image ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param uri         The URI which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull URI uri, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    IoFunctions.forOutputStreamDo( uri, outstream -> writeImage( outstream, format, image ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param path        The path which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull Path path, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    IoFunctions.forOutputStreamDo( path, outstream -> writeImage( outstream, format, image ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param file        The File which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull File file, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    IoFunctions.forOutputStreamDo( file, outstream -> writeImage( outstream, format, image ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param path        The path which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull String path, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    IoFunctions.forOutputStreamDo( path, outstream -> writeImage( outstream, format, image ) );
  }  

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param outstream   The OutputStream which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static void writeImage( @NonNull OutputStream outstream, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    try {
      if( ! ImageIO.write( image, format.getImageIOFormat(), outstream ) ) {
        throw FailureCode.IO.newException();
      }
    } catch( IOException ex ) {
      throw FailureCode.IO.newException( ex );
    }
  }
  
  /**
   * Creates a scaled image from an original one.
   * 
   * @param image        The image that is supposed to be scaled. Not <code>null</code>.
   * @param newWidth     The new width. Must be bigger than 0.
   * @param newHeight    The new height. Must be bigger than 0.
   * 
   * @return   The scaled image. Not <code>null</code>.
   */
  public static BufferedImage scaleImage( @NonNull BufferedImage image, int newWidth, int newHeight ) {
    
    int width  = image.getWidth();
    int height = image.getHeight();
    
    double fx = (double) newWidth  / width;
    double fy = (double) newHeight / height;
    
    AffineTransform   transform = AffineTransform.getScaleInstance( fx, fy );
    AffineTransformOp scaleOp   = new AffineTransformOp( transform, AffineTransformOp.TYPE_BILINEAR );

    return scaleOp.filter( image, new BufferedImage( newWidth, newHeight, image.getType() ) );
  
  }
  
  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin      The image that will be altered in place. Not <code>null</code>.
   * @param watermark   The watermark that shall be applied. Not <code>null</code>.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha ) {
    applyWatermark( origin, watermark, alpha, null, null, 0 );
  }

  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin      The image that will be altered in place. Not <code>null</code>.
   * @param watermark   The watermark that shall be applied. Not <code>null</code>.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param padding     Some padding for the watermark within the original image. 
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha, int padding ) {
    applyWatermark( origin, watermark, alpha, null, null, padding );
  }

  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin      The image that will be altered in place. Not <code>null</code>.
   * @param watermark   The watermark that shall be applied. Not <code>null</code>.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param alignment   The horizontal/vertical alignment to be chosen. Maybe <code>null</code>. 
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha, Alignment alignment ) {
    applyWatermark( origin, watermark, alpha, alignment, alignment, 0 );
  }

  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin      The image that will be altered in place. Not <code>null</code>.
   * @param watermark   The watermark that shall be applied. Not <code>null</code>.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param alignment   The horizontal/vertical alignment to be chosen. Maybe <code>null</code>. 
   * @param padding     Some padding for the watermark within the original image. 
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha, Alignment alignment, int padding ) {
    applyWatermark( origin, watermark, alpha, alignment, alignment, padding );
  }

  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin       The image that will be altered in place. Not <code>null</code>.
   * @param watermark    The watermark that shall be applied. Not <code>null</code>.
   * @param alpha        The alpha level. A range between 0.0 and 1.0 .
   * @param alignmentX   The horizontal alignment to be chosen. Maybe <code>null</code>. 
   * @param alignmentY   The vertical alignment to be chosen. Maybe <code>null</code>.
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha, Alignment alignmentX, Alignment alignmentY ) {
    applyWatermark( origin, watermark, alpha, alignmentX, alignmentY, 0 );
  }
  
  /**
   * Applies a watermark through an alpha channel. 
   * 
   * @param origin       The image that will be altered in place. Not <code>null</code>.
   * @param watermark    The watermark that shall be applied. Not <code>null</code>.
   * @param alpha        The alpha level. A range between 0.0 and 1.0 .
   * @param alignmentX   The horizontal alignment to be chosen. Maybe <code>null</code>. 
   * @param alignmentY   The vertical alignment to be chosen. Maybe <code>null</code>.
   * @param padding      Some padding for the watermark within the original image. 
   */
  public static void applyWatermark( @NonNull BufferedImage origin, @NonNull BufferedImage watermark, float alpha, Alignment alignmentX, Alignment alignmentY, int padding ) {
    
    if( alignmentX == null ) {
      alignmentX = Alignment.Left;
    }
    if( alignmentY == null ) {
      alignmentY = Alignment.Top;
    }
    
    int width  = origin.getWidth  () - 2 * padding;
    int height = origin.getHeight () - 2 * padding;
    
    if( (watermark.getWidth() > width) || (watermark.getHeight() > height) ) {
      watermark = scaleImage( watermark, width, height );
    }
    
    int x = toCoordinate( origin.getWidth  (), watermark.getWidth  (), padding, alignmentX );
    int y = toCoordinate( origin.getHeight (), watermark.getHeight (), padding, alignmentY );
    
    Graphics2D     g2d     = (Graphics2D) origin.getGraphics();
    AlphaComposite channel = AlphaComposite.getInstance( AlphaComposite.SRC_OVER, alpha );
    g2d.setComposite( channel );
    g2d.drawImage( watermark, x, y, null);
    
  }
  
  private static int toCoordinate( int imageSize, int watermarkSize, int padding, Alignment alignment ) {
    int result    = 0;
    int available = imageSize - watermarkSize;
    if( available > 0 ) {
      if( (alignment == Alignment.Center) || (alignment == Alignment.Middle) ) {
        result = (imageSize - watermarkSize) / 2;
      } else if( (alignment == Alignment.Right) || (alignment == Alignment.Bottom) ) {
        int size = imageSize - 2 * padding;
        if( size > watermarkSize ) {
          padding += (size - watermarkSize) / 2;
        }
        result = imageSize - padding;
      }
    }
    return result;
  }
  
} /* ENDCLASS */
