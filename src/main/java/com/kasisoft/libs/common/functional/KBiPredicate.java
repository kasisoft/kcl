package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KBiPredicate<T, U> {

    boolean test(T arg1, U arg2) throws Exception;

    @NotNull
    default KBiPredicate<T, U> and(@NotNull KBiPredicate<? super T, ? super U> other) {
        return ($t, $u) -> test($t, $u) && other.test($t, $u);
    }

    @NotNull
    default KBiPredicate<T, U> negate() {
        return ($t, $u) -> !test($t, $u);
    }

    @NotNull
    default KBiPredicate<T, U> or(@NotNull KBiPredicate<? super T, ? super U> other) {
        return ($t, $u) -> test($t, $u) || other.test($t, $u);
    }

    @NotNull
    default BiPredicate<T, U> protect() {
        return ($t, $u) -> {
            try {
                return test($t, $u);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
