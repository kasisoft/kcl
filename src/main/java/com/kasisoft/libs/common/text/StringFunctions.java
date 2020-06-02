package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Collection of functions used for String processing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFunctions {

  private StringFunctions() {
  }
  
  /**
   * Returns the basename for the supplied string which means to strip away the suffix if there's one.
   * 
   * @param name   The name which might contain a suffix. Not <code>null</code>.
   * 
   * @return   The basename without the suffix. Not <code>null</code>.
   */
  public static @NotNull String getBasename(@NotNull String name) {
   var lidx = name.lastIndexOf('.');
   if (lidx == -1) {
     return name;
   } else {
     return name.substring(0, lidx);
   }
  }
  
  /**
   * Changes the suffix for the supplied name. If the name doesn't provide a suffix it will be appended.
   * 
   * @param name     The name which might be altered. Not <code>null</code>.
   * @param suffix   The suffix which has to be added (without '.'). Neither <code>null</code> nor empty.
   * 
   * @return   The name with the updated suffix. Neither <code>null</code> nor empty.
   */
  public static @NotNull String changeSuffix(@NotNull String name, @NotNull String suffix) {
    return String.format("%s.%s", getBasename(name), suffix);
  }
  
    
  /**
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end.
   * 
   * @param input   The String that has to be altered. Maybe <code>null</code>.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  public static @Null String cleanup(@Null String input) {
    if (input != null) {
      input = input.trim();
      if (input.length() == 0) {
        input = null;
      }
    }
    return input;
  }
  
  /**
   * Makes the first character upper case if there's one.
   * 
   * @param input   The String where the first character has to be altered. Neither <code>null</code> nor empty.
   * 
   * @return   A possibly in-place altered input. Not <code>null</code>.
   */
  public static @NotNull String firstUp(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(input);
      $.firstUp();
      return $.toString();
    });
  }
  
  /**
   * Makes the first character lower case if there's one.
   * 
   * @param input   The CharSequence where the first character has to be altered. Neither <code>null</code> nor empty.
   * 
   * @return   A possibly in-place altered input. Not <code>null</code>.
   */
  public static @NotNull String firstDown(@NotNull String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(input);
      $.firstDown();
      return $.toString();
    });
  }
  
  
//  /**
//   * Generates a textual representation of the supplied list.
//   * 
//   * @param args   A list used to create a textual representation from. Maybe <code>null</code>.
//   * 
//   * @return   The textual representation of the supplied list.
//   */
//  public static String toString( Object[] args ) {
//    if( args == null ) {
//      return "null";
//    } else {
//      StringBuilder buffer = new StringBuilder();
//      if( args.length > 0 ) {
//        buffer.append( toString( args[0] ) );
//        for( int i = 1; i < args.length; i++ ) {
//          buffer.append( "," );
//          buffer.append( toString( args[i] ) );
//        }
//      }
//      return buffer.toString();
//    }
//  }
//  /**
//   * Performs a search & replace operation on the supplied input.
//   * 
//   * @param buffer    The buffer which has to be modified in place. Not <code>null</code>.
//   * @param search    The partial String to search for. Neither <code>null</code> nor empty.
//   * @param replace   The String to replace instead. Not <code>null</code>.
//   */
//  public static <T extends CharSequence> void replace( @NonNull T buffer, @NonNull String search, @NonNull String replace ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( buffer );
//    int                   index  = facade.indexOf( buffer, search, 0 );
//    while( index != -1 ) {
//      buffer = facade.replace( buffer, index, index + search.length(), replace );
//      index  = facade.indexOf( buffer, search, index + replace.length() );
//    }
//  }
//
//  /**
//   * Performs a search & replace operation on the supplied input.
//   * 
//   * @param input          The input which has to be modified. Not <code>null</code>.
//   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
//   * 
//   * @return   The modified String. Not <code>null</code>.
//   */
//  public static String replace( @NonNull String input, @NonNull Map<String,String> replacements ) {
//    StringFBuffer  buffer = new StringFBuffer( input );
//    replace( buffer, replacements );
//    return buffer.toString();
//  }
//
//  /**
//   * Performs a search & replace operation on the supplied input.
//   * 
//   * @param buffer         The buffer which has to be modified in place. Not <code>null</code>.
//   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
//   */
//  public static <T extends CharSequence> void replace( @NonNull T buffer, @NonNull Map<String,String> replacements ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( buffer );
//    Set<String>           search = replacements.keySet();
//    Tupel<String>         key    = new Tupel<>();
//    int                   index  = indexOf( facade, buffer, search, key, 0 );
//    while( index != -1 ) {
//      String searchstr  = key.getValue();
//      String replacestr = replacements.get( searchstr );
//      buffer            = facade.replace( buffer, index, index + searchstr.length(), replacestr );
//      index             = indexOf( facade, buffer, search, key, index + replacestr.length() );
//    }
//  }
//
//  /**
//   * Locates the index of the leftmost search string.
//   * 
//   * @param facade   The facade that will be used to access the buffer. Not <code>null</code>.
//   * @param input    The StringBuffer where to look at. Not <code>null</code>.
//   * @param keys     All used search strings. Not <code>null</code>.
//   * @param key      Receiver of the String that finally has been found. Not <code>null</code>.
//   * @param start    An initial index.
//   * 
//   * @return   The index where a search string has been found or -1 in case none has been found.
//   */
//  private static <T extends CharSequence> int indexOf( CharSequenceFacade<T> facade, T input, Set<String> keys, Tupel<String> key, int start ) {
//    int result = -1;
//    key.setValues( (String[]) null );
//    for( String current : keys ) {
//      int pos = facade.indexOf( input, current, start );
//      if( pos != -1 ) {
//        if( (result == -1) || (pos < result) ) {
//          result = pos;
//          key.setValues( current );
//        }
//      }
//    }
//    return result;
//  }
//
//  /**
//   * Returns <code>true</code> if one candidate is contained within the test value.
//   * 
//   * @param test         The element that has to be tested. Neither <code>null</code> nor empty.
//   * @param candidates   The list of allowed candidates. Neither <code>null</code> nor empty.
//   * 
//   * @return  true <=> The element which has to be tested is contained.
//   */
//  public static <T extends CharSequence> boolean contains( @NonNull T test, @NonNull String ... candidates ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( test );
//    for( String entry : candidates ) {
//      if( facade.contains( test, entry ) ) {
//        return true;
//      }
//    }
//    return false;
//  }
//  
//  /**
//   * Creates a concatenation of the supplied Strings. This function allows elements to be <code>null</code> which means
//   * that they're just be ignored.
//   * 
//   * @param delimiter   A delimiter which might be used. Maybe <code>null</code>.
//   * @param args        The list of Strings that has to be concatenated. Maybe <code>null</code>.
//   * 
//   * @return   The concatenated String. Not <code>null</code>.
//   */
//  public static String concatenate( String delimiter, String ... args ) {
//    if( args == null ) {
//      return "";
//    }
//    if( delimiter == null ) {
//      delimiter = "";
//    }
//    StringBuilder buffer = new StringBuilder();
//    for( int i = 0; i < args.length; i++ ) {
//      if( (args[i] != null) && (args[i].length() > 0) ) {
//        if( buffer.length() > 0 ) {
//          buffer.append( delimiter );
//        }
//        buffer.append( args[i] );
//      }
//    }
//    return buffer.toString();
//  }
//
//  /**
//   * Creates a concatenation of the supplied Strings. This function allows elements to be <code>null</code> which means
//   * that they're just be ignored.
//   * 
//   * @param delimiter   A delimiter which might be used. Maybe <code>null</code>.
//   * @param args        The collection of Strings that has to be concatenated. Maybe <code>null</code>.
//   * 
//   * @return   The concatenated String. Not <code>null</code>.
//   */
//  public static String concatenate( String delimiter, Collection<String> args ) {
//    if( args == null ) {
//      return "";
//    }
//    if( delimiter == null ) {
//      delimiter = "";
//    }
//    StringBuilder buffer = new StringBuilder();
//    for( String arg : args ) {
//      if( (arg != null) && (arg.length() > 0) ) {
//        if( buffer.length() > 0 ) {
//          buffer.append( delimiter );
//        }
//        buffer.append( arg );
//      }
//    }
//    return buffer.toString();
//  }
//
//  /**
//   * Returns <code>true</code> if both literals are equal. This function allows to ignore the case
//   * sensitivity by choice.
//   * 
//   * @param str1         One String to be used for the comparison. Not <code>null</code>. 
//   * @param str2         One String to be used for the comparison. Not <code>null</code>.
//   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
//   * 
//   * @return   <code>true</code> <=> Both Strings are equal.
//   */
//  public static boolean equals( @NonNull String str1, @NonNull String str2, boolean ignorecase ) {
//    if( ignorecase ) {
//      return str1.equalsIgnoreCase( str2 );
//    } else {
//      return str1.equals( str2 );
//    }
//  }
//
//  /**
//   * Compares two strings depending on the case sensitivity.
//   * 
//   * @param ignorecase   <code>true</code> <=> Ignore the cases.
//   * @param str1         One string to compare. Neither <code>null</code> nor empty.
//   * @param str2         One string to compare. Neither <code>null</code> nor empty.
//   * 
//   * @return   <code>true</code> <=> Both strings are equal.
//   */
//  public static boolean compare( boolean ignorecase, @NonNull String str1, @NonNull String str2 ) {
//    if( ignorecase ) {
//      return str1.equalsIgnoreCase( str2 );
//    } else {
//      return str1.equals( str2 );
//    }
//  }
//  
//  /**
//   * Alters the input while exchanging it's suffix.
//   * 
//   * @param input        The input which has to be altered. Maybe <code>null</code>.
//   * @param newsuffix    The new suffix without the dot at the beginning. Neither <code>null</code> nor empty.
//   * 
//   * @return   The altered input. <code>null</code> if there wasn't a replacable suffix.
//   */
//  public static String replaceSuffix( String input, @NonNull String newsuffix ) {
//    if( input != null ) {
//      int lidx = input.lastIndexOf('.');
//      if( lidx != -1 ) {
//        return String.format( "%s.%s", input.substring( 0, lidx ), newsuffix );
//      }
//    }
//    return null;
//  }
//  
//  /**
//   * Repeats the supplied text <param>n</param> times.
//   * 
//   * @param n      The number of concatenations that have to be performed.
//   * @param text   The text that has to be repeated. Maybe <code>null</code>.
//   * 
//   * @return   The concatenated reproduction string. Not <code>null</code>.
//   */
//  public static String repeat( int n, String text ) {
//    if( (n > 0) && (text != null) && (text.length() > 0) ) {
//      StringBuffer buffer = new StringBuffer();
//      while( n > 0 ) {
//        buffer.append( text );
//        n--;
//      }
//      return buffer.toString();
//    }
//    return "";
//  }
//
//  /**
//   * Returns a limited version of the supplied text.
//   * 
//   * @param text    The text that might require to be limited. Maybe <code>null</code>.
//   * @param limit   The maximum number of characters to be allowed.
//   * 
//   * @return   The limited version of the supplied text. Maybe <code>null</code>.
//   */
//  public static <T extends CharSequence> String limit( T text, int limit ) {
//    if( text == null ) {
//      return null;
//    } else {
//      CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( text );
//      if( text.length() > limit ) {
//        return facade.substring( text, 0, limit );
//      }
//      return text.toString();
//    }
//  }
//  
//  /**
//   * Creates a textual presentation with a padding using the space character.
//   * 
//   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
//   * @param limit     The maximum number of characters allowed.
//   * @param left      <code>true</code> <=> Use left padding.
//   * 
//   * @return   The text that is padded. Not <code>null</code>.
//   */
//  public static String padding( String text, int limit, boolean left ) {
//    return padding( text, limit, ' ', left );
//  }
//
//  /**
//   * Creates a textual presentation with a padding.
//   * 
//   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
//   * @param limit     The maximum number of characters allowed.
//   * @param padding   The padding character.
//   * @param left      <code>true</code> <=> Use left padding.
//   * 
//   * @return   The text that is padded. Not <code>null</code>.
//   */
//  public static String padding( String text, int limit, char padding, boolean left ) {
//    text = limit( text, limit );
//    if( text == null ) {
//      text = "";
//    }
//    int    diff   = limit - text.length();
//    String padstr = fillString( diff, padding );
//    if( left ) {
//      return String.format( "%s%s", text, padstr );
//    } else {
//      return String.format( "%s%s", padstr, text );
//    }
//  }
//  
//  /**
//   * Returns a list of lines based upon the supplied text.
//   * 
//   * @param text   The text which might contain multiple lines. Not <code>null</code>.
//   * 
//   * @return   A list of lines from the supplied text. Not <code>null</code>.
//   */
//  public static List<String> toLines( @NonNull String text ) {
//    Reader reader = null;
//    try {
//      reader = new StringReader( text );
//      return IoFunctions.readText( reader );
//    } finally {
//      MiscFunctions.close( reader );
//    }
//  }
//  
//  /**
//   * Trims the supplied sequence using the default whitespace characters.
//   * 
//   * @param input   The object which has to be investigated. Not <code>null</code>.
//   *                      
//   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
//   *           Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T trim( @NonNull T input ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.trim( input, " \t\r\n", null );
//  }
//  
//  
//  /**
//   * Trims the supplied sequence using the default whitespace characters.
//   * 
//   * @param input   The object which has to be investigated. Not <code>null</code>.
//   * @param left    <code>null</code> <=> Trim left and right.
//   *                <code>true</code> <=> Trim left.
//   *                <code>false</code> <=> Trim right.
//   *                      
//   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
//   *           Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T trim( @NonNull T input, Boolean left ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.trim( input, " \t\r\n", left );
//  }
//  
//  /**
//   * Trims the supplied sequence.
//   * 
//   * @param input   The object which has to be investigated. Not <code>null</code>.
//   * @param chars   The characters that decide whether the trimming shall happend. Neither <code>null</code> nor empty.
//   * @param left    <code>null</code> <=> Trim left and right.
//   *                <code>true</code> <=> Trim left.
//   *                <code>false</code> <=> Trim right.
//   *                      
//   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
//   *           Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T trim( @NonNull T input, @NonNull String chars, Boolean left ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.trim( input, chars, left );
//  }
//  
//  /**
//   * Returns a list with regions providing the ranges for a matched pattern.
//   * 
//   * @param pattern    The pattern to be matched. Not <code>null</code>.
//   * @param sequence   The sequence where to look for the pattern. Not <code>null</code>.
//   * 
//   * @return   A list of regions providing the positions within the sequence. Not <code>null</code>.
//   */
//  public static List<int[]> getRegexRegions( @NonNull Pattern pattern, @NonNull String sequence ) {
//    List<int[]> result  = new ArrayList<>();
//    Matcher     matcher = pattern.matcher( sequence );
//    while( matcher.find() ) {
//      result.add( new int[] { matcher.start(), matcher.end() } );
//    }
//    return result;
//  }
//
//  /**
//   * Transforms the supplied value into a camelcase representation.
//   * 
//   * @param input   The object which has to be changed. Not <code>null</code>.
//   *                      
//   * @return   The supplied sequence if possible. The content is altered to a camelcase variety. 
//   *           Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T camelCase( @NonNull T input ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.camelCase( input );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence starts with the test string.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence starts with the test literal.
//   */
//  public static <T extends CharSequence> boolean startsWith( @NonNull T input, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.startsWith( input, test );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence starts with the test string indepently of the character case.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence starts with the test literal.
//   */
//  public static <T extends CharSequence> boolean startsWithCI( @NonNull T input, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.startsWithCI( input, test );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence ends with the test string.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence ends with the test literal.
//   */
//  public static <T extends CharSequence> boolean endsWith( @NonNull T input, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.endsWith( input, test );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence ends with the test string indepently of the character case.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence ends with the test literal.
//   */
//  public static <T extends CharSequence> boolean endsWithCI( @NonNull T input, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.endsWithCI( input, test );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence contains a string at a certain location.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param idx     The index where to test from.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence contains the test literal at the specified location.
//   */
//  public static <T extends CharSequence> boolean containsAt( @NonNull T input, int idx, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.containsAt( input, idx, test );
//  }
//
//  /**
//   * Returns <code>true</code> if the supplied sequence contains a string at a certain location while ignoring
//   * character casing.
//   * 
//   * @param input   The sequence which has to be tested. Not <code>null</code>.
//   * @param idx     The index where to test from.
//   * @param test    The character sequence to be used for testing. Not <code>null</code>.
//   * 
//   * @return   <code>true</code> <=> The supplied sequence contains the test literal at the specified location.
//   */
//  public static <T extends CharSequence> boolean containsAtCI( @NonNull T input, int idx, CharSequence test ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return facade.containsAtCI( input, idx, test );
//  }
//
//  /**
//   * Replaces regions within some text.
//   * 
//   * @param input         The text which might be altered. Not <code>null</code>.
//   * @param sep           The opening/closing of a region (f.e. "(*"). Not <code>null</code>.
//   * @param replacement   The replacement value. Not <code>null</code>.
//   * @param nested        <code>true</code> <=> Repeat the replacement process to replace nested elements as well.
//   
//   * @return   The altered text. Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T replaceRegions( @NonNull T input, CharSequence sep, CharSequence replacement, boolean nested ) {
//    return replaceRegions( input, sep, sep, replacement, nested );
//  }
//  
//  /**
//   * Replaces regions within some text.
//   * 
//   * @param input         The text which might be altered. Not <code>null</code>.
//   * @param open          The opening of a region (f.e. "(*"). Not <code>null</code>.
//   * @param close         The closing of a region (f.e. "*)"). Not <code>null</code>.
//   * @param replacement   The replacement value. Not <code>null</code>.
//   * @param nested        <code>true</code> <=> Repeat the replacement process to replace nested elements as well.
//   
//   * @return   The altered text. Not <code>null</code>.
//   */
//  public static <T extends CharSequence> T replaceRegions( @NonNull T input, @NonNull CharSequence open, @NonNull CharSequence close, @NonNull CharSequence replacement, boolean nested ) {
//    CharSequenceFacade<T> facade = CharSequenceFacades.getFacade( input );
//    return new RegionReplacer<>( facade, open, close, replacement, nested ).apply( input );
//  }

} /* ENDCLASS */
