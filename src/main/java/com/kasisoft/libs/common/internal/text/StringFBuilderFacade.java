package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.text.*;

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
  public int indexOf( StringFBuilder sequence, CharSequence str, int first ) {
    return sequence.indexOf( str.toString(), first );
  }

  @Override
  public int lastIndexOf( StringFBuilder sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringFBuilder sequence, CharSequence str, int first ) {
    return sequence.lastIndexOf( str.toString(), first );
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
  public StringFBuilder deleteCharAt( StringFBuilder sequence, int idx ) {
    sequence.deleteCharAt( idx );
    return sequence;
  }

  @Override
  public StringFBuilder setCharAt( StringFBuilder sequence, int idx, char ch ) {
    sequence.setCharAt( idx, ch );
    return sequence;
  }

  @Override
  public StringFBuilder delete( StringFBuilder sequence, int start, int end ) {
    sequence.delete( start, end );
    return sequence;
  }

  @Override
  public StringFBuilder insert( StringFBuilder sequence, int offset, CharSequence value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public StringFBuilder insert( StringFBuilder sequence, int offset, char[] value ) {
    sequence.insert( offset, value );
    return sequence;
  }

  @Override
  public void write( StringFBuilder sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

  @Override
  public StringFBuilder firstUp( StringFBuilder sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toUpperCase( sequence.charAt(0) ) );
    }
    return sequence;
  }

  @Override
  public StringFBuilder firstDown( StringFBuilder sequence ) {
    if( sequence.length() > 0 ) {
      sequence.setCharAt( 0, Character.toLowerCase( sequence.charAt(0) ) );
    }
    return sequence;
  }
  
} /* ENDCLASS */
