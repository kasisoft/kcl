/**
 * Name........: URLAdapter
 * Description.: Simple adapter for URL types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import java.net.*;

/**
 * Simple adapter for URL types.
 */
public class URLAdapter extends NullSafeAdapter<String,URL> {

  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( URL v ) throws Exception {
    return v.toExternalForm();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL unmarshalImpl( String v ) throws Exception {
    return new URL( v );
  }

} /* ENDCLASS */
