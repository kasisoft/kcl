package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple adapter for byte types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ByteAdapter extends TypeAdapter<String,Byte> {

  static final String MAX   = "MAX";
  static final String MIN   = "MIN";
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public ByteAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public ByteAdapter( SimpleErrorHandler handler, String defval1, Byte defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull Byte v ) {
    return v.toString();
  }

  @Override
  public Byte unmarshalImpl( @NonNull String v ) {
    if( MAX.equalsIgnoreCase( v ) ) {
      return Byte.valueOf( Byte.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Byte.valueOf( Byte.MIN_VALUE );
    } else {
      return Byte.valueOf( v );
    }
  }

} /* ENDCLASS */
