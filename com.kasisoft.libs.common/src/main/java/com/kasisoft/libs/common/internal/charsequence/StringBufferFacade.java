/**
 * Name........: StringBufferFacade
 * Description.: Facade for StringBuffer.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

/**
 * Facade for StringBuffer.
 */
public class StringBufferFacade implements CharSequenceFacade<StringBuffer> {

  @Override
  public int indexOf( StringBuffer sequence, char ch, int first ) {
    return sequence.indexOf( String.valueOf(ch), first );
  }

  @Override
  public int indexOf( StringBuffer sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( StringBuffer sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringBuffer sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( StringBuffer sequence, String str ) {
    return sequence.indexOf( str ) != -1;
  }

  @Override
  public String substring( StringBuffer sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( StringBuffer sequence ) {
    return sequence.length();
  }

  @Override
  public StringBuffer replace( StringBuffer sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

} /* ENDCLASS */
