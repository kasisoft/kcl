package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.datatypes.*;

import com.kasisoft.libs.common.util.*;

import lombok.*;

import lombok.experimental.*;

/**
 * FileType for '7z' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SevenZipFileType implements FileType {

  static final byte[] MAGIC = new byte[] { (byte) 0x37, (byte) 0x7A, (byte) 0xBC, (byte) 0xAF, (byte) 0x27, (byte) 0x1C  }; 
  
  @Override
  public int getMinSize() {
    return 6;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC, 0 );
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
