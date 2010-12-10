/**
 * Name........: DoubleAdapter
 * Description.: Simple adapter for double types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;


/**
 * Simple adapter for double types.
 */
public class DoubleAdapter extends NullSafeAdapter<String,Double> {

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
   * {@inheritDoc}
   */
  public String marshalImpl( Double v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Double unmarshalImpl( String v ) {
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
