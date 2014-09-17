package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.util.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.regex.*;

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
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MapProperty<T> extends AbstractProperty<T,Map<String,T>,MapProperty> {

  private static final String FMT_PATTERN = "\\Q%s\\E\\s*(\\[\\s*(.+)\\s*\\])";

  private Map<String,T>   novalues;
  private Pattern         pattern;
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public MapProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter ) {
    super( property, typeadapter, false );
    pattern  = Pattern.compile( String.format( FMT_PATTERN, property ) );
    novalues = Collections.emptyMap();
  }
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   * @param req           <code>true</code> <=> The property must be available which means it's value is not allowed
   *                                            to be <code>null</code>.
   */
  public MapProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter, boolean req ) {
    super( property, typeadapter, req );
    pattern  = Pattern.compile( String.format( FMT_PATTERN, property ) );
    novalues = Collections.emptyMap();
  }

  /**
   * Removes all properties from a map (the supplied keys are backed for the map).
   * 
   * @param propertykeys   The supplied keys backing the map. Not <code>null</code>.
   */
  private void removeProperties( Set<?> propertykeys ) {
    List<String> toremove = new ArrayList<>( (Set<String>) propertykeys );
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
  public void setValue( @NonNull Map<String,String> properties, Map<String,T> newvalue ) {
    removeProperties( properties.keySet() );
    if( newvalue != null ) {
      for( Map.Entry<String,T> entry : newvalue.entrySet() ) {
        String key = String.format( "%s[%s]", getKey(), entry.getKey() );
        setProperty( properties, false, key, entry.getValue() );
      }
    }
  }

  /**
   * Applies the supplied value to the given properties.
   * 
   * @param properties   The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue     The new value to be set. Maybe <code>null</code>.
   */
  public void setValue( @NonNull Properties properties, Map<String,T> newvalue ) {
    removeProperties( properties.keySet() );
    if( newvalue != null ) {
      for( Map.Entry<String,T> entry : newvalue.entrySet() ) {
        String key = String.format( "%s[%s]", getKey(), entry.getKey() );
        setProperty( properties, true, key, entry.getValue() );
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
  public Map<String,T> getValue( @NonNull Map<String,String> properties ) {
    return getValue( properties, null );
  }
  
  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one. Maybe <code>null</code>.
   */
  public Map<String,T> getValue( @NonNull Properties properties ) {
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
  public Map<String,T> getValue( @NonNull Map<String,String> properties, Map<String,T> defvalue ) {
    return getTypedValues( getStringValues( properties ), defvalue );
  }

  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * @param defvalue     A default value to be used in case this property isn't available. Maybe <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public Map<String,T> getValue( @NonNull Properties properties, Map<String,T> defvalue ) {
    return getTypedValues( getStringValues( properties ), defvalue );
  }

  /**
   * Returns the textual value provided by the supplied properties (falls back to the system properties).
   * 
   * @param properties   The properties providing the current configuration. Maybe <code>null</code>.
   * 
   * @return   The textual value providing the value. Maybe <code>null</code>. 
   */
  private Map<String,String> getStringValues( Map<String,String> properties ) {
    Map<String,String> result = new HashMap<>();
    for( Map.Entry<String,String> entry : properties.entrySet() ) {
      Matcher matcher = pattern.matcher( entry.getKey() );
      if( matcher.matches() ) {
        String key  = matcher.group(2);
        result.put( key, StringFunctions.cleanup( entry.getValue() ) );
      }
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
  private Map<String,String> getStringValues( Properties properties ) {
    Map<String,String> result = new HashMap<>();
    for( Map.Entry<Object,Object> entry : properties.entrySet() ) {
      Matcher matcher = pattern.matcher( (String) entry.getKey() );
      if( matcher.matches() ) {
        String key  = matcher.group(2);
        result.put( key, StringFunctions.cleanup( (String) entry.getValue() ) );
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
  private Map<String,T> getTypedValues( Map<String,String> values, Map<String,T> defvalues ) {
    Map<String,T> result = new HashMap<>();
    if( defvalues == null ) {
      defvalues = novalues;
    } else {
      result.putAll( defvalues );
    }
    for( Map.Entry<String,String> entry : values.entrySet() ) {
      T defvalue = defvalues.get( entry.getKey() );
      T value    = getTypedValue( entry.getValue(), defvalue );
      if( value != null ) {
        result.put( entry.getKey(), value );
      }
    }
    return checkForResult( result );
  }
  
  private Map<String,T> checkForResult( Map<String,T> result ) {
    if( result.isEmpty() && isRequired() ) {
      // damn, we need to complain here
      throw new MissingPropertyException( getKey() );
    }
    return result;
  }

} /* ENDCLASS */
