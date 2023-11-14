package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Tupel<T> implements HasFirstAndLast<T, T> {

    private T[] values;

    private int length;

    /**
     * Changes the current values.
     *
     * @param newvalues
     *            The new values.
     */
    public Tupel(T[] newvalues) {
        setValues(newvalues);
    }

    public Tupel() {
        values = null;
        length = 0;
    }

    public T[] getValues() {
        return values;
    }

    @Override
    public @NotNull Optional<T> findLast() {
        if (length > 0) {
            return Optional.ofNullable(values[length - 1]);
        }
        return Optional.empty();
    }

    /**
     * Returns the first value if at least one has been provided.
     *
     * @return The first value.
     */
    @Override
    public @NotNull Optional<T> findFirst() {
        if (length > 0) {
            return Optional.ofNullable(values[0]);
        }
        return Optional.empty();
    }

    /**
     * Changes the current values.
     *
     * @param newvalues
     *            The new values.
     */
    public void setValues(T[] newvalues) {
        values = newvalues;
        length = newvalues != null ? newvalues.length : 0;
    }

    /**
     * Returns <code>true</code> if this Tupel doesn't contain anything.
     *
     * @return <code>true</code> <=> This Tupel doesn't contain anything.
     */
    public boolean isEmpty() {
        return length == 0;
    }

    @Override
    public String toString() {
        return "Tupel [values=" + Arrays.toString(values) + ", length=" + length + "]";
    }

} /* ENDCLASS */
