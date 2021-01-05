package com.kasisoft.libs.common.types;

import javax.validation.constraints.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.ToString;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tupel<T> implements HasFirstAndLast<T, T> {

  @Getter 
  T[]   values;
  int   length;
  
  /**
   * Changes the current values.
   * 
   * @param newvalues   The new values.
   */
  public Tupel(T ... newvalues) {
    setValues(newvalues);
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
   * @return   The first value.
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
   * @param newvalues   The new values.
   */
  public void setValues(T ... newvalues) {
    values = newvalues;
    length = newvalues != null ? newvalues.length : 0;
  }
  
  /**
   * Returns <code>true</code> if this Tupel doesn't contain anything.
   * 
   * @return   <code>true</code> <=> This Tupel doesn't contain anything.
   */
  public boolean isEmpty() {
    return length == 0;
  }
  
} /* ENDCLASS */
