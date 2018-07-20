package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

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
