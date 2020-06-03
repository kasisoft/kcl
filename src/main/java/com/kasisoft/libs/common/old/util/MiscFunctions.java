package com.kasisoft.libs.common.old.util;

import static com.kasisoft.libs.common.old.base.LibConfig.cfgDefaultVarFormat;
import static com.kasisoft.libs.common.old.base.LibConfig.cfgFalseValues;
import static com.kasisoft.libs.common.old.base.LibConfig.cfgTrueValues;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.sys.SystemInfo;
import com.kasisoft.libs.common.text.StringFunctions;
import com.kasisoft.libs.common.types.Pair;
import com.kasisoft.libs.common.types.TriConsumer;

import javax.swing.tree.DefaultMutableTreeNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.function.BiFunction;
import java.util.function.Function;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.sql.Connection;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

/**
 * Collection of various functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MiscFunctions {

  /**
   * Prevent instantiation.
   */
  private MiscFunctions() {
  }

  /**
   * Creates a replacement map for system properties.
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createEnvironmentReplacements() {
    return createEnvironmentReplacements( cfgDefaultVarFormat() );
  }

  /**
   * Creates a replacement map for system properties.
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createSystemPropertiesReplacements() {
    return createSystemPropertiesReplacements( cfgDefaultVarFormat() );
  }
  
  /**
   * Creates a replacement map for environment variables.
   * 
   * @param format   A format which describes the look of a variable expression. F.e. ${%s} and varname = basedir -> ${basedir}
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createEnvironmentReplacements( @NonNull String format ) {
    return createReplacementMap( System.getenv(), "env", format, Map::keySet, Function.<String>identity(), ($m, $k) -> $m.get($k) );
  }

  /**
   * Creates a replacement map for system properties.
   * 
   * @param format        A format which describes the look of a variable expression. F.e. ${%s} and varname = basedir -> ${basedir}
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createSystemPropertiesReplacements( @NonNull String format ) {
    return createReplacementMap( System.getProperties(), "sys", format, Properties::stringPropertyNames, Function.<String>identity(), ($p, $k) -> $p.getProperty($k) );
  }
  
  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createReplacementMap( @NonNull Properties settings, String prefix ) {
    return createReplacementMap( settings, prefix, cfgDefaultVarFormat(), Properties::stringPropertyNames, Function.<String>identity(), ($p, $k) -> $p.getProperty($k) );
  }

  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createReplacementMap( @NonNull Map<String, String> settings, String prefix ) {
    return createReplacementMap( settings, prefix, cfgDefaultVarFormat(), Map::keySet, Function.<String>identity(), ($m, $k) -> $m.get($k) );
  }

  
  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * @param format        A format which describes the look of a variable expression. F.e. ${%s} and varname = basedir -> ${basedir}
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createReplacementMap( @NonNull Properties settings, String prefix, @NonNull String format ) {
    return createReplacementMap( settings, prefix, format, Properties::stringPropertyNames, Function.<String>identity(), ($p, $k) -> $p.getProperty($k) );
  }

  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * @param format        A format which describes the look of a variable expression. F.e. ${%s} and varname = basedir -> ${basedir}
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static Map<String, String> createReplacementMap( @NonNull Map<String, String> settings, String prefix, @NonNull String format ) {
    return createReplacementMap( settings, prefix, format, Map::keySet, Function.<String>identity(), ($m, $k) -> $m.get($k) );
  }
  
  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * @param format        A format which describes the look of a variable expression. F.e. ${%s} and varname = basedir -> ${basedir}
   * @param getKeys       A function to get all keys of the settings object.
   * @param keyToStr      A function transforming the key into a literal.
   * @param getValue      The function to retrieve the value.
   * 
   * @return   A map with the text replacements for expressions.
   */
  public static <T, K> Map<String, String> createReplacementMap( @NonNull T settings, String prefix, @NonNull String format, @NonNull Function<T, ? extends Collection<K>> getKeys, @NonNull Function<K, String> keyToStr, @NonNull BiFunction<T, K, String> getValue ) {
    Map<String, String> result = new HashMap<>();
    Collection<K>       keys   = getKeys.apply( settings );
    if( (keys != null) && (!keys.isEmpty()) ) {
      String                   p        = StringFunctions.cleanup( prefix );
      Function<String, String> prefixer = prefix != null ? $ -> p + ":" + $ : Function.identity();
      keys.forEach( $ -> {
        String textvalue = getValue.apply( settings, $ );
        result.put( String.format( format, prefixer.apply( keyToStr.apply($) ) ), textvalue );
      });
    }
    return result;
  }
  
  /**
   * Returns the parent directory of the class folder or the jar.
   * 
   * @param clazz   The class used to identify the directory. Not <code>null</code>.
   * 
   * @return  The parent directory. Not <code>null</code>.
   */
  public static Path locateDirectory( @NonNull Class<?> clazz ) {
    return locateDirectory( clazz.getClassLoader(), clazz.getName().replace('.', '/') + ".class" );
  }

  /**
   * Returns the parent directory of the class folder or the jar.
   * 
   * @param lookupResource   The resource used for the lookup. Not <code>null</code>.
   * 
   * @return  The parent directory. <code>null</code> if the resource is not available.
   */
  public static Path locateDirectory( @NonNull String lookupResource ) {
    return locateDirectory( ResourceExtractor.class.getClassLoader(), lookupResource );
  }

  /**
   * Returns the parent directory of the class folder or the jar.
   * 
   * @param cl               The {@link ClassLoader} to be used. Not <code>null</code>.
   * @param lookupResource   The resource used for the lookup. Not <code>null</code>.
   * 
   * @return  The parent directory. <code>null</code> if the resource is not available.
   */
  public static Path locateDirectory( @NonNull ClassLoader cl, @NonNull String lookupResource ) {
    Path    result   = null;
    URL     resource = cl.getResource( lookupResource );
    if( resource != null ) {
      String  location = resource.toExternalForm();
      boolean isjar    = resource.toExternalForm().contains( "jar:" );
      int     idx      = location.indexOf( "file:" );
      location         = location.substring( idx + "file:".length() );
      if( isjar ) {
        idx       = location.indexOf( ".jar!" );
        location  = location.substring( 0, idx + ".jar".length() );
      } else {
        idx       = location.indexOf( lookupResource );
        location  = location.substring( 0, idx );
      }
      if( SystemInfo.getRunningOS().isWindowsLike() ) {
        location = StringFunctions.trim( location, "/", true );
      }
      result = Paths.get( location ).getParent().toAbsolutePath();
    }
    return result;
  }
  
  
  /**
   * Returns the 'minimum' of two {@link Comparable} objects.
   * 
   * @param a   The first candidate. Maybe <code>null</code>.
   * @param b   The second candidate. Maybe <code>null</code>.
   * 
   * @return   The 'minimum' instance. Maybe <code>null</code>.
   */
  public static <T extends Comparable> T min( T a, T b ) {
    if( (a != null) && (b != null) ) {
      if( a.compareTo(b) < 0 ) {
        return a;
      } else {
        return b;
      }
    } else if( a != null ) {
      return a;
    } else {
      return b;
    }
  }

  /**
   * Returns the 'maximum' of two {@link Comparable} objects.
   * 
   * @param a   The first candidate. Maybe <code>null</code>.
   * @param b   The second candidate. Maybe <code>null</code>.
   * 
   * @return   The 'maximum' instance. Maybe <code>null</code>.
   */
  public static <T extends Comparable> T max( T a, T b ) {
    if( (a != null) && (b != null) ) {
      if( a.compareTo(b) >= 0 ) {
        return a;
      } else {
        return b;
      }
    } else if( a != null ) {
      return a;
    } else {
      return b;
    }
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
    return new File( StringFunctions.replace( format, MiscFunctions.createSystemPropertiesReplacements() ) );
  }

  /**
   * Parses a date using a given list of patterns.
   * 
   * @param value      The value that has to be parsed. Neither <code>null</code> nor empty.
   * @param patterns   The allowed patterns. Neither <code>null</code> nor empty.
   * 
   * @return   The date that has been parsed or <code>null</code> in case of a failure.
   */
  public static Date parseDate( @NonNull String value, @NonNull String ... patterns ) {
    for( int i = 0; i < patterns.length; i++ ) {
      SimpleDateFormat formatter = new SimpleDateFormat( patterns[i], Locale.ROOT );
      try {
        return formatter.parse( value );
      } catch( ParseException ex ) {
        // simply ignore this. a failure is indicated by a null return value.
      }
    }
    return null;
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
   * Returns <code>true</code> if the supplied value can be interpreted as a boolean.
   * 
   * @param value   The value which has to be test. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The value can be interpreted as a boolean.
   */
  public static boolean isBoolean( String value ) {
    if( value != null ) {
      String lower = value.toLowerCase();
      return cfgTrueValues().contains( lower ) || cfgFalseValues().contains( lower );
    } else {
      return false;
    }
  }

  /**
   * Interpretes a value as a boolean.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   <code>true</code>  <=> If the supplied literal has one of the values {@link #TRUEVALUES} (case insensitive).
   *           <code>false</code> <=> All other cases.
   */
  public static boolean parseBoolean( String value ) {
    if( value != null ) {
      return cfgTrueValues().contains( value.toLowerCase() );
    } else {
      return false;
    }
  }

  /**
   * Interpretes a value as a byte.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The byte value. Maybe <code>null</code>.
   */
  public static Byte parseByte( String value ) {
    return parse( value, Byte::parseByte );
  }

  /**
   * Interpretes a value as a short.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The short value. Maybe <code>null</code>.
   */
  public static Short parseShort( String value ) {
    return parse( value, Short::parseShort );
  }

  /**
   * Interpretes a value as a integer.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The integer value. Maybe <code>null</code>.
   */
  public static Integer parseInt( String value ) {
    return parse( value, Integer::parseInt );
  }

  /**
   * Interpretes a value as a long.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The long value. Maybe <code>null</code>.
   */
  public static Long parseLong( String value ) {
    return parse( value, Long::parseLong );
  }

  /**
   * Interpretes a value as a float.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The float value. Maybe <code>null</code>.
   */
  public static Float parseFloat( String value ) {
    return parse( value, Float::parseFloat );
  }

  /**
   * Interpretes a value as a double.
   * 
   * @param value   The value which has to be parsed. Maybe <code>null</code>.
   * 
   * @return   The double value. Maybe <code>null</code>.
   */
  public static Double parseDouble( String value ) {
    return parse( value, Double::parseDouble );
  }

  /**
   * Parses a number and returns <code>null</code> if the value is invalid.
   * 
   * @param value   The value shall be parsed. Not <code>null</code>.
   * @param parse   The function used to parse the value. Not <code>null</code>.
   * 
   * @return   The parsed value or <code>null</code>.
   */
  private static <T> T parse( String value, Function<String, T> parse ) {
    if( value != null ) {
      try {
        return parse.apply( value );
      } catch( NumberFormatException ex ) {
        // cause to return null
      }
    }
    return null;
  }

  /**
   * Returns a new Comparator which can be used to sort Map.Entry records according to it's keys.
   * 
   * @param type   The type of the key. Not <code>null</code>.
   */
  public static <T extends Comparable> Comparator<Map.Entry<T, ?>> newKeyComparator( @NonNull Class<T> type ) {
    return new KeyComparator<>();
  }
  
  public static <T> Comparator<T> newComparator( @NonNull Function<T, Comparable> ... getters ) {
    return newComparator( true, getters );
  }
  
  public static <T> Comparator<T> newComparator( boolean nullsLow, @NonNull Function<T, Comparable> ... getters ) {
    return newComparator( nullsLow, Arrays.asList( getters ) );
  }
  
  public static <T> Comparator<T> newComparator( @NonNull List<Function<T, Comparable>> getters ) {
    return newComparator( true, getters );
  }
  
  public static <T> Comparator<T> newComparator( boolean nullsLow, @NonNull List<Function<T, Comparable>> getters ) {
    return new Comparator<T>() {

      @Override
      public int compare( T o1, T o2 ) {
        int result = 0;
        for( Function<T, Comparable> getter : getters ) {
          Comparable lhs = getter.apply( o1 );
          Comparable rhs = getter.apply( o2 );
          if( (lhs != null) || (rhs != null) ) {
            if( lhs == null ) {
              result = nullsLow ? -1 : 1;
            } else if( rhs == null ) {
              result = nullsLow ? 1 : -1;
            } else {
              result = lhs.compareTo( rhs );
            }
            if( result != 0 ) {
              break;
            }
          }
        }
        return result;
      }
      
    };
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
      } catch( IOException | ClassNotFoundException ex ) {
        // since we're only working in memory this should never happen
      }
    }
    return null;
  }
  
  /**
   * Closes the supplied Closeable. 
   * 
   * @param closeable   The Closeable that has to be closed. Maybe <code>null</code>.
   */
  public static void close( AutoCloseable closeable ) {
    if( closeable != null ) {
      try {
        closeable.close();
      } catch( Exception ex ) {
        throw KclException.wrap( ex );
      }
    }
  }

  /**
   * Closes the supplied Connection. 
   * 
   * @param connection   The connection that has to be closed. Maybe <code>null</code>.
   */
  public static void close( Connection connection ) {
    if( connection != null ) {
      try {
        connection.close();
      } catch( Exception ex ) {
        throw KclException.wrap( ex );
      }
    }
  }

  /**
   * Like {@link #close(Closeable)} without complaining.
   * 
   * @param closeable   The Closeable that has to be closed. Maybe <code>null</code>.
   */
  public static void closeQuietly( AutoCloseable closeable ) {
    if( closeable != null ) {
      try {
        closeable.close();
      } catch( Exception ex ) {
        // don't complain
      }
    }
  }

  /**
   * Like {@link #close(Connection)} without complaining.
   * 
   * @param connection   The connection that has to be closed. Maybe <code>null</code>.
   */
  public static void closeQuietly( Connection connection ) {
    if( connection != null ) {
      try {
        connection.close();
      } catch( Exception ex ) {
        // don't complain
      }
    }
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
   * Calculates the biggest common divisor.
   * 
   * @param a   One number.
   * @param b   Another number.
   * 
   * @return   The biggest common divisor.
   */
  public static int gcd( int a, int b ) {
    if( b == 0 ) {
      return a;
    } else {
      return gcd( b, a % b );
    }
  }
  
  /**
   * Returns the resource which might be provided by a ClassLoader instance accessible in this function.
   * 
   * @param resource   The resource that shall be loaded. Not blank.
   * 
   * @return   The URL allowing to access the resource. Maybe <code>null</code>.
   */
  public static URL getResource( @NonNull String resource ) {
    return getResource( (Class<?>) null, resource );
  }
  
  /**
   * Returns the resource which might be provided by a certain Class or the ClassLoader instances accessible
   * in this function.
   * 
   * @param caller     The calling class allowing to access a resource. Maybe <code>null</code>.
   * @param resource   The resource that shall be loaded. Not blank.
   * 
   * @return   The URL allowing to access the resource. Maybe <code>null</code>.
   */
  public static URL getResource( Class<?> caller, @NonNull String resource ) {
    URL result = null;
    if( (resource.length() > 0) && (resource.charAt(0) == '/') ) {
      resource = resource.substring(1);
    }
    if( caller != null ) {
      result = caller.getResource( resource );
    }
    if( result == null ) {
      result = getResource( Thread.currentThread().getContextClassLoader(), resource );
    }
    if( result == null ) {
      result = getResource( MiscFunctions.class.getClassLoader(), resource );
    }
    return result;
  }
  
  /**
   * Returns the resource which might be provided by a certain ClassLoader instance.
   * 
   * @param cl         The potential ClassLoader allowing to access a resource. Maybe <code>null</code>.
   * @param resource   The resource that shall be loaded. Not blank.
   * 
   * @return   The URL allowing to access the resource. Maybe <code>null</code>.
   */
  private static URL getResource( ClassLoader cl, String resource ) {
    URL result = null;
    if( cl != null ) {
      result = cl.getResource( resource );
    }
    return result;
  }

  /**
   * Creates a list of pairs from the supplied entries.
   * 
   * @param entries   The entries that will be returned as list of pairs. Maybe <code>null</code>.
   * 
   * @return   A list of pairs. Not <code>null</code>.
   */
  public static <R> List<Pair<R, R>> toPairs( R ... entries ) {
    List<Pair<R, R>> result = Collections.emptyList();
    if( entries != null ) {
      int count = entries.length / 2;
      result    = new ArrayList<>( count );
      for( int i = 0; i < count; i++ ) {
        result.add( new Pair<>( entries[ i * 2 + 0 ], entries[ i * 2 + 1 ] ) );
      }
    }
    return result;
  }

  /**
   * Creates a map from the supplied entries.
   * 
   * @param entries   The entries that will be returned as a map. Maybe <code>null</code>.
   * 
   * @return   A map providing all entries (unless the list length is odd). Not <code>null</code>.
   */
  public static <R1, R2> Map<R1, R2> toMap( Object ... entries ) {
    Map<R1, R2> result = Collections.emptyMap();
    if( entries != null ) {
      int count = entries.length / 2;
      result    = new HashMap<>( count );
      for( int i = 0; i < count; i++ ) {
        result.put( (R1) entries[ i * 2 + 0 ], (R2) entries[ i * 2 + 1 ] );
      }
    }
    return result;
  }

  private static List<String> split( String str ) {
    return Arrays.asList( str.split("/") )
      .stream()
      .map( StringFunctions::cleanup )
      .filter( $ -> $ != null )
      .collect( Collectors.toList() );
  }
  
  private static <T> void addNode( ParenthesizeTreeNode<T> parent, ParenthesizeTreeNode<T> child ) {
    if( child.parents.isEmpty() ) {
      parent.add( child );
    } else {
      String                   childName = child.parents.remove(0);
      ParenthesizeTreeNode<T>  childNode = parent.getChildByName( childName );
      if( childNode == null ) {
        // artificial node
        childNode = new ParenthesizeTreeNode<>( null, childName );
        parent.add( childNode );
      }
      addNode( childNode, child );
    }
  }
  
  private static <T> void iterate( ParenthesizeTreeNode<T> node, int depth, String prefix, TriConsumer<String, T, T> addChild ) {
    for( int i = 0; i < node.getChildCount(); i++ ) {
      ParenthesizeTreeNode<T> child = (ParenthesizeTreeNode<T>) node.getChildAt(i);
      addChild.accept( depth == 0 ? "/" : prefix, depth == 0 ? null : node.getValue(), child.getValue() );
      iterate( child, depth + 1, prefix + "/" + child.getName(), addChild );
    }
  }
  
  public static <T> void parenthesize( @NonNull List<T> values, @NonNull Function<T, String> toPath, @NonNull TriConsumer<String, T, T> addChild ) {

    List<ParenthesizeTreeNode<T>> nodes = new ArrayList<>();
    values.stream()
      .map( $ -> new ParenthesizeTreeNode( $, split( toPath.apply($) ) ) )
      .forEach( nodes::add );
    
    ParenthesizeTreeNode<T> root = new ParenthesizeTreeNode<>( null, split( "root" ) );
    nodes.forEach( $ -> addNode( root, $ ) );
    
    iterate( root, 0, "", addChild );
    
  }
  
  private static class ParenthesizeTreeNode<T> extends DefaultMutableTreeNode {
    
    @Getter
    T               value   = null;
    
    List<String>    parents = new ArrayList<>();
    
    public ParenthesizeTreeNode( T val, String name ) {
      super( name );
      value   = val;
      parents = Collections.emptyList();
    }
    
    public ParenthesizeTreeNode( T val, List<String> segments ) {
      super( segments.remove( segments.size() - 1 ) );
      value   = val;
      parents = segments;
    }
    
    public String getName() {
      return (String) getUserObject();
    }
    
    public ParenthesizeTreeNode<T> getChildByName( String name ) {
      ParenthesizeTreeNode<T> result = null;
      for( int i = 0; i < getChildCount(); i++ ) {
        ParenthesizeTreeNode<T> child = (ParenthesizeTreeNode<T>) getChildAt(i);
        if( name.equals( child.getName() ) ) {
          result = child;
          break;
        }
      }
      return result;
    }
    
  } /* ENDCLASS */
  
  /**
   * Implementation of a Comparator used for the key part of a Map.Entry.
   */
  private static final class KeyComparator<T extends Comparable> implements Comparator<Map.Entry<T, ?>> {
    
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
