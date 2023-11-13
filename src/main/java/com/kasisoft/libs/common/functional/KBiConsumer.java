package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KBiConsumer<T, U> {

  void accept(T input1, U input2) throws Exception;

  default KBiConsumer<T, U> andThen(@NotNull KBiConsumer<? super T, ? super U> after) {
    return (l, r) -> {
      accept(l, r);
      after.accept(l, r);
    };
  }

  default @NotNull BiConsumer<T, U> protect() {
    return (T t, U u) -> {
      try {
        accept(t, u);
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    };
  }

} /* ENDINTERFACE */
