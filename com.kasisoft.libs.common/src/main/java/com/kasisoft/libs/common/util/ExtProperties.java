/**
 * Name........: ExtProperties
 * Description.: Alternate Properties class. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;


import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.functionality.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.xml.adapters.*;

import java.util.regex.*;

import java.util.*;

import java.net.*;

import java.io.*;

import lombok.*;

/**
 * Alternate Properties class. This type has some differences compared to the original one, which are:
 * <ul>
 *   <li>Multilined properties aren't supported since I consider array types a better readable solution.</li>
 *   <li>The delimiter for key-value pairs have to be supplied at instantiation time. The original
 *   implementation accepts various characters but I personally think that a specified character
 *   should be consistently used within a single file, so I made this a requirement.</li>
 *   <li>This type doesn't support escaping for unicode characters as loading resources expects to
 *   provide a corresponding encoding.</li>
 * </ul>
 * 
 * You can also refer to the environment or the System.Properties using the following schemes:
 * 
 * <ul>
 *   <li><code>${env:PATH}</code> delivers the full path.</li>
 *   <li><code>${sys:line.separator}</code> delivers the line separator.</li>
 * </ul>
 */
public class ExtProperties {
  
  private static final String PREFIX_ENV    = "env:";
  private static final String PREFIX_SYS    = "sys:";  

  // formatting string used to create full variable expressions. f.e. ${bla.blub}
  private String                                   varformatter;

  // formatting string used to produce a complete property line (or property reference).
  // f.e. bla.blub[hello] = 12 or bla.blub[hello]
  private String                                   formatter;
  
  // regex pattern used for variable resolving
  private Pattern                                  varselector;
  
  // regex pattern used to resolve property keys
  private Pattern                                  propkeyselector;
  
  // this pattern is used to split the parts of a property key name. it's only allowed to be
  // used after the key has been "validated" using propkeyselector. 
  private Pattern                                  splitter;

  // some general settings for the properties file
  private String                                   delimiter;
  private String                                   commentintro;
  private boolean                                  emptyisnull;

  // data structures used to collect the property values
  private Set<String>                              names;
  private Map<String,Map<Integer,PropertyValue>>   indexed;
  private Map<String,Map<String,PropertyValue>>    associated;
  private Map<String,PropertyValue>                simple;

  // a map used for general replacements
  private Map<String,String>                       generalreplacements;
  
  // basic error handling
  private SimpleErrorHandler                       handler;
  
  // type adapters if desired by the client
  private Map<String,TypeAdapter>                  typeadapters;
  
  // for temporary use
  private List<String>                             lines;
  private Map<String,String>                       propertyvalues;
  private Tupel<String>                            pair;

  /**
   * Initialises this Properties implementation.
   */
  public ExtProperties() {
    this( null, null );
  }
  
  /**
   * Initialises this Properties implementation.
   * 
   * @param del       The delimiter to be used for the key value pairs. If not <code>null</code> it must be not empty.
   * @param comment   The literal which introduces a comment. If not <code>null</code> it must be not empty.
   */
  public ExtProperties( String del, String comment ) {
    delimiter           = del     != null ? del     : "=";
    commentintro        = comment != null ? comment : "#";
    varformatter        = "${%s%s}";
    formatter           = "%s[%s]%s%s";
    varselector         = Pattern.compile( "\\$\\{[\\w\\.\\[\\]]+\\}" );
    propkeyselector     = Pattern.compile( "^\\s*[\\w\\.]+\\s*\\[\\s*[\\w\\.]+\\s*\\]\\s*$" );
    splitter            = Pattern.compile( "[\\s\\[\\]]+" );
    emptyisnull         = false;
    names               = new HashSet<String>();
    indexed             = new Hashtable<String,Map<Integer,PropertyValue>>();
    associated          = new Hashtable<String,Map<String,PropertyValue>>();
    simple              = new HashMap<String,PropertyValue>();
    lines               = new ArrayList<String>();
    pair                = new Tupel<String>();
    generalreplacements = setupGeneralReplacements();
    handler             = null;
    typeadapters        = new Hashtable<String,TypeAdapter>();
  }
  
  public void registerTypeAdapter( @NonNull String key, @NonNull TypeAdapter adapter ) {
    typeadapters.put( key, adapter );
  }
  
  public void unregisterTypeAdapter( @NonNull String key ) {
    typeadapters.remove( key );
  }
  
  /**
   * Creates a map with variable settings from the current system environment.
   * 
   * @return   A map with variable settings from the current system environment. Not <code>null</code>.
   */
  private Map<String,String> setupGeneralReplacements() {
    
    Map<String,String> result       = new Hashtable<String,String>();
    
    // record the env entries
    Map<String,String> environment  = System.getenv();
    for( Map.Entry<String,String> env : environment.entrySet() ) {
      result.put( String.format( varformatter, PREFIX_ENV, env.getKey() ), env.getValue() );
    }
    
    // record the system properties
    Properties          sysprops  = System.getProperties();
    Enumeration<String> names     = (Enumeration<String>) sysprops.propertyNames();
    while( names.hasMoreElements() ) {
      String key    = names.nextElement();
      String value  = sysprops.getProperty( key );
      result.put( String.format( varformatter, PREFIX_SYS, key ), value );
    }
    
    return result;
    
  }

  /**
   * Changes the currently used error handler.
   * 
   * @param newhandler   The new error handler. If <code>null</code> there won't be any further notification.
   */
  public void setErrorHandler( SimpleErrorHandler newhandler ) {
    handler = newhandler;
  }
  
  /**
   * Returns the error handler that has to be used currently.
   * 
   * @return   The error handler that has to be used currently. Maybe <code>null</code>.
   */
  public SimpleErrorHandler getErrorHandler() {
    return handler;
  }
  
  /**
   * Returns <code>true</code> if empty values shall be treated as <code>null</code> values.
   * 
   * @param enable   <code>true</code> <=> Enable this treatment.
   */
  public synchronized void setEmptyIsNull( boolean enable ) {
    emptyisnull = enable;
  }
  
  /**
   * Returns <code>true</code> if empty values shall be treated as <code>null</code> values.
   * 
   * @return   <code>true</code> <=> Empty values shall be treated as <code>null</code> values.
   */
  public synchronized boolean isEmptyNull() {
    return emptyisnull;
  }

  /**
   * Clears all currently registered properties.
   */
  public synchronized void clear() {
    lines       . clear();
    names       . clear();
    indexed     . clear();
    associated  . clear();
    simple      . clear();
  }
  
  /**
   * Loads the current properties from a specific File location.
   * 
   * @param input      The source where to load the properties from. Not <code>null</code>.
   * @param encoding   The encoding to use. Maybe <code>null</code>.
   */
  public synchronized void load( @NonNull File input, Encoding encoding ) {
    lines = IoFunctions.readText( input, encoding );
    processProperties();
  }

  /**
   * Loads the current properties from a specific File location.
   * 
   * @param input      The source where to load the properties from. Not <code>null</code>.
   * @param encoding   The encoding to use. Maybe <code>null</code>.
   */
  public synchronized void load( @NonNull URL input, Encoding encoding ) {
    InputStream instream = null;
    try {
      instream = input.openStream();
      load( instream, encoding );
    } catch( IOException ex ) {
      throw new FailureException( FailureCode.IO, ex );
    } finally {
      MiscFunctions.close( instream );
    }
  }

  /**
   * Loads the current properties from an InputStream.
   * 
   * @param input      The source where to load the properties from. Not <code>null</code>.
   * @param encoding   The encoding to use. Maybe <code>null</code>.
   */
  public synchronized void load( @NonNull InputStream input, Encoding encoding ) {
    lines = IoFunctions.readText( encoding.openReader( input ) );
    processProperties();
  }
  
  /**
   * Loas the current properties from a Reader.
   * 
   * @param input   The Reader where to load the properties from. Not <code>null</code>.
   */
  public synchronized void load( @NonNull Reader input ) {
    lines = IoFunctions.readText( input );
    processProperties();
  }

  /**
   * Makes sure that array style properties follow a specific normalisation.
   */
  @SuppressWarnings("unchecked")
  private void processProperties() {
    for( int i = 0; i < lines.size(); i++ ) {
      String line = lines.get(i).trim();
      lines.set( i, line );
      if( line.length() > 0 ) {
        if( ! line.startsWith( commentintro ) ) {
          line = processProperty( line );
        }
      }
    }
  }
  
  /**
   * Identifies the property type of the supplied line.
   * 
   * @param line   The line containing a property with it's value. Neither <code>null</code> nor empty.
   * 
   * @return   The adjusted line (all whitespace stuff is removed there). Neither <code>null</code> nor empty.
   */
  private String processProperty( @NonNull String line ) {
    String  key   = null;
    String  value = null;
    int     idx   = line.indexOf( delimiter );
    if( idx == -1 ) {
      key = line;
    } else {
      key   = line.substring( 0, idx );
      value = line.substring( idx + delimiter.length() ).trim();
    }
    if( emptyisnull ) {
      value = StringFunctions.cleanup( value );
    }
    if( propkeyselector.matcher( key ).matches() ) {
      
      // we've got an indexed/associated property
      pair.setValues( splitter.split( key ) );
      try {
        
        // try with an indexed one
        Integer indexval = Integer.valueOf( pair.getLast() );
        setIndexedProperty( pair.getFirst(), indexval.intValue(), value );
        return String.format( formatter, pair.getFirst(), indexval, delimiter, value != null ? value : "" );
        
      } catch( NumberFormatException ex ) {
        
        // indexed fails, so it's an associated property
        setAssociatedProperty( pair.getFirst(), pair.getLast(), value );
        return String.format( formatter, pair.getFirst(), pair.getLast(), delimiter, value != null ? value : "" );
      
      }
      
    } else {
      // simpliest property
      setSimpleProperty( key, value );
      return String.format( "%s%s%s", key.trim(), delimiter, value != null ? value : "" );
    }
  }

  /**
   * @see Properties#setProperty(String, String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #setIndexedProperty(String, int, String)}, {@link #setAssociatedProperty(String, String, String)}
   * or {@link #setSimpleProperty(String, String)} directly since they are cheaper.
   */
  public synchronized void setProperty( @NonNull String key, String value ) {
    if( emptyisnull ) {
      // empty values will be treated as null values
      if ( (value != null) && value.length() == 0 ) {
        value = null;
      }
    }
    if( propkeyselector.matcher( key ).matches() ) {
      // we've got an indexed/associated property
      pair.setValues( splitter.split( key ) );
      try {
        // try with an indexed one
        setIndexedProperty( pair.getFirst(), Integer.parseInt( pair.getLast() ), value );
      } catch( NumberFormatException ex ) {
        // indexed fails, so it's an associated property
        setAssociatedProperty( pair.getFirst(), pair.getLast(), value );
      }
    } else {
      // simpliest property
      setSimpleProperty( key, value );
    }
  }

  /**
   * Sets an indexed property.
   * 
   * @param key      The property key itself. Neither <code>null</code> nor empty. 
   * @param index    The index to be used.
   * @param value    The value to be set. Maybe <code>null</code>.
   */
  public synchronized void setIndexedProperty( @NonNull String key, int index, String value ) {
    Map<Integer,PropertyValue> values = indexed.get( key );
    if( values == null ) {
      values = new HashMap<Integer,PropertyValue>();
      indexed.put( key, values );
    }
    Integer indexobj = Integer.valueOf( index );
    String  keyname  = String.format( formatter, key, indexobj, "", "" );
    values.put( indexobj, newPropertyValue( this, keyname, value ) );
    names.add( key );
  }
  
  /**
   * Sets an associated property.
   * 
   * @param key           The property key itself. Neither <code>null</code> nor empty. 
   * @param association   The association for this property. Not <code>null</code>.
   * @param value         The value to be set. Maybe <code>null</code>.
   */
  public synchronized void setAssociatedProperty( @NonNull String key, @NonNull String association, String value ) {
    Map<String,PropertyValue> values = associated.get( key );
    if( values == null ) {
      values = new HashMap<String,PropertyValue>();
      associated.put( key, values );
    }
    String keyname = String.format( formatter, key, association, "", "" );
    values.put( association, newPropertyValue( this, keyname, value ) );
    names.add( key );
  }

  /**
   * Sets a simple property.
   * 
   * @param key     The property key itself. Neither <code>null</code> nor empty.
   * @param value   The value to be set. Maybe <code>null</code>.
   */
  public synchronized void setSimpleProperty( @NonNull String key, String value ) {
    key = key.trim();
    names.add( key );
    simple.put( key, newPropertyValue( this, key, value ) );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to an indexed property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to an indexed property.
   */
  public synchronized boolean isIndexedProperty( @NonNull String key ) {
    return indexed.containsKey( key );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to an associative property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to an associative property.
   */
  public synchronized boolean isAssociatedProperty( @NonNull String key ) {
    return associated.containsKey( key );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to a simple property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to a simple property.
   */
  public synchronized boolean isSimpleProperty( @NonNull String key ) {
    return simple.containsKey( key );
  }

  /**
   * Returns an ordered list of property values. The list is ordered according to the indices.
   * Note: The index within the list doesn't necessarily correspond to the index of the property.
   * 
   * @param key         The key used to access the indexed properties. Neither <code>null</code> nor empty.
   * @param defvalues   The default values that will be returned in case the property doesn't exist.
   *                    Maybe <code>null</code>.
   *                    
   * @return   The list of sorted property values. Maybe <code>null</code> if the property doesn't exist and no default 
   *           values have been provided.
   */
  public synchronized <T> List<T> getIndexedProperties( @NonNull String key, List<T> defvalues ) {
    Map<Integer,PropertyValue> map = indexed.get( key );
    if( map != null ) {
      // get a list of the entries first
      List<Map.Entry<Integer,PropertyValue>> values = new ArrayList<Map.Entry<Integer,PropertyValue>>( map.entrySet() );
      // now sort them according to the indexes
      Collections.sort( values, MiscFunctions.newKeyComparator( Integer.class ) );
      // now map them, since we're only interested in the values
      List<String>          list    = FuFunctions.map( Predefined.<Integer,PropertyValue>toStringValueTransform(), values );
      TypeAdapter<String,T> adapter = typeadapters.get( key );
      if( adapter != null ) {
        return FuFunctions.map( adapter, list );
      } else {
        return (List<T>) list;
      }
    } else {
      return defvalues;
    }
  }

  /**
   * Returns the value of an indexed property.
   *  
   * @param key     The key used to access the property. Neither <code>null</code> nor empty.
   * @param index   The index used to access the value.
   * 
   * @return   The indexed value. Maybe <code>null</code> if the value didn't exist.
   */
  public synchronized String getIndexedProperty( @NonNull String key, int index ) {
    return getIndexedProperty( key, index, null );
  }
  
  /**
   * Returns the value of an indexed property.
   *  
   * @param key        The key used to access the property. Neither <code>null</code> nor empty.
   * @param index      The index used to access the value.
   * @param defvalue   A default value in case the value doesn't exist. Maybe <code>null</code>.
   * 
   * @return   The indexed value. Maybe <code>null</code> if the value didn't exist and no default value has been supplied.
   */
  public synchronized String getIndexedProperty( @NonNull String key, int index, String defvalue ) {
    return getIndexedOrAssociatedProperty( key, Integer.valueOf( index ), indexed, defvalue );
  }

  /**
   * Returns the value of an associated property. The result is typed in case an adapter has been registered using
   * {@link #registerTypeAdapter(String, TypeAdapter)}.
   * 
   * @param key           The key used to access the property. Neither <code>null</code> nor empty.
   * @param association   The association name used to access the value. Neither <code>null</code> nor empty.
   * @param defvalue      The default value which will be returned in case none is available yet.
   *                      Maybe <code>null</code>.
   * 
   * @return   The value stored using the associated property. Maybe <code>null</code> if none exists
   *           and no default value has been supplied.
   */
  public synchronized <T> T getAssociatedProperty( @NonNull String key, @NonNull String association, T defvalue ) {
    return getIndexedOrAssociatedProperty( key, association, associated, defvalue );
  }

  /**
   * Returns the value of an indexed/associated property. The result is typed if a {@link TypeAdapter} has been
   * registered via {@link #registerTypeAdapter(String, TypeAdapter)}.
   * 
   * @param key           The key used to access the property. Neither <code>null</code> nor empty.
   * @param association   The association name used to access the value. Neither <code>null</code> nor empty.
   * @param source        The map providing the value maps. Not <code>null</code>.
   * @param defvalue      The default value which will be returned in case none is available yet.
   *                      Maybe <code>null</code>.
   * 
   * @return   The value stored using the associated property. Maybe <code>null</code> if none exists and no default 
   *           value has been supplied.
   */
  private <T,V> V getIndexedOrAssociatedProperty( 
    String key, T association, Map<String,Map<T,PropertyValue>> source, V defvalue 
  ) {
    Map<T,PropertyValue> map = source.get( key );
    if( map != null ) {
      return getPropertyValue( key, map.get( association ), defvalue  );
    } else {
      return defvalue;
    }
  }
  
  /**
   * Returns a map of associated values for a specific property while providing a typed result in case a 
   * {@link TypeAdapter} has been registered using {@link #registerTypeAdapter(String, TypeAdapter)}.
   * 
   * @param key         The name of the property. Neither <code>null</code> nor empty.
   * @param defvalues   The default values which will be returned in case the property didn't exist.
   *                    Maybe <code>null</code>.
   *                    
   * @return   The map providing the associated values. <code>null</code> if the default values were <code>null</code>
   *           and there aren't any properties with the supplied key.
   */
  public synchronized <T> Map<String,T> getAssociatedProperties( @NonNull String key, Map<String,T> defvalues ) {
    Map<String,PropertyValue> map = associated.get( key );
    if( map != null ) {
      TypeAdapter<String,T>       adapter   = typeadapters.get( key );
      Transform<PropertyValue,T>  transform = null;
      if( adapter != null ) {
        transform = Predefined.joinTransforms( Predefined.<PropertyValue>toStringTransform(), adapter );
      } else {
        // <T> is supposed to be a String
        transform = (Transform<PropertyValue,T>) Predefined.<PropertyValue>toStringTransform();
      }
      return FuFunctions.mapValue( transform, map, defvalues );
    } else {
      return defvalues;
    }
  }

  /**
   * Returns the value of an associated property. The result is typed if an adapter has been registered using
   * {@link #registerTypeAdapter(String, TypeAdapter)}.
   * 
   * @param key           The key used to access the property. Neither <code>null</code> nor empty.
   * @param association   The association name used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The value stored using the associated property. Maybe <code>null</code> if none exists.
   */
  public synchronized <T> T getAssociatedProperty( @NonNull String key, @NonNull String association ) {
    return getAssociatedProperty( key, association, null );
  }

  /**
   * Returns the value associated with the supplied simple key.
   * 
   * @param key   The key used to access the property. Neither <code>null</code> nor empty.
   * 
   * @return   The property value or <code>null</code> in case it didn't exist.
   */
  public synchronized <T> T getSimpleProperty( @NonNull String key ) {
    return getSimpleProperty( key, null );
  }
  
  /**
   * Returns the value associated with the supplied simple key.
   * 
   * @param key        The key used to access the property. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used in case the property doesn't exist. Maybe <code>null</code>.
   * 
   * @return   The property value or <code>null</code> in case it didn't exist and no default value has been supplied.
   */
  public synchronized <T> T getSimpleProperty( @NonNull String key, T defvalue ) {
    return getPropertyValue( key, simple.get( key ), defvalue );
  }
  
  /**
   * Just a convenience method which allows to return a PropertyValue while doing a <code>null</code>
   * check.
   * 
   * @param key        The key that has been used to access the {@link PropertyValue}. Neither <code>null</code> nor empty.
   * @param value      The PropertyValue which content has to be returned. Maybe <code>null</code>.
   * @param defvalue   The default value to be used in case no PropertyValue has been given. Maybe <code>null</code>.
   *                   
   * @return   Maybe <code>null</code> if neither a PropertyValue nor a non-<code>null</code> default value has been 
   *           provided.
   */
  private <T> T getPropertyValue( @NonNull String key, PropertyValue value, T defvalue ) {
    if( value == null ) {
      return defvalue;
    } else {
      String                astext  = value.toString();
      TypeAdapter<String,T> adapter = typeadapters.get( key );
      if( adapter != null ) {
        return adapter.map( astext );
      } else {
        return (T) astext;
      }
    }
  }

  /**
   * @see Properties#getProperty(String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods 
   * {@link #getIndexedProperty(String, int)}, {@link #getAssociatedProperty(String, String)} or 
   * {@link #getSimpleProperty(String)} directly since they are cheaper.
   */
  public synchronized String getProperty( @NonNull String key ) {
    return getProperty( key, null );
  }
  
  /**
   * @see Properties#getProperty(String, String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #getIndexedProperty(String, int, String)}, {@link #getAssociatedProperty(String, String, String)}
   * or {@link #getSimpleProperty(String, String)} directly since they are cheaper.
   */
  public synchronized String getProperty( @NonNull String key, String defvalue ) {
    if( propkeyselector.matcher( key ).matches() ) {
      pair.setValues( splitter.split( key ) );
      try {
        return getIndexedProperty( pair.getFirst(), Integer.parseInt( pair.getLast() ), defvalue );
      } catch( NumberFormatException ex ) {
        return getAssociatedProperty( pair.getFirst(), pair.getLast(), defvalue );
      }
    } else {
      return getSimpleProperty( key, defvalue );
    }
  }
  
  /**
   * Writes the content of this properties table to the supplied stream.
   * 
   * @param output      The stream to receive the property data. Not <code>null</code>.
   * @param encoding    The encoding to be used. Maybe <code>null</code>.
   */
  public synchronized void store( @NonNull OutputStream output, Encoding encoding ) {
    storeLines();
    IoFunctions.writeText( output, lines, encoding );
  }
  
  /**
   * Writes the content of this properties table to the supplied file.
   * 
   * @param file        The file to receive the property data. Not <code>null</code>.
   * @param encoding    The encoding to be used. Maybe <code>null</code>.
   */
  public synchronized void store( @NonNull File file, Encoding encoding ) {
    storeLines();
    IoFunctions.writeText( file, lines, encoding );
  }

  /**
   * Writes the content of this properties table to the supplied writer.
   * 
   * @param writer   The writer to receive the property data. Not <code>null</code>.
   */
  public synchronized void store( @NonNull Writer writer ) {
    storeLines();
    IoFunctions.writeText( writer, lines );
  }
  
  /**
   * This function modifies the original content of this property file to conform with the current state.
   */
  private void storeLines() {
    
    List<String> newlines = new ArrayList<String>();
    
    // these are the property names currently available
    Set<String>  undone   = new HashSet<String>( names );
    for( int i = 0; i < lines.size(); i++ ) {
      
      String line = lines.get(i);
      if( (line.length() == 0) || line.startsWith( commentintro ) ) {
        // empty lines and comments can be directly reused
        newlines.add( line );
        continue;
      }
      
      // get the key from the current line (trimming not needed as it's being reduced while loading)
      int     idx   = line.indexOf( delimiter );
      String  key   = null;
      if( idx == -1 ) {
        key = line;
      } else {
        key = line.substring( 0, idx );
      }
      
      if( propkeyselector.matcher( key ).matches() ) {
        
        // it's an indexed/associated key
        pair.setValues( splitter.split( key ) );
        
        // only apply it if this property has not been used before
        if( undone.contains( pair.getFirst() ) ) {
          
          // apply all index properties with one go (upcoming key won't be copied as this is
          // flagged using the set 'undone').
          try {
            Integer.parseInt( pair.getLast() );
            applyProperties( newlines, pair.getFirst(), indexed, Integer.class );
          } catch( NumberFormatException ex ) {
            applyProperties( newlines, pair.getFirst(), associated, String.class );
          }
          undone.remove( pair.getFirst() );
          
        }
        
      } else {
        
        // just apply the simple property if possible
        if( undone.contains( key ) ) {
          applySimple( newlines, key );
          undone.remove( key );
        }
        
      }
      
    }
    
    // there are still properties that needs to be written, so we're appending them at the end
    // this happens if some have been added after loading or the map has been setup initially
    if( ! undone.isEmpty() ) {
      
      // I like to sort things, this way the result becomes more reproducable
      String[] sorted = undone.toArray( new String[ undone.size() ] );
      Arrays.sort( sorted );
      
      for( String currentkey : sorted ) {
        
        // just apply the properties depending on their type
        if( isSimpleProperty( currentkey ) ) {
          applySimple( newlines, currentkey );
        } else if( isIndexedProperty( currentkey ) ) {
          applyProperties( newlines, currentkey, indexed, Integer.class );
        } else if( isAssociatedProperty( currentkey ) ) {
          applyProperties( newlines, currentkey, associated, String.class );
        }
        
      }
      
    }
    
    lines = newlines;
    
  }

  /**
   * Applies the indexed property values. We're sorting here, too, to get reproducable results.
   * 
   * @param receiver   The new list of lines to be extended. Not <code>null</code>.
   * @param key        The key which values have to be added. Neither <code>null</code> nor empty.
   * @param values     The values providing the map which has to ba applied. Not <code>null</code>.
   * @param type       The type of the keys. Not <code>null</code>.
   */
  private <T extends Comparable> void applyProperties( 
    List<String> receiver, String key, Map<String,Map<T,PropertyValue>> values, Class<T> type 
  ) {
    Map<T,PropertyValue> map = values.get( key );
    if( map == null ) {
      // nothing to be done here
      return;
    }
    List<Map.Entry<T,PropertyValue>> list = new ArrayList<Map.Entry<T,PropertyValue>>( map.entrySet() );
    Collections.sort( list, MiscFunctions.newKeyComparator( type ) );
    for( int i = 0; i < list.size(); i++ ) {
      PropertyValue value = list.get(i).getValue();
      receiver.add( String.format( formatter, key, list.get(i).getKey(), delimiter, getPropertyValue( key, value, "" ) ) );
    }
  }
  
  /**
   * We're just adding a simple property here.
   * 
   * @param receiver   The new list of lines to be extended. Not <code>null</code>.
   * @param key        The key which value have to be added. Neither <code>null</code> nor empty.
   */
  private void applySimple( List<String> receiver, String key ) {
    if( ! simple.containsKey( key ) ) {
      // nothing to be done here
      return;
    }
    PropertyValue value = simple.get( key );
    receiver.add( String.format( "%s%s%s", key, delimiter, getPropertyValue( key, value, "" ) ) );
  }
  
  /**
   * @see Properties#propertyNames()
   */
  public synchronized Enumeration<String> propertyNames() {
    return ArrayFunctions.enumeration( names.toArray( new String[ names.size()] ) );
  }

  /**
   * Removes a specific property. If you know the type of property you should consider to use 
   * {@link #removeAssociatedProperty(String)}, {@link #removeAssociatedProperty(String, String)},
   * {@link #removeIndexedProperty(String)}, {@link #removeIndexedProperty(String, int)},
   * {@link #removeSimpleProperty(String)} instead as these operations are cheaper.
   * 
   * @param key   The key of the property to be removed. Neither <code>null</code> nor empty.
   */
  public synchronized void removeProperty( @NonNull String key ) {
    if( propkeyselector.matcher( key ).matches() ) {
      // we've got an indexed/associated property
      pair.setValues( splitter.split( key ) );
      try {
        // try with an indexed one
        removeIndexedProperty( pair.getFirst(), Integer.parseInt( pair.getLast() ) );
      } catch( NumberFormatException ex ) {
        // indexed fails, so it's an associated property
        removeAssociatedProperty( pair.getFirst(), pair.getLast() );
      }
    } else {
      // simpliest property
      removeSimpleProperty( key );
    }
  }

  /**
   * Removes all indexed properties for a specific key. If this property doesn't exists, this method will simply do 
   * nothing.
   * 
   * @param key   The key used to select all indexed properties. Neither <code>null</code> nor empty.
   */
  public synchronized void removeIndexedProperty( @NonNull String key ) {
    if( indexed.containsKey( key ) ) {
      indexed.remove( key );
      names.remove( key );
    }
  }

  /**
   * Removes all associated properties for a specific key. If this property doesn't exists, this method will simply do 
   * nothing.
   * 
   * @param key   The key used to select all associated properties. Neither <code>null</code> nor empty.
   */
  public synchronized void removeAssociatedProperty( @NonNull String key ) {
    if( associated.containsKey( key ) ) {
      associated.remove( key );
      names.remove( key );
    }
  }
  
  /**
   * Removes a specific indexed property value. If the property value doesn't exists, this method will simply do nothing.
   * 
   * @param key     The key used for the indexed properties. Neither <code>null</code> nor empty.
   * @param index   The index used to identify the value which will be removed.
   */
  public synchronized void removeIndexedProperty( @NonNull String key, int index ) {
    Map<Integer,PropertyValue> map = indexed.get( key );
    if( map != null ) {
      map.remove( Integer.valueOf( index ) );
      if( map.isEmpty() ) {
        indexed.remove( key );
        names.remove( key );
      }
    }
  }

  /**
   * Removes a specific associated property value. If the property value doesn't exist, this method will simply do nothing.
   * 
   * @param key           The key used for the associated properties. Neither <code>null</code> nor empty.
   * @param association   The index used to identify the value which will be removed. Neither <code>null</code> nor empty.
   */
  public synchronized void removeAssociatedProperty( @NonNull String key, String association ) {
    Map<String,PropertyValue> map = associated.get( key );
    if( map != null ) {
      map.remove( association );
      if( map.isEmpty() ) {
        associated.remove( key );
        names.remove( key );
      }
    }
  }
  
  /**
   * Removes the property values associated with the supplied key. If this property doesn't exist, this method will 
   * simply do nothing.
   * 
   * @param key   The key used to identify the property. Neither <code>null</code> nor empty.
   */
  public synchronized void removeSimpleProperty( @NonNull String key ) {
    simple.remove( key );
    names.remove( key );
  }

  private PropertyValue newPropertyValue( ExtProperties owner, String key, String value ) {
    if( value == null ) {
      // we don't need to create an instance for this
      return null;
    } else {
      if( value.indexOf( "${" ) != -1 ) {
        // it contains variables, so try to replace at least the general ones
        value = StringFunctions.replace( value, generalreplacements );
        if( value.indexOf( "${" ) != -1 ) {
          // still variables left over which will be evaluated when being requested
          return new EvalPropertyValue( owner, key, value );
        }
      }
      // simple constant value
      return new PropertyValue( value );
    }
  }

  /**
   * This PropertyValue implementation is used for values that require to be resolved.
   */
  private static class EvalPropertyValue extends PropertyValue {

    private ExtProperties   pthis;
    private String          varname;
    private Matcher         matcher;
    private StringBuilder   buffer;
    
    /**
     * Initialises this value using the supplied value.
     * 
     * @param val   The string value used to be stored within this type. Neither <code>null</code> nor empty.
     */
    public EvalPropertyValue( ExtProperties fac, String key, String val ) {
      super( val );
      pthis   = fac;
      varname = String.format( pthis.varformatter, "", key );
      matcher = pthis.varselector.matcher( super.content );
      buffer  = new StringBuilder();
    }
    
    /**
     * Evaluates this PropertyValue instance.
     * 
     * @return   The evaluated value. Not <code>null</code>.
     */
    private String evaluate() {
      boolean delete = pthis.propertyvalues == null;
      if( delete ) {
        // each resolved property value goes into this map. the first resolving starts the
        // process. this is necessary to prevent endless loop due to cyclic references
        pthis.propertyvalues = new HashMap<String,String>();
      }
      // originally set the identity value so '${bla}' would be substituted by '${bla}' in each
      // triggered call.
      pthis.propertyvalues.put( varname, varname );
      String value = evaluate( super.content );
      pthis.propertyvalues.put( varname, value );
      if( delete ) {
        // the first call has been done, so cleanup the table.
        pthis. propertyvalues = null;
      }
      return value;
    }
    
    /**
     * Evaluates the supplied value by resolving the contained variable references.
     * 
     * @param value   The current value to be resolved. Not <code>null</code>.
     * 
     * @return   The completely resolved value.
     */
    private String evaluate( String value ) {
      
      buffer.setLength(0);
      matcher.reset();
      
      // run through each occurences
      int laststart = 0;
      while( matcher.find() ) {
        
        int start = matcher.start();
        int end   = matcher.end();
        if( start > laststart ) {
          // there's some normal text that needs to be copied
          buffer.append( value.substring( laststart, start ) );
        }
        
        // extract the key expression (something like ${bla}).
        String key = value.substring( start, end );
        if( pthis.propertyvalues.containsKey( key ) ) {
          // this has already been resolved, so we can reuse the value here
          buffer.append( pthis.propertyvalues.get( key ) );
        } else {
          // we need to get the variable refernece itself in order to resolve it
          key = key.substring( "${".length(), key.length() - "}".length() );
          // append the referred variable value
          buffer.append( pthis.getProperty( key ) );
        }
        
        laststart  = end;
        
      }
      
      if( laststart < value.length() ) {
        // there's still some content left, so add it
        buffer.append( value.substring( laststart ) );
      }
      
      return buffer.toString();
      
    }

    @Override
    public String toString() {
      return evaluate();
    }

  } /* ENDCLASS */

  /**
   * PropertyValue implementation just representing a simple String.
   */
  private static class PropertyValue {

    private String   content;
    
    /**
     * Initialises this value using the supplied value.
     * 
     * @param val   The string value used to be stored within this type. Neither <code>null</code> nor empty.
     */
    public PropertyValue( String val ) {
      content = val;
    }
    
    @Override
    public String toString() {
      return content;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
