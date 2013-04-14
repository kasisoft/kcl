/**
 * Name........: Bucket
 * Description.: Collector for often used objects like collections, maps etc.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import java.util.*;

import java.lang.ref.*;
 
/**
 * Collector for often used objects like collections, maps etc. .
 */
public abstract class Bucket<T> {

  private List<SoftReference<T>>   references;
  
  /**
   * Initializes this bucket.
   */
  public Bucket() {
    references  = new LinkedList<>();
  }
  
  /**
   * Returns the number of references currently stored.
   * 
   * @return   The number of references currently stored.
   */
  public int getSize() {
    return references.size();
  }
  
  /**
   * Creates a new object (potentially a reused one).
   * 
   * @return   A new object.
   */
  public <P extends T> T allocate() {
    T result = null;
    while( (result == null) && (! references.isEmpty()) ) {
      SoftReference<T> reference = references.remove(0);
      result                     = reference.get();
      reference.clear();
    }
    if( result == null ) {
      result = create();
    }
    return result;
  }

  /**
   * Frees the supplied object, so it's allowed to be reused.
   * 
   * @param object
   *          The object that shall be freed. Not <code>null</code>.
   */
  public void free( T object ) {
    references.add( new SoftReference<T>( reset( object ) ) );
  }

  /**
   * Creates a new instance.
   * 
   * @return   A new instance. Not <code>null</code>.
   */
  public abstract <P extends T> P create();

  /**
   * Resets the supplied object.
   * 
   * @param object
   *          The object that is supposed to be cleared. Not <code>null</code>.
   *          
   * @return   The supplied object. Not <code>null</code>.
   */
  public abstract <P extends T> P reset( T object );

} /* ENDCLASS */
