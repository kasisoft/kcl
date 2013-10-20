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
 * 
 * @deprecated Use {@link com.kasisoft.libs.common.config.MissingPropertyException} instead. This type will be deleted
 *             with release 1.1 .
 */
@Deprecated
public class MissingPropertyException extends com.kasisoft.libs.common.config.MissingPropertyException {

  /**
   * Initializes this exception with some information about the missing property.
   *  
   * @param propertykey   The property that is missing. Neither <code>null</code> nor empty.
   */
  public MissingPropertyException( String propertykey ) {
    super( propertykey );
  }

} /* ENDCLASS */
