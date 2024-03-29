package com.kasisoft.libs.common.comparator;

import com.kasisoft.libs.common.annotation.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * Collection of useful Comparators.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Comparators {

    private static Map<String, Integer> PRIOS = new HashMap<>();

    public static Comparator<String> byStringLength(boolean reverse) {
        if (reverse) {
            return new SimpleComparator<String>($ -> -Integer.valueOf($.length()), Function.identity());
        }
        return new SimpleComparator<String>($ -> Integer.valueOf($.length()), Function.identity());
    }

    public static Comparator<Integer> byInteger() {
        return new SimpleComparator<Integer>(Function.identity());
    }

    public static Integer getPrio(@NotNull Class clazz) {
        synchronized (PRIOS) {
            var key = clazz.getName();
            return PRIOS.computeIfAbsent(key, $ -> {
                var prio = (Prio) clazz.getAnnotation(Prio.class);
                if (prio != null) {
                    return prio.value();
                } else {
                    return null;
                }
            });
        }
    }

    public static Comparator<Class> byClassPrio() {
        return new SimpleComparator<Class>(Comparators::getPrio);
    }

    public static Comparator<Class> byClassName(boolean caseSensitive) {
        if (caseSensitive) {
            return new SimpleComparator<Class>(Class::getName);
        }
        return new SimpleComparator<Class>($ -> $.getName().toLowerCase());
    }

    public static Comparator<Class> byClassSimpleName(boolean caseSensitive) {
        if (caseSensitive) {
            return new SimpleComparator<Class>(Class::getSimpleName, Class::getName);
        }
        return new SimpleComparator<Class>($ -> $.getSimpleName().toLowerCase(), $ -> $.getName().toLowerCase());
    }

    public static <K extends Comparable<K>, V> SimpleComparator<Map.Entry<K, V>> myMapKey() {
        return new SimpleComparator<Map.Entry<K, V>>($ -> $.getKey());
    }

    public static <T extends Comparable<T>> int nullSafeCompareTo(T o1, T o2) {
        return nullSafeCompareTo(o1, o2, ($1, $2) -> $1.compareTo($2));
    }

    public static <T> int nullSafeCompareTo(T o1, T o2, @NotNull Comparator<T> delegate) {
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if ((o1 != null) && (o2 != null)) {
            return delegate.compare(o1, o2);
        }
        if (o1 != null) {
            return 1;
        } else /* if (o2 != null) */ {
            return -1;
        }
    }

} /* ENDCLASS */
