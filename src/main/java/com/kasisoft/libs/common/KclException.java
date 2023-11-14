package com.kasisoft.libs.common;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * Specialisation of the RuntimeException which is commonly used within this library.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KclException extends RuntimeException {

    private static final long serialVersionUID = -7284302896487719569L;

    public KclException() {
        super();
    }

    public KclException(@NotNull Exception ex) {
        super(ex);
    }

    public KclException(@NotBlank String fmt, Object ... args) {
        super(fmt.formatted(args));
    }

    public KclException(@NotNull Exception ex, @NotBlank String fmt, Object ... args) {
        super(fmt.formatted(args), ex);
    }

    public static <R> R execute(@NotNull Supplier<R> supplier, @NotBlank String fmt, Object ... args) {
        try {
            return supplier.get();
        } catch (Exception ex) {
            throw KclException.wrap(ex, fmt, args);
        }
    }

    public static @NotNull KclException wrap(@NotNull Exception ex) {
        if (ex instanceof KclException kex) {
            return kex;
        }
        return new KclException(ex);
    }

    public static @NotNull KclException wrap(@NotNull Exception ex, @NotBlank String fmt, Object ... args) {
        if (ex instanceof KclException kex) {
            return kex;
        }
        return new KclException(ex, fmt, args);
    }

    public static KclException unwrap(Exception ex) {
        if (ex != null) {
            if (ex instanceof KclException kex) {
                return kex;
            } else if (ex.getCause() instanceof Exception cex) {
                return unwrap(cex);
            }
        }
        return null;
    }

} /* ENDCLASS */
