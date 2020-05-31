package com.kasisoft.libs.common.old.internal.text.op;

import com.kasisoft.libs.common.old.constants.Empty;
import com.kasisoft.libs.common.old.internal.text.CharSequenceFacade;
import com.kasisoft.libs.common.old.internal.text.CharSequenceFacades;
import com.kasisoft.libs.common.old.model.Pair;

import java.util.function.BiFunction;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * This function simple replaces multiple key-value pairs. This operation prefers an in-place operation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyValuesReplacer<T extends CharSequence> implements Function<T, T> {

  int                                      minLength;
  List<Pair<String, String>>               pairs;
  CharSequenceFacade<T>                    facade;
  CharSequenceFacade<StringBuilder>        sbFacade;
  StringBuilder                            representer;
  BiFunction<T, Pair<String, String>, T>   impl;
  
  private KeyValuesReplacer( CharSequenceFacade<T> csfacade ) {
    facade    = csfacade;
    minLength = Integer.MAX_VALUE;
    impl      = this::applyCaseSensitive;
  }
  
  public KeyValuesReplacer( CharSequenceFacade<T> csfacade, Map<String, String> replacements, boolean caseSensitive ) {
    this( csfacade );
    pairs = new ArrayList<>( replacements.size() );
    replacements.entrySet().forEach( this::addPair );
    init( caseSensitive );
  }
  
  public KeyValuesReplacer( CharSequenceFacade<T> csfacade, List<Pair<String, String>> replacements, boolean caseSensitive ) {
    this( csfacade );
    pairs = new ArrayList<>( replacements.size() );
    replacements.forEach( this::addPair );
    init( caseSensitive );
  }
  
  private void init( boolean caseSensitive ) {
    if( ! caseSensitive ) {
      pairs.forEach( $ -> $.setValue1( $.getValue1().toLowerCase() ) );
      representer = new StringBuilder();
      sbFacade    = CharSequenceFacades.getFacade( StringBuilder.class );
      impl        = this::applyCaseInsensitive;
    }
  }
  
  private void addPair( Map.Entry<String, String> pair ) {
    String key = pair.getKey();
    String val = pair.getValue();
    if( val == null ) {
      val = Empty.NO_STRING;
    }
    minLength  = Math.min( minLength, key.length() );
    pairs.add( new Pair<>( key, val ) );
  }
  
  @Override
  public T apply( T input ) {
    T   result = input;
    if( input.length() >= minLength ) {
      for( Pair<String, String> entry : pairs ) {
        result = impl.apply( result, entry );
      }
    }
    return result;
  }

  private T applyCaseSensitive( T result, Pair<String, String> entry ) {
    String key    = entry.getKey();
    int    keyLen = key.length();
    if( result.length() >= keyLen ) {
      String value = entry.getValue();
      int    idx   = facade.indexOf( result, key, 0 );
      while( idx != -1 ) {
        result = facade.replace( result, idx, idx + keyLen, value );
        idx    = facade.indexOf( result, key, idx + value.length() );
      }
    }
    return result;
  }

  private T applyCaseInsensitive( T result, Pair<String, String> entry ) {
    String key    = entry.getKey();
    int    keyLen = key.length();
    if( result.length() >= keyLen ) {
      representer.setLength(0);
      facade.write( result, representer );
      sbFacade.toLowerCase( representer );
      String value = entry.getValue();
      int    idx   = representer.indexOf( key, 0 );
      while( idx != -1 ) {
        result = facade.replace( result, idx, idx + keyLen, value );
        representer.replace( idx, idx + keyLen, value );
        idx    = representer.indexOf( key, idx + value.length() );
      }
    }
    return result;
  }

} /* ENDCLASS */
