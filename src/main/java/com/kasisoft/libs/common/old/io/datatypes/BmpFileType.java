package com.kasisoft.libs.common.old.io.datatypes;

import static com.kasisoft.libs.common.old.constants.Primitive.PByte;

import com.kasisoft.libs.common.old.constants.MimeType;

/**
 * FileType for 'bmp' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BmpFileType implements FileType {

  private static final byte[] MAGIC = "BM".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return PByte.compare( data, MAGIC );
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return getContentType().getMimeType();
  }

  @Override
  public MimeType getContentType() {
    return MimeType.Bitmap;
  }
  
} /* ENDCLASS */
