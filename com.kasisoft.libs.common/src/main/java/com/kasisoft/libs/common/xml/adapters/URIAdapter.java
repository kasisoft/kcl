package com.kasisoft.libs.common.xml.adapters;

import lombok.*;

import java.util.function.*;

import java.net.*;

/**
 * Simple adapter for URI types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URIAdapter extends TypeAdapter<String, URI> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public URIAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public URIAdapter( BiConsumer<Object, Exception> handler, String defval1, URI defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull URI v ) throws Exception {
    return v.toURL().toExternalForm();
  }

  @Override
  public URI unmarshalImpl( @NonNull String v ) throws Exception {
    return new URI( v );
  }

} /* ENDCLASS */
