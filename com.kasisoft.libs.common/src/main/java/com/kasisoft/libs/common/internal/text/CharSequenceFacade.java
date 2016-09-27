package com.kasisoft.libs.common.internal.text;

/**
 * A simple facade which is used to handle commonly used CharSequence types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface CharSequenceFacade<T extends CharSequence> {
  
  /**
   * Returns <code>true</code> if the supplied String is available at least once.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param str        The String that shall be looked for. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied String is contained.
   */
  boolean contains( T sequence, String str );
  
  /**
   * Returns the first occurrence of the supplied character starting by a specified index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param ch         The character which shall be looked for.
   * @param first      The offset where to start looking from.
   * 
   * @return   The index of the first occurrence or <code>-1</code> if none was found.
   */
  int indexOf( T sequence, char ch, int first );
  
  /**
   * Returns the first occurrence of the supplied String starting by a specified index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param str        The String which shall be looked for.
   * @param first      The offset where to start looking from.
   * 
   * @return   The index of the first occurrence or <code>-1</code> if none was found.
   */
  int indexOf( T sequence, String str, int first );

  /**
   * Returns the last occurrence of the supplied character starting by a specified index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param ch         The character which shall be looked for.
   * @param first      The offset where to start looking from.
   * 
   * @return   The index of the first occurrence or <code>-1</code> if none was found.
   */
  int lastIndexOf( T sequence, char ch, int first );
  
  /**
   * Returns the last occurrence of the supplied String starting by a specified index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param str        The String which shall be looked for.
   * @param first      The offset where to start looking from.
   * 
   * @return   The index of the first occurrence or <code>-1</code> if none was found.
   */
  int lastIndexOf( T sequence, String str, int first );
  
  /**
   * Returns a part of the supplied object.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param offset     The offset where to start copying from. 
   * @param end        The top offset (exclusive).
   * 
   * @return   The part. Not <code>null</code>.
   */
  String substring( T sequence, int offset, int end );

  /**
   * Replaces a fraction of the supplied sequence.
   * 
   * @param sequence      The object which has to be investigated. Not <code>null</code>.
   * @param offset        The offset where to start the replacement from. 
   * @param end           The ending offset (exclusive).
   * @param replacement   The text that shall be used as the replacement. Not <code>null</code>. 
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T replace( T sequence, int offset, int end, String replacement );

  /**
   * Trims the supplied sequence.
   * 
   * @param sequence      The object which has to be investigated. Not <code>null</code>.
   * @param chars         The characters that decide whether the trimming shall happend. Neither <code>null</code> nor empty.
   * @param left          <code>null</code> <=> Trim left and right.
   *                      <code>true</code> <=> Trim left.
   *                      <code>false</code> <=> Trim right.
   *                      
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  default T trim( T sequence, String chars, Boolean left ) {
    if( (left == null) || left.booleanValue() ) {
      while( (sequence.length() > 0) && (chars.indexOf( sequence.charAt(0) ) != -1) ) {
        sequence = deleteCharAt( sequence, 0 );
      }
    }
    if( (left == null) || (! left.booleanValue()) ) {
      while( (sequence.length() > 0) && (chars.indexOf( sequence.charAt( sequence.length() - 1 ) ) != -1) ) {
        sequence = deleteCharAt( sequence, sequence.length() - 1 );
      }
    }
    return sequence;
  }

  /**
   * Creates a lower case version of this sequence.
   * 
   * @param sequence      The object which has to be investigated. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  default T toLowerCase( T sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence = setCharAt( sequence, i, Character.toLowerCase( ch ) );
      }
    }
    return sequence;
  }

  /**
   * Creates a upper case version of this sequence.
   * 
   * @param sequence      The object which has to be investigated. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  default T toUpperCase( T sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence = setCharAt( sequence, i, Character.toUpperCase( ch ) );
      }
    }
    return sequence;
  }

  /**
   * Deletes a character at a certain index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param idx        The index that is supposed to be deleted.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T deleteCharAt( T sequence, int idx );
  
  /**
   * Sets a character at a certain index.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * @param idx        The index where to write to.
   * @param ch         The character that shall be written.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T setCharAt( T sequence, int idx, char ch );

  /**
   * Deletes a portion of the supplied sequence.
   * 
   * @param sequence   The sequence which has to be altered. Not <code>null</code>.
   * @param start      The index of the first character to delete.
   * @param end        The end of the deletion. The index is exclusive.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T delete( T sequence, int start, int end );
  
  /**
   * Inserts a String into the supplied sequence.
   * 
   * @param sequence   The sequence which has to be altered. Not <code>null</code>.
   * @param start      The index where to insert the value.
   * @param value      The value that should be inserted. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T insert( T sequence, int offset, String value );

  /**
   * Inserts a String into the supplied sequence.
   * 
   * @param sequence   The sequence which has to be altered. Not <code>null</code>.
   * @param start      The index where to insert the value.
   * @param value      The value that should be inserted. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T insert( T sequence, int offset, char[] value );

  /**
   * Writes the supplied sequence into the provided builder.
   * 
   * @param sequence   The sequence that shall be written. Not <code>null</code>.
   * @param builder    The receiving builder. Not <code>null</code>.
   */
  void write( T sequence, StringBuilder builder );
  
  /**
   * Makes the first character an upper case one.
   * 
   * @param sequence   The sequence which has to be altered. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T firstUp( T sequence );
  
  /**
   * Makes the first character a lower case one.
   * 
   * @param sequence   The sequence which has to be altered. Not <code>null</code>.
   * 
   * @return   The supplied sequence if possible. Otherwise it must be a correspondingly altered copy. 
   *           Not <code>null</code>.
   */
  T firstDown( T sequence );
  
} /* ENDINTERFACE */

