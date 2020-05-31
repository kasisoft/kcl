package com.kasisoft.libs.common.old.xml.adapters;

import java.util.function.BiConsumer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * Simple adapter for long array types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongArrayAdapter extends TypeAdapter<String, long[]> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public LongArrayAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public LongArrayAdapter( BiConsumer<Object, Exception> handler, String defval1, long[] defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull long[] v ) {
    StringBuilder builder = new StringBuilder();
    if( v.length > 0 ) {
      for( long value : v ) {
        builder.append(',').append( value );
      }
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }

  @Override
  public long[] unmarshalImpl( @NonNull String v ) {
    String[] elements = v.split(",");
    long[]   result   = new long[ elements.length ];
    for( int i = 0; i < elements.length; i++ ) {
      result[i] = Long.parseLong( elements[i] );
    }
    return result;
  }

} /* ENDCLASS */
