package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.util.*;

/**
 * Facade for StringFBuffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
  public StringFBuffer replace( StringFBuffer sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

  @Override
  public StringFBuffer trim( StringFBuffer sequence, String chars, Boolean left ) {
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
  public StringFBuffer toLowerCase( StringFBuffer sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence.setCharAt( i, Character.toLowerCase( ch ) );
      }
    }
    return sequence;
  }

  @Override
  public StringFBuffer toUpperCase( StringFBuffer sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence.setCharAt( i, Character.toUpperCase( ch ) );
      }
    }
    return sequence;
  }

  @Override
  public StringFBuffer deleteCharAt( StringFBuffer sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }

  @Override
  public void write( StringFBuffer sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

} /* ENDCLASS */
