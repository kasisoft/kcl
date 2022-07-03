package com.kasisoft.libs.common.types;

import javax.validation.constraints.*;

import java.util.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Triple<T1, T2, T3> implements HasFirstAndLast<T1, T3>{

  private T1   value1;
  private T2   value2;
  private T3   value3;

  public Triple() {
  }

  public Triple(T1 value1, T2 value2, T3 value3) {
    this.value1 = value1;
    this.value2 = value2;
    this.value3 = value3;
  }

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
  public T1 findFirst() {
    return value1;
  }

  @Override
  public T3 findLast() {
    return value3;
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

  public T3 getValue3() {
    return value3;
  }

  public void setValue3(T3 value3) {
    this.value3 = value3;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((value1 == null) ? 0 : value1.hashCode());
    result = prime * result + ((value2 == null) ? 0 : value2.hashCode());
    result = prime * result + ((value3 == null) ? 0 : value3.hashCode());
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
    Triple other = (Triple) obj;
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
    if (value3 == null) {
      if (other.value3 != null)
        return false;
    } else if (!value3.equals(other.value3))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Triple [value1=" + value1 + ", value2=" + value2 + ", value3=" + value3 + "]";
  }

} /* ENDCLASS */
