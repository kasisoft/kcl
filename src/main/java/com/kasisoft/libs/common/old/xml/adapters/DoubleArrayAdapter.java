package com.kasisoft.libs.common.old.xml.adapters;

import java.util.function.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Simple adapter for double array types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoubleArrayAdapter extends TypeAdapter<String, double[]> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public DoubleArrayAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public DoubleArrayAdapter( BiConsumer<Object, Exception> handler, String defval1, double[] defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull double[] v ) {
    StringBuilder builder = new StringBuilder();
    if( v.length > 0 ) {
      for( double value : v ) {
        builder.append(',').append( value );
      }
      builder.deleteCharAt(0);
    }
    return builder.toString();
  }

  @Override
  public double[] unmarshalImpl( @NonNull String v ) {
    String[] elements = v.split(",");
    double[]   result   = new double[ elements.length ];
    for( int i = 0; i < elements.length; i++ ) {
      result[i] = Double.parseDouble( elements[i] );
    }
    return result;
  }

} /* ENDCLASS */
