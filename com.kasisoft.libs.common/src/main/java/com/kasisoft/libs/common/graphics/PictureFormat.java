package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

import lombok.*;

/**
 * Collection of constants used to identify image formats.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum PictureFormat {

  Bmp     ( true  , MimeType.Bitmap                 , "bmp" ),
  Eps     ( false , MimeType.EncapsulatedPostscript , null  ),
  Gif     ( true  , MimeType.Gif                    , "gif" ),
  Jpeg    ( true  , MimeType.Jpeg                   , "jpg" ),
  Ps      ( false , MimeType.Postscript             , null  ),
  Svg     ( false , MimeType.Svg                    , null  );
  
  @Getter
  private boolean    rasterFormat;
  private MimeType   mimeType;
  
  private String     imageio;
  
  PictureFormat( boolean israster, MimeType mime, String iioname ) {
    rasterFormat  = israster;
    mimeType      = mime;
    imageio       = iioname;
  }
  
  /**
   * Returns the mime type associated with this picture format.
   *  
   * @return   The mime type associated with this picture format. Not <code>null</code>.
   */
  public MimeType getMimeType() {
    return mimeType;
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
