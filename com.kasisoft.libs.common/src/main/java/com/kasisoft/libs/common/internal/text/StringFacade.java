package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.constants.*;

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

  @Override
  public String toLowerCase( String sequence ) {
    return sequence.toLowerCase();
  }

  @Override
  public String toUpperCase( String sequence ) {
    return sequence.toUpperCase();
  }

  @Override
  public String deleteCharAt( String sequence, int idx ) {
    String result = null;
    if( idx == 0 ) {
      result = sequence.length() > 1 ? sequence.substring(1) : Empty.NO_STRING;
    } else if( idx == sequence.length() - 1 ) {
      result = sequence.substring( 0, sequence.length() - 1 );
    } else {
      result = sequence.substring( 0, idx ) + sequence.substring( idx + 1 );
    }
    return result;
  }
  
  @Override
  public void write( String sequence, StringBuilder builder ) {
    builder.append( sequence );
  }

} /* ENDCLASS */
