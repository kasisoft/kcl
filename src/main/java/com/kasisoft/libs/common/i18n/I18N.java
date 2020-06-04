package com.kasisoft.libs.common.i18n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple annotation providing a default value for a translatable constant.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
  
  /**
   * Returns a key which would override the field derived key name.
   * 
   * @return   A key which would override the field derived key name. Neither <code>null</code> nor empty.
   */
  String key() default "";
  
} /* ENDANNOTATION */
