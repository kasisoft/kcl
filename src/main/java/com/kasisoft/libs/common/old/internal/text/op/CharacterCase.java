package com.kasisoft.libs.common.old.internal.text.op;

import com.kasisoft.libs.common.old.internal.text.CharSequenceFacade;

import java.util.function.Function;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
