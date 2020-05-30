package com.kasisoft.libs.common.old.internal.text;

/**
 * Facade for StringBuilder.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringBuilderFacade implements CharSequenceFacade<StringBuilder> {

  @Override
  public int indexOf( StringBuilder sequence, char ch, int first ) {
    return sequence.indexOf( String.valueOf(ch), first );
  }

  @Override
  public int indexOf( StringBuilder sequence, CharSequence str, int first ) {
    return sequence.indexOf( str.toString(), first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, CharSequence str, int first ) {
    return sequence.lastIndexOf( str.toString(), first );
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
  public StringBuilder replace( StringBuilder sequence, int offset, int end, String replacement ) {
    sequence.replace( offset, end, replacement );
    return sequence;
  }

  @Override
  public StringBuilder deleteCharAt( StringBuilder sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }
  
  @Override
  public StringBuilder setCharAt( StringBuilder sequence, int idx, char ch ) {
    sequence.setCharAt( idx, ch );
    return sequence;
  }

  @Override
  public StringBuilder delete( StringBuilder sequence, int start, int end ) {
    sequence.delete( start, end );
    return sequence;
  }

  @Override
  public StringBuilder insert( StringBuilder sequence, int offset, CharSequence value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public StringBuilder insert( StringBuilder sequence, int offset, char[] value ) {
    sequence.insert( offset, value );
    return sequence;
  }
  
  @Override
  public void write( StringBuilder sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

  @Override
  public StringBuilder firstUp( StringBuilder sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toUpperCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

  @Override
  public StringBuilder firstDown( StringBuilder sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toLowerCase( sequence.charAt(0) ) );
    }
    return sequence;
  }
  
} /* ENDCLASS */
