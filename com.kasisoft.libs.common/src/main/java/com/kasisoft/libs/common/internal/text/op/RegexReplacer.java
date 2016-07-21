package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;

/**
 * This function simple replaces a key by a corresponding value. This operation prefers an in-place operation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegexReplacer<T extends CharSequence> implements Function<T, T> {

  Boolean                         which;
  String                          value;
  Function<String, String>        valueProvider;
  Pattern                         pattern;
  CharSequenceFacade<T>           facade;
  BiFunction<T, List<int[]>, T>   impl;

  private RegexReplacer( CharSequenceFacade<T> csfacade, Pattern regex, Boolean type ) {
    facade        = csfacade;
    pattern       = regex;
    which         = type;
    value         = null;
    valueProvider = null;
  }
  
  public RegexReplacer( CharSequenceFacade<T> csfacade, Pattern regex, Function<String, String> values, Boolean first ) {
    this( csfacade, regex, first );
    valueProvider = values;
    impl          = this::applyProvidedReplacement;
  }
    
  public RegexReplacer( CharSequenceFacade<T> csfacade, Pattern regex, String replacement, Boolean first ) {
    this( csfacade, regex, first );
    value = replacement;
    impl  = this::applySimpleReplacement;
  }
  
  private List<int[]> getMatches( T input ) {
    Matcher     matcher = pattern.matcher( input );
    List<int[]> result  = new ArrayList<>();
    while( matcher.find() ) {
      result.add( new int[] { matcher.start(), matcher.end() } );
    }
    if( (which != null) && (result.size() > 1) ) {
      if( which.booleanValue() ) {
        result = result.subList( 0, 1 );
      } else {
        result = result.subList( result.size() - 1, result.size() );
      }
    }
    return result;
  }
  
  @Override
  public T apply( T input ) {
    return impl.apply( input, getMatches( input ) );
  }
  
  private T applySimpleReplacement( T input, List<int[]> matches ) {
    T result = input;
    for( int i = matches.size() - 1; i >= 0; i-- ) {
      int    start = matches.get(i)[0];
      int    end   = matches.get(i)[1];
      result       = facade.delete( result, start, end   );
      result       = facade.insert( result, start, value );
    }
    return result;
  }

  private T applyProvidedReplacement( T input, List<int[]> matches ) {
    T result = input;
    for( int i = matches.size() - 1; i >= 0; i-- ) {
      int    start = matches.get(i)[0];
      int    end   = matches.get(i)[1];
      String key   = facade.substring( result, start, end );
      String val   = valueProvider.apply( key );
      result       = facade.delete( result, start, end );
      if( val != null ) {
        result = facade.insert( result, start, val );
      }
    }
    return result;
  }

} /* ENDCLASS */
