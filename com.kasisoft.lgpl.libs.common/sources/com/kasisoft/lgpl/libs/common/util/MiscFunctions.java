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

import com.kasisoft.lgpl.libs.common.sys.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.text.*;

import java.util.*;

import java.io.*;

import java.lang.reflect.*;

/**
 * Collection of various functions.
 */
@KDiagnostic
public class MiscFunctions {

  private static final Map<String,String> REPLACEMENTS = SystemProperty.createReplacementMap( true );
  
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
    @KNotEmpty(name="input")   String input 
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
   * @param fail        <code>true</code> <=> If the creation of the instance fails an 
   *                    ExtendedRuntimeException is generation with the constant code
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
   * @throws ExtendedRuntimeException   The instantiation failed. Will only be raised if
   *                                    <code>fail</code> is <code>true</code>.
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
        return clazz.getConstructor( params ).newInstance( args );
      }
    } catch( ClassNotFoundException      ex ) { reflectionFailure( fail, ex );
    } catch( InstantiationException      ex ) { reflectionFailure( fail, ex );
    } catch( IllegalAccessException      ex ) { reflectionFailure( fail, ex );
    } catch( SecurityException           ex ) { reflectionFailure( fail, ex );
    } catch( NoSuchMethodException       ex ) { reflectionFailure( fail, ex );
    } catch( IllegalArgumentException    ex ) { reflectionFailure( fail, ex );
    } catch( InvocationTargetException   ex ) { reflectionFailure( fail, ex );
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
  
} /* ENDCLASS */
