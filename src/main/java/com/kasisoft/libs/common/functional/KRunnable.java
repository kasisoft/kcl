package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@FunctionalInterface
public interface KRunnable {

    void run() throws Exception;

    @NotNull
    default Runnable protect() {
        return () -> {
            try {
                run();
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        };
    }

} /* ENDINTERFACE */
