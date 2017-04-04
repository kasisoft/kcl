package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * This function performs a camelcase transformation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CamelCase<T extends CharSequence> implements Function<T, T> {

  CharSequenceFacade<T>   facade;
  
  public CamelCase( CharSequenceFacade<T> csfacade ) {
    facade = csfacade;
  }
  
  @Override
  public T apply( T input ) {
    return facade.camelCase( input );
  }
  
} /* ENDCLASS */
