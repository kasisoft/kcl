/**
 * Name........: CharSequenceFacade
 * Description.: A simple facade which is used to handle commonly used CharSequence types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

/**
 * A simple facade which is used to handle commonly used CharSequence types.
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
   * Returns the length of the supplied sequence.
   * 
   * @param sequence   The object which has to be investigated. Not <code>null</code>.
   * 
   * @return   The length of the supplied sequenc.e
   */
  int length( T sequence );
  
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
  T trim( T sequence, String chars, Boolean left );
  
} /* ENDINTERFACE */

