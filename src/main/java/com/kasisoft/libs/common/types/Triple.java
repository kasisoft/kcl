package com.kasisoft.libs.common.types;

import javax.validation.constraints.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Triple<T1, T2, T3> implements HasFirstAndLast<T1, T3>{

  T1   value1;
  T2   value2;
  T3   value3;
  
  /**
   * Changes the current values.
   * 
   * @param val1   The first value.
   * @param val2   The second value.
   * @param val3   The third value.
   */
  public void setValues(T1 val1, T2 val2, T3 val3) {
    value1 = val1;
    value2 = val2;
    value3 = val3;
  }

  @Override
  public @NotNull Optional<T1> findFirst() {
    return Optional.ofNullable(value1);
  }

  @Override
  public @NotNull Optional<T3> findLast() {
    return Optional.ofNullable(value3);
  }
  
} /* ENDCLASS */
