package com.kasisoft.libs.common.util;

/**
 * Factory implementation used to fill up the Bucket.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface BucketFactory<T> {

  /**
   * Creates a new instance.
   * 
   * @return   A new instance. Not <code>null</code>.
   */
  <P extends T> P create();

  /**
   * Resets the supplied object.
   * 
   * @param object   The object that is supposed to be cleared. Not <code>null</code>.
   *          
   * @return   The supplied object. Not <code>null</code>.
   */
  <P extends T> P reset( T object );
  
} /* ENDINTERFACE */
