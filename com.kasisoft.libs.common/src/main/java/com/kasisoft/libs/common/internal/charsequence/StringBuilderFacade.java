/**
 * Name........: StringBuilderFacade
 * Description.: Facade for StringBuilder.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

/**
 * Facade for StringBuilder.
 */
public class StringBuilderFacade implements CharSequenceFacade<StringBuilder> {

  @Override
  public int indexOf( StringBuilder sequence, char ch, int first ) {
    return sequence.indexOf( String.valueOf(ch), first );
  }

  @Override
  public int indexOf( StringBuilder sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( StringBuilder sequence, String str ) {
    return sequence.indexOf( str ) != -1;
  }

  @Override
  public String substring( StringBuilder sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( StringBuilder sequence ) {
    return sequence.length();
  }

  @Override
  public StringBuilder replace( StringBuilder sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

} /* ENDCLASS */