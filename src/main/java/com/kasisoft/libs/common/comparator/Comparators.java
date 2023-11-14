package com.kasisoft.libs.common.comparator;

import com.kasisoft.libs.common.annotation.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Comparators {

  public static Comparator<Class>       CLASS_BY_NAME             = new SimpleComparator<Class>(Class::getName);
  public static Comparator<Class>       CLASS_BY_NAME_CI          = new SimpleComparator<Class>($ -> $.getName().toLowerCase());

  public static Comparator<Class>       CLASS_BY_SIMPLE_NAME      = new SimpleComparator<Class>(Class::getSimpleName, Class::getName);
  public static Comparator<Class>       CLASS_BY_SIMPLE_NAME_CI   = new SimpleComparator<Class>($ -> $.getSimpleName().toLowerCase(), $ -> $.getName().toLowerCase());

  public static Comparator<Class>       CLASS_BY_PRIORITY         = new SimpleComparator<Class>(Comparators::getPrio);

  public static Comparator<Integer>     INTEGER_NULLSAFE          = new SimpleComparator<Integer>(Function.identity());

  public static Comparator<String>      LENGTH                    = new SimpleComparator<String>($ -> Integer.valueOf($.length()), Function.identity());

  public static Comparator<String>      LENGTH_LONGEST_FIRST      = new SimpleComparator<String>($ -> -Integer.valueOf($.length()), Function.identity());

  private static Map<String, Integer>   PRIOS = new HashMap<>();

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

  public static <T extends Comparable<T>> int nullSafeCompareTo(T o1, T o2) {
    if ((o1 == null) && (o2 == null)) {
      return 0;
    }
    if ((o1 != null) && (o2 != null)) {
      return o1.compareTo(o2);
    }
    if (o1 != null) {
      return 1;
    } else /* if (o2 != null) */ {
      return -1;
    }
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

  /**
   * Implementation of a Comparator used for the key part of a Map.Entry.
   */
  public static <K extends Comparable<K>, V> SimpleComparator<Map.Entry<K, V>> mapKeyComparator() {
    return new SimpleComparator<Map.Entry<K, V>>($ -> $.getKey());
  }

} /* ENDCLASS */
