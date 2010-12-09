/**
 * Name........: BooleanAdapter
 * Description.: This is an adapter that allows to handle boolean values.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.libs.common.util.*;


/**
 * This is an adapter that allows to handle boolean values.
 */
public class BooleanAdapter extends NullSafeAdapter<String,Boolean> {

  /**
   * {@inheritDoc}
   */
  public String marshalImpl( Boolean v ) throws Exception {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Boolean unmarshalImpl( String v ) throws Exception {
    return Boolean.valueOf( MiscFunctions.parseBoolean( v ) );
  }

} /* ENDCLASS */
