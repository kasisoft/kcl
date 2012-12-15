/**
 * Name........: NullSafeAdapter
 * Description.: Simple adapter implementation which is <code>null</code> safe.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import javax.xml.bind.annotation.adapters.*;

/**
 * Simple adapter implementation which is <code>null</code> safe. This base implementation should be used if
 * <code>null</code> values are supposed to be adapter unchanged.
 */
public abstract class NullSafeAdapter<F,T> extends XmlAdapter<F,T> {

  /**
   * {@inheritDoc}
   */
  @Override
  public final F marshal( T v ) throws Exception {
    if( v != null ) {
      return marshalImpl( v );
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final T unmarshal( F v ) throws Exception {
    if( v != null ) {
      return unmarshalImpl( v );
    }
    return null;
  }

  /**
   * @see #marshal(Object)
   * 
   * @param v   The value is guarantueed to be not <code>null</code>.
   */
  protected abstract F marshalImpl( T v ) throws Exception;

  /**
   * @see #unmarshal(Object) 
   * 
   * @param v   The value is guarantueed to be not <code>null</code>.
   */
  protected abstract T unmarshalImpl( F v ) throws Exception;

} /* ENDCLASS */
