package com.kasisoft.libs.common.old.internal.text.op;

import com.kasisoft.libs.common.old.internal.text.CharSequenceFacade;

import java.util.function.Function;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
