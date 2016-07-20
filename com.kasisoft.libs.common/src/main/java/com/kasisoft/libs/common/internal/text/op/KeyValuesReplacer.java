package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.model.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * This function simple replaces multiple key-value pairs. This operation prefers an in-place operation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyValuesReplacer<T extends CharSequence> implements Function<T, T> {

  int                          minLength;
  List<Pair<String, String>>   pairs;
  CharSequenceFacade<T>        facade;
  
  private KeyValuesReplacer( CharSequenceFacade<T> csfacade ) {
    facade    = csfacade;
    minLength = Integer.MAX_VALUE;
  }
  
  public KeyValuesReplacer( CharSequenceFacade<T> csfacade, Map<String, String> replacements ) {
    this( csfacade );
    pairs     = new ArrayList<>( replacements.size() );
    for( Map.Entry<String, String> entry : replacements.entrySet() ) {
      String key = entry.getKey();
      String val = entry.getValue();
      if( val == null ) {
        val = Empty.NO_STRING;
      }
      minLength  = Math.min( minLength, key.length() );
      pairs.add( new Pair<>( key, val ) );
    }
  }
  
  public KeyValuesReplacer( CharSequenceFacade<T> csfacade, List<Pair<String, String>> replacements ) {
    this( csfacade );
    pairs     = new ArrayList<>( replacements.size() );
    for( Pair<String, String> entry : replacements ) {
      String key = entry.getKey();
      String val = entry.getValue();
      if( val == null ) {
        val = Empty.NO_STRING;
      }
      minLength  = Math.min( minLength, key.length() );
      pairs.add( new Pair<>( key, val ) );
    }
  }
  
  @Override
  public T apply( T input ) {
    T   result = input;
    int length = input.length();
    if( length >= minLength ) {
      for( Pair<String, String> entry : pairs ) {
        String key    = entry.getKey();
        int    keyLen = key.length();
        if( length >= keyLen ) {
          String value = entry.getValue();
          int    idx   = facade.indexOf( result, key, 0 );
          while( idx != -1 ) {
            result = facade.replace( result, idx, idx + keyLen, value );
            idx    = facade.indexOf( result, key, idx + value.length() );
          }
        }
      }
    }
    return result;
  }

} /* ENDCLASS */
