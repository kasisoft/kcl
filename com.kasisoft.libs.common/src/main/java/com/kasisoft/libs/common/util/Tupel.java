/**
 * Name........: Tupel
 * Description.: Simple class used to work as a container (f.e. out-parameters).
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

/**
 * Simple class used to work as a container (f.e. out-parameters).
 */
public class Tupel<T> {

  private T[]   values;
  
  /**
   * Changes the current values.
   * 
   * @param newvalues   The new values.
   */
  public Tupel( T ... newvalues ) {
    values = newvalues;
  }

  /**
   * Returns the current value. Generally this is the first value {@link #getFirst()}.
   * 
   * @return   The current value.
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
   * @param newvalues   The new values.
   */
  public void setValues( T ... newvalues ) {
    values = newvalues;
  }
  
  /**
   * Returns the current values.
   * 
   * @return   The current values.
   */
  public T[] getValues() {
    return values;
  }
  
} /* ENDCLASS */
