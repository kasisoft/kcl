package com.kasisoft.libs.common.old.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation allows to provide type information at runtime.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface GenericsTypes {

  Class<?>[] value();
  
} /* ENDANNOTATION */
