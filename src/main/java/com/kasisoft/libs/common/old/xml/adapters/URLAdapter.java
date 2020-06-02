package com.kasisoft.libs.common.old.xml.adapters;

import java.util.function.BiConsumer;

import java.net.URL;

import lombok.NonNull;

/**
 * Simple adapter for URL types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URLAdapter extends TypeAdapter<String, URL> {

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
  public URLAdapter( BiConsumer<Object, Exception> handler, String defval1, URL defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull URL v ) throws Exception {
    return v.toExternalForm();
  }

  @Override
  public URL unmarshalImpl( @NonNull String v ) throws Exception {
    return new URL( v );
  }

} /* ENDCLASS */