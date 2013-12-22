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
   */
  public DefaultBucketFactory( Class<? extends T> type ) {
    constructor = MiscFunctions.getConstructor( type );
    reset       = MiscFunctions.getMethod( type, "reset" );
    if( reset == null ) {
      reset     = MiscFunctions.getMethod( type, "clear" );
    }
    if( (constructor == null) || (reset == null) ) {
      throw new FailureException( FailureCode.Reflections );
    }
  }
  
  @Override
  public <P extends T> P create() {
    try {
      return (P) constructor.newInstance();
    } catch( Exception ex ) {
      throw new FailureException( FailureCode.Reflections, ex );
    }
  }

  @Override
  public <P extends T> P reset( T object ) {
    try {
      reset.invoke( object );
    } catch( Exception ex ) {
      throw new FailureException( FailureCode.Reflections, ex );
    }
    return (P) object;
  }

} /* ENDCLASS */
