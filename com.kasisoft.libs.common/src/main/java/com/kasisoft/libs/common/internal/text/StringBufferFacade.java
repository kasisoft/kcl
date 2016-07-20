package com.kasisoft.libs.common.internal.text;

/**
 * Facade for StringBuffer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
  public StringBuffer replace( StringBuffer sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

  @Override
  public StringBuffer trim( StringBuffer sequence, String chars, Boolean left ) {
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
  public StringBuffer toLowerCase( StringBuffer sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence.setCharAt( i, Character.toLowerCase( ch ) );
      }
    }
    return sequence;
  }

  @Override
  public StringBuffer toUpperCase( StringBuffer sequence ) {
    for( int i = 0; i < sequence.length(); i++ ) {
      char ch = sequence.charAt(i);
      if( Character.isLetter(ch) ) {
        sequence.setCharAt( i, Character.toUpperCase( ch ) );
      }
    }
    return sequence;
  }

  @Override
  public StringBuffer deleteCharAt( StringBuffer sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }
  
  @Override
  public void write( StringBuffer sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

} /* ENDCLASS */
