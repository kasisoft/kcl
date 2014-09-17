package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;

/**
 * A helper which allows to deal with the configuration properties.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ConfigurationHelper {

  /**
   * Returns a simple help text about the supplied properties.
   * 
   * @param properties   The properties that shall be turned into a help text. Maybe <code>null</code>.
   * 
   * @return   A simple help text about the supplied properties. Not <code>null</code>.
   */
  public static String help( AbstractProperty<?,?,?> ... properties ) {
    StringFBuffer                       buffer = new StringFBuffer();
    Map<String,AbstractProperty<?,?,?>> map    = new Hashtable<String,AbstractProperty<?,?,?>>();
    List<String>                        keys   = new ArrayList<String>();
    if( properties != null ) {
      for( AbstractProperty<?,?,?> property : properties ) {
        keys.add( property.getKey() );
        map.put( property.getKey(), property );
      }
    }
    Collections.sort( keys );
    for( String key : keys ) {
      AbstractProperty<?,?,?> property = map.get( key );
      buffer.appendF( "%s ", key );
      buffer.appendF( "(%s) ", property.isRequired() ? "mandatory" : "optional" );
      if( property instanceof SimpleProperty ) {
        SimpleProperty simpleproperty = (SimpleProperty) property;
        if( simpleproperty.getDefaultValue() != null ) {
          buffer.appendF( "(default=%s) ", simpleproperty.getDefaultValue() );
        }
      }
      buffer.appendF( ": %s\n", property.getDescription() != null ? property.getDescription() : "" );
    }
    return buffer.toString();
  }
  
  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Maybe <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( Map<String,String> props, SimpleProperty<?> ... properties ) {
    return createReplacementMap( props, "%%%s%%", "", properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Maybe <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( Properties props, SimpleProperty<?> ... properties ) {
    return createReplacementMap( props, "%%%s%%", "", properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Maybe <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Not <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( 
    Map<String,String> props, @NonNull String format, @NonNull String nullvalue, SimpleProperty<?> ... properties 
  ) {
    Map<String,String> result = new HashMap<String,String>();
    for( SimpleProperty<?> property : properties ) {
      String keypattern = String.format( format, property.getKey() );
      String value      = getValueAsText( property, props, nullvalue );
      result.put( keypattern, value );
    }
    return result;
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Maybe <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Not <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String,String> createReplacementMap( 
    Properties props, @NonNull String format, @NonNull String nullvalue, SimpleProperty<?> ... properties 
  ) {
    Map<String,String> result = new HashMap<String,String>();
    for( SimpleProperty<?> property : properties ) {
      String keypattern = String.format( format, property.getKey() );
      String value      = getValueAsText( property, props, nullvalue );
      result.put( keypattern, value );
    }
    return result;
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
  private static <T> String getValueAsText( SimpleProperty<T> property, Map<String,String> properties, String nullvalue ) {
    T value = null;
    try {
      value = property.getValue( properties );
    } catch( MissingPropertyException ex ) {
      // legal case for a required but missing property, so value stays null
    }
    if( value == null ) {
      return nullvalue;
    } else {
      return property.getAdapter().marshal( value );
    }
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
  private static <T> String getValueAsText( SimpleProperty<T> property, Properties properties, String nullvalue ) {
    T value = null;
    try {
      value = property.getValue( properties );
    } catch( MissingPropertyException ex ) {
      // legal case for a required but missing property, so value stays null
    }
    if( value == null ) {
      return nullvalue;
    } else {
      return property.getAdapter().marshal( value );
    }
  }

} /* ENDCLASS */
