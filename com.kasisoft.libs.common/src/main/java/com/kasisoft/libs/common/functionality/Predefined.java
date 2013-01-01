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
   * @return   The Transform instance allowing to transform any kind of type into a String.
   *           Not <code>null</code>.
   */
  public static final <T> Transform<T,String> toStringTransform() {
    return new ToString<T>();
  }

  /**
   * Returns a Transform which combines the supplied instances.
   * 
   * @param t1   A Transform which generates the source type for the second transformation. Not <code>null</code>.
   * @param t2   A second Transform which generates the result type. Not <code>null</code>.
   * 
   * @return   A Transform which combines the supplied Transform instances. Not <code>null</code>.
   */
  public static final <S1,S2,S3> Transform<S1,S3> joinTransforms( Transform<S1,S2> t1, Transform<S2,S3> t2 ) {
    return new Join<S1,S2,S3>( t1, t2 );
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
   * Returns a Transform instance allowing to transform any kind of value from a Map.Entry record into a String. 
   * <code>null</code> values remain <code>null</code> values.
   * 
   * @return   The Transform instance allowing to transform any kind of type into a String.
   *           Not <code>null</code>.
   */
  public static final <K,V> Transform<Map.Entry<K,V>,String> toStringValueTransform() {
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

  private static class Join<S1,S2,S3> implements Transform<S1,S3> {
    
    private Transform<S1,S2>   t1;
    private Transform<S2,S3>   t2;
    
    public Join( Transform<S1,S2> t1, Transform<S2,S3> t2 ) {
      this.t1 = t1;
      this.t2 = t2;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public S3 map( S1 input ) {
      return t2.map( t1.map( input ) );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
