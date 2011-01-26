/**
 * Name........: MiscFunctions
 * Description.: Collection of various functions. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.constants.*;
import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.libs.common.sys.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.text.*;

import java.util.regex.*;

import java.lang.reflect.*;

import java.util.*;

import java.io.*;

/**
 * Collection of various functions.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class MiscFunctions {

  private static final Map<String,String> REPLACEMENTS = SystemProperty.createReplacementMap( true );
  
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
   * Convenience function which waits until the supplied Thread finishes his task or will be 
   * interrupted.
   * 
   * @param thread   The Thread that will be executed. Maybe <code>null</code>.
   */
  public static final void joinThread( Thread thread ) {
    if( thread != null ) {
      try {
        thread.join();
      } catch( InterruptedException ex ) {
      }
    }
  }
  
  /**
   * Sleeps for the default time as defined in CommonProperty .
   */
  public static final void sleep() {
    Integer sleepingtime = CommonProperty.Sleep.getValue();
    sleep( sleepingtime.intValue() );
  }
  
  /**
   * Sleeps for the supplied number of milliseconds.
   * 
   * @param delay   The time to sleep in milliseconds.
   */
  public static final void sleep( 
    @KIPositive(name="delay")   long   delay 
  ) {
    while( delay > 0 ) {
      long before = System.currentTimeMillis();
      try {
        Thread.sleep( delay );
      } catch( InterruptedException ex ) {
      }
      long after  = System.currentTimeMillis();
      long diff   = after - before;
      delay       = delay - diff;
    }
  }
  
  /**
   * Convenience method which allows to write code in the style of 
   * <code>sleep(12, TimeUnit.Millisecond)</code>.
   * 
   * @param num    The number of units.
   * @param unit   The time unit itself. Not <code>null</code>.
   */
  public static final void sleep( 
    @KIPositive(name="num")   int        num, 
    @KNotNull(name="unit")    TimeUnit   unit 
  ) {
    sleep( unit.amount( num ) );
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
  public static final boolean compare( 
    @KNotNull(name="data")                  byte[]   data, 
    @KNotNull(name="tocompare")             byte[]   tocompare, 
    @KIPositive(name="offset", zero=true)   int      offset 
  ) {
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
  public static final boolean compare( 
    @KNotNull(name="data")                  char[]   data, 
    @KNotNull(name="tocompare")             char[]   tocompare, 
    @KIPositive(name="offset", zero=true)   int      offset 
  ) {
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
  public static final int indexOf( 
    @KNotNull(name="buffer")             byte[]   buffer, 
    @KNotNull(name="sequence")           byte[]   sequence, 
    @KIPositive(name="pos", zero=true)   int      pos 
  ) {
    if( sequence.length > buffer.length ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    int last = buffer.length - sequence.length;
    if( pos > last ) {
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
   * Tries to find a char sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * @param offset     The offset where to begin the search.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  public static final int indexOf( 
    @KNotNull(name="buffer")             char[]   buffer, 
    @KNotNull(name="sequence")           char[]   sequence, 
    @KIPositive(name="pos", zero=true)   int      pos 
  ) {
    if( sequence.length > buffer.length ) {
      // the sequence doesn't fit, so it's not available
      return -1;
    }
    int last = buffer.length - sequence.length;
    if( pos > last ) {
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
   * Tries to find a byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the byte sequence or -1 in case there's no sequence.
   */
  public static final int indexOf( 
    @KNotNull(name="data")       byte[]   data, 
    @KNotNull(name="sequence")   byte[]   sequence 
  ) {
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
  public static final int indexOf( 
    @KNotNull(name="data")       char[]   data, 
    @KNotNull(name="sequence")   char[]   sequence 
  ) {
    return indexOf( data, sequence, 0 );
  }
  
  /**
   * Creates a new byte sequence while inserting one into a data block. If the index is outside of
   * the destination no insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The byte sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the byte sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static final byte[] insert( 
    @KNotNull(name="destination")          byte[]   destination, 
    @KNotNull(name="newsequence")          byte[]   newsequence, 
    @KIPositive(name="index", zero=true)   int      index 
  ) {
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
   * Creates a new char sequence while inserting one into a data block. If the index is outside of
   * the destination no insertion takes place.
   * 
   * @param destination   The current data block which will be modified. Not <code>null</code>.
   * @param newsequence   The char sequence which has to be inserted. Not <code>null</code>.
   * @param index         The location where to insert the char sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  public static final char[] insert( 
    @KNotNull(name="destination")          char[]   destination, 
    @KNotNull(name="newsequence")          char[]   newsequence, 
    @KIPositive(name="index", zero=true)   int      index 
  ) {
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
  public static final String expandVariables( 
    @KNotEmpty(name="input")   String   input 
  ) {
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
   * @param format   A formatting String which is allowed to contain short property keys as
   *                 described in SystemProperty. Each key must be enclosed using a '%' character.
   *                 Neither <code>null</code> nor empty.
   * 
   * @return   The File location which has been evaluated. Not <code>null</code>.
   */
  public static final File expandFileLocation( 
    @KNotEmpty(name="format")   String   format 
  ) {
    return new File( StringFunctions.replace( format, REPLACEMENTS ) );
  }

  /**
   * Joins the supplied buffers.
   * 
   * @param buffers   A list of buffers which has to be joined. Not <code>null</code>.
   * 
   * @return   A joined buffer. Not <code>null</code>.
   */
  public static final byte[] joinBuffers( 
    @KNotNull(name="buffers")   byte[] ... buffers 
  ) {
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
  public static final char[] joinBuffers( 
    @KNotNull(name="buffers")   char[] ... buffers 
  ) {
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
  public static final Date parseDate( 
    @KNotEmpty(name="value")      String     value, 
    @KNotEmpty(name="patterns")   String ... patterns 
  ) {
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
   * @return   The date as a Calendar instance that has been parsed or <code>null</code> in case of 
   *           a failure.
   */
  public static final Calendar parseCalendar( String value, String ... patterns ) {
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
   * @return   <code>true</code>  <=> If the supplied literal has one of the values {@link #TRUEVALUES}
   *                                  (case insensitive).
   *           <code>false</code> <=> All other cases.
   */
  public static final boolean parseBoolean( @KNotNull(name="value") String value ) {
    return TRUEVALUES.contains( value.toLowerCase() );
  }

  /**
   * Instantiates the supplied class with the supplied arguments.
   * 
   * @param classname   The class that shall be instantiated. Neither <code>null</code> nor empty.
   * @param args        The arguments which have to be passed to the constructor. If omitted the
   *                    default constructor will be used. If passed each element must be 
   *                    non-<code>null</code> in order to determine the parameter type.
   * 
   * @return   <code>null</code> <=> If the class could not be instantiated otherwise the instance 
   *                                 itself.
   */
  public static final Object newInstance( String classname, Object ... args ) {
    return newInstance( false, classname, args );
  }
  
  /**
   * Instantiates the supplied class with the supplied arguments. The behaviour of this
   * method can be configured using the supplied flag.
   * 
   * @param fail        <code>true</code> <=> If the creation of the instance fails a 
   *                    FailureException is generation with the constant code
   *                    {@link CommonLibraryConstants#RTE_REFLECTIONS}. Otherwise this
   *                    method returns normally with the value <code>null</code>.
   * @param classname   The class that shall be instantiated. Neither <code>null</code> nor empty.
   * @param args        The arguments which have to be passed to the constructor. If omitted the
   *                    default constructor will be used. If passed each element must be 
   *                    non-<code>null</code> in order to determine the parameter type.
   * 
   * @return   If <code>fail</code> is <code>true</code> the value is not <code>null</code>.
   *           Otherwise it is <code>null</code> in case of a failure.
   *           
   * @throws FailureException   The instantiation failed. Will only be raised if <code>fail</code> 
   *                            is set to <code>true</code>.
   */
  @SuppressWarnings("unchecked")
  public static final Object newInstance( 
                                   boolean   fail, 
    @KNotEmpty(name="classname")   String    classname, 
                                   Object    ... args 
  ) {
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
    } catch( ClassNotFoundException      ex ) { reflectionFailure( fail, ex );
    } catch( InstantiationException      ex ) { reflectionFailure( fail, ex );
    } catch( IllegalAccessException      ex ) { reflectionFailure( fail, ex );
    } catch( SecurityException           ex ) { reflectionFailure( fail, ex );
    } catch( IllegalArgumentException    ex ) { reflectionFailure( fail, ex );
    } catch( InvocationTargetException   ex ) { reflectionFailure( fail, ex );
    }
    return null;
  }

  /**
   * Identifies a constructor by it's signature. This might be necessary if the appropriate 
   * Constructor uses an interface, so using a concrete type might fail to locate the right
   * Constructor.
   *  
   * @param candidates   The possible candidates of Constructors. Not <code>null</code>.
   * @param params       The current signature used to locate the Constructor. Not <code>null</code>.
   * 
   * @return   The Constructor if it could be found. Maybe <code>null</code>.
   */
  private static final Constructor findMatchingConstructor( Constructor[] candidates, Class<?>[] params ) {
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
  private static final void reflectionFailure( boolean fail, Exception cause ) {
    if( fail ) {
      throw new FailureException( FailureCode.Reflections, cause );
    }
  }
  
  /**
   * Returns a new Comparator which can be used to sort Map.Entry records according to it's keys.
   * 
   * @param type   The type of the key. Not <code>null</code>.
   */
  public static final <T extends Comparable> Comparator<Map.Entry<T,?>> newKeyComparator( 
    @KNotNull(name="type")   Class<T>   type 
  ) {
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
  public static final <T extends Comparable<T>> List<T> toSet( @KNotEmpty(name="list") List<T> list ) {
    Collections.sort( list );
    for( int i = list.size() - 1; i > 0; i-- ) {
      if( list.get(i).compareTo( list.get( i - 1 ) ) == 0 ) {
        list.remove(i);
      }
    }
    return list;
  }

  /**
   * Creates a copy of the supplied instance.
   * 
   * @param source   The instance that has to be copied. Maybe <code>null</code>.
   * 
   * @return   A copy of the supplied instance. <code>null</code> if <code>source</code> was <code>null</code>.
   */
  public static final <T extends Serializable> T clone( T source ) {
    if( source != null ) {
      try {
        ByteArrayOutputStream byteout   = new ByteArrayOutputStream();
        ObjectOutputStream    objectout = new ObjectOutputStream( byteout );
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
  public static List<int[]> getRegexRegions( 
    @KNotNull(name="pattern")    Pattern   pattern, 
    @KNotNull(name="sequence")   String    sequence 
  ) {
    List<int[]> result  = new ArrayList<int[]>();
    Matcher     matcher = pattern.matcher( sequence );
    while( matcher.find() ) {
      result.add( new int[] { matcher.start(), matcher.end() } );
    }
    return result;
  }
  
  /**
   * Implementation of a Comparator used for the key part of a Map.Entry.
   */
  private static final class KeyComparator<T extends Comparable> implements Comparator<Map.Entry<T,?>> {
    
    /**
     * {@inheritDoc}
     */
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
