package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.datatypes.*;

import com.kasisoft.libs.common.util.*;

import lombok.*;

import lombok.experimental.*;

/**
 * FileType for 'bzip2' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bzip2FileType implements FileType {

  static final byte[] MAGIC = "BZh".getBytes(); 
  
  @Override
  public int getMinSize() {
    return 3;
  }

  @Override
  public boolean isOfType( byte[] data ) {
    return ArrayFunctions.compare( data, MAGIC, 0 );
  }

  @Override
  public String getMimeType() {
    return MimeType.Bzip2.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }

  @Override
  public String getSuffix() {
    return ".bz2";
  }

} /* ENDCLASS */
