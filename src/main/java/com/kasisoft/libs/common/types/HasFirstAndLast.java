package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public interface HasFirstAndLast<F, L> {

    @NotNull
    Optional<F> findFirst();

    @NotNull
    Optional<L> findLast();

    default F getFirst() {
        return findFirst().orElse(null);
    }

    default L getLast() {
        return findLast().orElse(null);
    }

} /* ENDINTERFACE */
