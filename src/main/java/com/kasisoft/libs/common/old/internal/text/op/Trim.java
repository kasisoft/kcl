package com.kasisoft.libs.common.old.internal.text.op;

import com.kasisoft.libs.common.old.internal.text.CharSequenceFacade;

import java.util.function.Function;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * This function performs a trimming operation. This operation prefers an in-place operation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Trim<T extends CharSequence> implements Function<T, T> {

  Boolean                type;
  String                 chars;
  CharSequenceFacade<T>  facade;
  
  public Trim( CharSequenceFacade<T> csfacade, String wschars, Boolean left ) {
    facade = csfacade;
    chars  = wschars;
    type   = left;
    if( chars == null ) {
      chars = " \t\r\n";
    }
  }
  
  @Override
  public T apply( T input ) {
    return facade.trim( input, chars, type );
  }

} /* ENDCLASS */