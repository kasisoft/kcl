/**
 * Name........: StringFBuilderFacade
 * Description.: Facade for StringFBuilder.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

import com.kasisoft.libs.common.util.*;

/**
 * Facade for StringFBuilder.
 */
public class StringFBuilderFacade implements CharSequenceFacade<StringFBuilder> {

  @Override
  public int indexOf( StringFBuilder sequence, char ch, int first ) {
    return sequence.indexOf( String.valueOf(ch), first );
  }

  @Override
  public int indexOf( StringFBuilder sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( StringFBuilder sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringFBuilder sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( StringFBuilder sequence, String str ) {
    return sequence.indexOf( str ) != -1;
  }

  @Override
  public String substring( StringFBuilder sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( StringFBuilder sequence ) {
    return sequence.length();
  }

  @Override
  public StringFBuilder replace( StringFBuilder sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

} /* ENDCLASS */
