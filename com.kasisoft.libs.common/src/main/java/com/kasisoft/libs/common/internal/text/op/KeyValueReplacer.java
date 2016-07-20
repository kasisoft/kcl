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

  String                              key;
  String                              value;
  int                                 keyLen;
  int                                 valueLen;
  CharSequenceFacade<T>               facade;
  Function<T, T>                      impl;
  CharSequenceFacade<StringBuilder>   sbFacade;
  StringBuilder                       representer;
  
  public KeyValueReplacer( CharSequenceFacade<T> csfacade, String search, String replacement ) {
    this( csfacade, search, replacement, true );
  }
  
  public KeyValueReplacer( CharSequenceFacade<T> csfacade, String search, String replacement, boolean caseSensitive ) {
    facade   = csfacade;
    key      = search;
    value    = replacement;
    keyLen   = key.length();
    valueLen = value.length();
    impl     = this::applyCaseSensitive;
    if( ! caseSensitive ) {
      representer = new StringBuilder();
      impl        = this::applyCaseInsensitive;
      key         = key.toLowerCase();
      sbFacade    = CharSequenceFacades.getFacade( StringBuilder.class );
    }
  }
  
  @Override
  public T apply( T input ) {
    return impl.apply( input );
  }

  private T applyCaseSensitive( T input ) {
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

  private T applyCaseInsensitive( T input ) {
    T   result = input;
    int length = input.length();
    if( length >= keyLen ) {
      representer.setLength(0);
      facade.write( result, representer );
      sbFacade.toLowerCase( representer );
      int idx = representer.indexOf( key, 0 );
      while( idx != -1 ) {
        result = facade.replace( result, idx, idx + keyLen, value );
        representer.replace( idx, idx + keyLen, value );
        idx    = representer.indexOf( key, idx + valueLen );
      }
    }
    return result;
  }

} /* ENDCLASS */
