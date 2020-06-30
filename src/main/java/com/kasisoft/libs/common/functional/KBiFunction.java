package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.util.function.BiFunction;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface KBiFunction<T, U, R> {

  R apply(T input1, U input2) throws Exception;
  
  default <V> @NotNull KBiFunction<T, U, V> andThen(@NotNull KFunction<? super R, ? extends V> after) {
    return (T t, U u) -> after.apply(apply(t, u));
  }
  
  default @NotNull BiFunction<T, U, R> protect() {
    return (T t, U u) -> {
      try {
        return apply(t, u);
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    };
  }
  
} /* ENDINTERFACE */
