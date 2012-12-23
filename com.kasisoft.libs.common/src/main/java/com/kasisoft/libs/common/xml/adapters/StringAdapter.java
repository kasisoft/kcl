/**
 * Name........: StringAdapter
 * Description.: Simple adapter for String identity.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

/**
 * Simple adapter for String identity.
 */
public class StringAdapter extends NullSafeAdapter<String,String> {

  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( String v ) {
    return v;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String unmarshalImpl( String v ) {
    return v;
  }

} /* ENDCLASS */