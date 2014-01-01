/**
 * Name........: MiscFunctions
 * Description.: Collection of various functions. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.sys.*;

import java.text.*;

import java.util.regex.*;

import java.util.*;
import java.util.Date;

import java.net.*;

import java.io.*;

import java.lang.reflect.*;
import java.sql.*;

/**
 * Collection of various functions.
 */
public class MiscFunctions {

  private static final Map<String,String> REPLACEMENTS = SysProperty.createReplacementMap( true );
  
  private static final Set<String> TRUEVALUES;
  
  static {
    
    TRUEVALUES  = new HashSet<String>();
    
    TRUEVALUES  . add( "true"   );
    TRUEVALUES  . add( "ja"     );
    TRUEVALUES  . add( "yes"    );
    TRUEVALUES  . add( "on"     );
    TRUEVALUES  . add( "ein"    );
    TRUEVALUES  . add( "an"     );
    TRUEVALUES  . add( "1"      );
    TRUEVALUES  . add( "-1"     );
    
  }

  /**
   * Prevent instantiation.
   */
  private MiscFunctions() {
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Byte> asList( byte ... values ) {
    List<Byte> result = new ArrayList<Byte>( values.length );
    for( byte v : values ) {
      result.add( Byte.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Short> asList( short ... values ) {
    List<Short> result = new ArrayList<Short>( values.length );
    for( short v : values ) {
      result.add( Short.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Integer> asList( int ... values ) {
    List<Integer> result = new ArrayList<Integer>( values.length );
    for( int v : values ) {
      result.add( Integer.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Long> asList( long ... values ) {
    List<Long> result = new ArrayList<Long>( values.length );
    for( long v : values ) {
      result.add( Long.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Float> asList( float ... values ) {
    List<Float> result = new ArrayList<Float>( values.length );
    for( float v : values ) {
      result.add( Float.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Double> asList( double ... values ) {
    List<Double> result = new ArrayList<Double>( values.length );
    for( double v : values ) {
      result.add( Double.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Boolean> asList( boolean ... values ) {
    List<Boolean> result = new ArrayList<Boolean>( values.length );
    for( boolean v : values ) {
      result.add( Boolean.valueOf(v) );
    }
    return result;
  }

  /**
   * Like {@link Arrays#asList(Object...)} with the difference that these functions create objects from primitives.
   * 
   * @param values   A list of primitive values. Not <code>null</code>.
   * 
   * @return   A list of objects. Not <code>null</code>.
   */
  public static List<Character> asList( char ... values ) {
    List<Character> result = new ArrayList<Character>( values.length );
    for( char v : values ) {
      result.add( Character.valueOf(v) );
    }
    return result;
  }

  /**
   * Convenience function which waits until the supplied Thread finishes his task or will be interrupted.
   * 
   * @param thread   The Thread that will be executed. Maybe <code>null</code>.
   */
  public static void joinThread( Thread thread ) {
    if( thread != null ) {
      try {
        thread.join();
      } catch( InterruptedException ex ) {
      }
    }
  }
  
  /**
   * Returns <code>true</code> if a byte sequence could be found at a specific position.
   * 
   * @param data        The data block where to search for. Not <code>null</code>.
   * @param tocompare   The byte sequence which have to be compared with. Not <code>null</code>.
   * @param offset      The offset within the data block where the sequence seems to be located.
   * 
   * @return   <code>true</code> <=> The byte sequence is available at the specified offset.
   */
  public static boolean compare( byte[] data, byte[] tocompare, int offset ) {
    for( int i = 0; i < tocompare.length; i++, offset++ ) {
      if( offset == data.length ) {
        // premature end of the comparison process
        return false;
      }
      if( data[ offset ] != tocompare[i] ) {
        return false;
      }
    }
    return true; 
  }
  
  /**
   * Returns true if a char sequence could be found at a specific position.
   * 
   * @param data        The data block where to search for. Not <code>null</code>.
   * @param tocompare   The char sequence which have to be compared with. Not <code>null</code>.
   * @param offset      The offset within the data block where the sequence seems to be located.
   * 
   * @return   <code>true</code> <=> The char sequence is available at the specified offset.
   */
  public static boolean compare( char[] data, char[] tocompare, int offset ) {
    for( int i = 0; i < tocompare.length; i++, offset++ ) {
      if( offset == data.length ) {
        // premature end of the comparison process
        return false;
      }
      if( data[ offset ] != tocompare[i] ) {
        return false;
      }
    }
    return true; 
  }
  
  /**
   * Tries to find a byte sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the byte sequence or -1 in case there's no sequence.
   */
  public static int indexOf( byte[] buffer, byte[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence can't fit completely, so it's not available
      return -1;
    }
    for( int i = pos; i < last; i++ ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( byte[] buffer, byte[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    for( int i = last; i >= pos; i-- ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Tries to find the last character sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The character sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the last character sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( char[] buffer, char[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    for( int i = last; i >= pos; i-- ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public static int indexOf( char[] buffer, char[] sequence, int pos ) {
    int last = buffer.length - sequence.length;
    if( (last < 0) || (pos > last) ) {
      // the sequence can't fit completely, so it's not available
      return -1;
    }
    for( int i = pos; i < last; i++ ) {
      if( buffer[i] == sequence[0] ) {
        // we're having a possible match, so compare the sequence
        if( compare( buffer, sequence, i ) ) {
          return i;
        }
      }
    }
    return -1;
  }
  
  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( byte[] data, byte[] sequence ) {
    return lastIndexOf( data, sequence, 0 );
  }

  /**
   * Tries to find the last char sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last char sequence or -1 in case there's no sequence.
   */
  public static int lastIndexOf( char[] data, char[] sequence ) {
    return lastIndexOf( data, sequence, 0 );
  }

  /**
   * Tries to find a byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the byte sequence or -1 in case there's no sequence.
   */
  public static int indexOf( byte[] data, byte[] sequence ) {
    return indexOf( data, sequence, 0 );
  }

  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public static int indexOf( char[] data, char[] sequence ) {
    return indexOf( data, sequence, 0 );
  }
  
  /**
   * Copies a small range from a specific array.
   * 
   * @param source   The array providing the input data. Not <code>null</code>.
   * @param offset   The offset of the starting point within the input data. Must be in the range [0..source.length(
   * @param length   The amount of bytes which have to be copied. Must be greater than 0.
   * 
   * @return   A copy of the desired range. Neither <code>null</code> nor empty.
   */
  public static byte[] copyRange( byte[] source, int offset, int length ) {
    byte[] result = new byte[ length ];
    System.arraycopy( source, offset, result, 0, length );
    return result;
  }
  
  /**
   * Creates a new byte sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The byte sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the byte sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static byte[] insert( byte[] destination, byte[] newsequence, int index ) {
    if( destination.length == 0 ) {
      return new byte[0];
    }
    if( index >= destination.length ) {
      return Arrays.copyOf( destination, destination.length );
    }
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    if( index > 0 ) {
      byteout.write( destination, 0, index );
    }
    if( newsequence.length > 0 ) {
      byteout.write( newsequence, 0, newsequence.length );
    }
    if( index < destination.length ) {
      byteout.write( destination, index, destination.length - index );
    }
    return byteout.toByteArray();
  }

  /**
   * Creates a new char sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The char sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the char sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static char[] insert( char[] destination, char[] newsequence, int index ) {
    if( destination.length == 0 ) {
      return new char[0];
    }
    if( index >= destination.length ) {
      return Arrays.copyOf( destination, destination.length );
    }
    CharArrayWriter charout = new CharArrayWriter();
    if( index > 0 ) {
      charout.write( destination, 0, index );
    }
    if( newsequence.length > 0 ) {
      charout.write( newsequence, 0, newsequence.length );
    }
    if( index < destination.length ) {
      charout.write( destination, index, destination.length - index );
    }
    return charout.toCharArray();
  }
  
  /**
   * Expands the supplied input String while replacing environment variables with their values.
   * 
   * @param input   The String to expand. Neither <code>null</code> nor empty.
   * 
   * @return   A String with resolved environment variables. Neither <code>null</code> nor empty.
   */
  public static String expandVariables( String input ) {
    Map<String, String> variables     = new Hashtable<String, String>();
    Enumeration<?>      propertynames = System.getProperties().propertyNames();
    while( propertynames.hasMoreElements() ) {
      String    key   = (String) propertynames.nextElement();
      String    value = System.getProperty( key );
      String[]  keys  = SystemInfo.getRunningOS().getVariableKeys( key );
      for( String formattedkey : keys ) {
        variables.put( formattedkey, value );
      }
    }
    return StringFunctions.replace( input, variables );
  }

  /**
   * Returns a File location depending on the supplied formatting String.
   * 
   * @param format   A formatting String which is allowed to contain short property keys as described in SystemProperty. 
   *                 Each key must be enclosed using a '%' character. Neither <code>null</code> nor empty.
   * 
   * @return   The File location which has been evaluated. Not <code>null</code>.
   */
  public static File expandFileLocation( String format ) {
    return new File( StringFunctions.replace( format, REPLACEMENTS ) );
  }

  /**
   * Joins the supplied buffers.
   * 
   * @param buffers   A list of buffers which has to be joined. Not <code>null</code>.
   * 
   * @return   A joined buffer. Not <code>null</code>.
   */
  public static byte[] joinBuffers( byte[] ... buffers ) {
    int size = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        size += buffers[i].length;
      }
    }
    byte[] result = new byte[ size ];
    int    offset = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        System.arraycopy( buffers[i], 0, result, offset, buffers[i].length );
        offset += buffers[i].length;
      }
    }
    return result;
  }

  /**
   * Joins the supplied buffers.
   * 
   * @param buffers   A list of buffers which has to be joined. Not <code>null</code>.
   * 
   * @return   A joined buffer. Not <code>null</code>.
   */
  public static char[] joinBuffers( char[] ... buffers ) {
    int size = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        size += buffers[i].length;
      }
    }
    char[] result = new char[ size ];
    int    offset = 0;
    for( int i = 0; i < buffers.length; i++ ) {
      if( buffers[i] != null ) {
        System.arraycopy( buffers[i], 0, result, offset, buffers[i].length );
        offset += buffers[i].length;
      }
    }
    return result;
  }

  /**
   * Parses a date using a given list of patterns.
   * 
   * @param value      The value that has to be parsed. Neither <code>null</code> nor empty.
   * @param patterns   The allowed patterns. Neither <code>null</code> nor empty.
   * 
   * @return   The date that has been parsed or <code>null</code> in case of a failure.
   */
  public static Date parseDate( String value, String ... patterns ) {
    Date result = null;
    for( int i = 0; i < patterns.length; i++ ) {
      SimpleDateFormat formatter = new SimpleDateFormat( patterns[i] );
      try {
        result = formatter.parse( value );
      } catch( ParseException ex ) {
        // simply ignore this. a failure is indicated by a null return value.
      }
    }
    return result;
  }

  /**
   * Parses a date using a specific pattern.
   * 
   * @param value      The value that has to be parsed. Neither <code>null</code> nor empty.
   * @param patterns   The allowed patterns. Neither <code>null</code> nor empty.
   * 
   * @return   The date as a Calendar instance that has been parsed or <code>null</code> in case of a failure.
   */
  public static Calendar parseCalendar( String value, String ... patterns ) {
    Date date = parseDate( value, patterns );
    if( date != null ) {
      Calendar result = Calendar.getInstance();
      result.setTime( date );
      return result;
    } else {
      return null;
    }
  }
  
  /**
   * Interpretes a value as a boolean.
   * 
   * @param value   The value which has to be parsed. Not <code>null</code>.
   * 
   * @return   <code>true</code>  <=> If the supplied literal has one of the values {@link #TRUEVALUES} (case insensitive).
   *           <code>false</code> <=> All other cases.
   */
  public static boolean parseBoolean( String value ) {
    return TRUEVALUES.contains( value.toLowerCase() );
  }

  /**
   * Instantiates the supplied class with the supplied arguments.
   * 
   * @param classname   The class that shall be instantiated. Neither <code>null</code> nor empty.
   * @param args        The arguments which have to be passed to the constructor. If omitted the default constructor 
   *                    will be used. If passed each element must be non-<code>null</code> in order to determine the 
   *                    parameter type.
   * 
   * @return   <code>null</code> <=> If the class could not be instantiated otherwise the instance itself.
   */
  public static Object newInstance( String classname, Object ... args ) {
    return newInstance( false, classname, args );
  }
  
  /**
   * Instantiates the supplied class with the supplied arguments. The behaviour of this
   * method can be configured using the supplied flag.
   * 
   * @param fail        <code>true</code> <=> If the creation of the instance fails a FailureException is generation 
   *                    with the constant code {@link "CommonLibraryConstants#RTE_REFLECTIONS"}. Otherwise this method 
   *                    returns normally with the value <code>null</code>.
   * @param classname   The class that shall be instantiated. Neither <code>null</code> nor empty.
   * @param args        The arguments which have to be passed to the constructor. If omitted the default constructor 
   *                    will be used. If passed each element must be non-<code>null</code> in order to determine the 
   *                    parameter type.
   * 
   * @return   If <code>fail</code> is <code>true</code> the value is not <code>null</code>. Otherwise it is 
   *           <code>null</code> in case of a failure.
   *           
   * @throws FailureException   The instantiation failed. Will only be raised if <code>fail</code> is set to <code>true</code>.
   */
  @SuppressWarnings("unchecked")
  public static Object newInstance( boolean fail, String classname, Object ... args ) {
    try {
      Class clazz = Class.forName( classname );
      if( (args == null) || (args.length == 0) ) {
        return clazz.newInstance();
      } else {
        Class[] params = new Class[ args.length ];
        for( int i = 0; i < params.length; i++ ) {
          params[i] = args[i].getClass();
        }
        try {
          return clazz.getConstructor( params ).newInstance( args );
        } catch( NoSuchMethodException ex ) {
          Constructor[] constructors = clazz.getDeclaredConstructors();
          Constructor   constructor  = findMatchingConstructor( constructors, params );
          if( constructor != null ) {
            return constructor.newInstance( args );
          } else {
            reflectionFailure( fail, ex );
          }
        }
      }
    } catch( Exception ex ) { 
      reflectionFailure( fail, ex );
    }
    return null;
  }

  /**
   * Identifies a constructor by it's signature. This might be necessary if the appropriate Constructor uses an 
   * interface, so using a concrete type might fail to locate the right Constructor.
   *  
   * @param candidates   The possible candidates of Constructors. Not <code>null</code>.
   * @param params       The current signature used to locate the Constructor. Not <code>null</code>.
   * 
   * @return   The Constructor if it could be found. Maybe <code>null</code>.
   */
  private static Constructor findMatchingConstructor( Constructor[] candidates, Class<?>[] params ) {
    for( Constructor constructor : candidates ) {
      Class[] expectedparams = constructor.getParameterTypes();
      if( (expectedparams != null) && (expectedparams.length == params.length) ) {
        boolean matches = true;
        for( int i = 0; i < expectedparams.length; i++ ) {
          if( ! expectedparams[i].isAssignableFrom( params[i] ) ) {
            matches = false;
            break;
          }
        }
        if( matches ) {
          return constructor;
        }
      }
    }
    return null;
  }
  
  /**
   * Causes an exception if desired.
   * 
   * @param fail    <code>true</code> <=> An exception has to be thrown.
   * @param cause   The cause of the failure. Not <code>null</code>.
   */
  private static void reflectionFailure( boolean fail, Exception cause ) {
    if( fail ) {
      throw new FailureException( FailureCode.Reflections, cause );
    }
  }
  
  /**
   * Returns a new Comparator which can be used to sort Map.Entry records according to it's keys.
   * 
   * @param type   The type of the key. Not <code>null</code>.
   */
  public static <T extends Comparable> Comparator<Map.Entry<T,?>> newKeyComparator( Class<T> type ) {
    return new KeyComparator<T>();
  }
  
  /**
   * Sorts the supplied list and makes sure that every entry only occures once. 
   * 
   * @param list   The list of Comparable instances. This list must support the {@link List#remove(int)} method. 
   *               Not <code>null</code>.
   * 
   * @return   The supplied list. Not <code>null</code>.
   */
  public static <T extends Comparable<T>> List<T> toUniqueList( List<T> list ) {
    Collections.sort( list );
    for( int i = list.size() - 1; i > 0; i-- ) {
      if( list.get(i).compareTo( list.get( i - 1 ) ) == 0 ) {
        list.remove(i);
      }
    }
    return list;
  }
  
  /**
   * Creates a set from the supplied elements. 
   * 
   * @param elements   The elements that shall be collected within a set. Maybe <code>null</code>.
   * 
   * @return   The set created from the supplied elements. Not <code>null</code>.
   */
  public static <T> Set<T> toSet( T ... elements ) {
    Set<T> result = new HashSet<T>();
    if( elements != null ) {
      for( int i = 0; i < elements.length; i++ ) {
        result.add( elements[i] );
      }
    }
    return result;
  }

  /**
   * Creates a copy of the supplied instance.
   * 
   * @param source   The instance that has to be copied. Maybe <code>null</code>.
   * 
   * @return   A copy of the supplied instance. <code>null</code> if <code>source</code> was <code>null</code>.
   */
  public static <T extends Serializable> T clone( T source ) {
    if( source != null ) {
      ByteArrayOutputStream byteout   = null;
      ObjectOutputStream    objectout = null;
      try {
        byteout   = new ByteArrayOutputStream();
        objectout = new ObjectOutputStream( byteout );
        objectout.writeObject( source );
        objectout.flush();
        objectout.close();
        ByteArrayInputStream  bytein    = new ByteArrayInputStream( byteout.toByteArray() );
        ObjectInputStream     objectin  = new ObjectInputStream( bytein );
        return (T) objectin.readObject();
      } catch( IOException ex ) {
        // since we're only working in memory this should never happen
      } catch( ClassNotFoundException ex ) {
        // cannot happen since we've just serialised the type ourselves so it's guarantueed 
      }
    }
    return null;
  }
  
  /**
   * Returns a list with regions providing the ranges for a matched pattern.
   * 
   * @param pattern    The pattern to be matched. Not <code>null</code>.
   * @param sequence   The sequence where to look for the pattern. Not <code>null</code>.
   * 
   * @return   A list of regions providing the positions within the sequence. Not <code>null</code>.
   */
  public static List<int[]> getRegexRegions( Pattern pattern, String sequence ) {
    List<int[]> result  = new ArrayList<int[]>();
    Matcher     matcher = pattern.matcher( sequence );
    while( matcher.find() ) {
      result.add( new int[] { matcher.start(), matcher.end() } );
    }
    return result;
  }

  /**
   * Closes the supplied Socket. 
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param socket   The Socket that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an exception comes up.
   */
  public static void close( boolean fail, Socket socket ) {
    if( socket != null ) {
      try {
        socket.close();
      } catch( IOException ex ) {
        if( fail ) {
          throw new FailureException( FailureCode.Close, ex );
        }
      }
    }
  }

  /**
   * Closes the supplied ServerSocket. 
   * 
   * @param fail     <code>true</code> <=> Cause an exception if it happens.
   * @param socket   The ServerSocket that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an exception comes up.
   */
  public static void close( boolean fail, ServerSocket socket ) {
    if( socket != null ) {
      try {
        socket.close();
      } catch( IOException ex ) {
        if( fail ) {
          throw new FailureException( FailureCode.Close, ex );
        }
      }
    }
  }
  
  /**
   * Closes the supplied Closeable. 
   * 
   * @param fail        <code>true</code> <=> Cause an exception if it happens.
   * @param closeable   The Closeable that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an exception comes up.
   */
  public static void close( boolean fail, Closeable closeable ) {
    if( closeable != null ) {
      try {
        closeable.close();
      } catch( IOException ex ) {
        if( fail ) {
          throw new FailureException( FailureCode.Close, ex );
        }
      }
    }
  }

  /**
   * Closes the supplied Closeable. 
   * 
   * @param closeable   The Closeable that has to be closed. Maybe <code>null</code>.
   */
  public static void close( Closeable closeable ) {
    close( false, closeable );
  }

  /**
   * Closes the supplied Connection. 
   * 
   * @param fail         <code>true</code> <=> Cause an exception if it happens.
   * @param connection   The connection that has to be closed. Maybe <code>null</code>.
   * 
   * @throws FailureException   Will be launched only when <code>fail</code> is set to true and an exception comes up.
   */
  public static void close( boolean fail, Connection connection ) {
    if( connection != null ) {
      try {
        connection.close();
      } catch( SQLException ex ) {
        if( fail ) {
          throw new FailureException( FailureCode.Close, ex );
        }
      }
    }
  }
  
  /**
   * Closes the supplied Connection. 
   * 
   * @param connection   The connection that has to be closed. Maybe <code>null</code>.
   */
  public static void close( Connection connection ) {
    close( false, connection );
  }
  
  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @param servicetype   The desired service type. Not <code>null</code>
   * 
   * @return   A list with all SPI services currently available. Not <code>null</code>.
   */
  public static <T> List<T> loadSPIServices( Class<T> servicetype ) {
    List<T>          result    = new ArrayList<T>();
    ServiceLoader<T> spiloader = ServiceLoader.load( servicetype );
    for( T service : spiloader ) {
      result.add( service );
    }
    return result;
  }

  /**
   * Returns <code>true</code> if the supplied year is a leap year.
   * 
   * @param year   The year which has to be tested.
   * 
   * @return   <code>true</code> <=> The supplied year is a leap year.
   */
  public static boolean isLeapYear( int year ) {
    if( (year % 400) == 0 ) {
      return true;
    } else if( (year % 100) == 0 ) {
      return false;
    } else {
      return (year % 4) == 0;
    }
  }

  /**
   * Returns <code>true</code> if the supplied date is a leap year.
   * 
   * @param date   The date which has to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied date is a leap year.
   */
  @SuppressWarnings("deprecation")
  public static boolean isLeapYear( Date date ) {
    return isLeapYear( date.getYear() + 1900 );
  }
  
  /**
   * Joins the supplied arrays to a single one. It is legal to supply <code>null</code> values.
   * 
   * @param arrays   The arrays that are supplied to be joined. Maybe <code>null</code>.
   * 
   * @return   A joined array. Not <code>null</code>.
   */
  @SuppressWarnings("null")
  public static <T> T[] join( T[] ... arrays ) {
    int length = 0;
    int j      = -1;
    if( arrays != null ) {
      for( int i = arrays.length - 1; i >= 0; i-- ) {
        if( arrays[i] != null ) {
          length += arrays[i].length;
          j       = i;
        }
      }
    }
    if( length == 0 ) {
      return (T[]) Arrays.<T>asList().toArray();
    }
    T[] result = Arrays.copyOf( arrays[j], length );
    int offset = arrays[j].length;
    for( int i = j + 1; i < arrays.length; i++ ) {
      if( arrays[i] != null ) {
        System.arraycopy( arrays[i], 0, result, offset, arrays[i].length );
        offset += arrays[i].length;
      }
    }
    return result;
  }
  
  /**
   * Returns the constructor associated with a specific type.
   * 
   * @param type     The type used to access the constructor. Not <code>null</code>. 
   * @param params   The parameter types for the constructor. Maybe <code>null</code>.
   * 
   * @return   The Constructor if there is one apropriate one. Maybe <code>null</code>.
   */
  public static Constructor getConstructor( Class<?> type, Class<?> ... params ) {
    try {
      return type.getDeclaredConstructor( params );
    } catch( Exception ex ) {
      throw null;
    }
  }

  /**
   * Returns the method associated with a specific type.
   * 
   * @param type     The type used to access the method. Not <code>null</code>.
   * @param name     The name of the method. Neither <code>null</code> nor empty. 
   * @param params   The parameter types for the method. Maybe <code>null</code>.
   * 
   * @return   The Constructor if there is one apropriate one. Maybe <code>null</code>.
   */
  public static Method getMethod( Class<?> type, String name, Class<?> ... params ) {
    try {
      return type.getDeclaredMethod( name, params );
    } catch( Exception ex ) {
      throw null;
    }
  }

  /**
   * Implementation of a Comparator used for the key part of a Map.Entry.
   */
  private static final class KeyComparator<T extends Comparable> implements Comparator<Map.Entry<T,?>> {
    
    @Override
    public int compare( Map.Entry<T, ?> o1, Map.Entry<T, ?> o2 ) {
      if( (o1 == null) && (o2 == null) ) {
        return 0;
      }
      if( (o1 != null) && (o2 != null) ) {
        return o1.getKey().compareTo( o2.getKey() );
      }
      if( o1 != null ) {
        return 1;
      } else /* if( o2 != null ) */ {
        return -1;
      }
    }

  } /* ENDCLASS */
  
} /* ENDCLASS */
