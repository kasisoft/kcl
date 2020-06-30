package com.kasisoft.libs.common.graphics;

import com.kasisoft.libs.common.constants.MimeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Predicate;

import java.util.Arrays;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Collection of constants used to identify image formats.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum PictureFormat implements Predicate<String> {

  Bmp   (true , MimeType.Bitmap                , "bmp", Arrays.asList("bmp")),
  Eps   (false, MimeType.EncapsulatedPostscript, null , Arrays.asList("eps")),
  Gif   (true , MimeType.Gif                   , "gif", Arrays.asList("gif")),
  Jpeg  (true , MimeType.Jpeg                  , "jpg", Arrays.asList("jpg", "jpeg")),
  Png   (true , MimeType.Png                   , "png", Arrays.asList("png")),
  Ps    (false, MimeType.Postscript            , null , Arrays.asList("ps")),
  Svg   (false, MimeType.Svg                   , null , Arrays.asList("svg"));
  
  boolean       rasterFormat;
  MimeType      mimeType;
  String        imageIOFormat;
  List<String>  suffices;
  
  @Override
  public boolean test(@Null String t) {
    if (t != null) {
      var lidx      = t.lastIndexOf('.');
      var suffix    = t.substring(lidx);
      return suffices.contains(suffix);
    }
    return false;
  }
  
  public @NotNull String getSuffix() {
    return suffices.get(0);
  }
  
} /* ENDENUM */
