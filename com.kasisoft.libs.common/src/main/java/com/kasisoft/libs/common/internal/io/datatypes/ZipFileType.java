package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.datatypes.*;

import com.kasisoft.libs.common.util.*;

import lombok.*;

import lombok.experimental.*;

/**
 * FileType for 'zip' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZipFileType implements FileType {

  static final byte[] MAGIC = new byte[] { 'P', 'K', 3, 4 }; 
  
  @Override
  public int getMinSize() {
    return 4;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC, 0 );
  }

  @Override
  public String getMimeType() {
    return MimeType.Zip.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".zip";
  }

} /* ENDCLASS */
