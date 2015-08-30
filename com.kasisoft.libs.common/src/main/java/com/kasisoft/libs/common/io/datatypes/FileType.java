package com.kasisoft.libs.common.io.datatypes;

import java.util.function.*;

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
   */
  String getMimeType();
  
  /**
   * Returns the primary suffix for this file type.
   * 
   * @return   The primary suffix for this file type. Neither <code>null</code> nor empty.
   */
  String getSuffix();
  
} /* ENDINTERFACE */
