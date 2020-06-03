package com.kasisoft.libs.common.types;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Map;
import java.util.Optional;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pair<T1, T2> implements Map.Entry<T1, T2>, HasFirstAndLast<T1, T2> {

  T1   value1;
  T2   value2;
  
  /**
   * Changes the current values.
   * 
   * @param val1   The first value. Maybe <code>null</code>.
   * @param val2   The second value. Maybe <code>null</code>.
   */
  public void setValues(@Null T1 val1, @Null T2 val2) {
    value1 = val1;
    value2 = val2;
  }

  @Override
  public @Null T1 getKey() {
    return getValue1();
  }

  @Override
  public @Null T2 getValue() {
    return getValue2();
  }

  @Override
  public @Null T2 setValue(@Null T2 value) {
    T2 result = value2;
    value2    = value;
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
  
} /* ENDCLASS */
