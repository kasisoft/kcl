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
public class Bucket<T> {

  private List<SoftReference<T>>   references;
  private BucketFactory<T>         factory;

  /**
   * Initializes this bucket for a specific type.
   * 
   * @param type   The type that should be used for the creation. Not <code>null</code>.
   */
  public Bucket( Class<T> type ) {
    this( new DefaultBucketFactory<T>( type ) );
  }
  
  /**
   * Initializes this bucket.
   * 
   * @param bucketfactory   The factory that will be used to create/reset new objects. Not <code>null</code>.
   */
  public Bucket( BucketFactory<T> bucketfactory ) {
    references  = new LinkedList<SoftReference<T>>();
    factory     = bucketfactory;
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
    synchronized( references ) {
      while( (result == null) && (! references.isEmpty()) ) {
        SoftReference<T> reference = references.remove(0);
        result                     = reference.get();
        reference.clear();
      }
    }
    if( result == null ) {
      result = factory.create();
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
    synchronized( references ) {
      references.add( new SoftReference<T>( factory.reset( object ) ) );
    }
  }

} /* ENDCLASS */
