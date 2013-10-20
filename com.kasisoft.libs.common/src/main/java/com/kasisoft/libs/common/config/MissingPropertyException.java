/**
 * Name........: MissingPropertyException
 * Description.: Exception type that indicates that a required property is missing. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.config;

/**
 * Exception type that indicates that a required property is missing.
 */
@SuppressWarnings("deprecation")
public class MissingPropertyException extends com.kasisoft.libs.common.util.MissingPropertyException {

  /**
   * Initializes this exception with some information about the missing property.
   *  
   * @param propertykey   The property that is missing. Neither <code>null</code> nor empty.
   */
  public MissingPropertyException( String propertykey ) {
    super( propertykey );
  }
  
} /* ENDCLASS */
