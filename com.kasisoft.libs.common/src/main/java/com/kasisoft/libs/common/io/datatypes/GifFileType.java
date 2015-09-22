package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

/**
 * FileType for 'gif' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class GifFileType implements FileType {

  static final byte[] MAGIC1 = "GIF87a".getBytes(); 
  static final byte[] MAGIC2 = "GIF89a".getBytes();
  
  @Override
  public int getMinSize() {
    return 6;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return ArrayFunctions.compare( data, MAGIC1, 0 ) || ArrayFunctions.compare( data, MAGIC2, 0 );
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.Gif.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".gif";
  }

} /* ENDCLASS */
