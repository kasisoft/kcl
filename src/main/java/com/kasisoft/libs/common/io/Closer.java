package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Closer {

    private Consumer<Exception> errorHandler = Closer::defaultErrorHandler;

    public @NotNull Closer withErrorHandler(Consumer<Exception> handler) {
        this.errorHandler = handler != null ? handler : Closer::defaultErrorHandler;
        return this;
    }

    /**
     * Closes the supplied AutoCloseable.
     *
     * @param closeable
     *            The Closeable that has to be closed.
     */
    public void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                errorHandler.accept(ex);
            }
        }
    }

    /**
     * Like {@link #close(AutoCloseable)} without raising an error.
     *
     * @param closeable
     *            The Closeable that has to be closed.
     */
    public void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ex) {
                // don't complain
            }
        }
    }

    private static void defaultErrorHandler(@NotNull Exception ex) {
        throw KclException.wrap(ex);
    }

} /* ENDCLASS */
