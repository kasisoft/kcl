package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KSupplier<T> {

  T get() throws Exception;
  
  default @NotNull Supplier<T> protect() {
    return () -> {
      try {
        return get();
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    };
  }
  
} /* ENDINTERFACE */
