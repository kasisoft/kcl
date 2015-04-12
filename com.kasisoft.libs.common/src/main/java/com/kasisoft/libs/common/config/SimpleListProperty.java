package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

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
public class SimpleListProperty<T> extends AbstractProperty<T,List<T>,SimpleListProperty> {

  static final String DELIMITER = ",";
  
  /** Maybe <code>null</code>. */
  @Getter List<T>   defaultValue;

  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public SimpleListProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter ) {
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
  public SimpleListProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter, boolean req ) {
    super( property, typeadapter, req );
  }
  
  /**
   * Configures the default value for this property.
   * 
   * @param defvalue   The new default value for this property. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public SimpleListProperty<T> withDefault( List<T> defvalue ) {
    defaultValue = defvalue;
    return this;
  }
  
  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Map<String,String> properties, List<T> newvalue ) {
    setValueImpl( properties, newvalue );
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Properties properties, List<T> newvalue ) {
    setValueImpl( properties, newvalue );
  }

  private void setValueImpl( Map properties, List<T> newvalue ) {
    String value = valueAsString( newvalue );
    if( value == null ) {
      properties.remove( getKey() );
    } else {
      properties.put( getKey(), value );
    }
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public List<T> getValue( @NonNull Map<String,String> properties ) {
    List<String> values = getValueImpl( properties );
    List<T>      result = getTypedValues( values );
    if( result == null ) {
      result = defaultValue;
    }
    return checkForResult( result );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public List<T> getValue( @NonNull Properties properties ) {
    List<String> values = getValueImpl( properties );
    List<T>      result = getTypedValues( values );
    if( result == null ) {
      result = defaultValue;
    }
    return checkForResult( result );
  }

  private List<T> getTypedValues( List<String> values ) {
    List<T> result = null;
    if( values != null ) {
      result = new ArrayList<>( values.size() );
      for( int i = 0; i < values.size(); i++ ) {
        result.add( getTypedValue( values.get(i), null ) );
      }
    }
    return result;
  }
  
  /**
   * Returns the list values provided with the supplied map.
   * 
   * @param map   The map which provides the properties. Not <code>null</code>.
   * 
   * @return  The list values. Maybe <code>null</code>.
   */
  private List<String> getValueImpl( Map<?,?> map ) {
    List<String> result = null;
    String       value  = getProperty( map, getKey() );
    if( value != null ) {
      result = new ArrayList<>( Arrays.asList( value.split( DELIMITER ) ) );
    }
    return result;
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
  public String getTextualValue( @NonNull Map<String,String> properties ) {
    return getProperty( properties, getKey() );
  }
  
  private String valueAsString( List<T> values ) {
    String result = null;
    if( (values != null) && (! values.isEmpty()) ) {
      List<String> list = new ArrayList<>( values.size() );
      for( int i = 0; i < values.size(); i++ ) {
        list.add( getAdapter().marshal( values.get(i) ) );
      }
      result = StringFunctions.concatenate( DELIMITER, list );
    }
    return result;
  }

  private List<T> checkForResult( List<T> result ) {
    if( isRequired() ) {
      if( (result == null) || result.isEmpty() ) {
        // damn, we need to complain here
        throw new MissingPropertyException( getKey() );
      }
    }
    return result;
  }
  
} /* ENDCLASS */
