package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.internal.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import javax.imageio.*;

import javax.swing.*;

import jakarta.validation.constraints.*;

import java.nio.file.*;

import java.net.*;

import java.awt.image.*;

import java.awt.geom.*;

import java.awt.*;

import java.io.*;

/**
 * Several utility functions related to graphical operations.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GraphicsFunctions {

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param input   The resource which has to be loaded.
   *
   * @return   The image.
   */
  public static @NotNull BufferedImage readImage(@NotNull Path input) {
    return IoFunctions.forInputStream(input, $ -> readImage(input, $));
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param input   The resource which has to be loaded.
   *
   * @return   The image.
   */
  public static @NotNull BufferedImage readImage(@NotNull File input) {
    return IoFunctions.forInputStream(input, $ -> readImage(input, $));
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param input   The resource which has to be loaded.
   *
   * @return   The image.
   */
  public static @NotNull BufferedImage readImage(@NotNull URL input) {
    return IoFunctions.forInputStream(input, $ -> readImage(input, $));
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param input   The resource which has to be loaded.
   *
   * @return   The image.
   */
  public static @NotNull  BufferedImage readImage(@NotNull URI input) {
    return IoFunctions.forInputStream(input, $ -> readImage(input, $));
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param source     The source used to read the data.
   * @param instream   The InputStream which provides the content.
   *
   * @return   The image.
   */
  private static @NotNull BufferedImage readImage(@NotNull Object source, @NotNull InputStream instream) {
    try {
      return ImageIO.read(instream);
    } catch (Exception ex) {
      throw KclException.wrap(ex, Messages.error_failed_to_read_image, source);
    }
  }

  /**
   * Reads an image supported by the ImageIO subsystem.
   *
   * @param instream   The InputStream which provides the content.
   *
   * @return   The image.
   */
  public static @NotNull BufferedImage readImage(@NotNull InputStream instream) {
    try {
      return ImageIO.read(instream);
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param image       The image that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull Path destination, @NotNull BufferedImage image, @NotNull PictureFormat format) {
    IoFunctions.forOutputStreamDo(destination, $output -> writeImage(destination, $output, image, format));
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param image       The image that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull File destination, @NotNull BufferedImage image, @NotNull PictureFormat format) {
    IoFunctions.forOutputStreamDo(destination, $output -> writeImage(destination, $output, image, format));
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param image       The image that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull URI destination, @NotNull BufferedImage image, @NotNull PictureFormat format) {
    IoFunctions.forOutputStreamDo(destination, $output -> writeImage(destination, $output, image, format));
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param outstream   The OutputStream which will receive the content.
   * @param image       The image that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  private static void writeImage(@NotNull Object source, @NotNull OutputStream outstream, @NotNull BufferedImage image, @NotNull PictureFormat format) {
    try {
      if (format == PictureFormat.Jpeg) {
        image = to3ByteBGR(image);
      }
      if (!ImageIO.write(image, format.getImageIOFormat(), outstream)) {
        throw new KclException(Messages.error_failed_to_write_image, source, format);
      }
    } catch (Exception ex) {
      throw KclException.wrap(ex, Messages.error_failed_to_write_image, source, format);
    }
  }

  private static BufferedImage to3ByteBGR(BufferedImage image) {
    var result = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
    result.createGraphics().drawImage(image, 0, 0, null);
    return result;
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param outstream   The OutputStream which will receive the content.
   * @param image       The image that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull OutputStream outstream, @NotNull BufferedImage image, @NotNull PictureFormat format) {
    try {
      if (!ImageIO.write(image, format.getImageIOFormat(), outstream)) {
        throw new KclException();
      }
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param component   The component that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull Path destination, @NotNull JComponent component, @NotNull PictureFormat format) {
    writeImage(destination, createImage(component), format);
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param component   The component that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull File destination, @NotNull JComponent component, @NotNull PictureFormat format) {
    writeImage(destination, createImage(component), format);
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The destination to write to.
   * @param component   The component that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull URI destination, @NotNull JComponent component, @NotNull PictureFormat format) {
    writeImage(destination, createImage(component), format);
  }

  /**
   * Writes an image supported by the ImageIO subsystem.
   *
   * @param destination The OutputStream which will receive the content.
   * @param component   The component that has to be written.
   * @param format      The desired output format. Must be a raster format.
   */
  public static void writeImage(@NotNull OutputStream destination, @NotNull JComponent component, @NotNull PictureFormat format) {
    writeImage(destination, createImage(component), format);
  }

  /**
   * Creates an Image from the supplied component.
   *
   * @param component   The Component which has to be returned as an Image.
   *
   * @return   A visual representation of the supplied component.
   */
  public static @NotNull BufferedImage createImage(@NotNull Component component) {
    var result = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
    component.paint(result.getGraphics());
    return result;
  }

  /**
   * Creates a scaled image from an original one.
   *
   * @param image        The image that is supposed to be scaled.
   * @param newWidth     The new width. Must be bigger than 0.
   * @param newHeight    The new height. Must be bigger than 0.
   *
   * @return   The scaled image.
   */
  public static @NotNull BufferedImage scaleImage(@NotNull Image image, @Min(1) int newWidth, @Min(1) int newHeight) {
    BufferedImage bufferedImage = null;
    if (image instanceof BufferedImage) {
      bufferedImage = (BufferedImage) image;
    } else {
      bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
      bufferedImage.createGraphics().drawImage(image, 0, 0, null);
    }
    return scaleImage(bufferedImage, newWidth, newHeight);
  }

  /**
   * Creates a scaled image from an original one.
   *
   * @param image        The image that is supposed to be scaled.
   * @param newWidth     The new width. Must be bigger than 0.
   * @param newHeight    The new height. Must be bigger than 0.
   *
   * @return   The scaled image.
   */
  public static @NotNull BufferedImage scaleImage(@NotNull BufferedImage image, @Min(1) int newWidth, @Min(1) int newHeight) {

    var width       = image.getWidth();
    var height      = image.getHeight();

    var fx          = (double) newWidth  / width;
    var fy          = (double) newHeight / height;

    var transform   = AffineTransform.getScaleInstance(fx, fy);
    var scaleOp     = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);

    return scaleOp.filter(image, new BufferedImage(newWidth, newHeight, image.getType()));

  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin      The image that will be altered in place.
   * @param watermark   The watermark that shall be applied.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha) {
    applyWatermark(origin, watermark, alpha, null, null, 0);
  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin      The image that will be altered in place.
   * @param watermark   The watermark that shall be applied.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param padding     Some padding for the watermark within the original image.
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha, int padding) {
    applyWatermark(origin, watermark, alpha, null, null, padding);
  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin      The image that will be altered in place.
   * @param watermark   The watermark that shall be applied.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param alignment   The horizontal/vertical alignment to be chosen.
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha, Alignment alignment) {
    applyWatermark(origin, watermark, alpha, alignment, alignment, 0);
  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin      The image that will be altered in place.
   * @param watermark   The watermark that shall be applied.
   * @param alpha       The alpha level. A range between 0.0 and 1.0 .
   * @param alignment   The horizontal/vertical alignment to be chosen.
   * @param padding     Some padding for the watermark within the original image.
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha, Alignment alignment, int padding) {
    applyWatermark(origin, watermark, alpha, alignment, alignment, padding);
  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin       The image that will be altered in place.
   * @param watermark    The watermark that shall be applied.
   * @param alpha        The alpha level. A range between 0.0 and 1.0 .
   * @param alignmentX   The horizontal alignment to be chosen.
   * @param alignmentY   The vertical alignment to be chosen.
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha, Alignment alignmentX, Alignment alignmentY) {
    applyWatermark( origin, watermark, alpha, alignmentX, alignmentY, 0);
  }

  /**
   * Applies a watermark through an alpha channel.
   *
   * @param origin       The image that will be altered in place.
   * @param watermark    The watermark that shall be applied.
   * @param alpha        The alpha level. A range between 0.0 and 1.0 .
   * @param alignmentX   The horizontal alignment to be chosen.
   * @param alignmentY   The vertical alignment to be chosen.
   * @param padding      Some padding for the watermark within the original image.
   */
  public static void applyWatermark(@NotNull BufferedImage origin, @NotNull BufferedImage watermark, float alpha, Alignment alignmentX, Alignment alignmentY, int padding) {

    if (alignmentX == null) {
      alignmentX = Alignment.Left;
    }
    if (alignmentY == null) {
      alignmentY = Alignment.Top;
    }

    var width  = origin.getWidth  () - 2 * padding;
    var height = origin.getHeight () - 2 * padding;

    if ((watermark.getWidth() > width) || (watermark.getHeight() > height)) {
      watermark = scaleImage(watermark, width, height);
    }

    var x       = toCoordinate(origin.getWidth  (), watermark.getWidth  (), padding, alignmentX);
    var y       = toCoordinate(origin.getHeight (), watermark.getHeight (), padding, alignmentY);
    var g2d     = (Graphics2D) origin.getGraphics();
    var channel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);

    g2d.setComposite(channel);
    g2d.drawImage(watermark, x, y, null);

  }

  private static int toCoordinate(int imageSize, int watermarkSize, int padding, @NotNull Alignment alignment) {
    var result    = 0;
    var available = imageSize - watermarkSize;
    if (available > 0) {
      if ((alignment == Alignment.Center) || (alignment == Alignment.Middle)) {
        result = (imageSize - watermarkSize) / 2;
      } else if ((alignment == Alignment.Right) || (alignment == Alignment.Bottom)) {
        result = imageSize - watermarkSize - padding;
      } else if ((alignment == Alignment.Left) || (alignment == Alignment.Top)) {
        result = padding;
      }
    }
    return result;
  }

} /* ENDCLASS */
