package com.kasisoft.libs.common.old.i18n;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A simple annotation providing the resource for the translations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface I18NBasename {

  /**
   * Returns the basename pointing to the resources that are supposed to be used.
   * 
   * @return   The basename pointing to the resources that are supposed to be used. Neither <code>null</code> nor empty.
   */
  String resource();
  
  /**
   * Returns a prefix that has to be used for each {@link I18N} annotated fieldname in order to provide distinctive 
   * names.
   * 
   * @return   The prefix that has to be used for each {@link I18N} annotated fieldname in order to provide distinctive
   *           names. Not <code>null</code>.
   */
  String prefix() default "";
  
} /* ENDANNOTATION */
