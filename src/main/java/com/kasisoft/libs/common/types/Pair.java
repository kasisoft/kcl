package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("rawtypes")
public class Pair<T1, T2> implements Map.Entry<T1, T2>, HasFirstAndLast<T1, T2> {

    private T1 value1;

    private T2 value2;

    public Pair() {
    }

    public Pair(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public T1 getValue1() {
        return value1;
    }

    public void setValue1(T1 value1) {
        this.value1 = value1;
    }

    public T2 getValue2() {
        return value2;
    }

    public void setValue2(T2 value2) {
        this.value2 = value2;
    }

    /**
     * Changes the current values.
     *
     * @param val1
     *            The first value.
     * @param val2
     *            The second value.
     */
    public void setValues(T1 val1, T2 val2) {
        value1 = val1;
        value2 = val2;
    }

    @Override
    public T1 getKey() {
        return getValue1();
    }

    @Override
    public T2 getValue() {
        return getValue2();
    }

    @Override
    public T2 setValue(T2 value) {
        T2 result = value2;
        value2 = value;
        return result;
    }

    @Override
    public @NotNull Optional<T1> findFirst() {
        return Optional.ofNullable(getValue1());
    }

    @Override
    public @NotNull Optional<T2> findLast() {
        return Optional.ofNullable(getValue2());
    }

    @Override
    public int hashCode() {
        final int prime  = 31;
        int       result = 1;
        result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
        result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pair other = (Pair) obj;
        if (value1 == null) {
            if (other.value1 != null)
                return false;
        } else if (!value1.equals(other.value1))
            return false;
        if (value2 == null) {
            if (other.value2 != null)
                return false;
        } else if (!value2.equals(other.value2))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Pair [value1=" + value1 + ", value2=" + value2 + "]";
    }

} /* ENDCLASS */
