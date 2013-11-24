/**
 * Name........: FloatAdapter
 * Description.: Simple adapter for float types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

/**
 * Simple adapter for float types.
 */
public class FloatAdapter extends TypeAdapter<String,Float> {

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
  public FloatAdapter() {
    this( null, null, null );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public FloatAdapter( SimpleErrorHandler handler, String defval1, Float defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( Float v ) {
    return v.toString();
  }

  @Override
  public Float unmarshalImpl( String v ) {
    if( NAN.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.NaN );
    } else if( NEG_INF.equalsIgnoreCase( v ) || NEG_INFINITY.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.NEGATIVE_INFINITY );
    } else if( POS_INF.equalsIgnoreCase( v ) || POS_INFINITY.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.POSITIVE_INFINITY );
    } else if( INF.equalsIgnoreCase( v ) || INFINITY.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.POSITIVE_INFINITY );
    } else if( MAX.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Float.valueOf( Float.MIN_VALUE );
    } else {
      return Float.valueOf( v );
    }
  }

} /* ENDCLASS */
