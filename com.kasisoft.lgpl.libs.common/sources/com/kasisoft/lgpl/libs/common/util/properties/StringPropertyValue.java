/**
 * Name........: StringPropertyValue
 * Description.: PropertyValue implementation just representing a simple String.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util.properties;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * PropertyValue implementation just representing a simple String.
 */
public class StringPropertyValue implements PropertyValue {

  private String   content;
  
  /**
   * Initialises this value using the supplied value.
   * 
   * @param val   The string value used to be stored within this type. Neither <code>null</code> nor empty.
   */
  public StringPropertyValue( @KNotEmpty(name="val") String val ) {
    content = val;
  }
  
  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    return content.hashCode();
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString() {
    return content;
  }
  
} /* ENDCLASS */
