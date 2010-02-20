/**
 * Name........: ExtProperties
 * Description.: Alternate Properties class. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.functionality.*;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;
import java.util.regex.*;

import java.io.*;

/**
 * Alternate Properties class. This type has some differences compared to the original one, which
 * are:
 * <ul>
 *   <li>Multilined properties aren't supported since I consider array types a better readable solution.</li>
 *   <li>The delimiter for key-value pairs have to be supplied at instantiation time. The original
 *   implementation accepts various characters but I personally think that a specified character
 *   should be consistently used within a single file, so I made this a requirement.</li>
 *   <li>This type doesn't support escaping for unicode characters as loading resources expects to
 *   provide a corresponding encoding.</li>
 * </ul>
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ExtProperties {
  
  /**
   * Each value allows to decide how an indexed/associated property will be stored.
   */
  public static enum KeyStyle {
    
    Braces      ( "^\\s*[\\w\\.]+\\s*(\\s*[\\w\\.]+\\s*)\\s*$"      , "[\\s()]+"     , "%s(%s)%s%s" ) ,
    ArrayBraces ( "^\\s*[\\w\\.]+\\s*\\[\\s*[\\w\\.]+\\s*\\]\\s*$"  , "[\\s\\[\\]]+" , "%s[%s]%s%s" ) ,
    CurlyBraces ( "^\\s*[\\w\\.]+\\s*\\{\\s*[\\w\\.]+\\s*\\}\\s*$"  , "[\\s\\{\\}]+" , "%s{%s}%s%s" ) ;
    
    private Pattern   validation;
    private Pattern   splitter;
    private String    formatter;
    
    /**
     * Configures this enumeration value.
     * 
     * @param validationpattern   This pattern is used to match the property key.
     * @param splitpattern        This is a much simplier pattern which allows to distinguish
     *                            the basic key and it's index/association.
     * @param lineformat          A formatting String used to regenerate property lines.
     */
    KeyStyle( String validationpattern, String splitpattern, String lineformat ) {
      validation  = Pattern.compile( validationpattern );
      splitter    = Pattern.compile( splitpattern );
      formatter   = lineformat;
    }
    
    /**
     * Creates a property line according to this style.
     * 
     * @param key         The basic key name, that has been used. Not <code>null</code>.
     * @param index       The index/association to be used. Not <code>null</code>.
     * @param delimiter   The delimiter used to separate the value. Not <code>null</code>.
     * @param value       The value associated with the property key. Maybe <code>null</code>.
     * 
     * @return   The property line providing the content.
     */
    public String toLine( String key, Object index, String delimiter, String value ) {
      if( value == null ) {
        value = "";
      }
      return String.format( formatter, key, index, delimiter, value );
    }
    
    /**
     * Returns <code>true</code> if the supplied key is matched by this style.
     * 
     * @param fullkey   The full key provided by the properties map.
     * 
     * @return   <code>true</code> if the supplied key is matched by this style.
     */
    public boolean matches( String fullkey ) {
      return validation.matcher( fullkey ).matches();
    }
    
    /**
     * Simply selects the key and the index/association portion of a key.
     * 
     * @param fullkey    The full key.
     * @param receiver   The receiver for the index/association portion of the key.
     */
    public void select( String fullkey, Tupel<String> receiver ) {
      receiver.setValues( splitter.split( fullkey ) );
    }
    
  } /* ENDENUM */
  
  private Set<String>                       names;
  private Map<String,Map<Integer,String>>   indexed;
  private Map<String,Map<String,String>>    associated;
  private Map<String,String>                simple;
  private KeyStyle                          keystyle;
  private boolean                           emptyisnull;
  
  private String                            delimiter;
  private String                            commentintro;
  
  // for temporary use
  private List<String>                      lines;

  /**
   * Initialises this Properties implementation.
   */
  public ExtProperties() {
    this( "=" );
  }
  
  /**
   * Initialises this Properties implementation.
   * 
   * @param del   The delimiter to be used for the key value pairs.
   */
  public ExtProperties( @KNotEmpty(name="del") String del ) {
    lines         = new ArrayList<String>();
    delimiter     = del;
    commentintro  = "#";
    emptyisnull   = false;
    names         = new HashSet<String>();
    indexed       = new Hashtable<String,Map<Integer,String>>();
    associated    = new Hashtable<String,Map<String,String>>();
    simple        = new HashMap<String,String>();
    keystyle      = KeyStyle.ArrayBraces;
  }
  
  /**
   * Changes the KeyStyle to be used for the properties.
   * 
   * @param newkeystyle   The new KeyStyle to be used. Not <code>null</code>.
   */
  public synchronized void setKeyStyle( @KNotNull(name="newkeystyle") KeyStyle newkeystyle ) {
    keystyle = newkeystyle;
  }
  
  /**
   * Returns the KeyStyle currently used.
   * 
   * @return   The KeyStyle currently used. Not <code>null</code>.
   */
  public synchronized KeyStyle getKeyStyle() {
    return keystyle;
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
   * Changes the delimiter for the key-value pairs.
   * 
   * @param del   The new delimiter for the key-value pairs. Neither <code>null</code> nor empty.
   */
  public synchronized void setDelimiter( @KNotEmpty(name="del") String del ) {
    delimiter = del;
  }

  /**
   * Returns the current delimiter for the key-value pairs.
   * 
   * @return   The current delimiter for the key-value pairs. Neither <code>null</code> nor empty.
   */
  public synchronized String getDelimiter() {
    return delimiter;
  }
  
  /**
   * Changes the literal that introduces a comment on a line.
   * 
   * @param intro   The new literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   */
  public synchronized void setCommentIntro( @KNotEmpty(name="intro") String intro ) {
    commentintro = intro;
  }
  
  /**
   * Returns the literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   * 
   * @return   The literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   */
  public synchronized String getCommentIntro() {
    return commentintro;
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
   * @param encoding   The encoding to use. Neither <code>null</code> nor empty.
   */
  public synchronized void load( 
    @KFile(name="input")         File       input, 
    @KNotNull(name="encoding")   Encoding   encoding 
  ) {
    lines = IoFunctions.readText( input, encoding );
    processProperties();
  }
  
  /**
   * Loads the current properties from an InputStream.
   * 
   * @param input      The source where to load the properties from. Not <code>null</code>.
   * @param encoding   The encoding to use. Neither <code>null</code> nor empty.
   */
  public synchronized void load( 
    @KNotNull(name="input")      InputStream   input, 
    @KNotNull(name="encoding")   Encoding      encoding 
  ) {
    lines = IoFunctions.readText( encoding.openReader( input ) );
    processProperties();
  }
  
  /**
   * Loas the current properties from a Reader.
   * 
   * @param input   The Reader where to load the properties from. Not <code>null</code>.
   */
  public synchronized void load( @KNotNull(name="input") Reader input ) {
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
  private String processProperty( String line ) {
    String  key   = null;
    String  value = null;
    int     idx   = line.indexOf( delimiter );
    if( idx == -1 ) {
      key = line;
    } else {
      key   = line.substring( 0, idx );
      value = line.substring( idx + delimiter.length() ).trim();
    }
    if( keystyle.matches( key ) ) {
      // we've got an indexed/associated property
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
      try {
        // try with an indexed one
        Integer indexval = Integer.valueOf( pair.getLast() );
        setIndexedProperty( pair.getFirst(), indexval.intValue(), value );
        return keystyle.toLine( pair.getFirst(), indexval, delimiter, value );
      } catch( NumberFormatException ex ) {
        // indexed fails, so it's an associated property
        setAssociatedProperty( pair.getFirst(), pair.getLast(), value );
        return keystyle.toLine( pair.getFirst(), pair.getLast(), delimiter, value );
      }
    } else {
      // simpliest property
      setSimpleProperty( key, value );
      return String.format( "%s%s%s", key, delimiter, value != null ? value : "" );
    }
  }

  /**
   * @see Properties#setProperty(String, String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #setIndexedProperty(String, int, String)}, {@link #setAssociatedProperty(String, String, String)}
   * or {@link #setSimpleProperty(String, String)} directly since they are cheaper.
   */
  public synchronized void setProperty( 
    @KNotEmpty(name="key")   String   key, 
                             String   value 
  ) {
    if( emptyisnull ) {
      // empty values will be treated as null values
      if ( (value != null) && value.length() == 0 ) {
        value = null;
      }
    }
    if( keystyle.matches( key ) ) {
      // we've got an indexed/associated property
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
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
  public synchronized void setIndexedProperty( 
    @KNotEmpty(name="key")   String   key, 
                             int      index, 
                             String   value 
  ) {
    Map<Integer,String> values = indexed.get( key );
    if( values == null ) {
      values = new HashMap<Integer,String>();
      indexed.put( key, values );
    }
    values.put( Integer.valueOf( index ), value );
    names.add( key );
  }
  
  /**
   * Sets an associated property.
   * 
   * @param key           The property key itself. Neither <code>null</code> nor empty. 
   * @param association   The association for this property.
   * @param value         The value to be set. Maybe <code>null</code>.
   */
  public synchronized void setAssociatedProperty( 
    @KNotEmpty(name="key")   String   key, 
                             String   association, 
                             String   value 
  ) {
    Map<String,String> values = associated.get( key );
    if( values == null ) {
      values = new HashMap<String,String>();
      associated.put( key, values );
    }
    values.put( association, value );
    names.add( key );
  }

  /**
   * Sets a simple property.
   * 
   * @param key     The property key itself. Neither <code>null</code> nor empty.
   * @param value   The value to be set. Maybe <code>null</code>.
   */
  public synchronized void setSimpleProperty( 
    @KNotEmpty(name="key")   String   key, 
                             String   value 
  ) {
    names.add( key );
    simple.put( key, value );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to an indexed property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to an indexed property.
   */
  public synchronized boolean isIndexedProperty( @KNotEmpty(name="key") String key ) {
    return indexed.containsKey( key );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to an associative property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to an associative property.
   */
  public synchronized boolean isAssociatedProperty( @KNotEmpty(name="key") String key ) {
    return associated.containsKey( key );
  }
  
  /**
   * Returns <code>true</code> if the supplied key refers to a simple property.
   * 
   * @param key   The key that has to be tested. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied key refers to a simple property.
   */
  public synchronized boolean isSimpleProperty( @KNotEmpty(name="key") String key ) {
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
   * @return   The list of sorted property values. Maybe <code>null</code> if the property doesn't
   *           exist and no default values have been provided.
   */
  public synchronized List<String> getIndexedProperty( 
    @KNotEmpty(name="key")   String         key, 
                             List<String>   defvalues 
  ) {
    Map<Integer,String> map = indexed.get( key );
    if( map != null ) {
      // get a list of the entries first
      List<Map.Entry<Integer,String>> values = new ArrayList<Map.Entry<Integer,String>>( map.entrySet() );
      // now sort them according to the indexes
      Collections.sort( values, new LocalBehaviour() );
      // now map them, since we're only interested in the values
      return FuFunctions.map( new LocalBehaviour(), values );
    } else {
      return defvalues;
    }
  }
  
  /**
   * Returns the value of an indexed property.
   *  
   * @param key        The key used to access the property. Neither <code>null</code> nor empty.
   * @param index      The index used to access the value.
   * 
   * @return   The indexed value. Maybe <code>null</code> if the value didn't exist.
   */
  public synchronized String getIndexedProperty( 
    @KNotEmpty(name="key")   String   key, 
                             int      index 
  ) {
    return getIndexedProperty( key, index, null );
  }
  
  /**
   * Returns the value of an indexed property.
   *  
   * @param key        The key used to access the property. Neither <code>null</code> nor empty.
   * @param index      The index used to access the value.
   * @param defvalue   A default value in case the value doesn't exist. Maybe <code>null</code>.
   * 
   * @return   The indexed value. Maybe <code>null</code> if the value didn't exist and no default
   *           value has been supplied.
   */
  public synchronized String getIndexedProperty( 
    @KNotEmpty(name="key")   String   key, 
                             int      index, 
                             String   defvalue 
  ) {
    Map<Integer,String> values = indexed.get( key );
    if( values != null ) {
      String result = values.get( Integer.valueOf( index ) );
      if( result == null ) {
        return defvalue;
      } else {
        return result;
      }
    } else {
      return defvalue;
    }
  }

  /**
   * Returns a map of associated values for a specific property.
   * 
   * @param key         The name of the property. Neither <code>null</code> nor empty.
   * @param defvalues   The default values which will be returned in case the property didn't exist.
   *                    Maybe <code>null</code>.
   *                    
   * @return   The map providing the associated values. If it's not the default values you're allowed
   *           to do changes to this map without altering this properties. Maybe <code>null</code>.
   */
  public synchronized Map<String,String> getAssociatedProperty( 
    @KNotEmpty(name="key")   String               key, 
                             Map<String,String>   defvalues 
  ) {
    Map<String,String> map = associated.get( key );
    if( map != null ) {
      return new HashMap<String,String>( map );
    } else {
      return defvalues;
    }
  }

  /**
   * Returns the value of an associated property.
   * 
   * @param key           The key used to access the property. Neither <code>null</code> nor empty.
   * @param association   The association name used to access the value. Neither <code>null</code> nor empty.
   * 
   * @return   The value stored using the associated property. Maybe <code>null</code> if none exists.
   */
  public synchronized String getAssociatedProperty( String key, String association ) {
    return getAssociatedProperty( key, association, null );
  }

  /**
   * Returns the value of an associated property.
   * 
   * @param key           The key used to access the property. Neither <code>null</code> nor empty.
   * @param association   The association name used to access the value. Neither <code>null</code> nor empty.
   * @param defvalue      The default value which will be returned in case none is available yet.
   *                      Maybe <code>null</code>.
   * 
   * @return   The value stored using the associated property. Maybe <code>null</code> if none exists
   *           and no default value has been supplied.
   */
  public synchronized String getAssociatedProperty( 
    @KNotEmpty(name="key")           String   key, 
    @KNotEmpty(name="association")   String   association, 
                                     String   defvalue 
  ) {
    Map<String,String> values = associated.get( key );
    if( values != null ) {
      String result = values.get( association );
      if( result == null ) {
        return defvalue;
      } else {
        return result;
      }
    } else {
      return defvalue;
    }
  }

  /**
   * Returns the value associated with the supplied simple key.
   * 
   * @param key   The key used to access the property. Neither <code>null</code> nor empty.
   * 
   * @return   The property value or <code>null</code> in case it didn't exist.
   */
  public synchronized String getSimpleProperty( String key ) {
    return getSimpleProperty( key, null );
  }
  
  /**
   * Returns the value associated with the supplied simple key.
   * 
   * @param key        The key used to access the property. Neither <code>null</code> nor empty.
   * @param defvalue   The default value to be used in case the property doesn't exist. 
   *                   Maybe <code>null</code>.
   * 
   * @return   The property value or <code>null</code> in case it didn't exist and no default value
   *           has been supplied.
   */
  public synchronized String getSimpleProperty( 
    @KNotEmpty(name="key")   String   key, 
                             String   defvalue 
  ) {
    String result = simple.get( key );
    if( result == null ) {
      return defvalue;
    } else {
      return result;
    }
  }

  /**
   * @see Properties#getProperty(String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #getIndexedProperty(String, int)}, {@link #getAssociatedProperty(String, String)}
   * or {@link #getSimpleProperty(String)} directly since they are cheaper.
   */
  public synchronized String getProperty( String key ) {
    return getProperty( key, null );
  }
  
  /**
   * @see Properties#getProperty(String, String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #getIndexedProperty(String, int, String)}, {@link #getAssociatedProperty(String, String, String)}
   * or {@link #getSimpleProperty(String, String)} directly since they are cheaper.
   */
  public synchronized String getProperty( String key, String defvalue ) {
    if( keystyle.matches( key ) ) {
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
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
   * @param encoding    The encoding to be used. Not <code>null</code>.
   */
  public synchronized void store( 
    @KNotNull(name="output")     OutputStream   output, 
    @KNotNull(name="encoding")   Encoding       encoding 
  ) {
    storeLines();
    IoFunctions.writeText( output, lines, encoding );
  }
  
  /**
   * Writes the content of this properties table to the supplied file.
   * 
   * @param file        The file to receive the property data. Not <code>null</code>.
   * @param encoding    The encoding to be used. Not <code>null</code>.
   */
  public synchronized void store( 
    @KNotNull(name="file")       File       file, 
    @KNotNull(name="encoding")   Encoding   encoding 
  ) {
    storeLines();
    IoFunctions.writeText( file, lines, encoding );
  }

  /**
   * Writes the content of this properties table to the supplied writer.
   * 
   * @param writer   The writer to receive the property data. Not <code>null</code>.
   */
  public synchronized void store( @KNotNull(name="writer") Writer writer ) {
    storeLines();
    IoFunctions.writeText( writer, lines );
  }
  
  /**
   * This function modifies the original content of this property file to conform with the current
   * state.
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
      
      if( keystyle.matches( key ) ) {
        
        // it's an indexed/associated key
        Tupel<String> pair = new Tupel<String>();
        keystyle.select( key, pair );
        
        // only apply it if this property has not been used before
        if( undone.contains( pair.getFirst() ) ) {
          
          // apply all index properties with one go (upcoming key won't be copied as this is
          // flagged using the set 'undone').
          try {
            Integer.parseInt( pair.getLast() );
            applyIndexed( newlines, pair.getFirst() );
          } catch( NumberFormatException ex ) {
            applyAssociated( newlines, pair.getFirst() );
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
          applyIndexed( newlines, currentkey );
        } else if( isAssociatedProperty( currentkey ) ) {
          applyAssociated( newlines, currentkey );
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
   */
  private void applyIndexed( List<String> receiver, String key ) {
    Map<Integer,String>             map  = indexed.get( key );
    if( map == null ) {
      // nothing to be done here
      return;
    }
    List<Map.Entry<Integer,String>> list = new ArrayList<Map.Entry<Integer,String>>( map.entrySet() );
    Collections.sort( list, new LocalBehaviour<Integer>() );
    for( int i = 0; i < list.size(); i++ ) {
      receiver.add( keystyle.toLine( key, list.get(i).getKey(), delimiter, list.get(i).getValue() ) );
    }
  }

  /**
   * Applies the associated property values. We're sorting here, too, to get reproducable results.
   * 
   * @param receiver   The new list of lines to be extended. Not <code>null</code>.
   * @param key        The key which values have to be added. Neither <code>null</code> nor empty.
   */
  private void applyAssociated( List<String> receiver, String key ) {
    Map<String,String>             map  = associated.get( key );
    if( map == null ) {
      // nothing to be done here
      return;
    }
    List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>( map.entrySet() );
    Collections.sort( list, new LocalBehaviour<String>() );
    for( int i = 0; i < list.size(); i++ ) {
      receiver.add( keystyle.toLine( key, list.get(i).getKey(), delimiter, list.get(i).getValue() ) );
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
    String value = simple.get( key );
    if( value == null ) {
      value = "";
    }
    receiver.add( String.format( "%s%s%s", key, delimiter, value ) );
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
  public synchronized void removeProperty( @KNotEmpty(name="key") String key ) {
    if( keystyle.matches( key ) ) {
      // we've got an indexed/associated property
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
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
   * Removes all indexed properties for a specific key. If this property doesn't exists, this method
   * will simply do nothing.
   * 
   * @param key   The key used to select all indexed properties. Neither <code>null</code> nor empty.
   */
  public synchronized void removeIndexedProperty( @KNotEmpty(name="key") String key ) {
    if( indexed.containsKey( key ) ) {
      indexed.remove( key );
    }
  }

  /**
   * Removes a specific indexed property value. If the property value doesn't exists, this method
   * will simply do nothing.
   * 
   * @param key     The key used for the indexed properties. Neither <code>null</code> nor empty.
   * @param index   The index used to identify the value which will be removed.
   */
  public synchronized void removeIndexedProperty( 
    @KNotEmpty(name="key")   String   key, 
                             int      index 
  ) {
    Map<Integer,String> map = indexed.get( key );
    if( map != null ) {
      map.remove( Integer.valueOf( index ) );
    }
  }

  /**
   * Removes all associated properties for a specific key. If this property doesn't exists, this method
   * will simply do nothing.
   * 
   * @param key   The key used to select all associated properties. Neither <code>null</code> nor empty.
   */
  public synchronized void removeAssociatedProperty( @KNotEmpty(name="key") String key ) {
    if( associated.containsKey( key ) ) {
      associated.remove( key );
    }
  }
  
  /**
   * Removes a specific associated property value. If the property value doesn't exist, this method
   * will simply do nothing.
   * 
   * @param key           The key used for the associated properties. Neither <code>null</code> nor empty.
   * @param association   The index used to identify the value which will be removed. Neither <code>null</code> nor empty.
   */
  public synchronized void removeAssociatedProperty( 
    @KNotEmpty(name="key")           String   key,
    @KNotEmpty(name="association")   String   association 
  ) {
    Map<String,String> map = associated.get( key );
    if( map != null ) {
      map.remove( association );
    }
  }
  
  /**
   * Removes the property values associated with the supplied key. If this property doesn't exist,
   * this method will simply do nothing.
   * 
   * @param key   The key used to identify the property. Neither <code>null</code> nor empty.
   */
  public synchronized void removeSimpleProperty( @KNotEmpty(name="key") String key ) {
    simple.remove( key );
  }

  /**
   * Implementation of custom behaviour.
   */
  private static final class LocalBehaviour<T extends Comparable> implements Comparator<Map.Entry<T,String>>, 
                                                                             Transform<Map.Entry<Integer,String>,String> {

    /**
     * {@inheritDoc}
     */
    public int compare( Map.Entry<T,String> entry1, Map.Entry<T,String> entry2 ) {
      return entry1.getKey().compareTo( entry2.getKey() );
    }

    /**
     * {@inheritDoc}
     */
    public String map( Map.Entry<Integer, String> input ) {
      return input.getValue();
    }

  } /* ENDCLASS */
    
} /* ENDCLASS */
