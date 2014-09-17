package com.kasisoft.libs.common.internal.charsequence;

/**
 * Facade for String.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringFacade implements CharSequenceFacade<String> {

  @Override
  public int indexOf( String sequence, char ch, int first ) {
    return sequence.indexOf( ch, first );
  }

  @Override
  public int indexOf( String sequence, String str, int first ) {
    return sequence.indexOf( str, first );
  }

  @Override
  public int lastIndexOf( String sequence, char ch, int first ) {
    return sequence.lastIndexOf( ch, first );
  }

  @Override
  public int lastIndexOf( String sequence, String str, int first ) {
    return sequence.lastIndexOf( str, first );
  }

  @Override
  public boolean contains( String sequence, String str ) {
    return sequence.contains( str );
  }

  @Override
  public String substring( String sequence, int offset, int end ) {
    return sequence.substring( offset, end );
  }

  @Override
  public int length( String sequence ) {
    return sequence.length();
  }

  @Override
  public String replace( String sequence, int offset, int end, String replacement ) {
    StringBuilder builder = new StringBuilder( sequence );
    builder.replace( offset, end, replacement );
    return builder.toString();
  }

  @Override
  public String trim( String sequence, String chars, Boolean left ) {
    StringBuilder builder = new StringBuilder( sequence );
    if( (left == null) || left.booleanValue() ) {
      while( (builder.length() > 0) && (chars.indexOf( builder.charAt(0) ) != -1) ) {
        builder.deleteCharAt(0);
      }
    }
    if( (left == null) || (! left.booleanValue()) ) {
      while( (builder.length() > 0) && (chars.indexOf( builder.charAt( builder.length() - 1 ) ) != -1) ) {
        builder.deleteCharAt( builder.length() - 1 );
      }
    }
    return builder.toString();
  }

} /* ENDCLASS */
