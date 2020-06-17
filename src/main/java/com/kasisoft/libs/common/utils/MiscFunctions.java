package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.constants.Digest;

import com.kasisoft.libs.common.old.sys.SystemInfo;
import com.kasisoft.libs.common.old.util.ResourceExtractor;
import com.kasisoft.libs.common.text.StringFunctions;
import com.kasisoft.libs.common.types.Pair;

import java.util.function.BiFunction;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.net.URL;

import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
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
  
  public static <R> List<R> trim(List<R> input) {
    while ((!input.isEmpty()) && (input.get(0) == null)) {
      input.remove(0);
    }
    while ((!input.isEmpty()) && (input.get(input.size() - 1) == null)) {
      input.remove(input.size() - 1);
    }
    return input;
  }
  
  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   *
   * @param length  The length to be used for the calculation.
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  public static int adjustIndex(int length, int index, boolean isEnd) {
    if (index < 0) {
      return length + index;
    } else if ((index == 0) && isEnd) {
      return length;
    }
    return index;
  }


  public static String getGravatarLink( String email, Integer size ) {
    // @spec [25-11-2017:KASI] https://en.gravatar.com/site/implement/hash/ 
    String result = null;
    if( email != null ) {
      result = StringFunctions.cleanup( email.toLowerCase() );
      result = Digest.MD5.digestToString( result.getBytes() );
      result = String.format( "https://www.gravatar.com/avatar/%s", result );
      if( size != null ) {
        result += String.format( "?s=%d", size );
      }
    }
    return result;

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
