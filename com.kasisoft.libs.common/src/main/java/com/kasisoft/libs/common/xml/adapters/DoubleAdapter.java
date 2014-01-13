/**
 * Name........: DoubleAdapter
 * Description.: Simple adapter for double types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;

/**
 * Simple adapter for double types.
 */
public class DoubleAdapter extends TypeAdapter<String,Double> {

  private static final String NAN           = "NaN";
  private static final String NEG_INF       = "-INF";
  private static final String NEG_INFINITY  = "-INFINITY";
  private static final String POS_INF       = "+INF";
  private static final String POS_INFINITY  = "+INFINITY";
  private static final String INF           = "INF";
  private static final String INFINITY      = "INFINITY";
  private static final String MAX           = "MAX";
  private static final String MIN           = "MIN";
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public DoubleAdapter() {
    this( null, null, null );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public DoubleAdapter( SimpleErrorHandler handler, String defval1, Double defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( @NonNull Double v ) {
    return v.toString();
  }

  @Override
  public Double unmarshalImpl( @NonNull String v ) {
    if( NAN.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.NaN );
    } else if( NEG_INF.equalsIgnoreCase( v ) || NEG_INFINITY.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.NEGATIVE_INFINITY );
    } else if( POS_INF.equalsIgnoreCase( v ) || POS_INFINITY.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.POSITIVE_INFINITY );
    } else if( INF.equalsIgnoreCase( v ) || INFINITY.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.POSITIVE_INFINITY );
    } else if( MAX.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Double.valueOf( Double.MIN_VALUE );
    } else {
      return Double.valueOf( v );
    }
  }

} /* ENDCLASS */
