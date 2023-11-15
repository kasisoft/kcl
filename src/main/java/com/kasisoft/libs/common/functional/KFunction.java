package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KFunction<T, R> {

    R apply(T input) throws Exception;

    @NotNull
    default <V> KFunction<V, R> compose(@NotNull KFunction<? super V, ? extends T> before) {
        return $v -> apply(before.apply($v));
    }

    @NotNull
    default <V> KFunction<T, V> andThen(@NotNull KFunction<? super R, ? extends V> after) {
        return $v -> after.apply(apply($v));
    }

    @NotNull
    default Function<T, R> protect() {
        return $t -> {
            try {
                return apply($t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

    @NotNull
    static <T> KFunction<T, T> identity() {
        return $t -> $t;
    }

} /* ENDINTERFACE */
