/**
 * Name........: IntegerAdapter
 * Description.: Simple adapter for integer types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

/**
 * Simple adapter for integer types.
 */
public class IntegerAdapter extends NullSafeAdapter<String,Integer> {

  private static final String MAX   = "MAX";
  private static final String MIN   = "MIN";
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( Integer v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
