/**
 * Name........: Predefined
 * Description.: Collection of predefined types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.functionality;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

/**
 * Collection of predefined types.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class Predefined {

  /**
   * Prevent instantiation.
   */
  private Predefined() {
  }
  
  /**
   * Creates a new filter which combines the parameters with an OR operation.
   * 
   * @param left    One filter to be used. Not <code>null</code>.
   * @param right   Another filter to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing an OR operation on both parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> or( 
    @KNotNull(name="left")    Filter<T>   left, 
    @KNotNull(name="right")   Filter<T>   right 
  ) {
    return new Or<T>( left, right );
  }

  /**
   * Creates a new filter which combines the parameters with an AND operation.
   * 
   * @param left    One filter to be used. Not <code>null</code>.
   * @param right   Another filter to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing an AND operation on both parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> and( 
    @KNotNull(name="left")    Filter<T>   left, 
    @KNotNull(name="right")   Filter<T>   right 
  ) {
    return new And<T>( left, right );
  }

  /**
   * Creates a new filter which combines the parameter with a NOT operation.
   * 
   * @param left    One filter to be used. Not <code>null</code>.
   * 
   * @return   A Filter performing a NOT operation on the parameters. Not <code>null</code>.
   */
  public static final <T> Filter<T> not( 
    @KNotNull(name="inner")   Filter<T>   inner 
  ) {
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
  public static final <T> Transform<T,String> toStringTransform( 
    @KNotNull(name="clazz")   Class<T>   clazz 
  ) {
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
  public static final <K,V> Transform<Map.Entry<K,V>,String> toStringKeyTransform( 
    @KNotNull(name="keyclass")     Class<K>   keyclass, 
    @KNotNull(name="valueclass")   Class<V>   valueclass 
  ) {
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
  public static final <K,V> Transform<Map.Entry<K,V>,String> toStringValueTransform( 
    @KNotNull(name="keyclass")     Class<K>   keyclass, 
    @KNotNull(name="valueclass")   Class<V>   valueclass 
  ) {
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

    private Filter<T>    left;
    private Filter<T>    right;
    
    public Or( Filter<T> leftfilter, Filter<T> rightfilter ) {
      left  = leftfilter;
      right = rightfilter;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean accept( T input ) {
      return left.accept( input ) || right.accept( input );
    }
    
  } /* ENDCLASS */

  /** 
   * Implementation of an AND operation.
   */
  private static class And<T> implements Filter<T> {

    private Filter<T>    left;
    private Filter<T>    right;
    
    public And( Filter<T> leftfilter, Filter<T> rightfilter ) {
      left  = leftfilter;
      right = rightfilter;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean accept( T input ) {
      return left.accept( input ) && right.accept( input );
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
