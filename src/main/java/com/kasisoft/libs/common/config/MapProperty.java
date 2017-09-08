package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import lombok.experimental.*;

import lombok.*;

import java.util.regex.*;

import java.util.*;

/**
 * This type allows to easily make use of typed properties. It's being essentially used as specified in the following
 * code segments:
 * 
 * <pre>
 * interface MyProperties {
 *   
 *   ...
 *   MapProperty<URL> Website = new MapProperty<>( "website", new URLAdapter() );
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
 *      Properties       props = ...
 *      Map<String,URL>  site  = Website.getValues( props );
 *   }
 *   ...
 *   
 * </pre>
 * 
 * If the {@link TypeAdapter} instance shall generate an exception it's advisable to make use of the 
 * {@link MissingPropertyException}.
 * 
 * Pleae note that you can enforce {@link #getValue(Map)} to return non-null values if you're supplying an empty list
 * through {@link #withDefault(List)}. Default values to provide default values per key. That being said the result
 * will contain the defaults key-value pair if it had not been overidden by an actual pair within the properties.
 * 
 * <strong>Also note that the API changed slightly with version 1.7 as non existing values resulted in empty lists
 * rather than <code>null</code>.</strong> 
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapProperty<T> extends AbstractProperty<T, Map<String, T>, MapProperty> {

  private static final String FMT_PATTERN = "\\Q%s\\E\\s*(\\[\\s*(.+)\\s*\\])";

  Pattern                 pattern;
  
  /** Maybe <code>null</code>. */
  @Getter Map<String,T>   defaultValue;


  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public MapProperty( String property, TypeAdapter<String, T> typeadapter ) {
    super( property, typeadapter, false );
    pattern  = Pattern.compile( String.format( FMT_PATTERN, property ) );
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public MapProperty( String property, TypeAdapter<String, T> typeadapter, boolean req ) {
    super( property, typeadapter, req );
    pattern  = Pattern.compile( String.format( FMT_PATTERN, property ) );
  }

  /**
   * Configures the default value for this property.
   * 
   * @param defvalue   The new default value for this property. Maybe <code>null</code>.
   * 
   * @return   this
   */
  public MapProperty<T> withDefault( Map<String, T> defvalue ) {
    defaultValue = defvalue;
    return this;
  }

  /**
   * Removes all properties from a map (the supplied keys are backed for the map).
   * 
   * @param propertykeys   The supplied keys backing the map. Not <code>null</code>.
   */
  private void removeProperties( Set<?> propertykeys ) {
    val toremove = new ArrayList<String>( (Set<String>) propertykeys );
    for( int i = toremove.size() - 1; i >= 0; i-- ) {
      if( ! pattern.matcher( toremove.get(i) ).matches() ) {
        toremove.remove(i);
      }
    }
    propertykeys.removeAll( toremove );
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Map<String, String> properties, Map<String, T> newvalue ) {
    setValueImpl( properties, newvalue );
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Properties properties, Map<String, T> newvalue ) {
    setValueImpl( properties, newvalue );
  }

  private void setValueImpl( Map props, Map<String, T> newvalue ) {
    removeProperties( props.keySet() );
    if( newvalue != null ) {
      for( Map.Entry<String, T> entry : newvalue.entrySet() ) {
        val key = String.format( "%s[%s]", getKey(), entry.getKey() );
        setProperty( props, key, entry.getValue() );
      }
    }
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public Map<String, T> getValue( @NonNull Map<String, String> properties ) {
    Map<String, String> values = getValueImpl( properties );
    return checkForResult( getTypedValues( values, null ) );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public Map<String, T> getValue( @NonNull Properties properties ) {
    Map<String, String> values = getValueImpl( properties );
    return checkForResult( getTypedValues( values, null ) );
  }

  /**
   * Returns the map value provided by the supplied properties.
   * 
   * @param map   The properties providing the current configuration. Maybe <code>null</code>.
   * 
   * @return   The map value providing the content. Not <code>null</code>. 
   */
  private Map<String, String> getValueImpl( Map<?, ?> map ) {
    val result = new Hashtable<String, String>();
    for( Object propkey : map.keySet() ) {
      Matcher matcher = pattern.matcher( (String) propkey );
      if( matcher.matches() ) {
        val key    = matcher.group(2);
        val value  = getProperty( map, (String) propkey );
        if( value != null ) {
          result.put( key, value );
        }
      }
    }
    return result;
  }
  
  /**
   * Returns the typed values from their String representations.
   * 
   * @param values      The String values. Not <code>null</code>.
   * @param defvalues   The default values provided by the caller. Maybe <code>null</code>.
   * 
   * @return   The typed values. Not <code>null</code>.
   */
  private Map<String, T> getTypedValues( Map<String, String> values, Map<String, T> defvalues ) {
    val result = new HashMap<String, T>();
    if( defaultValue != null ) {
      result.putAll( defaultValue );
    }
    if( defvalues != null ) {
      result.putAll( defvalues );
    }
    for( Map.Entry<String, String> entry : values.entrySet() ) {
      T defvalue = result.get( entry.getKey() );
      T value    = getTypedValue( entry.getValue(), defvalue );
      if( value != null ) {
        result.put( entry.getKey(), value );
      }
    }
    return result;
  }
  
  private Map<String, T> checkForResult( Map<String, T> result ) {
    if( isRequired() ) {
      if( (result == null) || result.isEmpty() ) {
        // damn, we need to complain here
        throw new MissingPropertyException( getKey() );
      }
    }
    return result;
  }

} /* ENDCLASS */
