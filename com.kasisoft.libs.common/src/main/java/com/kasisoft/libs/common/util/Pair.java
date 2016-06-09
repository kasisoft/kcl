package com.kasisoft.libs.common.util;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pair<T1, T2> implements Map.Entry<T1, T2> {

  T1   value1;
  T2   value2;
  
  /**
   * Changes the current values.
   * 
   * @param val1   The first value. Maybe <code>null</code>.
   * @param val2   The second value. Maybe <code>null</code>.
   */
  public void setValues( T1 val1, T2 val2 ) {
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
  public T2 setValue( T2 value ) {
    T2 result = value2;
    value2    = value;
    return result;
  }
  
} /* ENDCLASS */
