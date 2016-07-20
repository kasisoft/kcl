package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * This function simple replaces a key by a corresponding value. This operation prefers an in-place operation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyValueReplacer<T extends CharSequence> implements Function<T, T> {

  String                 key;
  String                 value;
  int                    keyLen;
  int                    valueLen;
  CharSequenceFacade<T>  facade;
  
  public KeyValueReplacer( CharSequenceFacade<T> csfacade, String search, String replacement ) {
    facade   = csfacade;
    key      = search;
    value    = replacement;
    keyLen   = key.length();
    valueLen = value.length();
  }
  
  @Override
  public T apply( T input ) {
    T   result = input;
    int length = input.length();
    if( length >= keyLen ) {
      int idx    = facade.indexOf( result, key, 0 );
      while( idx != -1 ) {
        result = facade.replace( result, idx, idx + keyLen, value );
        idx    = facade.indexOf( result, key, idx + valueLen );
      }
    }
    return result;
  }

} /* ENDCLASS */
