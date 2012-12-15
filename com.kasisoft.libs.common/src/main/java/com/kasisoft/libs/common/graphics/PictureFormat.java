/**
 * Name........: PictureFormat
 * Description.: Collection of constants used to identify image formats.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

/**
 * Collection of constants used to identify image formats.
 */
public enum PictureFormat {

  Bmp     ( true  , MimeType.Bitmap                 , "bmp" ),
  Eps     ( false , MimeType.EncapsulatedPostscript , null  ),
  Gif     ( true  , MimeType.Gif                    , "gif" ),
  Jpeg    ( true  , MimeType.Jpeg                   , "jpg" ),
  Ps      ( false , MimeType.Postscript             , null  ),
  Svg     ( false , MimeType.Svg                    , null  );
  
  private boolean    raster;
  private MimeType   mimetype;
  private String     imageio;
  
  PictureFormat( boolean israster, MimeType mime, String iioname ) {
    raster    = israster;
    mimetype  = mime;
    imageio   = iioname;
  }
  
  /**
   * Returns <code>true</code> if this format is a raster format.
   * 
   * @return   <code>true</code> <=> This format is a raster format.
   */
  public boolean isRasterFormat() {
    return raster;
  }
  
  /**
   * Returns the mime type associated with this picture format.
   *  
   * @return   The mime type associated with this picture format. Not <code>null</code>.
   */
  public MimeType getMimeType() {
    return mimetype;
  }
  
  /**
   * Returns the format name used for the ImageIO subsystem. This function only returns valid
   * values if {@link #isRasterFormat()} is <code>true</code>.
   * 
   * @return   The format name used for the ImageIO subsystem. If not <code>null</code> it's not empty.
   */
  public String getImageIOFormat() {
    return imageio;
  }
  
} /* ENDENUM */
