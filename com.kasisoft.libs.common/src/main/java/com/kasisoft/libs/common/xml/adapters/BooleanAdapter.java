/**
 * Name........: BooleanAdapter
 * Description.: This is an adapter that allows to handle boolean values.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

/**
 * This is an adapter that allows to handle boolean values.
 */
public class BooleanAdapter extends NullSafeAdapter<String,Boolean> {

  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( Boolean v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Boolean unmarshalImpl( String v ) {
    return Boolean.valueOf( MiscFunctions.parseBoolean( v ) );
  }

} /* ENDCLASS */
