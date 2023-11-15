package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KConsumer<T> {

    void accept(T input) throws Exception;

    @NotNull
    default KConsumer<T> andThen(@NotNull KConsumer<? super T> after) {
        return $t -> {
            accept($t);
            after.accept($t);
        };
    }

    @NotNull
    default Consumer<T> protect() {
        return $t -> {
            try {
                accept($t);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
