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

import lombok.*;

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
@ToString
@EqualsAndHashCode
public class TypedProperty<T> {

  private static final Map<String,TypedProperty<?>>   TYPEDPROPERTIES = new Hashtable<String,TypedProperty<?>>();
  
  private String                  key;
  private String                  description;
  private TypeAdapter<String,T>   adapter;
  private T                       defaultvalue;
  private boolean                 required;

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public TypedProperty( String property, TypeAdapter<String,T> typeadapter ) {
    this( property, null, typeadapter, false, null );
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
    this( property, null, typeadapter, req, null );
  }

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param defvalue      The default value which has to be used in case no value has been given. 
   *                      Maybe <code>null</code>.
   */
  public TypedProperty( String property, TypeAdapter<String,T> typeadapter, T defvalue ) {
    this( property, null, typeadapter, false, defvalue );
  }

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param desc          A description which provides further information about this property. Maybe <code>null</code>.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public TypedProperty( String property, String desc, TypeAdapter<String,T> typeadapter ) {
    this( property, desc, typeadapter, false, null );
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param desc          A description which provides further information about this property. Maybe <code>null</code>.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public TypedProperty( String property, String desc, TypeAdapter<String,T> typeadapter, boolean req ) {
    this( property, desc, typeadapter, req, null );
  }

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param desc          A description which provides further information about this property. Maybe <code>null</code>.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param defvalue      The default value which has to be used in case no value has been given. 
   *                      Maybe <code>null</code>.
   */
  public TypedProperty( String property, String desc, TypeAdapter<String,T> typeadapter, T defvalue ) {
    this( property, desc, typeadapter, false, defvalue );
  }

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param desc          A description which provides further information about this property. Maybe <code>null</code>.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   * @param defvalue      The default value which has to be used in case no value has been given. 
   *                      Maybe <code>null</code>.
   */
  private TypedProperty( String property, String desc, TypeAdapter<String,T> typeadapter, boolean req, T defvalue ) {
    key           = property;
    description   = StringFunctions.cleanup( desc );
    adapter       = typeadapter;
    required      = req;
    defaultvalue  = defvalue;
    TYPEDPROPERTIES.put( property, this );
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
      // use the default value provided by the caller
      result = defvalue;
    }
    if( result == null ) {
      // use the default value provided by this property
      result = defaultvalue;
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

  /**
   * Small helper function which always delivers a property value as a text.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * @param nullvalue    A specific value which has to be delivered in case of a <code>null</code> value. 
   *                     Maybe <code>null</code>.
   * 
   * @return   The textual value of this property. Maybe <code>null</code> if <param>nullvalue</param> was.
   */
  private String getValueAsText( Map<String,String> properties, String nullvalue ) {
    T value = null;
    try {
      value = getValue( properties );
    } catch( MissingPropertyException ex ) {
      // legal case for a required but missing property, so value stays null
    }
    if( value == null ) {
      return nullvalue;
    } else {
      return adapter.marshal( value );
    }
  }
  
  /**
   * Returns a simple help text about the currently registered properties.
   * 
   * @return   A simple help text about the currently registered properties. Not <code>null</code>.
   */
  public static final String help() {
    StringFBuffer buffer = new StringFBuffer();
    List<String>  keys   = new ArrayList<String>( TYPEDPROPERTIES.keySet() );
    Collections.sort( keys );
    for( String key : keys ) {
      TypedProperty<?> property = TYPEDPROPERTIES.get( key );
      buffer.appendF( "%s ", key );
      buffer.appendF( "(%s) ", property.required ? "mandatory" : "optional" );
      if( property.defaultvalue != null ) {
        buffer.appendF( "(default=%s) ", property.defaultvalue );
      }
      buffer.appendF( ": %s\n", property.description != null ? property.description : "" );
    }
    return buffer.toString();
  }
  
  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( Map<String,String> properties ) {
    return createReplacementMap( properties, "%%%s%%", "" );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( Properties properties ) {
    return createReplacementMap( properties, "%%%s%%", "" );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Not <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( Map<String,String> properties, String format, String nullvalue ) {
    Map<String,String> result = new HashMap<String,String>();
    for( TypedProperty property : TYPEDPROPERTIES.values() ) {
      String keypattern = String.format( format, property.getKey() );
      String value      = property.getValueAsText( properties, nullvalue );
      result.put( keypattern, value );
    }
    return result;
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Not <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( Properties properties, String format, String nullvalue ) {
    Map<String,String> result = new HashMap<String,String>();
    for( TypedProperty property : TYPEDPROPERTIES.values() ) {
      String keypattern = String.format( format, property.getKey() );
      String value      = property.getValueAsText( properties, nullvalue );
      result.put( keypattern, value );
    }
    return result;
  }

} /* ENDCLASS */
