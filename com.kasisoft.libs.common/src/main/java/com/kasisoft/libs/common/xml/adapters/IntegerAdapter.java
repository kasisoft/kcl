package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple adapter for integer types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegerAdapter extends TypeAdapter<String,Integer> {

  static final String MAX   = "MAX";
  static final String MIN   = "MIN";
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public IntegerAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public IntegerAdapter( SimpleErrorHandler handler, String defval1, Integer defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull Integer v ) {
    return v.toString();
  }

  @Override
  public Integer unmarshalImpl( @NonNull String v ) {
    if( MAX.equalsIgnoreCase( v ) ) {
      return Integer.valueOf( Integer.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Integer.valueOf( Integer.MIN_VALUE );
    } else {
      return Integer.valueOf( v );
    }
  }

} /* ENDCLASS */
