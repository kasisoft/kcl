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
  public int indexOf( StringBuffer sequence, CharSequence str, int first ) {
    return sequence.indexOf( str.toString(), first );
  }

  @Override
  public int lastIndexOf( StringBuffer sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringBuffer sequence, CharSequence str, int first ) {
    return sequence.lastIndexOf( str.toString(), first );
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
  public StringBuffer deleteCharAt( StringBuffer sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }
  
  @Override
  public StringBuffer setCharAt( StringBuffer sequence, int idx, char ch ) {
    sequence.setCharAt( idx, ch );
    return sequence;
  }
  
  @Override
  public StringBuffer delete( StringBuffer sequence, int start, int end ) {
    sequence.delete( start, end );
    return sequence;
  }

  @Override
  public StringBuffer insert( StringBuffer sequence, int offset, CharSequence value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public StringBuffer insert( StringBuffer sequence, int offset, char[] value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public void write( StringBuffer sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

  @Override
  public StringBuffer firstUp( StringBuffer sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toUpperCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

  @Override
  public StringBuffer firstDown( StringBuffer sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toLowerCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

} /* ENDCLASS */
