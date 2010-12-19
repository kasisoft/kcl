/**
 * Name........: StringFBuffer
 * Description.: StringBuffer equivalent which supports formatting.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * StringF(ormatting)Buffer equivalent which supports formatting. This buffer also supports negative indices which means
 * that the original index is calculated beginning from the end of the buffer.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class StringFBuffer {

  private StringBuffer   origin;
  
  /**
   * @see StringBuffer#StringBuffer()
   */
  public StringFBuffer() {
    origin = new StringBuffer();
  }

  /**
   * @see StringBuffer#StringBuffer(int)
   */
  public StringFBuffer( int capacity ) {
    origin = new StringBuffer( capacity );
  }

  /**
   * @see StringBuffer#StringBuffer(String)
   */
  public StringFBuffer( String str ) {
    origin = new StringBuffer( str );
  }

  /**
   * @see StringBuffer#StringBuffer(CharSequence)
   */
  public StringFBuffer( CharSequence seq ) {
    origin = new StringBuffer( seq );
  }

  /**
   * @see StringBuffer#length() 
   */
  public int length() {
    return origin.length();
  }

  /**
   * @see StringBuffer#capacity()
   */
  public int capacity() {
    return origin.capacity();
  }

  /**
   * @see StringBuffer#ensureCapacity(int)
   */
  public void ensureCapacity( int minimum ) {
    origin.ensureCapacity( minimum );
  }

  /**
   * @see StringBuffer#trimToSize()
   */
  public void trimToSize() {
    origin.trimToSize();
  }

  /**
   * @see StringBuffer#setLength(int)
   */
  public void setLength( int newlength ) {
    origin.setLength( newlength );
  }

  /**
   * @see StringBuffer#charAt(int)
   */
  public char charAt( int index ) {
    return origin.charAt( adjustIndex( index ) );
  }

  /**
   * @see StringBuffer#codePointAt(int)
   */
  public int codePointAt( int index ) {
    return origin.codePointAt( adjustIndex( index ) );
  }

  /**
   * @see StringBuffer#codePointBefore(int)
   */
  public int codePointBefore( int index ) {
    return origin.codePointBefore( adjustIndex( index ) );
  }

  /**
   * @see StringBuffer#codePointCount(int, int)
   */
  public int codePointCount( int begin, int end ) {
    return origin.codePointCount( adjustIndex( begin ), adjustIndex( end ) );
  }

  /**
   * @see StringBuffer#offsetByCodePoints(int, int)
   */
  public int offsetByCodePoints( int index, int codepointoffset ) {
    return origin.offsetByCodePoints( adjustIndex( index ), codepointoffset );
  }

  /**
   * @see StringBuffer#getChars(int, int, char[], int)
   */
  public void getChars( int start, int end, char[] destination, int destbegin ) {
    origin.getChars( adjustIndex( start ), adjustIndex( end ), destination, destbegin );
  }

  /**
   * @see StringBuffer#setCharAt(int, char)
   */
  public void setCharAt( int index, char ch ) {
    origin.setCharAt( adjustIndex( index ), ch );
  }

  /**
   * @see StringBuffer#append(Object)
   */
  public StringFBuffer append( Object obj ) {
    origin = origin.append( obj );
    return this;
  }

  /**
   * @see StringBuffer#append(String) 
   */
  public StringFBuffer append( String str ) {
    origin = origin.append( str );
    return this;
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public StringFBuffer append( StringBuffer buffer ) {
    origin = origin.append( buffer );
    return this;
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public StringFBuffer append( StringFBuffer buffer ) {
    origin = origin.append( buffer.origin );
    return this;
  }

  /**
   * @see StringBuffer#append(CharSequence)
   */
  public StringFBuffer append( CharSequence sequence ) {
    origin = origin.append( sequence );
    return this;
  }

  /**
   * @see StringBuffer#append(CharSequence, int, int)
   */
  public StringFBuffer append( CharSequence sequence, int start, int end ) {
    origin = origin.append( sequence, start, end );
    return this;
  }

  /**
   * @see StringBuffer#append(char[])
   */
  public StringFBuffer append( char[] charray ) {
    origin = origin.append( charray );
    return this;
  }

  /**
   * @see StringBuffer#append(char[], int, int)
   */
  public StringFBuffer append( char[] charray, int offset, int length ) {
    origin = origin.append( charray, offset, length );
    return this;
  }
  
  /**
   * @see StringBuffer#append(boolean)
   */
  public StringFBuffer append( boolean value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * @see StringBuffer#append(char)
   */
  public StringFBuffer append( char value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * @see StringBuffer#append(int)
   */
  public StringFBuffer append( int value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * Appends some values using a specific format pattern.
   * 
   * @param format   The pattern to use.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuffer appendF( String format, Object ... args ) {
    return append( String.format( format, args ) );
  }
  
  /**
   * @see StringBuffer#appendCodePoint(int)
   */
  public StringFBuffer appendCodePoint( int codepoint ) {
    origin = origin.appendCodePoint( codepoint );
    return this;
  }

  /**
   * @see StringBuffer#append(long)
   */
  public StringFBuffer append( long value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * @see StringBuffer#append(float)
   */
  public StringFBuffer append( float value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * @see StringBuffer#append(double)
   */
  public StringFBuffer append( double value ) {
    origin = origin.append( value );
    return this;
  }

  /**
   * @see StringBuffer#delete(int, int)
   */
  public StringFBuffer delete( int start, int end ) {
    origin = origin.delete( adjustIndex( start ), adjustIndex( end ) );
    return this;
  }

  /**
   * @see StringBuffer#deleteCharAt(int)
   */
  public StringFBuffer deleteCharAt( int index ) {
    origin = origin.deleteCharAt( adjustIndex( index ) );
    return this;
  }

  /**
   * @see StringBuffer#replace(int, int, String)
   */
  public StringFBuffer replace( int start, int end, String str ) {
    origin = origin.replace( adjustIndex( start ), adjustIndex( end ), str );
    return this;
  }

  /**
   * @see StringBuffer#substring(int)
   */
  public String substring( int start ) {
    return origin.substring( adjustIndex( start ) );
  }

  /**
   * @see StringBuffer#subSequence(int, int)
   */
  public CharSequence subSequence( int start, int end ) {
    return origin.subSequence( adjustIndex( start ), adjustIndex( end ) );
  }

  /**
   * @see StringBuffer#substring(int, int)
   */
  public String substring( int start, int end ) {
    return origin.substring( adjustIndex( start ), adjustIndex( end ) );
  }

  /**
   * @see StringBuffer#insert(int, char[], int, int)
   */
  public StringFBuffer insert( int index, char[] charray, int offset, int length ) {
    origin = origin.insert( adjustIndex( offset ), charray, offset, length );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, Object)
   */
  public StringFBuffer insert( int offset, Object obj ) {
    origin = origin.insert( adjustIndex( offset ), obj );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, String)
   */
  public StringFBuffer insert( int offset, String value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * Inserts some values using a specific format pattern.
   * 
   * @param offset   The location where to insert the formatted content.
   * @param format   The pattern to use.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuffer insertF( int offset, String format, Object ... args ) {
    return insert( adjustIndex( offset ), String.format( format, args ) );
  }

  /**
   * @see StringBuffer#insert(int, char[])
   */
  public StringFBuffer insert( int offset, char[] value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, CharSequence)
   */
  public StringFBuffer insert( int offset, CharSequence value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, CharSequence, int, int)
   */
  public StringFBuffer insert( int offset, CharSequence value, int start, int end ) {
    origin = origin.insert( adjustIndex( offset ), value, start, end );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, boolean)
   */
  public StringFBuffer insert( int offset, boolean value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, char)
   */
  public StringFBuffer insert( int offset, char value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, int)
   */
  public StringFBuffer insert( int offset, int value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, long)
   */
  public StringFBuffer insert( int offset, long value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, float)
   */
  public StringFBuffer insert( int offset, float value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#insert(int, double)
   */
  public StringFBuffer insert( int offset, double value ) {
    origin = origin.insert( adjustIndex( offset ), value );
    return this;
  }

  /**
   * @see StringBuffer#indexOf(String)
   */
  public int indexOf( String str ) {
    return origin.indexOf( str );
  }

  /**
   * @see StringBuffer#indexOf(String, int)
   */
  public int indexOf( String str, int index ) {
    return origin.indexOf( str, adjustIndex( index ) );
  }

  /**
   * @see StringBuffer#lastIndexOf(String)
   */
  public int lastIndexOf( String str ) {
    return origin.lastIndexOf( str );
  }

  /**
   * @see StringBuffer#lastIndexOf(String, int)
   */
  public int lastIndexOf( String str, int index ) {
    return origin.lastIndexOf( str, adjustIndex( index ) );
  }
  
  /**
   * @see StringBuffer#reverse()
   */
  public StringFBuffer reverse() {
    origin = origin.reverse();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    return origin.toString();
  }
  
  /**
   * This function removes leading whitespace from this buffer.
   */
  public void trimLeading() {
    while( (length() > 0) && Character.isWhitespace( charAt(0) ) ) {
      deleteCharAt(0);
    }
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  public void trimTrailing() {
    while( (length() > 0) && Character.isWhitespace( charAt(-1) ) ) {
      deleteCharAt(-1);
    }
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  public void trim() {
    trimLeading();
    trimTrailing();
  }

  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   * 
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  private int adjustIndex( int index ) {
    if( index < 0 ) {
      return origin.length() + index;
    }
    return index;
  }

} /* ENDCLASS */
