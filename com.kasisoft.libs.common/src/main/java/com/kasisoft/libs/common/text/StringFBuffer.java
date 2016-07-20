package com.kasisoft.libs.common.text;

import lombok.experimental.*;

import lombok.*;

import java.util.regex.*;

import java.util.*;

import java.io.*;

/**
 * StringF(ormatting)Buffer equivalent which supports formatting. This buffer also supports negative indices which means
 * that the original index is calculated beginning from the end of the buffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringFBuffer implements Serializable, CharSequence {

  static final long serialVersionUID = 0x7ABEDA21D57AD988L;
  
  // only used for temporary purposes
  transient List<String>   collector;
  
  // the original implementation
  StringBuffer   origin;
  
  /**
   * @see StringBuffer#StringBuffer()
   */
  public StringFBuffer() {
    origin    = new StringBuffer();
    collector = new ArrayList<>();
  }

  /**
   * @see StringBuffer#StringBuffer(int)
   */
  public StringFBuffer( int capacity ) {
    origin    = new StringBuffer( capacity );
    collector = new ArrayList<>();
  }

  /**
   * @see StringBuffer#StringBuffer(String)
   */
  public StringFBuffer( @NonNull String str ) {
    origin    = new StringBuffer( str );
    collector = new ArrayList<>();
  }

  /**
   * @see StringBuffer#StringBuffer(CharSequence)
   */
  public StringFBuffer( @NonNull CharSequence seq ) {
    origin    = new StringBuffer( seq );
    collector = new ArrayList<>();
  }

  /**
   * @see StringBuffer#length() 
   */
  @Override
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
  @Override
  public char charAt( int index ) {
    synchronized( origin ) {
      return origin.charAt( adjustIndex( index ) );
    }
  }

  /**
   * @see StringBuffer#codePointAt(int)
   */
  public int codePointAt( int index ) {
    synchronized( origin ) {
      return origin.codePointAt( adjustIndex( index ) );
    }
  }

  /**
   * @see StringBuffer#codePointBefore(int)
   */
  public int codePointBefore( int index ) {
    synchronized( origin ) {
      return origin.codePointBefore( adjustIndex( index ) );
    }
  }

  /**
   * @see StringBuffer#codePointCount(int, int)
   */
  public int codePointCount( int begin, int end ) {
    synchronized( origin ) {
      return origin.codePointCount( adjustIndex( begin ), adjustIndex( end ) );
    }
  }

  /**
   * @see StringBuffer#offsetByCodePoints(int, int)
   */
  public int offsetByCodePoints( int index, int codepointoffset ) {
    synchronized( origin ) {
      return origin.offsetByCodePoints( adjustIndex( index ), codepointoffset );
    }
  }

  /**
   * @see StringBuffer#getChars(int, int, char[], int)
   */
  public void getChars( int start, int end, @NonNull char[] destination, int destbegin ) {
    synchronized( origin ) {
      origin.getChars( adjustIndex( start ), adjustIndex( end ), destination, destbegin );
    }
  }

  /**
   * @see StringBuffer#setCharAt(int, char)
   */
  public void setCharAt( int index, char ch ) {
    synchronized( origin ) {
      origin.setCharAt( adjustIndex( index ), ch );
    }
  }

  /**
   * @see StringBuffer#append(Object)
   */
  public StringFBuffer append( @NonNull Object obj ) {
    synchronized( origin ) {
      origin = origin.append( obj );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(String) 
   */
  public StringFBuffer append( @NonNull String str ) {
    synchronized( origin ) {
      origin = origin.append( str );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public StringFBuffer append( @NonNull StringBuffer buffer ) {
    synchronized( origin ) {
      origin = origin.append( buffer );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(StringBuffer)
   */
  public StringFBuffer append( @NonNull StringFBuffer buffer ) {
    synchronized( origin ) {
      origin = origin.append( buffer.origin );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(CharSequence)
   */
  public StringFBuffer append( @NonNull CharSequence sequence ) {
    synchronized( origin ) {
      origin = origin.append( sequence );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(CharSequence, int, int)
   */
  public StringFBuffer append( @NonNull CharSequence sequence, int start, int end ) {
    synchronized( origin ) {
      origin = origin.append( sequence, start, end );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(char[])
   */
  public StringFBuffer append( @NonNull char[] charray ) {
    synchronized( origin ) {
      origin = origin.append( charray );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(char[], int, int)
   */
  public StringFBuffer append( @NonNull char[] charray, int offset, int length ) {
    synchronized( origin ) {
      origin = origin.append( charray, offset, length );
      return this;
    }
  }
  
  /**
   * @see StringBuffer#append(boolean)
   */
  public StringFBuffer append( boolean value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(char)
   */
  public StringFBuffer append( char value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(int)
   */
  public StringFBuffer append( int value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * Appends some values using a specific format pattern.
   * 
   * @param format   The pattern to use. Neither <code>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuffer appendF( @NonNull String format, Object ... args ) {
    return append( String.format( format, args ) );
  }
  
  /**
   * @see StringBuffer#appendCodePoint(int)
   */
  public StringFBuffer appendCodePoint( int codepoint ) {
    synchronized( origin ) {
      origin = origin.appendCodePoint( codepoint );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(long)
   */
  public StringFBuffer append( long value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(float)
   */
  public StringFBuffer append( float value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * @see StringBuffer#append(double)
   */
  public StringFBuffer append( double value ) {
    synchronized( origin ) {
      origin = origin.append( value );
      return this;
    }
  }

  /**
   * @see StringBuffer#delete(int, int)
   */
  public StringFBuffer delete( int start, int end ) {
    synchronized( origin ) {
      origin = origin.delete( adjustIndex( start ), adjustIndex( end ) );
      return this;
    }
  }

  /**
   * @see StringBuffer#deleteCharAt(int)
   */
  public StringFBuffer deleteCharAt( int index ) {
    synchronized( origin ) {
      origin = origin.deleteCharAt( adjustIndex( index ) );
      return this;
    }
  }

  /**
   * @see StringBuffer#replace(int, int, String)
   */
  public StringFBuffer replace( int start, int end, @NonNull String str ) {
    synchronized( origin ) {
      origin = origin.replace( adjustIndex( start ), adjustIndex( end ), str );
      return this;
    }
  }

  /**
   * @see StringBuffer#substring(int)
   */
  public String substring( int start ) {
    synchronized( origin ) {
      return origin.substring( adjustIndex( start ) );
    }
  }

  /**
   * @see StringBuffer#subSequence(int, int)
   */
  @Override
  public CharSequence subSequence( int start, int end ) {
    synchronized( origin ) {
      return origin.subSequence( adjustIndex( start ), adjustIndex( end ) );
    }
  }

  /**
   * @see StringBuffer#substring(int, int)
   */
  public String substring( int start, int end ) {
    synchronized( origin ) {
      return origin.substring( adjustIndex( start ), adjustIndex( end ) );
    }
  }

  /**
   * @see StringBuffer#insert(int, char[], int, int)
   */
  public StringFBuffer insert( int index, @NonNull char[] charray, int offset, int length ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( index ), charray, offset, length );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, Object)
   */
  public StringFBuffer insert( int offset, @NonNull Object obj ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), obj );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, String)
   */
  public StringFBuffer insert( int offset, @NonNull String value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * Inserts some values using a specific format pattern.
   * 
   * @param offset   The location where to insert the formatted content.
   * @param format   The pattern to use. Neither <cdoe>null</code> nor empty.
   * @param args     The arguments for this pattern.
   * 
   * @return   The current buffer.
   */
  public StringFBuffer insertF( int offset, @NonNull String format, Object ... args ) {
    synchronized( origin ) {
      return insert( adjustIndex( offset ), String.format( format, args ) );
    }
  }

  /**
   * @see StringBuffer#insert(int, char[])
   */
  public StringFBuffer insert( int offset, @NonNull char[] value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, CharSequence)
   */
  public StringFBuffer insert( int offset, @NonNull CharSequence value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, CharSequence, int, int)
   */
  public StringFBuffer insert( int offset, @NonNull CharSequence value, int start, int end ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value, start, end );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, boolean)
   */
  public StringFBuffer insert( int offset, boolean value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, char)
   */
  public StringFBuffer insert( int offset, char value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, int)
   */
  public StringFBuffer insert( int offset, int value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, long)
   */
  public StringFBuffer insert( int offset, long value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, float)
   */
  public StringFBuffer insert( int offset, float value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#insert(int, double)
   */
  public StringFBuffer insert( int offset, double value ) {
    synchronized( origin ) {
      origin = origin.insert( adjustIndex( offset ), value );
      return this;
    }
  }

  /**
   * @see StringBuffer#indexOf(String)
   */
  public int indexOf( @NonNull String str ) {
    return origin.indexOf( str );
  }

  /**
   * @see StringBuffer#indexOf(String, int)
   */
  public int indexOf( @NonNull String str, int index ) {
    synchronized( origin ) {
      return origin.indexOf( str, adjustIndex( index ) );
    }
  }

  /**
   * Like {@link StringBuffer#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  public int indexOf( String ... literals ) {
    return indexOf( 0, literals );
  }
  
  /**
   * Like {@link StringBuffer#indexOf(String)} with the difference that this function provides the position of the
   * leftmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the leftmost found literal or -1 if none matched.
   */
  public int indexOf( int index, String ... literals ) {
    synchronized( origin ) {
      index      = adjustIndex( index );
      int result = -1;
      if( literals != null ) {
        for( String literal : literals ) {
          int pos = origin.indexOf( literal, index );
          if( pos != -1 ) {
            if( result == -1 ) {
              result = pos;
            } else {
              result = Math.min( result, pos );
            }
          }
        }
      }
      return result;
    }
  }
  
  /**
   * @see StringBuffer#lastIndexOf(String)
   */
  public int lastIndexOf( @NonNull String str ) {
    return origin.lastIndexOf( str );
  }

  /**
   * @see StringBuffer#lastIndexOf(String, int)
   */
  public int lastIndexOf( @NonNull String str, int index ) {
    synchronized( origin ) {
      return origin.lastIndexOf( str, adjustIndex( index ) );
    }
  }

  /**
   * Like {@link StringBuffer#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  public int lastIndexOf( String ... literals ) {
    return lastIndexOf( -1, literals );
  }
  
  /**
   * Like {@link StringBuffer#lastIndexOf(String,int)} with the difference that this function provides the position of 
   * the rightmost literal which could be found.
   * 
   * @param index      The index used as the starting point for the lookup.
   * @param literals   A list of literals that will be checked. Maybe <code>null</code>.
   * 
   * @return   The index of the rightmost found literal or -1 if none matched.
   */
  public int lastIndexOf( int index, String ... literals ) {
    synchronized( origin ) {
      index      = adjustIndex( index );
      int result = -1;
      if( literals != null ) {
        for( String literal : literals ) {
          int pos = origin.lastIndexOf( literal, index );
          if( pos != -1 ) {
            if( result == -1 ) {
              result = pos;
            } else {
              result = Math.max( result, pos );
            }
          }
        }
      }
      return result;
    }
  }
  
  /**
   * @see StringBuffer#reverse()
   */
  public StringFBuffer reverse() {
    synchronized( origin ) {
      origin = origin.reverse();
      return this;
    }
  }

  @Override
  public String toString() {
    return origin.toString();
  }
  
  /**
   * This function removes leading whitespace from this buffer.
   */
  public void trimLeading() {
    synchronized( origin ) {
      while( (length() > 0) && Character.isWhitespace( charAt(0) ) ) {
        deleteCharAt(0);
      }
    }
  }

  /**
   * This function removes trailing whitespace from this buffer.
   */
  public void trimTrailing() {
    synchronized( origin ) {
      while( (length() > 0) && Character.isWhitespace( charAt(-1) ) ) {
        deleteCharAt(-1);
      }
    }
  }

  /**
   * This function removes leading and trailing whitespace from this buffer.
   */
  public void trim() {
    synchronized( origin ) {
      trimLeading();
      trimTrailing();
    }
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

  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  public boolean startsWith( @NonNull String totest ) {
    return startsWith( true, totest );
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer starts with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal starts with the supplied literal.
   */
  public boolean startsWith( boolean casesensitive, @NonNull String totest ) {
    synchronized( origin ) {
      if( totest.length() > origin.length() ) {
        return false;
      } else {
        String part = origin.substring( 0, totest.length() );
        return StringFunctions.compare( ! casesensitive, part, totest );
      }
    }
  }

  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal ends with the supplied literal.
   */
  public boolean endsWith( @NonNull String totest ) {
    return endsWith( true, totest );
  }

  /**
   * Returns <code>true</code> if the content of this buffer ends with the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal ends with the supplied literal.
   */
  public boolean endsWith( boolean casesensitive, @NonNull String totest ) {
    synchronized( origin ) {
      if( totest.length() > origin.length() ) {
        return false;
      } else {
        String part = origin.substring( origin.length() - totest.length() );
        return StringFunctions.compare( ! casesensitive, part, totest );
      }
    }
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer equals the supplied literal.
   *  
   * @param totest   The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal is equal.
   */
  public boolean equals( @NonNull String totest ) {
    return equals( true, totest );
  }
  
  /**
   * Returns <code>true</code> if the content of this buffer equals the supplied literal.
   *  
   * @param casesensitive   <code>true</code> <=> Performs a case sensitive comparison.
   * @param totest          The text used for the comparison. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The literal is equal.
   */
  public boolean equals( boolean casesensitive, @NonNull String totest ) {
    return StringFunctions.compare( ! casesensitive, origin.toString(), totest );
  }

  /**
   * Removes a collection of characters from this buffer.
   * 
   * @param toremove   A list of characters which have to be removed. Neither <code>null</code> nor empty.
   * 
   * @return   The altered input. Not <code>null</code>.
   */
  public StringFBuffer remove( @NonNull String toremove ) {
    synchronized( origin ) {
      for( int i = length() - 1; i >= 0; i-- ) {
        if( toremove.indexOf( charAt(i) ) != -1 ) {
          deleteCharAt(i);
        }
      }
    }
    return this;
  }

  /**
   * Returns a splitted representation of this buffer except the delimiting characters. In difference to the function
   * {@link String#split(String)} this one doesn't use a regular expression.
   * 
   * @param delimiters   A list of characters providing the delimiters for the splitting. 
   *                     Neither <code>null</code> nor empty.
   *                     
   * @return   A splitted list without the delimiting character. Not <code>null</code>.
   */
  public String[] split( @NonNull String delimiters ) {
    synchronized( origin ) {
      collector.clear();
      StringTokenizer tokenizer = new StringTokenizer( origin.toString(), delimiters, false );
      while( tokenizer.hasMoreTokens() ) {
        collector.add( tokenizer.nextToken() );
      }
      return collector.toArray( new String[ collector.size() ] );
    }
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param regex   A regular expression used for the splitting. Neither <code>null</code> nor empty.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  public String[] splitRegex( @NonNull String regex ) {
    synchronized( origin ) {
      return splitRegex( Pattern.compile( regex ) );
    }
  }
  
  /**
   * Like {@link #split(String)} with the difference that this function accepts a regular expression for the splitting.
   * 
   * @param pattern   A pattern providing the regular expression used for the splitting. Not <code>null</code>.
   *                     
   * @return   A splitted list without fragments matching the supplied regular expression. Not <code>null</code>.
   */
  public String[] splitRegex( @NonNull Pattern pattern) {
    synchronized( origin ) {
      collector.clear();
      Matcher matcher = pattern.matcher( origin );
      int     last    = 0;
      boolean match   = false;
      while( matcher.find() ) {
        match         = true;
        if( matcher.start() > last ) {
          collector.add( origin.substring( last, matcher.start() ) );
        }
        last = matcher.end();
      }
      if( match && (last < origin.length() - 1 ) ) {
        collector.add( origin.substring( last ) );
      }
      if( ! match ) {
        // there was not at least one match
        collector.add( origin.toString() );
      }
      return collector.toArray( new String[ collector.size() ] );
    }
  }

  /**
   * @see String#replace(char, char)
   * 
   * @param from   The character which has to be replaced.
   * @param to     The character which has to be used instead.
   * 
   * @return   This buffer without <code>from</code> characters. Not <code>null</code>.
   */
  public StringFBuffer replace( char from, char to ) {
    synchronized( origin ) {
      for( int i = 0; i < origin.length(); i++ ) {
        if( origin.charAt(i) == from ) {
          origin.setCharAt( i, to );
        }
      }
      return this;
    }
  }

  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced. 
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceAll( @NonNull String regex, @NonNull String replacement ) {
    synchronized( origin ) {
      return replaceAll( Pattern.compile( regex ), replacement );
    }
  }
  
  /**
   * Replaces all occurrences of a regular expression with a specified replacement.
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution.
   *                      Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceAll( @NonNull Pattern pattern, @NonNull String replacement ) {
    synchronized( origin ) {
      Matcher     matcher = pattern.matcher( origin );
      List<int[]> matches = new ArrayList<>();
      while( matcher.find() ) {
        matches.add( new int[] { matcher.start(), matcher.end() } );
      }
      for( int i = matches.size() - 1; i >= 0; i-- ) {
        int start = matches.get(i)[0];
        int end   = matches.get(i)[1];
        origin.delete( start, end         );
        origin.insert( start, replacement );
      }
      return this;
    }
  }

  /**
   * Like {@link #replaceAll(String, String)} but only the first occurrence of the regular expression will be replaced. 
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced.
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceFirst( @NonNull String regex, @NonNull String replacement ) {
    synchronized( origin ) {
      return replaceFirst( Pattern.compile( regex ), replacement );
    }
  }
  
  /**
   * Like {@link #replaceAll(String, String)} but only the first occurrence of the regular expression will be replaced. 
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution. Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceFirst( @NonNull Pattern pattern, @NonNull String replacement ) {
    synchronized( origin ) {
      Matcher matcher = pattern.matcher( origin );
      if( matcher.find() ) {
        origin.delete( matcher.start(), matcher.end() );
        origin.insert( matcher.start(), replacement );
      }
      return this;
    }
  }

  /**
   * Like {@link #replaceAll(String, String)} but only the last occurrence of the regular expression will be replaced. 
   * 
   * @param regex         The regular expression used to select the fragments that will be replaced.
   *                      Neither <code>null</code> nor empty.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceLast( @NonNull String regex, @NonNull String replacement ) {
    synchronized( origin ) {
      return replaceLast( Pattern.compile( regex ), replacement );
    }
  }
  
  /**
   * Like {@link #replaceAll(String, String)} but only the last occurrence of the regular expression will be replaced. 
   * 
   * @param pattern       The Pattern providing the regular expression for the substitution. Not <code>null</code>.
   * @param replacement   The replacement which has to be used instead. Not <code>null</code>.
   * 
   * @return   This buffer. Not <code>null</code>.
   */
  public StringFBuffer replaceLast( @NonNull Pattern pattern, @NonNull String replacement ) {
    synchronized( origin ) {
      Matcher matcher   = pattern.matcher( origin );
      int     start     = -1;
      int     end       = -1;
      while( matcher.find() ) {
        start = matcher.start();
        end   = matcher.end();
      }
      if( start != -1 ) {
        origin.delete( start, end );
        origin.insert( start, replacement );
      }
      return this;
    }
  }

} /* ENDCLASS */
