/**
 * Name........: Predefined
 * Description.: Collection of predefined types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.functionality;

import java.util.*;

/**
 * Collection of predefined types.
 */
public class Predefined {

  /**
   * Prevent instantiation.
   */
  private Predefined() {
  }
  
  /**
   * Creates a new filter which combines the parameters with an OR operation.
   * 
   * @param filters   The filters to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing an OR operation on both parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> or( Filter<T> ... filters ) {
    return new Or<T>( filters );
  }

  /**
   * Creates a new filter which combines the parameters with an AND operation.
   * 
   * @param filters   The filters to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing an AND operation on both parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> and( Filter<T> ... filters ) {
    return new And<T>( filters );
  }

  /**
   * Creates a new filter which combines the parameter with a NOT operation.
   * 
   * @param left    One filter to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing a NOT operation on the parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> not( Filter<T> inner ) {
    return new Not<T>( inner );
  }
  
  /**
   * Returns a Transform instance allowing to transform any kind of type into a String.
   * <code>null</code> values remain <code>null</code> values.
   * 
   * @param clazz   The type which has to be transformed. Not <code>null</code>.
   * 
   * @return   The Transform instance allowing to transform any kind of type into a String.
   *           Not <code>null</code>.
   */
  public static final <T> Transform<T,String> toStringTransform( Class<T> clazz ) {
    return new ToString<T>();
  }

  /**
   * Returns a Transform instance allowing to transform any kind of key from a Map.Entry record 
   * into a String. <code>null</code> values remain <code>null</code> values.
   * 
   * @param keyclass     The type of the key. Not <code>null</code>.
   * @param valueclass   The type of the value. Not <code>null</code>.
   * 
   * @return   The Transform instance allowing to transform any kind of type into a String.
   *           Not <code>null</code>.
   */
  public static final <K,V> Transform<Map.Entry<K,V>,String> toStringKeyTransform( Class<K> keyclass, Class<V> valueclass ) {
    return new KeyToString<K,V>();
  }

  /**
   * Returns a Transform instance allowing to transform any kind of value from a Map.Entry record 
   * into a String. <code>null</code> values remain <code>null</code> values.
   * 
   * @param keyclass     The type of the key. Not <code>null</code>.
   * @param valueclass   The type of the value. Not <code>null</code>.
   * 
   * @return   The Transform instance allowing to transform any kind of type into a String.
   *           Not <code>null</code>.
   */
  public static final <K,V> Transform<Map.Entry<K,V>,String> toStringValueTransform( Class<K> keyclass, Class<V> valueclass ) {
    return new ValueToString<K,V>();
  }

  /** 
   * Implementation of a NOT operation.
   */
  private static class Not<T> implements Filter<T> {

    private Filter<T>   inner;
    
    public Not( Filter<T> filter ) {
      inner  = filter;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean accept( T input ) {
      return ! inner.accept( input );
    }
    
  } /* ENDCLASS */

  /** 
   * Implementation of an OR operation.
   */
  private static class Or<T> implements Filter<T> {

    private Filter<T>[]   atoms;
    
    public Or( Filter<T> ... filters ) {
      atoms = filters;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean accept( T input ) {
      for( Filter<T> filter : atoms ) {
        if( filter.accept( input ) ) {
          return true;
        }
      }
      return false;
    }
    
  } /* ENDCLASS */

  /** 
   * Implementation of an AND operation.
   */
  private static class And<T> implements Filter<T> {

    private Filter<T>[]   atoms;
    
    public And( Filter<T> ... filters ) {
      atoms = filters;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean accept( T input ) {
      for( Filter<T> filter : atoms ) {
        if( ! filter.accept( input ) ) {
          return false;
        }
      }
      return true;
    }
    
  } /* ENDCLASS */

  /**
   * Transforms a type into a String.
   */
  private static class ToString<T> implements Transform<T,String> {

    /**
     * {@inheritDoc}
     */
    public String map( T input ) {
      if( input == null ) {
        return null;
      } else {
        return input.toString();
      }
    }
    
  } /* ENDCLASS */

  /**
   * Transforms the key of a Map.Entry record into a String.
   */
  private static class KeyToString<K,V> implements Transform<Map.Entry<K,V>,String> {

    /**
     * {@inheritDoc}
     */
    public String map( Map.Entry<K,V> input ) {
      if( input == null ) {
        return null;
      } else {
        if( input.getKey() == null ) {
          return null;
        } else {
          return input.getKey().toString();
        }
      }
    }
    
  } /* ENDCLASS */

  /**
   * Transforms the value of a Map.Entry record into a String.
   */
  private static class ValueToString<K,V> implements Transform<Map.Entry<K,V>,String> {

    /**
     * {@inheritDoc}
     */
    public String map( Map.Entry<K,V> input ) {
      if( input == null ) {
        return null;
      } else {
        if( input.getValue() == null ) {
          return null;
        } else {
          return input.getValue().toString();
        }
      }
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
