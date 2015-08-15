package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.base.*;

import com.kasisoft.libs.common.ui.*;

import com.kasisoft.libs.common.io.*;

import lombok.*;

import javax.swing.*;

import javax.imageio.*;

import java.net.*;

import java.awt.image.*;

import java.io.*;

import java.nio.file.*;

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
  
} /* ENDCLASS */
