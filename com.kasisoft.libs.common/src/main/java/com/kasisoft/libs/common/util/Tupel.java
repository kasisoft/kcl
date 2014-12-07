package com.kasisoft.libs.common.util;

import lombok.*;
import lombok.experimental.*;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tupel<T> {

  /** Maybe <code>null</code>. */
  @Getter T[]   values;
  
  /**
   * Changes the current values.
   * 
   * @param newvalues   The new values. Maybe <code>null</code>.
   */
  public Tupel( T ... newvalues ) {
    values = newvalues;
  }

  /**
   * Returns the current value. Generally this is the first value {@link #getFirst()}.
   * 
   * @return   The current value. Maybe <code>null</code>.
   */
  public T getValue() {
    return getFirst();
  }
  
  /**
   * Returns the last value if at least one has been provided.
   * 
   * @return   The last value. Maybe <code>null</code>.
   */
  public T getLast() {
    if( (values != null) && (values.length > 0) ) {
      return values[ values.length - 1 ];
    } else {
      return null;
    }
  }
  
  /**
   * Returns the first value if at least one has been provided.
   * 
   * @return   The first value. Maybe <code>null</code>.
   */
  public T getFirst() {
    if( (values != null) && (values.length > 0) ) {
      return values[0];
    } else {
      return null;
    }
  }
  
  /**
   * Changes the current values.
   * 
   * @param newvalues   The new values. Maybe <code>null</code>.
   */
  public void setValues( T ... newvalues ) {
    values = newvalues;
  }
  
  /**
   * Returns the length of this Tupel.
   * 
   * @return   The length of this Tupel.
   */
  public int getLength() {
    if( values != null ) {
      return values.length;
    } else {
      return 0;
    }
  }

  /**
   * Returns <code>true</code> if this Tupel doesn't contain anything.
   * 
   * @return   <code>true</code> <=> This Tupel doesn't contain anything.
   */
  public boolean isEmpty() {
    return getLength() == 0;
  }
  
} /* ENDCLASS */
