package com.kasisoft.libs.common.functionality;

/**
 * Transforms an input into a specific output.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated Will be removed with version 1.8 which will use Java 8.
 */
@Deprecated
public interface Transform<F,T> {

  /**
   * Maps a specific output for the supplied input.
   * 
   * @param input   The input object which has to be mapped.
   * 
   * @return   The mapped object.
   */
  T map( F input );
  
} /* ENDINTERFACE */
