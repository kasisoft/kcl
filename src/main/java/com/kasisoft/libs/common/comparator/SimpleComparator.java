package com.kasisoft.libs.common.comparator;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Abstract comparator.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleComparator<T> implements Comparator<T> {

  Function<T, ? extends Comparable<?>>[]    getters;
  
  public SimpleComparator(@NotNull Function<T, ? extends Comparable<?>> ... getters) {
    this.getters = getters;
  }
  
  @Override
  public int compare(T o1, T o2) {
    var result = 0;
    var idx    = 0;
    while ((result == 0) && (idx < getters.length)) {
      var getter = getters[idx++];
      result = Comparators.<Comparable>nullSafeCompareTo(
        o1 != null ? getter.apply(o1) : null, 
        o2 != null ? getter.apply(o2) : null
      );
    }
    return result;
  }

} /* ENDCLASS */
