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

import lombok.*;

/**
 * Collection of various functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MiscFunctions {

  private static final Map<String,String> REPLACEMENTS = SysProperty.createReplacementMap( true );
  
  private static final Set<String> TRUEVALUES;
  
  static {
    
    TRUEVALUES  = new HashSet<>();
    
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
   * Expands the supplied input String while replacing environment variables with their values.
   * 
   * @param input   The String to expand. Neither <code>null</code> nor empty.
   * 
   * @return   A String with resolved environment variables. Neither <code>null</code> nor empty.
   */
  public static String expandVariables( @NonNull String input ) {
    Map<String, String> variables     = new Hashtable<>();
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
  public static File expandFileLocation( @NonNull String format ) {
    return new File( StringFunctions.replace( format, REPLACEMENTS ) );
  }

  /**
   * Parses a date using a given list of patterns.
   * 
   * @param value      The value that has to be parsed. Neither <code>null</code> nor empty.
   * @param patterns   The allowed patterns. Neither <code>null</code> nor empty.
   * 
   * @return   The date that has been parsed or <code>null</code> in case of a failure.
   */
  public static Date parseDate(@NonNull String value, @NonNull String ... patterns ) {
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
  public static Calendar parseCalendar( @NonNull String value, @NonNull String ... patterns ) {
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
  public static boolean parseBoolean( @NonNull String value ) {
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
  public static Object newInstance( @NonNull String classname, Object ... args ) {
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
  public static Object newInstance( boolean fail, @NonNull String classname, Object ... args ) {
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
            return FailureException.raiseIf( fail, FailureCode.Reflections, ex, classname );
          }
        }
      }
    } catch( Exception ex ) { 
      return FailureException.raiseIf( fail, FailureCode.Reflections, ex, classname );
    }
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
  private static Constructor findMatchingConstructor( @NonNull Constructor[] candidates, @NonNull Class<?>[] params ) {
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
   * Returns a new Comparator which can be used to sort Map.Entry records according to it's keys.
   * 
   * @param type   The type of the key. Not <code>null</code>.
   */
  public static <T extends Comparable> Comparator<Map.Entry<T,?>> newKeyComparator( @NonNull Class<T> type ) {
    return new KeyComparator<>();
  }
  
  /**
   * Sorts the supplied list and makes sure that every entry only occures once. 
   * 
   * @param list   The list of Comparable instances. This list must support the {@link List#remove(int)} method. 
   *               Not <code>null</code>.
   * 
   * @return   The supplied list. Not <code>null</code>.
   */
  public static <T extends Comparable<T>> List<T> toUniqueList( @NonNull List<T> list ) {
    Collections.sort( list );
    for( int i = list.size() - 1; i > 0; i-- ) {
      if( list.get(i).compareTo( list.get( i - 1 ) ) == 0 ) {
        list.remove(i);
      }
    }
    return list;
  }

  /**
   * Creates a list of <param>count</param> elements while repeating the supplied one.
   * 
   * @param count     The number of elements that shall be created.
   * @param element   The element that shall be repeated. Maybe <code>null</code>.
   * 
   * @return   A list with the supplied amount of elements. Not <code>null</code>.
   */
  public static <T> List<T> repeat( int count, T element ) {
    List<T> result = new ArrayList<>( count );
    for( int i = 0; i < count; i++ ) {
      result.add( element );
    }
    return result;
  }
  
  /**
   * Creates a set from the supplied elements. 
   * 
   * @param elements   The elements that shall be collected within a set. Maybe <code>null</code>.
   * 
   * @return   The set created from the supplied elements. Not <code>null</code>.
   */
  public static <T> Set<T> toSet( T ... elements ) {
    Set<T> result = new HashSet<>();
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
  public static List<int[]> getRegexRegions( @NonNull Pattern pattern, @NonNull String sequence ) {
    List<int[]> result  = new ArrayList<>();
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
        FailureException.raiseIf( fail, FailureCode.Close, ex );
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
        FailureException.raiseIf( fail, FailureCode.Close, ex );
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
        FailureException.raiseIf( fail, FailureCode.Close, ex );
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
        FailureException.raiseIf( fail, FailureCode.Close, ex );
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
  public static boolean isLeapYear( @NonNull Date date ) {
    return isLeapYear( date.getYear() + 1900 );
  }
  
  /**
   * Returns the constructor associated with a specific type.
   * 
   * @param type     The type used to access the constructor. Not <code>null</code>. 
   * @param params   The parameter types for the constructor. Maybe <code>null</code>.
   * 
   * @return   The Constructor if there is one apropriate one. Maybe <code>null</code>.
   */
  public static Constructor getConstructor( @NonNull Class<?> type, Class<?> ... params ) {
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
  public static Method getMethod( @NonNull Class<?> type, @NonNull String name, Class<?> ... params ) {
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
