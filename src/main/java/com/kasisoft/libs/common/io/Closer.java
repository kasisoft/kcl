package com.kasisoft.libs.common.io;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Consumer;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Closer {

  Consumer<Exception>       errorHandler = $ -> {};
  
  public @NotNull Closer withErrorHandler(@Null Consumer<Exception> handler) {
    this.errorHandler = handler != null ? handler : $ -> {};
    return this;
  }
  
  /**
   * Closes the supplied AutoCloseable. 
   * 
   * @param closeable   The Closeable that has to be closed.
   */
  public void close(@Null AutoCloseable closeable) {
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
   * @param closeable   The Closeable that has to be closed.
   */
  public void closeQuietly(@Null AutoCloseable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ex) {
        // don't complain
      }
    }
  }

} /* ENDCLASS */
