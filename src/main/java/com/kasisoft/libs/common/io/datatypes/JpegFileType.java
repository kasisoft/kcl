package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

/**
 * FileType for 'jpeg' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class JpegFileType implements FileType {

  private static final byte[] MAGIC = "JFIF".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 10;
  }

  @Override
  public boolean test( byte[] data ) {
    if( getMinSize() <= data.length ) {
      return PByte.compare( data, MAGIC, 6 );
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
