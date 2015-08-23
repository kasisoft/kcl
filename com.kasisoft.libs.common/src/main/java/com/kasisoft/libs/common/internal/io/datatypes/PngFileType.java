package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.io.datatypes.*;

import lombok.experimental.*;

import lombok.*;

/**
 * FileType for 'png' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PngFileType implements FileType {

  static final byte[] MAGIC = "PNG".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return ArrayFunctions.compare( data, MAGIC, 1 );
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.Png.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".png";
  }

} /* ENDCLASS */
