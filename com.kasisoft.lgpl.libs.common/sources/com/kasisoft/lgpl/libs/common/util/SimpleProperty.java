/**
 * Name........: SimpleProperty
 * Description.: Accessor type for a simple property.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.xml.bind.annotation.adapters.*;

/**
 * Accessor type for a simple property.
 */
public class SimpleProperty<T> {

  private String                 key;
  private XmlAdapter<String,T>   adapter;
  
  /**
   * Initialises this accessor using a specific key and an adapter used to access the type.
   * 
   * @param property     The name of the property which has to be access. 
   *                     Neither <code>null</code> nor empty. 
   * @param xmladapter   The adapter used to convert the type. Not <code>null</code>.
   */
  public SimpleProperty( 
    @KNotEmpty(name="property")    String                 property, 
    @KNotNull(name="xmladapter")   XmlAdapter<String,T>   xmladapter
  ) {
    key     = property;
    adapter = xmladapter;
  }

  /**
   * Returns the value stored within the supplied properties.
   * 
   * @param props   The Properties providing the values. Not <code>null</code>.
   * 
   * @return   The stored value. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public T get( @KNotNull(name="props") ExtProperties props ) {
    return get( props, null );
  }
  
  /**
   * Returns the value stored within the supplied properties.
   * 
   * @param props      The Properties providing the values. Not <code>null</code>.
   * @param defvalue   A default value in case there's no corresponding property value.
   *                   Maybe <code>null</code>.
   * 
   * @return   The stored value. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public T get( @KNotNull(name="props") ExtProperties props, T defvalue ) {
    String value = props.getSimpleProperty( key );
    return unmarshal( props.getErrorHandler(), value, defvalue );
  }

  /**
   * Changes the value for a property.
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   * @param value   The value which has to be set. Not <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public void set( @KNotNull(name="props") ExtProperties props, @KNotNull(name="value") T value ) {
    props.setSimpleProperty( key, marshal( props.getErrorHandler(), value ) );
  }

  /**
   * Removes this property from the supplied Properties set.
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   */
  public void remove( @KNotNull(name="props") ExtProperties props ) {
    props.removeSimpleProperty( key );
  }
  
  /**
   * {@inheritDoc}
   */
  public int hashCode() {
    return key.hashCode();
  }
  
  /**
   * {@inheritDoc}
   */
  public String toString() {
    return key;
  }

  /**
   * {@inheritDoc}
   * 
   * Equality assumes that only one instance with a specific key is being created.
   */
  public boolean equals( Object o ) {
    if( o == this ) {
      return true;
    }
    if( o instanceof SimpleProperty ) {
      SimpleProperty other = (SimpleProperty) o;
      return key.equals( other.key );
    } else {
      return false;
    }
  }
  
  /**
   * Marshals a value depending on the current adapter implementation.
   * 
   * @param errorhandler   The {@link SimpleErrorHandler} implementation used to communicate errors.
   *                       Maybe <code>null</code>.
   * @param value          The value that has to be marshalled. Not <code>null</code>.
   * 
   * @return   The marshalled value. Maybe <code>null</code>.
   */
  private String marshal( SimpleErrorHandler errorhandler, T value ) {
    try {
      return adapter.marshal( value );
    } catch( Exception ex ) {
      if( errorhandler != null ) {
        errorhandler.failure( this, ex.getMessage(), ex );
      }
      return null;
    }
  }

  /**
   * Unmarshals a value depending on the current adapter implementation.
   * 
   * @param errorhandler   The {@link SimpleErrorHandler} implementation used to communicate errors.
   *                       Maybe <code>null</code>.
   * @param value          The value that has to be unmarshalled. Not <code>null</code>.
   * @param defvalue       A default value to be used in case of a conversion error.
   * 
   * @return   The unmarshalled value. Maybe <code>null</code>.
   */
  private T unmarshal( SimpleErrorHandler errorhandler, String value, T defvalue ) {
    try {
      return adapter.unmarshal( value );
    } catch( Exception ex ) {
      if( errorhandler != null ) {
        errorhandler.failure( this, ex.getMessage(), ex );
      }
      return defvalue;
    }
  }
  
} /* ENDCLASS */
