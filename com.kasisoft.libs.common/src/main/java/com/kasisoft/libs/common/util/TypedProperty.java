/**
 * Name........: TypedProperty
 * Description.: This type allows to easily make use of typed properties.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: Daniel.Kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.config.*;
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
 * 
 * @deprecated Use {@link SimpleProperty} instead. This type will be deleted with release 1.1 .
 */
@Deprecated
public class TypedProperty<T> extends SimpleProperty<T> {

  private static final Map<String,TypedProperty<?>>   TYPEDPROPERTIES = new Hashtable<String,TypedProperty<?>>();
  
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
    super( property, typeadapter, req );
    withDescription( desc );
    withDefault( defvalue );
    TYPEDPROPERTIES.put( property, this );
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
    T result = getValue( System.getProperties() );
    if( result == null ) {
      result = defvalue;
    }
    return result;
  }

  /**
   * Returns a simple help text about the currently registered properties.
   * 
   * @return   A simple help text about the currently registered properties. Not <code>null</code>.
   */
  public static String help() {
    return ConfigurationHelper.help( TYPEDPROPERTIES.values().toArray( new SimpleProperty[ TYPEDPROPERTIES.size() ] ) );
  }
  
  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( Map<String,String> properties ) {
    return createReplacementMap( properties, "%%%s%%", "" );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( Properties properties ) {
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
  public static Map<String,String> createReplacementMap( Map<String,String> properties, String format, String nullvalue ) {
    return ConfigurationHelper.createReplacementMap( properties, format, nullvalue, TYPEDPROPERTIES.values().toArray( new SimpleProperty[ TYPEDPROPERTIES.size() ] ) );
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
  public static Map<String,String> createReplacementMap( Properties properties, String format, String nullvalue ) {
    return ConfigurationHelper.createReplacementMap( properties, format, nullvalue, TYPEDPROPERTIES.values().toArray( new SimpleProperty[ TYPEDPROPERTIES.size() ] ) );
  }

} /* ENDCLASS */
