package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KRunnable {

  void run() throws Exception;

  default @NotNull Runnable protect() {
    return () -> {
      try {
        run();
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    };
  }

} /* ENDINTERFACE */
