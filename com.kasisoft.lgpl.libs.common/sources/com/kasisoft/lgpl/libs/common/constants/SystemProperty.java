/**
 * Name........: SystemProperty
 * Description.: Listing of system properties used to be accessed.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

/**
 * Listing of system properties used to be accessed.
 */
public enum SystemProperty {

  ClassPath             ( "java.class.path"             , "classpath"   , false ),
  ClassVersion          ( "java.class.version"          , "classver"    , false ),
  EndorsedDirs          ( "java.endorsed.dirs"          , "endorsed"    , true  ),
  ExtDirs               ( "java.ext.dirs"               , "extdirs"     , true  ),
  FileEncoding          ( "file.encoding"               , "encoding"    , false ),
  FileSeparator         ( "file.separator"              , "filesep"     , false ),
  JavaHome              ( "java.home"                   , "jre"         , true  ),
  JavaVersion           ( "java.version"                , "javaver"     , false ),
  LineSeparator         ( "line.separator"              , "linesep"     , false ),
  OsName                ( "os.name"                     , "os"          , false ),
  Path                  ( "java.library.path"           , "path"        , false ),
  RuntimeVersion        ( "java.runtime.version"        , "runtimever"  , false ),
  SpecificationVersion  ( "java.specification.version"  , "specver"     , false ),
  TempDir               ( "java.io.tmpdir"              , "temp"        , true  ),
  UserDir               ( "user.dir"                    , "pwd"         , true  ),
  UserHome              ( "user.home"                   , "home"        , true  );
  
  private String     key;
  private String     shortkey;
  private boolean    fileseparator;
  
  /**
   * Sets up this value with the associated property key.
   * 
   * @param propertykey     The property key to be used. Neither <code>null</code> nor empty.
   * @param propertyshort   A short key mainly used for substitution purposes.
   *                        Neither <code>null</code> nor empty.
   * @param filesep         <code>true</code> <=> Expand the value using a file separator.
   */
  SystemProperty( String propertykey, String propertyshort, boolean filesep ) {
    key           = propertykey;
    shortkey      = propertyshort;
    fileseparator = filesep;
  }
  
  /**
   * Returns the property key allowing to access a system property.
   * 
   * @return   The property key allowing to access a system property. 
   *           Neither <code>null</code> nor empty.
   */
  public String getKey() {
    return key;
  }
  
  /**
   * Returns the short key allowing to access a system property.
   * 
   * @return   The short key allowing to access a system property.
   *           Neither <code>null</code> nor empty.
   */
  public String getShortKey() {
    return shortkey;
  }
  
  /**
   * Returns the value for this property from the system properties.
   * 
   * @return   The value associated with the property.
   */
  public String getValue() {
    return getValue( System.getProperties(), false );
  }
  
  /**
   * Returns the value for this property. If the property cannot be found in the supplied map the
   * system properties will be used.
   * 
   * @param properties   The properties used to access the value.
   * 
   * @return   The value associated with the property.
   */
  public String getValue( Properties properties ) {
    return getValue( properties, true );
  }
  
  /**
   * Returns the value for this property.
   * 
   * @param properties   The properties used to access the value.
   * @param fallback     <code>true</code> <=> Use system properties as a fallback solution.
   * 
   * @return   The value associated with the property.
   */
  public String getValue( Properties properties, boolean fallback ) {
    String defvalue = null;
    if( fallback ) {
      defvalue = System.getProperty( getKey() );
    }
    String result   = properties.getProperty( getKey(), defvalue );
    if( (result != null) && fileseparator ) {
      String filesep = FileSeparator.getValue();
      if( ! result.endsWith( filesep ) ) {
        result = String.format( "%s%s", result, filesep );
      }
    }
    return result;
  }
  
  /**
   * Returns the enumeration value associated with the supplied short key.
   * 
   * @param key   The short key used to refer to a system property.  
   *              Neither <code>null</code> nor empty.
   *              
   * @return   The enumeration value or <code>null</code> in case the key could not be found.
   */
  public static final SystemProperty getByShortkey( 
    @KNotEmpty(name="key")   String   key 
  ) {
    for( SystemProperty sysprop : SystemProperty.values() ) {
      if( key.equals( sysprop.getShortKey() ) ) {
        return sysprop;
      }
    }
    return null;
  }

  /**
   * Returns the enumeration value associated with the supplied long key.
   * 
   * @param key   The long key used to refer to a system property.  
   *              Neither <code>null</code> nor empty.
   *              
   * @return   The enumeration value or <code>null</code> in case the key could not be found.
   */
  public static final SystemProperty getByLongkey( 
    @KNotEmpty(name="key")   String   key 
  ) {
    for( SystemProperty sysprop : SystemProperty.values() ) {
      if( key.equals( sysprop.getKey() ) ) {
        return sysprop;
      }
    }
    return null;
  }

  /**
   * Returns the enumeration value associated with the supplied key (either the long or the short one).
   * 
   * @param key   The short/long key used to refer to a system property.  
   *              Neither <code>null</code> nor empty.
   *              
   * @return   The enumeration value or <code>null</code> in case the key could not be found.
   */
  public static final SystemProperty getByKey( 
    @KNotEmpty(name="key")   String   key 
  ) {
    for( SystemProperty sysprop : SystemProperty.values() ) {
      if( key.equals( sysprop.getKey() ) || key.equals( sysprop.getShortKey() ) ) {
        return sysprop;
      }
    }
    return null;
  }

  /**
   * Creates a replacement map used to substitute system properties. The key is encapsulated
   * by '%' characters.
   * 
   * @param shortkey     <code>true</code> <=> Use the short key representation to create the key.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( boolean shortkey ) {
    return createReplacementMap( "%%%s%%", shortkey );
  }
  
  /**
   * Creates a replacement map used to substitute system properties.
   * 
   * @param format     A formatting String with one %s format code. This is used in order
   *                   to support various key formats. Neither <code>null</code> nor empty.
   * @param shortkey   <code>true</code> <=> Use the short key representation to create the key.
   *
   * @return   A Map containing key-value pairs for a possible replacement. Not <code>null</code>.
   */
  public static final Map<String,String> createReplacementMap( 
    @KNotEmpty(name="format")   String    format, 
                                boolean   shortkey 
  ) {
    Map<String,String> result = new Hashtable<String,String>();
    for( SystemProperty sysprop : SystemProperty.values() ) {
      if( shortkey ) {
        result.put( String.format( format, sysprop.getShortKey() ), sysprop.getValue() );
      } else {
        result.put( String.format( format, sysprop.getKey() ), sysprop.getValue() );
      }
    }
    return result;
  }
  
} /* ENDENUM */
