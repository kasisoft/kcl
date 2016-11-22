package com.kasisoft.libs.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

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
