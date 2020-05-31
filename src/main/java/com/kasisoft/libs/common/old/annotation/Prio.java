package com.kasisoft.libs.common.old.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This annotation allows to provide some ordering to class types.
 * 
 * @see com.kasisoft.libs.common.old.old.comparator.PrioComparator
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface Prio {

  int value() default 0;
  
} /* ENDINTERFACE */
