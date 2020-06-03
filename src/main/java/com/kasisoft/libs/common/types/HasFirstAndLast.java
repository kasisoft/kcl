package com.kasisoft.libs.common.types;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Optional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface HasFirstAndLast<F, L> {

  @NotNull Optional<F> findFirst();
  
  @NotNull Optional<L> findLast();

  default @Null F getFirst() {
    return findFirst().orElse(null);
  }
  
  default @Null L getLast() {
    return findLast().orElse(null);
  }
  
} /* ENDINTERFACE */
