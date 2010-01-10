/**
 * Name........: CommonProperty
 * Description.: Listing of library related properties used to be accessed.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

import java.io.*;

/**
 * Listing of library related properties used to be accessed. Each key is simply constructed using
 * the following naming convention:
 * 
 *    <base-package-path> '#' <property-name>
 *    
 * F.e. <code>com.kasisoft.lgpl.libs.common#DEBUG</code>
 * 
 */
public enum CommonProperty {

  /** valuetype: Boolean */
  Debug       ( "com.kasisoft.lgpl.libs.common#DEBUG"       , false , Boolean.FALSE                     , Boolean.class ),
  /** valuetype: Integer */
  IoRetries   ( "com.kasisoft.lgpl.libs.common#IORETRIES"   , false , Integer.valueOf(5)                , Integer.class ),
  /** valuetype: Integer */
  Sleep       ( "com.kasisoft.lgpl.libs.common#SLEEP"       , false , Integer.valueOf(100)              , Integer.class ),
  /** valuetype: Integer */
  BufferCount ( "com.kasisoft.lgpl.libs.common#BUFFERCOUNT" , false , Integer.valueOf(4096)             , Integer.class ),
  /** valuetype: String  */
  TempDir     ( "com.kasisoft.lgpl.libs.common#TEMPDIR"     , true  , SystemProperty.TempDir.getValue() , String.class  );
  
  private String     key;
  private Object     defvalue;
  private boolean    filesystem;
  private Class<?>   typeclass;
  
  /**
   * Sets up this value with the associated property key.
   * 
   * @param propertykey   The property key to be used. Neither <code>null</code> nor empty.
   * @param filesys       <code>true</code> <=> The property is related to a filesystem value.
   * @param defval        The default value to be used if none could be found. Not <code>null</code>.
   */
  CommonProperty( String propertykey, boolean filesys, Object defval, Class<?> clazz ) {
    key         = propertykey;
    filesystem  = filesys;
    defvalue    = defval;
    typeclass   = clazz;
  }
  
  /**
   * Returns the property key allowing to access a property.
   * 
   * @return   The property key allowing to access a property. 
   *           Neither <code>null</code> nor empty.
   */
  public String getKey() {
    return key;
  }
  
  /**
   * Returns the value for this property from the system properties.
   * 
   * @return   The value associated with the property. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  public <T> T getValue() {
    return getValue( System.getProperties(), false );
  }
  
  /**
   * Returns the value for this property. If the property cannot be found in the supplied map the
   * system properties will be used.
   * 
   * @param properties   The properties used to access the value.
   * 
   * @return   The value associated with the property. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  public <T> T getValue( Properties properties ) {
    return getValue( properties, true );
  }
  
  /**
   * Returns the value for this property.
   * 
   * @param properties   The properties used to access the value.
   * @param fallback     <code>true</code> <=> Use system properties as a fallback solution.
   * 
   * @return   The value associated with the property. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  public <T> T getValue( Properties properties, boolean fallback ) {
    String defvalue = null;
    if( fallback ) {
      defvalue = System.getProperty( getKey() );
    }
    String result = properties.getProperty( getKey(), defvalue );
    return processValue( result );
  }

  /**
   * Returns the value for this property. If the property cannot be found in the supplied map the
   * system properties will be used.
   * 
   * @param properties   The properties used to access the value.
   * 
   * @return   The value associated with the property. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  public <T> T getValue( Map<String,String> properties ) {
    return getValue( properties, true );
  }
  
  /**
   * Returns the value for this property.
   * 
   * @param properties   The properties used to access the value.
   * @param fallback     <code>true</code> <=> Use system properties as a fallback solution.
   * 
   * @return   The value associated with the property. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  public <T> T getValue( Map<String,String> properties, boolean fallback ) {
    String defvalue = null;
    if( fallback ) {
      defvalue = System.getProperty( getKey() );
    }
    String result   = defvalue;
    if( properties.containsKey( getKey() ) ) {
      result = properties.get( getKey() );
    }
    return processValue( result );
  }

  /**
   * Processes the supplied value if necessary.
   * 
   * @param value   The value which has to be altered. Maybe <code>null</code>.
   * 
   * @return   The altered value. Not <code>null</code>.
   * 
   * @throws ClassCastException in case the properties value type is not meant to be used for the value type.
   */
  private <T> T processValue( String value ) {
    if( value != null ) {
      if( filesystem ) {
        // just make sure since the properties might come from an external map,
        // so we should deliver valid pathes
        value = value.replace( '\\', '/' ).replace( '/', File.separatorChar );
      }
    } else {
      value = String.valueOf( defvalue );
    }
    Object objvalue = value;
    if( typeclass == Boolean.class ) {
      objvalue = Boolean.valueOf( "yes".equalsIgnoreCase( value ) || "ja".equalsIgnoreCase( value ) || "true".equalsIgnoreCase( value ) );
    } else if( typeclass == Integer.class ) {
      objvalue = Integer.valueOf( value ); 
    } else {
      objvalue = value;
    }
    return (T) objvalue;
  }
  
  /**
   * Returns the enumeration value associated with the supplied key (either the long or the short one).
   * 
   * @param key   The short/long key used to refer to a system property.  
   *              Neither <code>null</code> nor empty.
   *              
   * @return   The enumeration value or <code>null</code> in case the key could not be found.
   */
  public static final CommonProperty getByKey( 
    @KNotEmpty(name="key")   String   key 
  ) {
    for( CommonProperty sysprop : CommonProperty.values() ) {
      if( key.equals( sysprop.getKey() ) ) {
        return sysprop;
      }
    }
    return null;
  }

  /**
   * Creates a replacement map used to substitute system properties. The key is encapsulated
   * by '%' characters.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap() {
    return createReplacementMap( "%%%s%%" );
  }
  
  /**
   * Creates a replacement map used to substitute system properties.
   * 
   * @param format     A formatting String with one %s format code. This is used in order
   *                   to support various key formats. Neither <code>null</code> nor empty.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( 
    @KNotEmpty(name="format")   String    format
  ) {
    Map<String,String> result = new Hashtable<String,String>();
    for( CommonProperty sysprop : CommonProperty.values() ) {
      result.put( String.format( format, sysprop.getKey() ), String.valueOf( sysprop.getValue() ) );
    }
    return result;
  }
  
} /* ENDENUM */
