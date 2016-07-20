package com.kasisoft.libs.common.internal.text;

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
  public int indexOf( StringBuilder sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, char ch, int first ) {
    return sequence.lastIndexOf( String.valueOf(ch), first );
  }

  @Override
  public int lastIndexOf( StringBuilder sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
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
  public void write( StringBuilder sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

} /* ENDCLASS */
