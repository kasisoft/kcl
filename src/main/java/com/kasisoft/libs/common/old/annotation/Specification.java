package com.kasisoft.libs.common.old.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is purely meant to be used for documentation purposes.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Specification {

  /**
   * Returns the location of a specification. Typically a URL instance.
   * 
   * @return   The location of a specification. Neither <code>null</code> nor empty.
   */
  String value();
  
  /**
   * Returns the date when the specification has been checked the last time.
   * 
   * @return   The date when the specification has been checked the last time. Maybe <code>null</code>.
   */
  String date() default "";
  
} /* ENDANNOTATION */