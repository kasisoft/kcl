package com.kasisoft.libs.common.graphics;

import static com.kasisoft.libs.common.io.DefaultIO.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.ui.*;

import javax.swing.*;

import javax.imageio.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.io.*;

import lombok.*;

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
   * @param input   The resource which has to be loaded.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static <T> BufferedImage readImage( @NonNull T input ) {
    return inputstreamEx( input ).forInputStream( input, GraphicsFunctions::readImage ).orElse( null );
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
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param output      The destination to write to.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param component   The component that has to be written. Not <code>null</code>.
   */
  public static <T> boolean writeImage( @NonNull T output, @NonNull PictureFormat format, @NonNull JComponent component ) {
    return outputstreamEx( output ).forOutputStreamDo( output, format, component, GraphicsFunctions::writeImage );
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
   * @param output      The destination to write to.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   */
  public static <T> boolean writeImage( @NonNull T output, @NonNull PictureFormat format, @NonNull BufferedImage image ) {
    return outputstreamEx( output ).forOutputStreamDo( output, format, image, GraphicsFunctions::writeImage );
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
        throw new KclException();
      }
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
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
  public static BufferedImage scaleImage( @NonNull Image image, int newWidth, int newHeight ) {
    BufferedImage bufferedImage = null;
    if( image instanceof BufferedImage ) {
      bufferedImage = (BufferedImage) image;
    } else {
      bufferedImage = new BufferedImage( image.getWidth( null ), image.getHeight( null ), BufferedImage.TYPE_INT_ARGB );
      bufferedImage.createGraphics().drawImage( image, 0, 0, null );
    }
    return scaleImage( bufferedImage, newWidth, newHeight );
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
