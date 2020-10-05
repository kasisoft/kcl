package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KBiPredicate<T, U> {

  boolean test(T arg1, U arg2) throws Exception;
  
  default @NotNull KBiPredicate<T, U> and(@NotNull KBiPredicate<? super T, ? super U> other) {
    return (T t, U u) -> test(t, u) && other.test(t, u);
  }

  default @NotNull KBiPredicate<T, U> negate() {
    return (T t, U u) -> !test(t, u);
  }

  default @NotNull KBiPredicate<T, U> or(@NotNull KBiPredicate<? super T, ? super U> other) {
    return (T t, U u) -> test(t, u) || other.test(t, u);
  }

  default @NotNull BiPredicate<T, U> protect() {
    return (T t, U u) -> {
      try {
        return test(t, u);
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    };
  }
  
} /* ENDINTERFACE */
