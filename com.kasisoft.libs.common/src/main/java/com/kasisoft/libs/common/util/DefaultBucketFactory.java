/**
 * Name........: DefaultBucketFactory
 * Description.: Default implementation of a BucketFactory. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import java.lang.reflect.*;

import lombok.*;

/**
 * Default implementation of a BucketFactory.
 */
public class DefaultBucketFactory<T> implements BucketFactory<T> {

  private Constructor   constructor;
  private Method        reset;
  
  /**
   * Initializes this factory using the supplied type.
   * 
   * @param type   The type that is used to create entries. Not <code>null</code>.
   * 
   * @throws FailureException   There's neither a default constructor nor a method named <code>reset</code> or
   *                            <code>clear</code>.
   */
  public DefaultBucketFactory( @NonNull Class<? extends T> type ) {
    constructor = MiscFunctions.getConstructor( type );
    reset       = MiscFunctions.getMethod( type, "reset" );
    if( reset == null ) {
      reset     = MiscFunctions.getMethod( type, "clear" );
    }
    if( (constructor == null) || (reset == null) ) {
      throw FailureException.newFailureException( FailureCode.Reflections );
    }
    try {
      T probe = (T) constructor.newInstance();
      reset.invoke( probe );
    } catch( Exception ex ) {
      throw FailureException.newFailureException( FailureCode.Reflections, ex );
    }
  }
  
  @Override
  public <P extends T> P create() {
    try {
      return (P) constructor.newInstance();
    } catch( Exception ex ) {
      // won't happen as we've checked that within the constructor
      return null;
    }
  }

  @Override
  public <P extends T> P reset( @NonNull T object ) {
    try {
      reset.invoke( object );
      return (P) object;
    } catch( Exception ex ) {
      // won't happen as we've checked that within the constructor
      return null;
    }
  }

} /* ENDCLASS */
