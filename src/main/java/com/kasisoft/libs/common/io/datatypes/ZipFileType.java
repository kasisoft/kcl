package com.kasisoft.libs.common.io.datatypes;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.constants.*;

/**
 * FileType for 'zip' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ZipFileType implements FileType {

  private static final byte[] MAGIC = new byte[] { 'P', 'K', 3, 4 }; 
  
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
    return MimeType.Zip;
  }

} /* ENDCLASS */
