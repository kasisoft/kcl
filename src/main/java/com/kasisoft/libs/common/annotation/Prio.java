package com.kasisoft.libs.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.*;

/**
 * This annotation allows to provide some ordering to class types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface Prio {

    int value() default 0;

} /* ENDINTERFACE */
