package com.kasisoft.libs.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Container for multiple {@link Specification} elements.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Specifications {

  /**
   * Returns a list of {@link Specification} elements.
   * 
   * @return   A list of {@link Specification} elements.
   */
  Specification[] value();
  
} /* ENDANNOTATION */
