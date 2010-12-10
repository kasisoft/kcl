/**
 * Name........: StringAdapter
 * Description.: Simple adapter for String identity.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.tools.diagnostic.*;


/**
 * Simple adapter for String identity.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class StringAdapter extends NullSafeAdapter<String,String> {

  /**
   * {@inheritDoc}
   */
  public String marshalImpl( String v ) {
    return v;
  }

  /**
   * {@inheritDoc}
   */
  public String unmarshalImpl( String v ) {
    return v;
  }

} /* ENDCLASS */
