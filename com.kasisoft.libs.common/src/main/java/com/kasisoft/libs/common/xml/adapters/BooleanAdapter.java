package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;

/**
 * This is an adapter that allows to handle boolean values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BooleanAdapter extends TypeAdapter<String,Boolean> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public BooleanAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public BooleanAdapter( SimpleErrorHandler handler, String defval1, Boolean defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull Boolean v ) {
    return v.toString();
  }

  @Override
  public Boolean unmarshalImpl( @NonNull String v ) {
    return Boolean.valueOf( MiscFunctions.parseBoolean( v ) );
  }

} /* ENDCLASS */
