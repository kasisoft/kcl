package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import com.kasisoft.libs.common.constants.*;

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
public class KeyValueByMapReplacer<T extends CharSequence> implements Function<T, T> {

  int                     minLength;
  Map<String, String>     pairs;
  CharSequenceFacade<T>   facade;
  
  public KeyValueByMapReplacer( CharSequenceFacade<T> csfacade, Map<String, String> replacements ) {
    facade    = csfacade;
    minLength = Integer.MAX_VALUE;
    pairs     = new HashMap<>( replacements.size() );
    for( Map.Entry<String, String> entry : replacements.entrySet() ) {
      String key = entry.getKey();
      String val = entry.getValue();
      if( val == null ) {
        val = Empty.NO_STRING;
      }
      minLength  = Math.min( minLength, key.length() );
      pairs.put( key, val );
    }
  }
  
  @Override
  public T apply( T input ) {
    T   result = input;
    int length = input.length();
    if( length >= minLength ) {
      for( Map.Entry<String, String> entry : pairs.entrySet() ) {
        String key    = entry.getKey();
        int    keyLen = key.length();
        if( length >= keyLen ) {
          String value  = entry.getValue();
          int    idx    = facade.indexOf( result, key, 0 );
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
