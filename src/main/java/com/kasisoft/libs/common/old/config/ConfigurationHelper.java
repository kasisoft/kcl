package com.kasisoft.libs.common.old.config;

import static com.kasisoft.libs.common.old.internal.Messages.format_default;
import static com.kasisoft.libs.common.old.internal.Messages.label_mandatory;
import static com.kasisoft.libs.common.old.internal.Messages.label_optional;

import com.kasisoft.libs.common.old.util.MiscFunctions;
import com.kasisoft.libs.common.text.StringFBuffer;

import java.util.function.Function;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.val;

/**
 * A helper which allows to deal with the configuration properties.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationHelper {

  static final String PREFIX_ENV    = "env:";
  static final String PREFIX_SYS    = "sys:";

  /**
   * Returns a simple help text about the supplied properties.
   * 
   * @param properties   The properties that shall be turned into a help text. Maybe <code>null</code>.
   * 
   * @return   A simple help text about the supplied properties. Not <code>null</code>.
   */
  public static String help( AbstractProperty<?, ?, ?> ... properties ) {
    val buffer = new StringFBuffer();
    val map    = new Hashtable<String, AbstractProperty<?, ?, ?>>();
    val keys   = new ArrayList<String>();
    if( properties != null ) {
      for( AbstractProperty<?, ?, ?> property : properties ) {
        keys.add( property.getKey() );
        map.put( property.getKey(), property );
      }
    }
    Collections.sort( keys );
    keys.forEach( $ -> appendProperty( buffer, $, map.get( $ ) ) );
    return buffer.toString();
  }

  private static void appendProperty( StringFBuffer buffer, String key, AbstractProperty<?, ?, ?> property ) {
    buffer.appendF( "%s ", key );
    buffer.appendF( "(%s) ", property.isRequired() ? label_mandatory : label_optional );
    if( property instanceof SimpleProperty ) {
      val simpleproperty = (SimpleProperty) property;
      if( simpleproperty.getDefaultValue() != null ) {
        buffer.append( format_default.format( simpleproperty.getDefaultValue() ) );
        buffer.append( " " );
      }
    }
    buffer.appendF( ": %s\n", property.getDescription() != null ? property.getDescription() : "" );
  }
  
  /**
   * Creates a copy of the supplied map while quoting the keys in order to be useful in conjunction with regular 
   * expressions.
   * 
   * @param replacementmap   The map which serves as the input. Not <code>null</code>.
   * 
   * @return   A map of replacements with regex quoted keys. Not <code>null</code>.
   */
  public static Map<Pattern, String> quoteKeys( @NonNull Map<String, String> replacementmap ) {
    val result = new HashMap<Pattern, String>();
    replacementmap.forEach( (k, v) ->  result.put( Pattern.compile( Pattern.quote(k) ), v ) );
    return result;
  }
  
  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Not <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. 
   *                     If <code>null</code> all properties of <param>props</param> will be used (obviously it's not
   *                     allowed to be <code>null</code> in this case).
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap( @NonNull Map<String, String> props, SimpleProperty<?> ... properties ) {
    return createReplacementMapImpl( props, "%%%s%%", null, properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Not <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *                     If <code>null</code> all properties of <param>props</param> will be used (obviously it's not
   *                     allowed to be <code>null</code> in this case).
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap( @NonNull Properties props, SimpleProperty<?> ... properties ) {
    return createReplacementMapImpl( props, "%%%s%%", null, properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Not <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Maybe <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *                     If <code>null</code> all properties of <param>props</param> will be used (obviously it's not
   *                     allowed to be <code>null</code> in this case).
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap( 
    @NonNull Map<String, String> props, @NonNull String format, String nullvalue, SimpleProperty<?> ... properties 
  ) {
    return createReplacementMapImpl( props, format, nullvalue, properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Not <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Maybe <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *                     If <code>null</code> all properties of <param>props</param> will be used (obviously it's not
   *                     allowed to be <code>null</code> in this case).
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap( 
    @NonNull Properties props, @NonNull String format, String nullvalue, SimpleProperty<?> ... properties 
  ) {
    return createReplacementMapImpl( props, format, nullvalue, properties );
  }

  /**
   * Creates a replacement map used to substitute properties.
   * 
   * @param props        The properties providing the current configuration. Not <code>null</code>.
   * @param format       A formatting String with one %s format code. This is used in order to support various key 
   *                     formats. Neither <code>null</code> nor empty.
   * @param nullvalue    The textual value which has to be used when a null value has been encountered. 
   *                     Maybe <code>null</code>.
   * @param properties   The properties that shall be returned in the replacement map. Maybe <code>null</code>.
   *                     If <code>null</code> all properties of <param>props</param> will be used (obviously it's not
   *                     allowed to be <code>null</code> in this case).
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  private static Map<String, String> createReplacementMapImpl( 
    Map props, String format, String nullvalue, SimpleProperty<?> ... properties 
  ) {
    Map<String, String> result = Collections.emptyMap();
    if( (properties == null) || (properties.length == 0) ) {
      result = MiscFunctions.createReplacementMap( props, null, format, Map::keySet, Function.identity(), ($m, $k) -> (String) $m.get($k) );
    } else {
      // process simple properties
      result = new HashMap<>();
      for( SimpleProperty<?> property : properties ) {
        result.put( String.format( format, property.getKey() ), getValueAsText( property, props, nullvalue ) );
      }
    }
    return result;
  }
  
  /**
   * Creates a map with variable settings from the current system environment.
   * 
   * @return   A map with variable settings from the current system environment. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap() {
    return createReplacementMap( "%%%s%%" );
  }
  
  /**
   * Creates a map with variable settings from the current system environment.
   * 
   * @param varformatter    The formatter for the generation of properties. Neither <code>null</code> nor empty.
   * 
   * @return   A map with variable settings from the current system environment. Not <code>null</code>.
   */
  public static Map<String, String> createReplacementMap( @NonNull String varformatter ) {
    
    val result = new Hashtable<String, String>();
    
    // record the env entries
    System.getenv().forEach( (k, v) -> result.put( newKey( varformatter, PREFIX_ENV, k ), v ) );

    // record the system properties
    System.getProperties().forEach( (k, v) -> result.put( newKey( varformatter, PREFIX_SYS, (String) k ), (String) v ) );
    
    return result;
    
  }
  
  private static String newKey( String formatter, String prefix, String key ) {
    return String.format( formatter, String.format( "%s%s", prefix, key ) );
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
  private static <T> String getValueAsText( SimpleProperty<T> property, Map properties, String nullvalue ) {
    T value = getValue( properties, property );
    if( value == null ) {
      return nullvalue;
    } else {
      return property.getAdapter().marshal( value );
    }
  }
  
  @SuppressWarnings("cast")
  private static <T> T getValue( Map properties, SimpleProperty<T> property ) {
    try {
      return (T) property.getValue( properties );
    } catch( MissingPropertyException ex ) {
      // legal case for a required but missing property, so value stays null
      return null;
    }
  }

} /* ENDCLASS */
