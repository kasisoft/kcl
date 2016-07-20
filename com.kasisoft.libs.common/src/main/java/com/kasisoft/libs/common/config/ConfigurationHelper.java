package com.kasisoft.libs.common.config;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.regex.*;

import java.util.*;

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
    StringFBuffer                          buffer = new StringFBuffer();
    Map<String, AbstractProperty<?, ?, ?>> map    = new Hashtable<>();
    List<String>                           keys   = new ArrayList<>();
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
      SimpleProperty simpleproperty = (SimpleProperty) property;
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
    Map<Pattern, String> result = new HashMap<>();
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
    Map<String, String> result = new HashMap<>();
    if( (properties == null) || (properties.length == 0) ) {
      // process all properties (if the type is a map it must provide String values !)
      for( Object keyobj : props.keySet() ) {
        String keypattern = String.format( format, keyobj );
        String value      = StringFunctions.cleanup( (String) props.get( keyobj ) );
        if( value == null ) {
          value = nullvalue;
        }
        result.put( keypattern, value );
      }
    } else {
      // process simple properties
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
    
    Map<String, String> result = new Hashtable<>();
    
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
