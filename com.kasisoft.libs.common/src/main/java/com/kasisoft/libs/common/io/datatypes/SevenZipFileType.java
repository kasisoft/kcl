package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.util.*;

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
      return ArrayFunctions.compare( data, MAGIC, 0 );
    } else {
      return false;
    }
  }

  @Override
  public String getMimeType() {
    return MimeType.SevenZip.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".7z";
  }

} /* ENDCLASS */
