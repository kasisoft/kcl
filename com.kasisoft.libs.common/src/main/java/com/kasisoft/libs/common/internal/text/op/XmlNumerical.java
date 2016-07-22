package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;

/**
 * Xml encoding/decoding for specific characters.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class XmlNumerical<T extends CharSequence> implements Function<T, T> { 

  Pattern                         pattern;
  CharSequenceFacade<T>           facade;
  Predicate<Integer>              encodingTest;
  boolean                         strict;
  boolean                         encode;

  public XmlNumerical( CharSequenceFacade<T> csfacade, Predicate<Integer> test, boolean strictDecode, boolean encoding ) {
    facade        = csfacade;
    encodingTest  = test;
    pattern       = Pattern.compile( "\\Q&#\\E(\\d+)\\Q;\\E" );
    strict        = strictDecode;
    encode        = encoding;
    if( encodingTest == null ) {
      encodingTest = this::defaultEncodable;
    }
  }
    
  @Override
  public T apply( T input ) {
    if( encode ) {
      return applyEncoding( input );
    } else {
      return applyDecoding( input, getMatches( input ) );
    }
  }
  
  private T applyEncoding( T input ) {
    T result = input;
    for( int i = input.length() - 1; i >= 0; i-- ) {
      int ch = (int) input.charAt(i);
      if( encodingTest.test( ch ) ) {
        result = facade.deleteCharAt( result, i );
        result = facade.insert( result, i, String.format( "&#%d;", ch ) );
      }
    }
    return result;
  }
  
  private List<int[]> getMatches( T input ) {
    Matcher     matcher = pattern.matcher( input );
    List<int[]> result  = new ArrayList<>();
    while( matcher.find() ) {
      result.add( new int[] { matcher.start(), matcher.end() } );
    }
    return result;
  }
  
  private T applyDecoding( T input, List<int[]> matches ) {
    T result = input;
    for( int i = matches.size() - 1; i >= 0; i-- ) {
      int    start = matches.get(i)[0];
      int    end   = matches.get(i)[1];
      int    num   = Integer.parseInt( facade.substring( result, start + 2, end - 1 ) );
      if( strict && (!encodingTest.test( num )) ) {
        continue;
      }
      char[] val   = Character.toChars( num );
      result       = facade.delete( result, start, end );
      result       = facade.insert( result, start, val );
    }
    return result;
  }
  
  private boolean defaultEncodable( Integer ch ) {
    return ch.intValue() > 127;
  }
  
} /* ENDCLASS */
