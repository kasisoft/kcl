/**
 * Name........: GraphicsFunctions
 * Description.: Several utility functions related to graphical operations.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.base.*;

import javax.imageio.*;

import java.net.*;

import java.awt.image.*;

import java.io.*;

/**
 * Several utility functions related to graphical operations.
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
  public static final BufferedImage readImage( File file ) {
    return readImage( false, file );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param fail   <code>true</code> <=> Cause an exception if the resource could not be loaded.
   * @param file   The resource which has to be loaded. Must be a valid file.
   * 
   * @return   The image. <code>null</code> if fail is set to <code>false</code> and the resource could not be loaded.
   */
  public static final BufferedImage readImage( boolean fail, File file ) {
    try {
      return ImageIO.read( file );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, ex );
      } else {
        return null;
      }
    }
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param url   The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static final BufferedImage readImage( URL url ) {
    return readImage( false, url );
  }
  
  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param fail   <code>true</code> <=> Cause an exception if the resource could not be loaded.
   * @param url    The resource which has to be loaded. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if fail is set to <code>false</code> and the resource could not be loaded.
   */
  public static final BufferedImage readImage( boolean fail, URL url ) {
    try {
      return ImageIO.read( url );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, ex );
      } else {
        return null;
      }
    }
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param instream   The InputStream which provides the content. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if the resource could not be loaded.
   */
  public static final BufferedImage readImage( InputStream instream ) {
    return readImage( false, instream );
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   * 
   * @param fail       <code>true</code> <=> Cause an exception if the resource could not be loaded.
   * @param instream   The InputStream which provides the content. Not <code>null</code>.
   * 
   * @return   The image. <code>null</code> if fail is set to <code>false</code> and the resource
   *           could not be loaded.
   */
  public static final BufferedImage readImage( boolean fail, InputStream instream ) {
    try {
      return ImageIO.read( instream );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, ex );
      } else {
        return null;
      }
    }
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param file     The resource which has to be saved. Not <code>null</code>.
   * @param format   The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image    The image that has to be written. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> Saving the image succeeded.
   */
  public static final boolean writeImage( File file, PictureFormat format, BufferedImage image ) {
    return writeImage( false, file, format, image );
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param fail     <code>true</code> <=> Cause an exception if the resource could not be written.
   * @param file     The resource which has to be saved. Not <code>null</code>.
   * @param format   The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image    The image that has to be written. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> Saving the image succeeded. If fail has been set to <code>true</code>
   *           an exception will be raised instead.
   */
  public static final boolean writeImage( boolean fail, File file, PictureFormat format, BufferedImage image ) {
    boolean result = false;
    try {
      result = ImageIO.write( image, format.getImageIOFormat(), file );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, ex );
      }
    }
    if( (! result) && fail ) {
      throw new FailureException( FailureCode.IO );
    }
    return result;
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param outstream   The OutputStream which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> Saving the image succeeded.
   */
  public static final boolean writeImage( OutputStream outstream, PictureFormat format, BufferedImage image ) {
    return writeImage( false, outstream, format, image );
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   * 
   * @param fail        <code>true</code> <=> Cause an exception if the resource could not be written.
   * @param outstream   The OutputStream which will receive the content. Not <code>null</code>.
   * @param format      The desired output format. Must be a raster format. Not <code>null</code>.
   * @param image       The image that has to be written. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> Saving the image succeeded. If fail has been set to <code>true</code>
   *           an exception will be raised instead.
   */
  public static final boolean writeImage( boolean fail, OutputStream outstream, PictureFormat format, BufferedImage image ) {
    boolean result = false;
    try {
      result = ImageIO.write( image, format.getImageIOFormat(), outstream );
    } catch( IOException ex ) {
      if( fail ) {
        throw new FailureException( FailureCode.IO, ex );
      }
    }
    if( (! result) && fail ) {
      throw new FailureException( FailureCode.IO );
    }
    return result;
  }
  
} /* ENDCLASS */