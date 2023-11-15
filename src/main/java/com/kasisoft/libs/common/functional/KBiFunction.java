package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KBiFunction<T, U, R> {

    R apply(T input1, U input2) throws Exception;

    @NotNull
    default <V> KBiFunction<T, U, V> andThen(@NotNull KFunction<? super R, ? extends V> after) {
        return ($t, $u) -> after.apply(apply($t, $u));
    }

    @NotNull
    default BiFunction<T, U, R> protect() {
        return ($t, $u) -> {
            try {
                return apply($t, $u);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
