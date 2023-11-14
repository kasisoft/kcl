package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KConsumer<T> {

    void accept(T input) throws Exception;

    default @NotNull KConsumer<T> andThen(@NotNull KConsumer<? super T> after) {
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

    default @NotNull Consumer<T> protect() {
        return (T t) -> {
            try {
                accept(t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
