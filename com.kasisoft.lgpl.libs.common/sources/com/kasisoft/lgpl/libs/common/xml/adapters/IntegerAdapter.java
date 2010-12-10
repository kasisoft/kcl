/**
 * Name........: IntegerAdapter
 * Description.: Simple adapter for integer types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.tools.diagnostic.*;


/**
 * Simple adapter for integer types.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class IntegerAdapter extends NullSafeAdapter<String,Integer> {

  private static final String MAX   = "MAX";
  private static final String MIN   = "MIN";
  
  /**
   * {@inheritDoc}
   */
  public String marshalImpl( Integer v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Integer unmarshalImpl( String v ) {
    if( MAX.equalsIgnoreCase( v ) ) {
      return Integer.valueOf( Integer.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Integer.valueOf( Integer.MIN_VALUE );
    } else {
      return Integer.valueOf( v );
    }
  }

} /* ENDCLASS */
