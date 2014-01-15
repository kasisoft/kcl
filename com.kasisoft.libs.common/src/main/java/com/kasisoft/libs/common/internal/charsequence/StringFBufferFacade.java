/**
 * Name........: StringFBufferFacade
 * Description.: Facade for StringFBuffer.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.internal.charsequence;

import com.kasisoft.libs.common.util.*;

/**
 * Facade for StringFBuffer.
 */
public class StringFBufferFacade implements CharSequenceFacade<StringFBuffer> {

  @Override
  public int indexOf( StringFBuffer sequence, char ch, int first ) {
    return sequence.indexOf( String.valueOf(ch), first );
  }

  @Override
  public int indexOf( StringFBuffer sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( StringFBuffer sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringFBuffer sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( StringFBuffer sequence, String str ) {
    return sequence.indexOf( str ) != -1;
  }
  
  @Override
  public String substring( StringFBuffer sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( StringFBuffer sequence ) {
    return sequence.length();
  }

  @Override
  public StringFBuffer replace( StringFBuffer sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

} /* ENDCLASS */
