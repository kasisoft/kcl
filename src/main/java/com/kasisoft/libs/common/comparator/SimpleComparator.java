package com.kasisoft.libs.common.comparator;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * Provides a Comparator that compares several fields of the compared instance
 * in a certain order.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class SimpleComparator<T> implements Comparator<T> {

    private Function<T, ? extends Comparable<?>>[] getters;

    public SimpleComparator(@NotNull Function<T, ? extends Comparable<?>> ... getters) {
        this.getters = getters;
    }

    @Override
    public int compare(T o1, T o2) {
        var result = 0;
        var idx    = 0;
        while ((result == 0) && (idx < getters.length)) {
            var getter = getters[idx++];
            result = Comparators.<Comparable> nullSafeCompareTo(
                o1 != null ? getter.apply(o1) : null,
                o2 != null ? getter.apply(o2) : null
            );
        }
        return result;
    }

} /* ENDCLASS */
