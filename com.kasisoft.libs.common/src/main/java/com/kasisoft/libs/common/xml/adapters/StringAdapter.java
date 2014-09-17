package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;

/**
 * Simple adapter for String identity.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringAdapter extends TypeAdapter<String,String> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public StringAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public StringAdapter( SimpleErrorHandler handler, String defval1, String defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull String v ) {
    return v;
  }

  @Override
  public String unmarshalImpl( @NonNull String v ) {
    return v;
  }

} /* ENDCLASS */
