package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record Pair<T1, T2>(T1 value1, T2 value2) implements HasFirstAndLast<T1, T2> {

    @Override
    @NotNull
    public Optional<T1> findFirst() {
        return Optional.ofNullable(value1());
    }

    @Override
    @NotNull
    public Optional<T2> findLast() {
        return Optional.ofNullable(value2());
    }

} /* ENDRECORD */
