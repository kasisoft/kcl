/**
 * Name........: StringAdapter
 * Description.: Simple adapter for String identity.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;


/**
 * Simple adapter for String identity.
 */
public class StringAdapter extends NullSafeAdapter<String,String> {

  /**
   * {@inheritDoc}
   */
  public String marshalImpl( String v ) throws Exception {
    return v;
  }

  /**
   * {@inheritDoc}
   */
  public String unmarshalImpl( String v ) throws Exception {
    return v;
  }

} /* ENDCLASS */
