package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Collection of constants used to identify image formats.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum PictureFormat {

  Bmp     ( true  , MimeType.Bitmap                 , "bmp" ),
  Eps     ( false , MimeType.EncapsulatedPostscript , null  ),
  Gif     ( true  , MimeType.Gif                    , "gif" ),
  Jpeg    ( true  , MimeType.Jpeg                   , "jpg" ),
  Png     ( true  , MimeType.Png                    , "png" ),
  Ps      ( false , MimeType.Postscript             , null  ),
  Svg     ( false , MimeType.Svg                    , null  );
  
  @Getter boolean    rasterFormat;
  
  /** Not <code>null</code>. */
  @Getter MimeType   mimeType;
  
  /** If not <code>null</code> it's not empty. */
  @Getter String     imageIOFormat;
  
  PictureFormat( boolean israster, MimeType mime, String iioname ) {
    rasterFormat  = israster;
    mimeType      = mime;
    imageIOFormat = iioname;
  }
  
} /* ENDENUM */
