package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.internal.text.op.*;

import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Default implementation of a factory providing text operations. This implementation is based upon the use
 * of the {@link CharSequenceFacade} types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextProcessingFactoryImpl<T extends CharSequence> implements TextProcessingFactory<T> {
  
  CharSequenceFacade   facade;
  
  public TextProcessingFactoryImpl( @NonNull Class<T> type ) {
    facade = CharSequenceFacades.getFacade( type );
  }

  @Override
  public Function<T, T> replace( String key, String value ) {
    return Functions.nullSafe( new KeyValueReplacer<>( facade, key, value ) );
  }

  @Override
  public Function<T, T> replace( Map<String, String> replacements ) {
    return Functions.nullSafe( new KeyValueByMapReplacer<>( facade, replacements ) );
  }

  @Override
  public Function<T, T> replace( List<Pair<String, String>> replacements ) {
    return Functions.nullSafe( new KeyValueByPairsReplacer<>( facade, replacements ) );
  }

  @Override
  public Function<T, T> trim() {
    return trim( null );
  }

  @Override
  public Function<T, T> trim( String wschars ) {
    return Functions.nullSafe( new Trim<>( facade, wschars, null ) );
  }

  @Override
  public Function<T, T> trimLeft() {
    return trimLeft( null );
  }

  @Override
  public Function<T, T> trimLeft( String wschars ) {
    return Functions.nullSafe( new Trim<>( facade, wschars, Boolean.TRUE ) );
  }

  @Override
  public Function<T, T> trimRight() {
    return trimRight( null );
  }

  @Override
  public Function<T, T> trimRight( String wschars ) {
    return Functions.nullSafe( new Trim<>( facade, wschars, Boolean.FALSE ) );
  }
  
} /* ENDCLASS */
