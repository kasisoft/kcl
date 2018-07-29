package com.kasisoft.libs.common.xml.adapters;

import java.util.function.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Simple adapter for integer array types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntArrayAdapter extends TypeAdapter<String, int[]> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public IntArrayAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public IntArrayAdapter( BiConsumer<Object, Exception> handler, String defval1, int[] defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull int[] v ) {
    StringBuilder builder = new StringBuilder();
    if( v.length > 0 ) {
      for( int value : v ) {
        builder.append(',').append( value );
      }
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }

  @Override
  public int[] unmarshalImpl( @NonNull String v ) {
    String[] elements = v.split(",");
    int[]    result   = new int[ elements.length ];
    for( int i = 0; i < elements.length; i++ ) {
      result[i] = Integer.parseInt( elements[i] );
    }
    return result;
  }

} /* ENDCLASS */
