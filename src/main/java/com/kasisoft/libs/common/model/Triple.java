package com.kasisoft.libs.common.model;

import lombok.experimental.*;

import lombok.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data @NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Triple<T1, T2, T3> {

  T1   value1;
  T2   value2;
  T3   value3;
  
  /**
   * Changes the current values.
   * 
   * @param val1   The first value. Maybe <code>null</code>.
   * @param val2   The second value. Maybe <code>null</code>.
   * @param val3   The third value. Maybe <code>null</code>.
   */
  public void setValues( T1 val1, T2 val2, T3 val3 ) {
    value1 = val1;
    value2 = val2;
    value3 = val3;
  }
  
} /* ENDCLASS */
