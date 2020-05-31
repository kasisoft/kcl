package com.kasisoft.libs.common.old.xml.adapters;

import java.util.function.BiConsumer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * Simple adapter for long types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongAdapter extends TypeAdapter<String,Long> {

  private static final String MAX   = "MAX";
  private static final String MIN   = "MIN";
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public LongAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public LongAdapter( BiConsumer<Object,Exception> handler, String defval1, Long defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull Long v ) {
    return v.toString();
  }

  @Override
  public Long unmarshalImpl( @NonNull String v ) {
    if( MAX.equalsIgnoreCase( v ) ) {
      return Long.valueOf( Long.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Long.valueOf( Long.MIN_VALUE );
    } else {
      return Long.valueOf( v );
    }
  }

} /* ENDCLASS */
