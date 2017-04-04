package com.kasisoft.libs.common.internal.text.op;

import com.kasisoft.libs.common.internal.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * This function performs a change for the character cases.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharacterCase<T extends CharSequence> implements Function<T, T> {

  Function<T, T>         impl;
  CharSequenceFacade<T>  facade;
  
  public CharacterCase( CharSequenceFacade<T> csfacade, boolean upper ) {
    facade = csfacade;
    impl   = upper ? this::applyUpperCase : this::applyLowerCase;
  }
  
  @Override
  public T apply( T input ) {
    return impl.apply( input );
  }
  
  private T applyLowerCase( T input ) {
    return facade.toLowerCase( input );
  }

  private T applyUpperCase( T input ) {
    return facade.toUpperCase( input );
  }

} /* ENDCLASS */
