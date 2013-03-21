/**
 * Name........: TypedProperty
 * Description.: This type allows to easily make use of typed properties.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: Daniel.Kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

/**
 * This type allows to easily make use of typed properties. It's being essentially used as specified in the following
 * code segments:
 * 
 * <pre>
 * interface MyProperties {
 *   
 *   ...
 *   TypedProperty<URL> Website = new TypedProperty<URL>( "website", new URLAdapter(), false );
 *   ...
 *   
 * }
 * </pre>
 * 
 * After that it can be used as followed:
 * 
 * <pre>
 * 
 *   ...
 *   import static MyProperties.*;
 *   ...
 *   {
 *      Properties props = ...
 *      URL        site  = Website.getValue( props );
 *   }
 *   ...
 *   
 * </pre>
 * 
 * If the {@link TypeAdapter} instance shall generate an exception it's advisable to make use of the 
 * {@link MissingPropertyException}.
 */
public class TypedProperty<T> {

  private String                  key;
  private TypeAdapter<String,T>   adapter;
  private boolean                 required;

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public TypedProperty( String property, TypeAdapter<String,T> typeadapter ) {
    this( property, typeadapter, false );
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public TypedProperty( String property, TypeAdapter<String,T> typeadapter, boolean req ) {
    key       = property;
    adapter   = typeadapter;
    required  = req;
  }
  
  /**
   * Returns the key that is used to access the data.
   * 
   * @return   The key that is used to access the data. Neither <code>null</code> nor empty.
   */
  public String getKey() {
    return key;
  }
  
  /**
   * Returns <code>true</code> if this property is required.
   * 
   * @return   <code>true</code> <=> This property is required.
   */
  public boolean isRequired() {
    return required;
  }
  
  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( Map<String,String> properties, T newvalue ) {
    if( newvalue == null ) {
      properties.remove( key );
    } else {
      properties.put( key, adapter.marshal( newvalue ) );
    }
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( Properties properties, T newvalue ) {
    if( newvalue == null ) {
      properties.remove( key );
    } else {
      properties.setProperty( key, adapter.marshal( newvalue ) );
    }
  }

  /**
   * Applies the supplied value to the system properties.
   * 
   * @param newvalue   The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( T newvalue ) {
    setValue( System.getProperties(), newvalue );
  }

  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public T getValue( Map<String,String> properties ) {
    return getValue( properties, null );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * @param defvalue     A default value to be used in case this property isn't available. Maybe <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public T getValue( Map<String,String> properties, T defvalue ) {
    return getValueImpl( getStringValue( properties ), defvalue );
  }

  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public T getValue( Properties properties ) {
    return getValue( properties, null );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * @param defvalue     A default value to be used in case this property isn't available. Maybe <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public T getValue( Properties properties, T defvalue ) {
    return getValueImpl( getStringValue( properties ), defvalue );
  }

  /**
   * Returns the current value provided by the system properties.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public T getValue() {
    return getValue( (T) null );
  }
  
  /**
   * Returns the current value provided by the system properties.
   * 
   * @param defvalue   A default value to be used in case this property isn't available. Maybe <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public T getValue( T defvalue ) {
    return getValueImpl( getStringValueImpl( null ), defvalue );
  }

  /**
   * Delivers the typed value provided by it's textual representation.
   * 
   * @param value      The current textual presentation. Maybe <code>null</code>.
   * @param defvalue   The default value which is supposed to be used if the value isn't valid. Maybe <code>null</code>.
   * 
   * @return   The typed value. Maybe <code>null</code>.
   */
  private T getValueImpl( String value, T defvalue ) {
    T result = null;
    if( value != null ) {
      // conversion errors will only be filed if the adapter comes with a specific handling
      result = adapter.unmarshal( value );
    }
    if( result == null ) {
      result = defvalue;
    }
    if( (result == null) && required ) {
      // damn, we need to complain here
      throw new MissingPropertyException( key );
    }
    return result;
  }
  
  /**
   * Returns the textual value provided by the supplied properties (falls back to the system properties).
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * 
   * @return   The textual value providing the value. Maybe <code>null</code>. 
   */
  private String getStringValue( Map<String,String> properties ) {
    String result = properties != null ? properties.get( key ) : null;
    return getStringValueImpl( result );
  }

  /**
   * Returns the textual value provided by the supplied properties (falls back to the system properties).
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * 
   * @return   The textual value providing the value. Maybe <code>null</code>. 
   */
  private String getStringValue( Properties properties ) {
    String result = properties != null ? properties.getProperty( key ) : null;
    return getStringValueImpl( result );
  }

  /**
   * Performs the fall back to the system property if necessary.
   * 
   * @param value   The current value as provided by the properties. Maybe <code>null</code>.
   * 
   * @return   The textual value. Maybe <code>null</code>.
   */
  private String getStringValueImpl( String value ) {
    String result = value;
    if( result == null ) {
      result = System.getProperty( key );
    }
    return StringFunctions.cleanup( result );
  }
  
} /* ENDCLASS */
