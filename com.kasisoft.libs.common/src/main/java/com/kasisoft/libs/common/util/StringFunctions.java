/**
 * Name........: StringFunctions
 * Description.: Collection of functions used for String processing.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import java.util.*;

import java.io.*;

/**
 * Collection of functions used for String processing.
 */
public class StringFunctions {

  private StringFunctions() {
  }
  
  /**
   * Allocates some character buffer.
   * 
   * @param size   The size of the buffer if set. <code>null</code> means to use a default value.
   * 
   * @return   The buffer itself. Not <code>null</code>.
   */
  public static char[] allocateChars( Integer size ) {
    return Primitive.PChar.<char[]>getBuffers().allocate( size );
  }

  /**
   * Releases the supplied buffer so it can be reused later.
   * 
   * @param buffer   The buffer which has to be released. Not <code>null</code>.
   */
  public static void releaseChars( char[] buffer ) {
    Primitive.PChar.<char[]>getBuffers().release( buffer );
  }

  /**
   * Returns the last index of some literal.
   * 
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( String input, String ... literals ) {
    return lastIndexOf( input.length(), input, literals );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( String input, char ... characters ) {
    return lastIndexOf( input.length(), input, characters );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( StringBuffer input, char ... characters ) {
    return lastIndexOf( input.length(), input, characters );
  }

  /**
   * Returns the last index of some literal.
   * 
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( StringBuffer input, String ... literals ) {
    return lastIndexOf( input.length(), input, literals );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( int first, String input, char ... characters ) {
    int result = -1;
    for( int i = 0; i < characters.length; i++ ) {
      char ch  = characters[i];
      int  pos = input.lastIndexOf( ch, first );
      if( pos != -1 ) {
        result = Math.max( result, pos );
      }
    }
    return result;
  }

  /**
   * Returns the last index of some literal.
   * 
   * @param first      The first position where to start looking from.
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( int first, String input, String ... literals ) {
    int result = -1;
    for( int i = 0; i < literals.length; i++ ) {
      String literal  = literals[i];
      int    pos      = input.lastIndexOf( literal, first );
      if( pos != -1 ) {
        result = Math.max( result, pos );
      }
    }
    return result;
  }

  /**
   * Returns the last index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( int first, StringBuffer input, char ... characters ) {
    int result = -1;
    for( int i = 0; i < characters.length; i++ ) {
      char ch  = characters[i];
      int  pos = input.lastIndexOf( String.valueOf( ch ), first );
      if( pos != -1 ) {
        result = Math.max( result, pos );
      }
    }
    return result;
  }

  /**
   * Returns the last index of some character.
   * 
   * @param first      The first position where to start looking from.
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static int lastIndexOf( int first, StringBuffer input, String ... literals ) {
    int result = -1;
    for( int i = 0; i < literals.length; i++ ) {
      String literal  = literals[i];
      int    pos      = input.lastIndexOf( literal, first );
      if( pos != -1 ) {
        result = Math.max( result, pos );
      }
    }
    return result;
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( String input, String ... literals ) {
    return indexOf( 0, input, literals );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( String input, char ... characters ) {
    return indexOf( 0, input, characters );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( StringBuffer input, char ... characters ) {
    return indexOf( 0, input, characters );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( StringBuffer input, String ... literals ) {
    return indexOf( 0, input, literals );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, String input, char ... characters ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < characters.length; i++ ) {
      char ch  = characters[i];
      int  pos = input.indexOf( ch, first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first      The first position where to start looking from.
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, String input, String ... literals ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < literals.length; i++ ) {
      String literal  = literals[i];
      int    pos      = input.indexOf( literal, first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, StringBuffer input, char ... characters ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < characters.length; i++ ) {
      char ch  = characters[i];
      int  pos = input.indexOf( String.valueOf( ch ), first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, StringBuilder input, char ... characters ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < characters.length; i++ ) {
      char ch  = characters[i];
      int  pos = input.indexOf( String.valueOf( ch ), first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first      The first position where to start looking from.
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, StringBuffer input, String ... literals ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < literals.length; i++ ) {
      String literal  = literals[i];
      int    pos      = input.indexOf( literal, first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first      The first position where to start looking from.
   * @param input      The String where the characters have to be looked for. Neither <code>null</code> nor empty.
   * @param literals   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static int indexOf( int first, StringBuilder input, String ... literals ) {
    int result = Integer.MAX_VALUE;
    for( int i = 0; i < literals.length; i++ ) {
      String literal  = literals[i];
      int    pos      = input.indexOf( literal, first );
      if( pos != -1 ) {
        result = Math.min( result, pos );
      }
    }
    if( result == Integer.MAX_VALUE ) {
      return -1;
    } else {
      return result;
    }
  }

  /**
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be trimmed so there 
   * won't be any whitespace at the beginning or the end.
   * 
   * @param input   The String that has to be altered.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  public static String cleanup( String input ) {
    if( input != null ) {
      input = input.trim();
      if( input.length() == 0 ) {
        input = null;
      }
    }
    return input;
  }
  
  /**
   * Generates a String with a specified length and initialises it with a selected character.
   * 
   * @param length      The length of the desired String.
   * @param character   The character used to insert.
   * 
   * @return   A String with the supplied length and characters.
   */
  public static String fillString( int length, char character ) {
    char[] buffer = allocateChars( Integer.valueOf( length ) );
    try {
      Arrays.fill( buffer, character );
      return new String( buffer, 0, length );
    } finally {
      releaseChars( buffer );
    }
  }
  
  /**
   * Converts the trace supplied with the supplied Throwable instance into a string.
   * 
   * @param t   The Throwable instance which trace shall be returned.
   * 
   * @return   A String providing the stack trace. Not <code>null</code>.
   */
  public static String toString( Throwable t ) {
    if( t == null ) {
      return "null";
    }
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    PrintStream           printer = new PrintStream( byteout );
    t.printStackTrace( printer );
    return new String( byteout.toByteArray() );
  }
  
  /**
   * Returns a textual representation of the supplied object.
   * 
   * @param obj    The object which textual representation is desired.
   * 
   * @return   The textual representation of the supplied object.
   */
  public static String toString( Object obj ) {
    if( obj == null ) {
      return "null";
    } else {
      return String.valueOf( obj );
    }
  }
  
  /**
   * Generates a textual representation of the supplied list.
   * 
   * @param args   A list used to create a textual representation from.
   * 
   * @return   The textual representation of the supplied list.
   */
  public static String toString( Object[] args ) {
    if( args == null ) {
      return "null";
    } else {
      StringBuilder buffer = new StringBuilder();
      if( args.length > 0 ) {
        buffer.append( toString( args[0] ) );
        for( int i = 1; i < args.length; i++ ) {
          buffer.append( "," );
          buffer.append( toString( args[i] ) );
        }
      }
      return buffer.toString();
    }
  }

  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input     The input which has to be modified. Not <code>null</code>.
   * @param search    The partial String to search for. Neither <code>null</code> nor empty.
   * @param replace   The String to replace instead. Not <code>null</code>.
   * 
   * @return   The modified String. Not <code>null</code>.
   */
  public static String replace( String input, String search, String replace ) {
    StringFBuffer buffer = new StringFBuffer( input );
    replace( buffer, search, replace );
    return buffer.toString();
  }

  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param buffer    The buffer which has to be modified in place. Not <code>null</code>.
   * @param search    The partial String to search for. Neither <code>null</code> nor empty.
   * @param replace   The String to replace instead. Not <code>null</code>.
   */
  public static void replace( StringFBuffer buffer, String search, String replace ) {
    int index  = buffer.indexOf( search, 0 );
    while( index != -1 ) {
      buffer.replace( index, index + search.length(), replace );
      index = buffer.indexOf( search, index + replace.length() );
    }
  }

  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param input          The input which has to be modified. Not <code>null</code>.
   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
   * 
   * @return   The modified String.
   */
  public static String replace( String input, Map<String,String> replacements ) {
    StringFBuffer  buffer = new StringFBuffer( input );
    replace( buffer, replacements );
    return buffer.toString();
  }

  /**
   * Performs a search & replace operation on the supplied input.
   * 
   * @param buffer         The buffer which has to be modified in place. Not <code>null</code>.
   * @param replacements   A Map of String's used to run the search replace operation. Not <code>null</code>.
   */
  public static void replace( StringFBuffer buffer, Map<String,String> replacements ) {
    Set<String>   search = replacements.keySet();
    Tupel<String> key    = new Tupel<String>();
    int           index  = indexOf( buffer, search, key, 0 );
    while( index != -1 ) {
      String searchstr  = key.getValue();
      String replacestr = replacements.get( searchstr );
      buffer.replace( index, index + searchstr.length(), replacestr );
      index             = indexOf( buffer, search, key, index + replacestr.length() );
    }
  }

  /**
   * Locates the index of the leftmost search string.
   * 
   * @param input   The StringBuffer where to look at. Not <code>null</code>.
   * @param keys    All used search strings. Not <code>null</code>.
   * @param key     Receiver of the String that finally has been found. Not <code>null</code>.
   * @param start   An initial index.
   * 
   * @return   The index where a search string has been found or -1 in case none has been found.
   */
  private static int indexOf( StringFBuffer input, Set<String> keys, Tupel<String> key, int start ) {
    int result = -1;
    key.setValues( (String[]) null );
    for( String current : keys ) {
      int pos = input.indexOf( current, start );
      if( pos != -1 ) {
        if( (result == -1) || (pos < result) ) {
          result = pos;
          key.setValues( current );
        }
      }
    }
    return result;
  }

  /**
   * Returns <code>true</code> if one candidate is contained within the test value.
   * 
   * @param test         The element that has to be tested. Neither <code>null</code> nor empty.
   * @param candidates   The list of allowed candidates. Neither <code>null</code> nor empty.
   * 
   * @return  true <=> The element which has to be tested is contained.
   */
  public static boolean contains( String test, String ... candidates ) {
    for( String entry : candidates ) {
      if( test.contains( entry ) ) {
        return true;
      }
    }
    return false;
  }
  
  /**
   * Tests whether a test has a specified ending or not.
   * 
   * @param test         The String which has to be tested. Neither <code>null</code> nor empty.
   * @param candidates   The allowed endings. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The String has a specified ending.
   */
  public static boolean endsWith( String test, String ... candidates ) {
    for( String entry : candidates ) {
      if( test.endsWith( entry ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Tests whether a test has a specified beginning or not.
   * 
   * @param test         The String which has to be tested. Neither <code>null</code> nor empty.
   * @param candidates   The allowed beginnings. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The String has a specified beginning.
   */
  public static boolean startsWith( String test, String ... candidates ) {
    for( String entry : candidates ) {
      if( test.startsWith( entry ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a concatenation of the supplied Strings. This function allows elements to be 
   * <code>null</code> which means that they're just be ignored.
   * 
   * @param delimiter   A delimiter which might be used. Maybe <code>null</code>.
   * @param args        The list of Strings that has to be concatenated. Maybe <code>null</code>.
   * 
   * @return   The concatenated String. Not <code>null</code>.
   */
  public static String concatenate( String delimiter, String ... args ) {
    if( args == null ) {
      return "";
    }
    if( delimiter == null ) {
      delimiter = "";
    }
    StringBuilder buffer = new StringBuilder();
    for( int i = 0; i < args.length; i++ ) {
      if( (args[i] != null) && (args[i].length() > 0) ) {
        if( buffer.length() > 0 ) {
          buffer.append( delimiter );
        }
        buffer.append( args[i] );
      }
    }
    return buffer.toString();
  }
  
  /**
   * Returns <code>true</code> if both literals are equal. This function allows to ignore the case
   * sensitivity by choice.
   * 
   * @param str1         One String to be used for the comparison. Not <code>null</code>. 
   * @param str2         One String to be used for the comparison. Not <code>null</code>.
   * @param ignorecase   <code>true</code> <=> Ignore case sensitivity.
   * 
   * @return   <code>true</code> <=> Both Strings are equal.
   */
  public static boolean equals( String str1, String str2, boolean ignorecase ) {
    if( ignorecase ) {
      return str1.equalsIgnoreCase( str2 );
    } else {
      return str1.equals( str2 );
    }
  }

  /**
   * Compares two strings depending on the case sensitivity.
   * 
   * @param ignorecase   <code>true</code> <=> Ignore the cases.
   * @param str1         One string to compare. Neither <code>null</code> nor empty.
   * @param str2         One string to compare. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> Both strings are equal.
   */
  public static boolean compare( boolean ignorecase, String str1, String str2 ) {
    if( ignorecase ) {
      return str1.equalsIgnoreCase( str2 );
    } else {
      return str1.equals( str2 );
    }
  }
  
  /**
   * Alters the input while exchanging it's suffix.
   * 
   * @param input        The input which has to be altered. Maybe <code>null</code>.
   * @param newsuffix    The new suffix without the dot at the beginning. Neither <code>null</code> nor empty.
   * 
   * @return   The altered input. <code>null</code> if there wasn't a replacable suffix.
   */
  public static String replaceSuffix( String input, String newsuffix ) {
    if( input != null ) {
      int lidx = input.lastIndexOf('.');
      if( lidx != -1 ) {
        return String.format( "%s.%s", input.substring( 0, lidx ), newsuffix );
      }
    }
    return null;
  }
  
  /**
   * Repeats the supplied text <param>n</param> times.
   * 
   * @param n      The number of concatenations that have to be performed.
   * @param text   The text that has to be repeated. Neither <code>null</code> nor empty.
   * 
   * @return   The concatenated reproduction string. Not <code>null</code>.
   */
  public static String repeat( int n, String text ) {
    if( (n > 0) && (text != null) && (text.length() > 0) ) {
      StringBuffer buffer = new StringBuffer();
      while( n > 0 ) {
        buffer.append( text );
        n--;
      }
      return buffer.toString();
    }
    return "";
  }

  /**
   * Returns a limited version of the supplied text.
   * 
   * @param text    The text that might require to be limited. Maybe <code>null</code>.
   * @param limit   The maximum number of characters to be allowed.
   * 
   * @return   The limited version of the supplied text. Maybe <code>null</code>.
   */
  public static String limit( String text, int limit ) {
    if( (text != null) && (text.length() > limit) ) {
      return text.substring( 0, limit );
    }
    return text;
  }
  
  /**
   * Creates a textual presentation with a padding using the space character.
   * 
   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
   * @param limit     The maximum number of characters allowed.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded. Not <code>null</code>.
   */
  public static String padding( String text, int limit, boolean left ) {
    return padding( text, limit, ' ', left );
  }

  /**
   * Creates a textual presentation with a padding.
   * 
   * @param text      The text that is supposed to be filled with padding. Maybe <code>null</code>.
   * @param limit     The maximum number of characters allowed.
   * @param padding   The padding character.
   * @param left      <code>true</code> <=> Use left padding.
   * 
   * @return   The text that is padded. Not <code>null</code>.
   */
  public static String padding( String text, int limit, char padding, boolean left ) {
    text = limit( text, limit );
    if( text == null ) {
      text = "";
    }
    int    diff   = limit - text.length();
    String padstr = fillString( diff, padding );
    if( left ) {
      return String.format( "%s%s", text, padstr );
    } else {
      return String.format( "%s%s", padstr, text );
    }
  }
  
} /* ENDCLASS */
