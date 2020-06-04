package com.kasisoft.libs.common.old.io.datatypes;

import static com.kasisoft.libs.common.old.constants.Primitive.PByte;

import com.kasisoft.libs.common.constants.MimeType;

/**
 * FileType for 'png' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PngFileType implements FileType {

  private static final byte[] MAGIC = "PNG".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return PByte.compare( data, MAGIC, 1 );
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
    return MimeType.Png;
  }

} /* ENDCLASS */
