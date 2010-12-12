/**
 * Name........: URLAdapter
 * Description.: Simple adapter for URL types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.net.*;

/**
 * Simple adapter for URL types.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class URLAdapter extends NullSafeAdapter<String,URL> {

  /**
   * {@inheritDoc}
   */
  public String marshalImpl( URL v ) throws Exception {
    return v.toExternalForm();
  }

  /**
   * {@inheritDoc}
   */
  public URL unmarshalImpl( String v ) throws Exception {
    return new URL( v );
  }

} /* ENDCLASS */
