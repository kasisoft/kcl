package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.util.*;

/**
 * Facade for StringFBuilder.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
  public StringFBuilder replace( StringFBuilder sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

  @Override
  public StringFBuilder trim( StringFBuilder sequence, String chars, Boolean left ) {
    if( (left == null) || left.booleanValue() ) {
      while( (sequence.length() > 0) && (chars.indexOf( sequence.charAt(0) ) != -1) ) {
        sequence.deleteCharAt(0);
      }
    }
    if( (left == null) || (! left.booleanValue()) ) {
      while( (sequence.length() > 0) && (chars.indexOf( sequence.charAt( sequence.length() - 1 ) ) != -1) ) {
        sequence.deleteCharAt( sequence.length() - 1 );
      }
    }
    return sequence;
  }
  
  @Override
  public void write( StringFBuilder sequence, StringFBuilder builder ) {
    builder.append( sequence );
  }

} /* ENDCLASS */
