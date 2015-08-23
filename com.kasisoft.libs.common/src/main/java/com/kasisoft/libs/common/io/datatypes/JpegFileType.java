package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * FileType for 'jpeg' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JpegFileType implements FileType {

  static final byte[] MAGIC = "JFIF".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 10;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return ArrayFunctions.compare( data, MAGIC, 6 );
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.Jpeg.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".jpg";
  }

} /* ENDCLASS */
