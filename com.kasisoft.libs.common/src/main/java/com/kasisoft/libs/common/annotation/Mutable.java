package com.kasisoft.libs.common.annotation;

import java.lang.annotation.*;

/**
 * This annotation indicates that a return parameter/argument etc. is mutable.
 * 
 * @author noreply@aperto.com
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface Mutable {

  /**
   * Returns <code>true</code> if the object is allowed to be altered.
   * 
   * @return   <code>true</code> <=> The object is allowed to be altered.
   */
  boolean value() default true;
  
  /**
   * If this value is set to <code>true</code> the indication of a mutable instance only applies to non empty
   * objects.  
   * 
   * @return   <code>true</code> <=> The indication of a mutable instance only applies to non empty objects.
   */
  boolean onlyNonEmpty() default true;
  
} /* ENDANNOTATION */
