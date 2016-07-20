package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

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
    chars  = StringFunctions.cleanup( wschars );
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
