package com.kasisoft.libs.common.io.datatypes;

import com.kasisoft.libs.common.constants.*;

import java.util.function.*;

import lombok.*;

/**
 * A simple definition of characteristics used to identify file types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface FileType extends Predicate<byte[]> {
  
  /**
   * Returns <code>true</code> if the supplied data indicates to be of this type.
   * 
   * @param data   The data which has to be examined. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The data indicates this type.
   */
  @Override
  boolean test( byte[] data );
  
  /**
   * Returns the minimum number of bytes needed to identify this type.
   * 
   * @return   The minimum number of bytes needed to identify this type.
   */
  int getMinSize();
  
  /**
   * Returns the mime type corresponding to this type instance.
   * 
   * @return   The mime type corresponding to this type instance. Neither <code>null</code> nor empty.
   * 
   * @deprecated [29-JUL-2018:KASI]   Will be removed with version 3.5. Use {@link #getContentType()} instead.
   */
  @Deprecated
  default String getMimeType() {
    return getContentType().getMimeType();
  }
  
  /**
   * Returns the primary suffix for this file type.
   * 
   * @return   The primary suffix for this file type. Neither <code>null</code> nor empty.
   */
  default String getSuffix() {
    return getContentType().getPrimarySuffixWithDot();
  }

  @NonNull
  MimeType getContentType();

} /* ENDINTERFACE */
