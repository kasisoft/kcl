/**
 * Name........: I18N
 * Description.: A simple annotation providing a default value for a translatable constant.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.i18n;

import java.lang.annotation.*;

/**
 * A simple annotation providing a default value for a translatable constant.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18N {

  /**
   * Returns the default value for the annotated field.
   * 
   * @return   The default value for the annotated field. Neither <code>null</code> nor empty.
   */
  String value();
  
} /* ENDANNOTATION */
