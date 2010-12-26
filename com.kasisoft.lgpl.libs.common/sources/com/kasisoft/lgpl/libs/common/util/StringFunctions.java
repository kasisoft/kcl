/**
 * Name........: StringFunctions
 * Description.: Collection of functions used for String processing.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.text.*;

import java.util.*;

import java.io.*;

/**
 * Collection of functions used for String processing.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class StringFunctions {

  private static final Buffers<char[]> CHARBUFFERS = Buffers.newBuffers( Primitive.PChar );

  private static final DecimalFormat FORMAT2 = new DecimalFormat(   "00" );
  private static final DecimalFormat FORMAT3 = new DecimalFormat(  "000" );
  private static final DecimalFormat FORMAT4 = new DecimalFormat( "0000" );

  /**
   * Allocates some character buffer.
   * 
   * @param size   The size of the buffer if set. <code>null</code> means to use a default value.
   * 
   * @return   The buffer itself. Not <code>null</code>.
   */
  public static final char[] allocateChars( Integer size ) {
    return CHARBUFFERS.allocate( size );
  }

  /**
   * Releases the supplied buffer so it can be reused later.
   * 
   * @param buffer   The buffer which has to be released. Not <code>null</code>.
   */
  public static final void releaseChars( 
    @KNotNull(name="buffer")   char[]   buffer 
  ) {
    CHARBUFFERS.release( buffer );
  }

  /**
   * Returns the last index of some literal.
   * 
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KNotNull(name="input")       String   input, 
    @KNotEmpty(name="literals")   String   ... literals 
  ) {
    return lastIndexOf( input.length(), input, literals );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KNotNull(name="input")         String   input, 
    @KNotEmpty(name="characters")   char     ... characters 
  ) {
    return lastIndexOf( input.length(), input, characters );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KNotNull(name="input")         StringBuffer   input, 
    @KNotEmpty(name="characters")   char           ... characters 
  ) {
    return lastIndexOf( input.length(), input, characters );
  }

  /**
   * Returns the last index of some literal.
   * 
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KNotNull(name="input")       StringBuffer   input, 
    @KNotEmpty(name="literals")   String         ... literals 
  ) {
    return lastIndexOf( input.length(), input, literals );
  }

  /**
   * Returns the last index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KIPositive(name="first",zero=true)   int      first, 
    @KNotNull(name="input")               String   input, 
    @KNotEmpty(name="characters")         char     ... characters 
  ) {
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
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KIPositive(name="first",zero=true)   int      first, 
    @KNotNull(name="input")               String   input, 
    @KNotEmpty(name="literals")           String   ... literals 
  ) {
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
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KIPositive(name="first",zero=true)   int            first, 
    @KNotNull(name="input")               StringBuffer   input, 
    @KNotEmpty(name="characters")         char           ... characters 
  ) {
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
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (rightmost index) otherwise -1.
   */
  public static final int lastIndexOf( 
    @KIPositive(name="first",zero=true)   int            first, 
    @KNotNull(name="input")               StringBuffer   input, 
    @KNotEmpty(name="literals")           String         ... literals 
  ) {
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
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KNotNull(name="input")       String   input, 
    @KNotEmpty(name="literals")   String   ... literals 
  ) {
    return indexOf( 0, input, literals );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KNotNull(name="input")         String   input, 
    @KNotEmpty(name="characters")   char     ... characters 
  ) {
    return indexOf( 0, input, characters );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KNotNull(name="input")         StringBuffer   input, 
    @KNotEmpty(name="characters")   char           ... characters 
  ) {
    return indexOf( 0, input, characters );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KNotNull(name="input")       StringBuffer   input, 
    @KNotEmpty(name="literals")   String         ... literals 
  ) {
    return indexOf( 0, input, literals );
  }

  /**
   * Returns the first index of some character.
   * 
   * @param first        The first position where to start looking from.
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KIPositive(name="first",zero=true)   int      first, 
    @KNotNull(name="input")               String   input, 
    @KNotEmpty(name="characters")         char     ... characters 
  ) {
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
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of literals used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KIPositive(name="first",zero=true)   int      first, 
    @KNotNull(name="input")               String   input, 
    @KNotEmpty(name="literals")           String   ... literals 
  ) {
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
   * @param input        The String where the characters have to be looked for.
   *                     Neither <code>null</code> nor empty.
   * @param characters   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a character has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KIPositive(name="first",zero=true)   int            first, 
    @KNotNull(name="input")               StringBuffer   input, 
    @KNotEmpty(name="characters")         char           ... characters 
  ) {
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
   * @param input      The String where the characters have to be looked for.
   *                   Neither <code>null</code> nor empty.
   * @param literals   A list of characters used to test. Must have a minimum length of 1.
   * 
   * @return   The index where a literal has been found (leftmost index) otherwise -1.
   */
  public static final int indexOf( 
    @KIPositive(name="first",zero=true)   int            first, 
    @KNotNull(name="input")               StringBuffer   input, 
    @KNotEmpty(name="literals")           String         ... literals 
  ) {
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
   * Makes sure that the supplied String is either <code>null</code> or not empty. The text will be
   * trimmed so there won't be any whitespace at the beginning or the end.
   * 
   * @param input   The String that has to be altered.
   * 
   * @return   <code>null</code> or a non-empty String.
   */
  public static final String cleanup( String input ) {
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
  public static final String fillString( 
    @KIPositive(name="length")   int    length, 
                                 char   character 
  ) {
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
  public static final String toString( Throwable t ) {
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
  public static final String toString( Object obj ) {
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
  public static final String toString( Object[] args ) {
    if( args == null ) {
      return "null";
    } else {
      StringBuffer buffer = new StringBuffer();
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
  public static final String replace( 
    @KNotNull(name="input")     String   input, 
    @KNotEmpty(name="search")   String   search, 
    @KNotNull(name="replace")   String   replace 
  ) {
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
  public static final void replace( 
    @KNotNull(name="buffer")    StringFBuffer   buffer, 
    @KNotEmpty(name="search")   String          search, 
    @KNotNull(name="replace")   String          replace 
  ) {
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
  public static final String replace( 
    @KNotNull(name="input")          String               input, 
    @KNotNull(name="replacements")   Map<String,String>   replacements
  ) {
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
  public static final void replace( 
    @KNotNull(name="buffer")         StringFBuffer        buffer, 
    @KNotNull(name="replacements")   Map<String,String>   replacements
  ) {
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
  private static final int indexOf( 
    StringFBuffer input, Set<String> keys, Tupel<String> key, int start 
  ) {
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
   * Creates a decimal formatted String from the supplied value. Two cyphers with preceeding zeros.
   * 
   * @param value   The value used to get a formatted String.
   * 
   * @return   A formatted String. Neither <code>null</code> nor empty.
   */
  public static final String decFormat2( int value ) {
    return FORMAT2.format( value );
  }

  /**
   * Creates a decimal formatted String from the supplied value. Three cyphers with preceeding zeros.
   * 
   * @param value   The value used to get a formatted String.
   * 
   * @return   A formatted String. Neither <code>null</code> nor empty.
   */
  public static final String decFormat3( int value ) {
    return FORMAT3.format( value );
  }

  /**
   * Creates a decimal formatted String from the supplied value. Four cyphers with preceeding zeros.
   * 
   * @param value   The value used to get a formatted String.
   * 
   * @return   A formatted String. Neither <code>null</code> nor empty.
   */
  public static final String decFormat4( int value ) {
    return FORMAT4.format( value );
  }

  /**
   * Returns <code>true</code> if one candidate is contained within the test value.
   * 
   * @param test         The element that has to be tested. Neither <code>null</code> nor empty.
   * @param candidates   The list of allowed candidates. Neither <code>null</code> nor empty.
   * 
   * @return  true <=> The element which has to be tested is contained.
   */
  public static final boolean contains( 
    @KNotEmpty(name="test")         String   test, 
    @KNotEmpty(name="candidates")   String   ... candidates 
  ) {
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
  public static final boolean endsWith( 
    @KNotEmpty(name="test")         String   test, 
    @KNotEmpty(name="candidates")   String   ... candidates 
  ) {
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
  public static final boolean startsWith( 
    @KNotEmpty(name="test")         String   test, 
    @KNotEmpty(name="candidates")   String   ... candidates 
  ) {
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
  public static final String concatenate( String delimiter, String ... args ) {
    if( args == null ) {
      return "";
    }
    if( delimiter == null ) {
      delimiter = "";
    }
    StringBuffer buffer = new StringBuffer();
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
  public static final boolean equals( String str1, String str2, boolean ignorecase ) {
    if( ignorecase ) {
      return str1.equalsIgnoreCase( str2 );
    } else {
      return str1.equals( str2 );
    }
  }


} /* ENDCLASS */
