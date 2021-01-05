package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.utils.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractFileType implements FileType {

  int           minSize;
  MimeType      contentType;
  int           offset;
  List<byte[]>  magics;
  
  @Override
  public int getMinSize() {
    return minSize;
  }
  
  @Override
  public MimeType getContentType() {
    return contentType;
  }
  
  @Override
  public boolean test(byte[] data) {
    if ((data != null) && (getMinSize() <= data.length)) {
      for (byte[] magic : magics) {
        if (PrimitiveFunctions.compare(data, magic, offset)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return getContentType().getMimeType();
  }

} /* ENDCLASS */
