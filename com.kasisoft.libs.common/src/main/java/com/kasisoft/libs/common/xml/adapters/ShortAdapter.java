/**
 * Name........: ShortAdapter
 * Description.: Simple adapter for short types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

/**
 * Simple adapter for short types.
 */
public class ShortAdapter extends TypeAdapter<String,Short> {

  private static final String MAX   = "MAX";
  private static final String MIN   = "MIN";
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public ShortAdapter() {
    this( null, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public ShortAdapter( SimpleErrorHandler handler, String defval1, Short defval2 ) {
    super( handler, defval1, defval2 );
  }

  @Override
  public String marshalImpl( Short v ) {
    return v.toString();
  }

  @Override
  public Short unmarshalImpl( String v ) {
    if( MAX.equalsIgnoreCase( v ) ) {
      return Short.valueOf( Short.MAX_VALUE );
    } else if( MIN.equalsIgnoreCase( v ) ) {
      return Short.valueOf( Short.MIN_VALUE );
    } else {
      return Short.valueOf( v );
    }
  }

} /* ENDCLASS */
