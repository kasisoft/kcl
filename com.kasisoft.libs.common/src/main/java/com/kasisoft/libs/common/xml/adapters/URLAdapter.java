/**
 * Name........: URLAdapter
 * Description.: Simple adapter for URL types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import java.net.*;

/**
 * Simple adapter for URL types.
 */
public class URLAdapter extends TypeAdapter<String,URL> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public URLAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public URLAdapter( SimpleErrorHandler handler, String defval1, URL defval2 ) {
    super( handler, defval1, defval2 );
  }

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
