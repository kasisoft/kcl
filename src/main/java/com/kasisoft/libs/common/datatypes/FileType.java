package com.kasisoft.libs.common.datatypes;

import com.kasisoft.libs.common.constants.MimeType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Predicate;

/**
 * A simple definition of characteristics used to identify file types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface FileType extends Predicate<byte[]> {
  
  /**
   * Returns <code>true</code> if the supplied data indicates to be of this type.
   * 
   * @param data   The data which has to be examined.
   * 
   * @return   <code>true</code> <=> The data indicates this type.
   */
  @Override
  boolean test(@Null byte[] data);
  
  /**
   * Returns the minimum number of bytes needed to identify this type.
   * 
   * @return   The minimum number of bytes needed to identify this type.
   */
  @Min(1) int getMinSize();
  
  /**
   * Returns the primary suffix for this file type.
   * 
   * @return   The primary suffix for this file type.
   */
  default @NotBlank String getSuffix() {
    return getContentType().getPrimarySuffixWithDot();
  }

  @NotNull
  MimeType getContentType();

} /* ENDINTERFACE */
