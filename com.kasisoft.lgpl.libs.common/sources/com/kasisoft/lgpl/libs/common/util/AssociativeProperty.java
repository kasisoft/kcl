/**
 * Name........: AssociativeProperty
 * Description.: Accessor type for an associative property.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.xml.bind.annotation.adapters.*;

import java.util.*;

/**
 * Accessor type for an associative property.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class AssociativeProperty<T> {

  private static final Map<String,Object> EMPTY_DEFAULTS = new HashMap<String,Object>();
  
  private String                 key;
  private XmlAdapter<String,T>   adapter;
  
  /**
   * Initialises this accessor using a specific key and an adapter used to access the type.
   * 
   * @param property     The name of the property which has to be access. 
   *                     Neither <code>null</code> nor empty. 
   * @param xmladapter   The adapter used to convert the type. Not <code>null</code>.
   */
  public AssociativeProperty( 
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
   * @return   The stored values. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public Map<String,T> get( @KNotNull(name="props") ExtProperties props ) {
    return get( props, (Map<String,T>) null );
  }
  
  /**
   * Returns the value stored within the supplied properties. If a value cannot be converted and
   * no exception is supposed to be thrown the default value is taken if there's one.
   * 
   * @param props       The Properties providing the values. Not <code>null</code>.
   * @param defvalues   Default values which have to be used. Maybe <code>null</code>.
   * 
   * @return   The stored values. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public Map<String,T> get( @KNotNull(name="props") ExtProperties props, Map<String,T> defvalues ) {
    Map<String,String> values = props.getAssociatedProperty( key, (Map<String,String>) null );
    if( values == null ) {
      return defvalues;
    } else {
      if( defvalues == null ) {
        defvalues = (Map<String,T>) EMPTY_DEFAULTS;
      }
      Map<String,T> result = new HashMap<String,T>();
      for( Map.Entry<String,String> entry : values.entrySet() ) {
        T defvalue = defvalues.get( entry.getKey() );
        result.put( entry.getKey(), unmarshal( props.getErrorHandler(), entry.getValue(), defvalue ) );
      }
      return result;
    }
  }

  /**
   * Returns a specific association property.
   * 
   * @param props         The Properties providing the data. Not <code>null</code>.
   * @param association   The association used to access a specific pair. 
   *                      Neither <code>null</code> nor empty.
   *                      
   * @return   The associated property value. Maybe <code>null</code>.
   */
  public T get( @KNotNull(name="props") ExtProperties props, @KNotEmpty(name="association") String association ) {
    return get( props, association, null );
  }

  /**
   * Returns a specific association property.
   * 
   * @param props         The Properties providing the data. Not <code>null</code>.
   * @param association   The association used to access a specific pair. 
   *                      Neither <code>null</code> nor empty.
   * @param defvalue      The default value to be returned if none can be found.
   *                      Maybe <code>null</code>.
   *                      
   * @return   The associated property value. Maybe <code>null</code>.
   */
  public T get( @KNotNull(name="props") ExtProperties props, @KNotEmpty(name="association") String association, T defvalue ) {
    String result = props.getAssociatedProperty( key, association );
    return unmarshal( props.getErrorHandler(), result, defvalue );
  }

  /**
   * Changes the values for a property. 
   * 
   * @param props    The Properties which need to be altered. Not <code>null</code>.
   * @param values   The values which have to be set. Not <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public void set( @KNotNull(name="props") ExtProperties props, @KNotNull(name="values") Map<String,T> values ) {
    for( Map.Entry<String,T> entry : values.entrySet() ) {
      props.setAssociatedProperty( key, entry.getKey(), marshal( props.getErrorHandler(), entry.getValue() ) );
    }
  }
  
  /**
   * Changes the values for a single associative property. 
   * 
   * @param props         The Properties which need to be altered. Not <code>null</code>.
   * @param association   The associative key used to store the property.
   *                      Neither <code>null</code> nor empty.
   * @param value         The value which has to be stored. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation
   *                          of failures is enabled.
   */
  public void set( @KNotNull(name="props") ExtProperties props, @KNotNull(name="association") String association, T value ) {
    props.setAssociatedProperty( key, association, marshal( props.getErrorHandler(), value ) );
  }

  /**
   * Removes all associative properties from the supplied set.
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   */
  public void remove( @KNotNull(name="props") ExtProperties props ) {
    props.removeAssociatedProperty( key );
  }

  /**
   * Removes a specific associative property from the supplied set.
   * 
   * @param props         The Properties which need to be altered. Not <code>null</code>.
   * @param association   The name of the association key which value has to be removed.
   *                      Neither <code>null</code> nor empty.
   */
  public void remove( @KNotNull(name="props") ExtProperties props, @KNotEmpty(name="association") String association ) {
    props.removeAssociatedProperty( key, association );
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
    if( o instanceof AssociativeProperty ) {
      AssociativeProperty other = (AssociativeProperty) o;
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
