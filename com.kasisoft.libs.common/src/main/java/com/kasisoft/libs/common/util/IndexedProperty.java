/**
 * Name........: IndexedProperty
 * Description.: Accessor type for an indexed property.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;

import javax.xml.bind.annotation.adapters.*;

import java.util.*;

/**
 * Accessor type for an indexed property.
 */
public class IndexedProperty<T> {

  private static final List<Object> EMPTY_DEFAULTS = new ArrayList<Object>();
  
  private String                 key;
  private XmlAdapter<String,T>   adapter;
  
  /**
   * Initialises this accessor using a specific key and an adapter used to access the type.
   * 
   * @param property     The name of the property which has to be access. Neither <code>null</code> nor empty. 
   * @param xmladapter   The adapter used to convert the type. Not <code>null</code>.
   */
  public IndexedProperty( String property, XmlAdapter<String,T> xmladapter) {
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
   * @throws FailureException if the property value cannot be converted properly and the generation of failures is enabled.
   */
  public List<T> get( ExtProperties props ) {
    return get( props, (List<T>) null );
  }
  
  /**
   * Returns the value stored within the supplied properties. If a value cannot be converted and no exception is 
   * supposed to be thrown the default value is taken if there's one.
   * 
   * @param props       The Properties providing the values. Not <code>null</code>.
   * @param defvalues   Default values which have to be used. Maybe <code>null</code>.
   * 
   * @return   The stored values. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation of failures is enabled.
   */
  public List<T> get( ExtProperties props, List<T> defvalues ) {
    List<String> values = props.getIndexedProperty( key, (List<String>) null );
    if( values == null ) {
      return defvalues;
    } else {
      if( defvalues == null ) {
        defvalues = (List<T>) EMPTY_DEFAULTS;
      }
      List<T> result = new ArrayList<T>( values.size() );
      for( int i = 0; i < values.size(); i++ ) {
        T defvalue = i < defvalues.size() ? defvalues.get(i) : null;
        result.add( unmarshal( props.getErrorHandler(), values.get(i), defvalue ) );
      }
      return result;
    }
  }

  /**
   * Returns a specific indexed property.
   * 
   * @param props   The Properties providing the data. Not <code>null</code>.
   * @param index   The index used to access the property value. 
   *                      
   * @return   The indexed property value. Maybe <code>null</code>.
   */
  public T get( ExtProperties props, int index ) {
    return get( props, index, null );
  }

  /**
   * Returns a specific indexed property.
   * 
   * @param props      The Properties providing the data. Not <code>null</code>.
   * @param index      The index used to access the property value. 
   * @param defvalue   The default value to be returned if none can be found. Maybe <code>null</code>.
   *                      
   * @return   The indexed property value. Maybe <code>null</code>.
   */
  public T get( ExtProperties props, int index, T defvalue ) {
    String result = props.getIndexedProperty( key, index );
    return unmarshal( props.getErrorHandler(), result, defvalue );
  }

  /**
   * Changes the values for a single indexed property. 
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   * @param index   The index used to access the property value. 
   * @param value   The value which has to be stored. Maybe <code>null</code>.
   * 
   * @throws FailureException if the property value cannot be converted properly and the generation of failures is enabled.
   */
  public void set( ExtProperties props, int index, T value ) {
    props.setIndexedProperty( key, index, marshal( props.getErrorHandler(), value ) );
  }

  /**
   * Removes all indexed properties from the supplied set.
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   */
  public void remove( ExtProperties props ) {
    props.removeIndexedProperty( key );
  }

  /**
   * Removes a specific indexed property from the supplied set.
   * 
   * @param props   The Properties which need to be altered. Not <code>null</code>.
   * @param index   The index used to access the property value. 
   */
  public void remove( ExtProperties props, int index ) {
    props.removeIndexedProperty( key, index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return key.hashCode();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return key;
  }

  /**
   * {@inheritDoc}
   * 
   * Equality assumes that only one instance with a specific key is being created.
   */
  @Override
  public boolean equals( Object o ) {
    if( o == this ) {
      return true;
    }
    if( o instanceof IndexedProperty ) {
      IndexedProperty other = (IndexedProperty) o;
      return key.equals( other.key );
    } else {
      return false;
    }
  }
  
  /**
   * Marshals a value depending on the current adapter implementation.
   * 
   * @param errorhandler   The {@link SimpleErrorHandler} implementation used to communicate errors. Maybe <code>null</code>.
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
   * @param errorhandler   The {@link SimpleErrorHandler} implementation used to communicate errors. Maybe <code>null</code>.
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
