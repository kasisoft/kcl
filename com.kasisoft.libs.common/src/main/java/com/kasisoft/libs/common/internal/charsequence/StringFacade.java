/**
 * Name........: StringFacade
 * Description.: Facade for String.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

/**
 * Facade for String.
 */
public class StringFacade implements CharSequenceFacade<String> {

  @Override
  public int indexOf( String sequence, char ch, int first ) {
    return sequence.indexOf( ch, first );
  }

  @Override
  public int indexOf( String sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( String sequence, char ch, int first ) {
    return sequence.lastIndexOf( ch, first );
  }

  @Override
  public int lastIndexOf( String sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( String sequence, String str ) {
    return sequence.contains( str );
  }

  @Override
  public String substring( String sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( String sequence ) {
    return sequence.length();
  }

  @Override
  public String replace( String sequence, int offset, int end, String replacement ) {
    StringBuilder builder = new StringBuilder( sequence );
    builder.replace( offset, end, replacement );
    return builder.toString();
  }

} /* ENDCLASS */
