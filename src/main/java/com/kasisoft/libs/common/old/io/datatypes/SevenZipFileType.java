package com.kasisoft.libs.common.old.io.datatypes;

import static com.kasisoft.libs.common.old.constants.Primitive.*;

import com.kasisoft.libs.common.old.constants.*;

/**
 * FileType for '7z' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SevenZipFileType implements FileType {

  private static final byte[] MAGIC = new byte[] { (byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C  }; 
  
  @Override
  public int getMinSize() {
    return 6;
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
    return MimeType.SevenZip;
  }

} /* ENDCLASS */
