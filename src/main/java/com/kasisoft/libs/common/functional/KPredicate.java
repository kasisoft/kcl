package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KPredicate<T> {

    boolean test(T arg) throws Exception;

    default KPredicate<T> and(@NotNull KPredicate<? super T> other) {
        return $t -> test($t) && other.test($t);
    }

    @NotNull
    default KPredicate<T> negate() {
        return $t -> !test($t);
    }

    @NotNull
    default KPredicate<T> or(@NotNull KPredicate<? super T> other) {
        return $t -> test($t) || other.test($t);
    }

    @NotNull
    default Predicate<T> protect() {
        return $t -> {
            try {
                return test($t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
