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
 * </ul>
 */
@KDiagnostic
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
   * Returns <code>true</code> if empty values shall be treated as <code>null</code> values.
   * 
   * @param enable   <code>true</code> <=> Enable this treatment.
   */
  public void setEmptyIsNull( boolean enable ) {
    emptyisnull = enable;
  }
  
  /**
   * Returns <code>true</code> if empty values shall be treated as <code>null</code> values.
   * 
   * @return   <code>true</code> <=> Empty values shall be treated as <code>null</code> values.
   */
  public boolean isEmptyNull() {
    return emptyisnull;
  }

  /**
   * Changes the delimiter for the key-value pairs.
   * 
   * @param del   The new delimiter for the key-value pairs. Neither <code>null</code> nor empty.
   */
  public void setDelimiter( @KNotEmpty(name="del") String del ) {
    delimiter = del;
  }

  /**
   * Returns the current delimiter for the key-value pairs.
   * 
   * @return   The current delimiter for the key-value pairs. Neither <code>null</code> nor empty.
   */
  public String getDelimiter() {
    return delimiter;
  }
  
  /**
   * Changes the literal that introduces a comment on a line.
   * 
   * @param intro   The new literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   */
  public void setCommentIntro( @KNotEmpty(name="intro") String intro ) {
    commentintro = intro;
  }
  
  /**
   * Returns the literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   * 
   * @return   The literal that introduces a comment on a line. Neither <code>null</code> nor empty.
   */
  public String getCommentIntro() {
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
   * 
   * @throws IOException   Either the loading process failed or the encoding information was wrong.
   */
  public synchronized void load( 
    @KFile(name="input")         File       input, 
    @KNotNull(name="encoding")   Encoding   encoding 
  ) throws IOException {
    lines = IoFunctions.readText( input, encoding );
    processProperties();
  }
  
  /**
   * Loads the current properties from an InputStream.
   * 
   * @param input      The source where to load the properties from. Not <code>null</code>.
   * @param encoding   The encoding to use. Neither <code>null</code> nor empty.
   * 
   * @throws IOException   Either the loading process failed or the encoding information was wrong.
   */
  public synchronized void load( 
    @KNotNull(name="input")      InputStream   input, 
    @KNotNull(name="encoding")   Encoding      encoding 
  ) throws IOException {
    lines = IoFunctions.readText( encoding.openReader( input ) );
    processProperties();
  }
  
  /**
   * Loas the current properties from a Reader.
   * 
   * @param input   The Reader where to load the properties from. Not <code>null</code>.
   * 
   * @throws IOException   Either the saving process failed or the encoding information was wrong.
   */
  public synchronized void load( 
    @KNotNull(name="input")   Reader   input 
  ) throws IOException {
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
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
      try {
        Integer indexval = Integer.valueOf( pair.getLast() );
        setIndexedProperty( pair.getFirst(), indexval.intValue(), value );
        return keystyle.toLine( pair.getFirst(), indexval, delimiter, value );
      } catch( NumberFormatException ex ) {
        setAssociatedProperty( pair.getFirst(), pair.getLast(), value );
        return keystyle.toLine( pair.getFirst(), pair.getLast(), delimiter, value );
      }
    } else {
      setSimpleProperty( key, value );
      return String.format( "%s%s%s", key, delimiter, value );
    }
  }

  /**
   * @see Properties#setProperty(String, String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #setIndexedProperty(String, int, String)}, {@link #setAssociatedProperty(String, String, String)}
   * or {@link #setSimpleProperty(String, String)} directly since they are cheaper.
   */
  public void setProperty( String key, String value ) {
    if( emptyisnull ) {
      // empty values will be treated as null values
      if ( (value != null) && value.length() == 0 ) {
        value = null;
      }
    }
    if( keystyle.matches( key ) ) {
      Tupel<String> pair = new Tupel<String>();
      keystyle.select( key, pair );
      try {
        setIndexedProperty( pair.getFirst(), Integer.parseInt( pair.getLast() ), value );
      } catch( NumberFormatException ex ) {
        setAssociatedProperty( pair.getFirst(), pair.getLast(), value );
      }
    } else {
      setSimpleProperty( key, value );
    }
  }
  
  public void setIndexedProperty( String key, int index, String value ) {
    Map<Integer,String> values = indexed.get( key );
    if( values == null ) {
      values = new HashMap<Integer,String>();
      indexed.put( key, values );
    }
    values.put( Integer.valueOf( index ), value );
    names.add( key );
  }
  
  public void setAssociatedProperty( String key, String association, String value ) {
    Map<String,String> values = associated.get( key );
    if( values == null ) {
      values = new HashMap<String,String>();
      associated.put( key, values );
    }
    values.put( association, value );
    names.add( key );
  }
  
  public void setSimpleProperty( String key, String value ) {
    names.add( key );
    simple.put( key, value );
  }
  
  public boolean isIndexedProperty( @KNotEmpty(name="key") String key ) {
    return indexed.containsKey( key );
  }
  
  public boolean isAssociatedProperty( @KNotEmpty(name="key") String key ) {
    return associated.containsKey( key );
  }
  
  public boolean isSimpleProperty( @KNotEmpty(name="key") String key ) {
    return simple.containsKey( key );
  }

  public List<String> getIndexedProperty( String key, List<String> defvalues ) {
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
  
  public String getIndexedProperty( String key, int index ) {
    return getIndexedProperty( key, index, null );
  }
  
  public String getIndexedProperty( String key, int index, String defvalue ) {
    Map<Integer,String> values = indexed.get( key );
    if( values != null ) {
      if( values.containsKey( Integer.valueOf( index ) ) ) {
        return values.get( Integer.valueOf( index ) );
      } else {
        return defvalue;
      }
    } else {
      return defvalue;
    }
  }
  
  public Map<String,String> getAssociatedProperty( String key, Map<String,String> defvalues ) {
    Map<String,String> map = associated.get( key );
    if( map != null ) {
      return new HashMap<String,String>( map );
    } else {
      return defvalues;
    }
  }

  public String getAssociatedProperty( String key, String association ) {
    return getAssociatedProperty( key, association, null );
  }
  
  public String getAssociatedProperty( String key, String association, String defvalue ) {
    Map<String,String> values = associated.get( key );
    if( values != null ) {
      if( values.containsKey( association ) ) {
        return values.get( association );
      } else {
        return defvalue;
      }
    } else {
      return defvalue;
    }
  }

  public String getSimpleProperty( String key ) {
    return getSimpleProperty( key, null );
  }
  
  public String getSimpleProperty( String key, String defvalue ) {
    if( simple.containsKey( key ) ) {
      return simple.get( key );
    } else {
      return defvalue;
    }
  }

  /**
   * @see Properties#getProperty(String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #getIndexedProperty(String, int)}, {@link #getAssociatedProperty(String, String)}
   * or {@link #getSimpleProperty(String)} directly since they are cheaper.
   */
  public String getProperty( String key ) {
    return getProperty( key, null );
  }
  
  /**
   * @see Properties#getProperty(String,String)
   * 
   * If you already know the property type you want to setup, you should use one of the methods
   * {@link #getIndexedProperty(String, int, String)}, {@link #getAssociatedProperty(String, String, String)}
   * or {@link #getSimpleProperty(String, String)} directly since they are cheaper.
   */
  public String getProperty( String key, String defvalue ) {
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
  
  public void store( OutputStream output, Encoding encoding ) {
    storeLines();
    IoFunctions.writeText( output, lines, encoding );
  }
  
  public void store( File file, Encoding encoding ) {
    storeLines();
    IoFunctions.writeText( file, lines, encoding );
  }

  public void store( Writer writer ) {
    storeLines();
    IoFunctions.writeText( writer, lines );
  }
  
  private void storeLines() {
    List<String> newlines = new ArrayList<String>();
    Set<String>  undone   = new HashSet<String>( names );
    for( int i = 0; i < lines.size(); i++ ) {
      String line = lines.get(i);
      if( (line.length() == 0) || line.startsWith( commentintro ) ) {
        newlines.add( line );
      } else {
        int     idx   = line.indexOf( delimiter );
        String  key   = null;
        if( idx == -1 ) {
          key = line;
        } else {
          key = line.substring( 0, idx );
        }
        if( keystyle.matches( key ) ) {
          Tupel<String> pair = new Tupel<String>();
          keystyle.select( key, pair );
          if( undone.contains( pair.getFirst() ) ) {
            try {
              Integer.parseInt( pair.getLast() );
              applyIndexed( newlines, pair.getFirst() );
              undone.remove( pair.getFirst() );
            } catch( NumberFormatException ex ) {
              applyAssociated( newlines, pair.getFirst() );
              undone.remove( pair.getFirst() );
            }
          }
        } else {
          if( undone.contains( key ) ) {
            applySimple( newlines, key );
            undone.remove( key );
          }
        }
      }
    }
    if( ! undone.isEmpty() ) {
      String[] sorted = undone.toArray( new String[ undone.size() ] );
      Arrays.sort( sorted );
      for( String currentkey : sorted ) {
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

  private void applyIndexed( List<String> receiver, String key ) {
    List<Map.Entry<Integer,String>> list = new ArrayList<Map.Entry<Integer,String>>( indexed.get( key ).entrySet() );
    Collections.sort( list, new LocalBehaviour<Integer>() );
    for( int i = 0; i < list.size(); i++ ) {
      receiver.add( keystyle.toLine( key, list.get(i).getKey(), delimiter, list.get(i).getValue() ) );
    }
  }

  private void applyAssociated( List<String> receiver, String key ) {
    List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>( associated.get( key ).entrySet() );
    Collections.sort( list, new LocalBehaviour<String>() );
    for( int i = 0; i < list.size(); i++ ) {
      receiver.add( keystyle.toLine( key, list.get(i).getKey(), delimiter, list.get(i).getValue() ) );
    }
  }
  
  private void applySimple( List<String> receiver, String key ) {
    String value = simple.get( key );
    if( value == null ) {
      value = "";
    }
    receiver.add( String.format( "%s%s%s", key, delimiter, value ) );
  }
  
  /**
   * @see Properties#propertyNames()
   */
  public Enumeration<String> propertyNames() {
    return ArrayFunctions.enumeration( names.toArray( new String[ names.size()] ) );
  }
  
  public static final void main( String[] args ) throws IOException {
    File file = new File( "sample.properties" );
    ExtProperties props = new ExtProperties();
    props.load( file, Encoding.getDefault() );
  }

  private static final class LocalBehaviour<T extends Comparable> implements Comparator<Map.Entry<T,String>>, Transform<Map.Entry<Integer,String>,String> {

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
