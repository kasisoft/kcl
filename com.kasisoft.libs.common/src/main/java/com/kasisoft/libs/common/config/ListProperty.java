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
 *   ListProperty<URL> Website = new ListProperty<>( "website", new URLAdapter() );
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
 *      List<URL>  sites = Website.getValues( props );
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
public class ListProperty<T> extends AbstractProperty<T,List<T>,ListProperty> {

  private static final String FMT_PATTERN = "\\Q%s\\E\\s*(\\[\\s*(\\d+)\\s*\\])";

  private Pattern   pattern;
  
  /**
   * Initializes this typed property with the supplied adapter which is being used for the conversion. This constructor
   * creates optional properties.
   *   
   * @param property      The textual property key. Neither <code>null</code> nor empty.
   * @param typeadapter   The {@link TypeAdapter} instance which performs the actual conversion. Not <code>null</code>.
   */
  public ListProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter ) {
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
  public ListProperty( @NonNull String property, @NonNull TypeAdapter<String,T> typeadapter, boolean req ) {
    super( property, typeadapter, req );
    pattern  = Pattern.compile( String.format( FMT_PATTERN, property ) );
  }

  /**
   * Removes all properties from a map (the supplied keys are backed for the map).
   * 
   * @param propertykeys   The supplied keys backing the map. Not <code>null</code>.
   */
  private void removeProperties( @NonNull Set<?> propertykeys ) {
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

  /**
   * Applies the supplied values to the properties.
   * 
   * @param props       The properties instance that will be updated. Not <code>null</code>.
   * @param newvalue    The new value to be set. Maybe <code>null</code>.
   */
  private void setValueImpl( Map props, List<T> newvalue ) {
    removeProperties( props.keySet() );
    if( newvalue != null ) {
      for( int i = 0; i < newvalue.size(); i++ ) {
        String key = String.format( "%s[%s]", getKey(), Integer.valueOf(i) );
        setProperty( props, key, newvalue.get(i) );
      }
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
    return getTypedValues( getValueImpl( properties ) );
  }

  /**
   * Returns the current value provided by the supplied properties.
   * 
   * @param properties   The properties providing the current settings. Not <code>null</code>.
   * 
   * @return   The value if there was one or the default value. Maybe <code>null</code>.
   */
  public List<T> getValue( @NonNull Properties properties ) {
    return getTypedValues( getValueImpl( properties ) );
  }

  /**
   * Returns the list values provided with the supplied map.
   * 
   * @param map   The map which provides the properties. Not <code>null</code>.
   * 
   * @return  The list values. Not <code>null</code>.
   */
  private List<String> getValueImpl( Map<?,?> map ) {
    Map<Integer,String> result = new Hashtable<>();
    for( Map.Entry entry : map.entrySet() ) {
      Matcher matcher = pattern.matcher( (String) entry.getKey() );
      if( matcher.matches() ) {
        Integer index = Integer.valueOf( matcher.group(2) );
        result.put( index, StringFunctions.cleanup( (String) entry.getValue() ) );
      }
    }
    List<Integer> sorted = new ArrayList<>( result.keySet() );
    Collections.sort( sorted );
    List<String>  list   = new ArrayList<>();
    for( int i = 0; i < sorted.size(); i++ ) {
      list.add( result.get( sorted.get(i) ) );
    }
    return list;
  }
  
  /**
   * Returns the typed values from their String representations.
   * 
   * @param values   The String values. Not <code>null</code>.
   * 
   * @return   The typed values. Not <code>null</code>.
   */
  private List<T> getTypedValues( List<String> values ) {
    List<T> result = getAdapter().unmarshal( values );
    return checkForResult( result );
  }
  
  private List<T> checkForResult( List<T> result ) {
    if( result.isEmpty() && isRequired() ) {
      // damn, we need to complain here
      throw new MissingPropertyException( getKey() );
    }
    return result;
  }

} /* ENDCLASS */
