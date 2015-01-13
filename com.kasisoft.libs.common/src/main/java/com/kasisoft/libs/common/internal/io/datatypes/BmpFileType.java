package com.kasisoft.libs.common.internal.io.datatypes;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.datatypes.*;
import com.kasisoft.libs.common.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * FileType for 'bmp' files.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BmpFileType implements FileType {

  static final byte[] MAGIC = "BM".getBytes(); 
  
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
    return MimeType.Bitmap.getMimeType();
  }

  @Override
  public String toString() {
    return getMimeType();
  }
  
} /* ENDCLASS */
