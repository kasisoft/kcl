package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KFunction<T, R> {

    R apply(T input) throws Exception;

    default <V> @NotNull KFunction<V, R> compose(@NotNull KFunction<? super V, ? extends T> before) {
        return (V v) -> apply(before.apply(v));
    }

    default <V> @NotNull KFunction<T, V> andThen(@NotNull KFunction<? super R, ? extends V> after) {
        return (T t) -> after.apply(apply(t));
    }

    default @NotNull Function<T, R> protect() {
        return (T t) -> {
            try {
                return apply(t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

    static <T> @NotNull KFunction<T, T> identity() {
        return t -> t;
    }

} /* ENDINTERFACE */
