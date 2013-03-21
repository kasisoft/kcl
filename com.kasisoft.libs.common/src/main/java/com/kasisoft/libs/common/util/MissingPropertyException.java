/**
 * Name........: MissingPropertyException
 * Description.: Exception type that indicates that a required property is missing. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

/**
 * Exception type that indicates that a required property is missing.
 */
public class MissingPropertyException extends RuntimeException {

  private String   property;
  
  /**
   * Initializes this exception with some information about the missing property.
   *  
   * @param propertykey   The property that is missing. Neither <code>null</code> nor empty.
   */
  public MissingPropertyException( String propertykey ) {
    property  = propertykey;
  }

  /**
   * Returns the property that is missing.
   * 
   * @return   The property that is missing. Neither <code>null</code> nor empty.
   */
  public String getProperty() {
    return property;
  }
  
} /* ENDCLASS */
