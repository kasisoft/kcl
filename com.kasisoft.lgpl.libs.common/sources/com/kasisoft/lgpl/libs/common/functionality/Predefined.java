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

/**
 * Collection of predefined types.
 */
@KDiagnostic
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

} /* ENDCLASS */
