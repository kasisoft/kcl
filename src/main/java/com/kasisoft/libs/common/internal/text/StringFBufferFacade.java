package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.text.*;

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
  public int indexOf( StringFBuffer sequence, CharSequence str, int first ) {
    return sequence.indexOf( str.toString(), first );
  }

  @Override
  public int lastIndexOf( StringFBuffer sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringFBuffer sequence, CharSequence str, int first ) {
    return sequence.lastIndexOf( str.toString(), first );
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
  public StringFBuffer deleteCharAt( StringFBuffer sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }
  
  @Override
  public StringFBuffer setCharAt( StringFBuffer sequence, int idx, char ch ) {
    sequence.setCharAt( idx, ch );
    return sequence;
  }

  @Override
  public StringFBuffer delete( StringFBuffer sequence, int start, int end ) {
    sequence.delete( start, end );
    return sequence;
  }

  @Override
  public StringFBuffer insert( StringFBuffer sequence, int offset, CharSequence value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public StringFBuffer insert( StringFBuffer sequence, int offset, char[] value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public void write( StringFBuffer sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

  @Override
  public StringFBuffer firstUp( StringFBuffer sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toUpperCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

  @Override
  public StringFBuffer firstDown( StringFBuffer sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toLowerCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

} /* ENDCLASS */