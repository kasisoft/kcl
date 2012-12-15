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

import com.kasisoft.lgpl.tools.diagnostic.*;


/**
 * This is an adapter that allows to handle boolean values.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class BooleanAdapter extends NullSafeAdapter<String,Boolean> {

  /**
   * {@inheritDoc}
   */
  public String marshalImpl( Boolean v ) {
    return v.toString();
  }

  /**
   * {@inheritDoc}
   */
  public Boolean unmarshalImpl( String v ) {
    return Boolean.valueOf( MiscFunctions.parseBoolean( v ) );
  }

} /* ENDCLASS */
