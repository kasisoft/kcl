package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KSupplier<T> {

    T get() throws Exception;

    @NotNull
    default Supplier<T> protect() {
        return () -> {
            try {
                return get();
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
