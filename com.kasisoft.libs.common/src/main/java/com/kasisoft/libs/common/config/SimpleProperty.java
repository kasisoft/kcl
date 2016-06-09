package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * This type allows to easily make use of typed properties. It's being essentially used as specified in the following
 * code segments:
 * 
 * <pre>
 * interface MyProperties {
 *   
 *   ...
 *   SimpleProperty<URL> Website = new SimpleProperty<>( "website", new URLAdapter() );
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
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleProperty<T> extends AbstractProperty<T, T, SimpleProperty> {

  /** Maybe <code>null</code>. */
  @Getter T   defaultValue;

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public SimpleProperty( @NonNull String property, @NonNull TypeAdapter<String, T> typeadapter ) {
    super( property, typeadapter, false );
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public SimpleProperty( @NonNull String property, @NonNull TypeAdapter<String, T> typeadapter, boolean req ) {
    super( property, typeadapter, req );
  }
  
  /**
   * Configures the default value for this property.
   * 
   * @param defvalue   The new default value for this property. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public SimpleProperty<T> withDefault( T defvalue ) {
    defaultValue = defvalue;
    return this;
  }
  
  /**
   * Configures the default for this property.
   * 
   * @param defvalue   The new textual default value for this property. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public SimpleProperty<T> withTextualDefault( String defvalue ) {
    return withDefault( getAdapter().unmarshal( defvalue ) );
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Map<String, String> properties, T newvalue ) {
    setProperty( properties, getKey(), newvalue );
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Properties properties, T newvalue ) {
    setProperty( properties, getKey(), newvalue );
  }

  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public T getValue( @NonNull Map<String, String> properties ) {
    return checkForResult( getTypedValue( getProperty( properties, getKey() ), getDefaultValue() ) );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public T getValue( @NonNull Properties properties ) {
    return checkForResult( getTypedValue( getProperty( properties, getKey() ), getDefaultValue() ) );
  }

  /**
   * Returns the textual value provides with the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public String getTextualValue( @NonNull Properties properties ) {
    return getProperty( properties, getKey() );
  }

  /**
   * Returns the textual value provides with the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public String getTextualValue( @NonNull Map<String, String> properties ) {
    return getProperty( properties, getKey() );
  }

  private T checkForResult( T result ) {
    if( (result == null) && isRequired() ) {
      // damn, we need to complain here
      throw new MissingPropertyException( getKey() );
    }
    return result;
  }
  
} /* ENDCLASS */
