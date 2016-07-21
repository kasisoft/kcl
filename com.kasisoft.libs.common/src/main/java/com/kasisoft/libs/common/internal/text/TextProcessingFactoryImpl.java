package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.internal.text.op.*;

import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.regex.*;

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
  public Function<T, T> replace( @NonNull String key, String value ) {
    return replace( key, value, true );
  }

  @Override
  public Function<T, T> replace( @NonNull String key, String value, boolean caseSensitive ) {
    return Functions.nullSafe( new KeyValueReplacer<>( facade, key, value, caseSensitive ) );
  }

  @Override
  public Function<T, T> replace( @NonNull Map<String, String> replacements ) {
    return replace( replacements, true );
  }

  @Override
  public Function<T, T> replace( @NonNull Map<String, String> replacements, boolean caseSensitive ) {
    return Functions.nullSafe( new KeyValuesReplacer<>( facade, replacements, caseSensitive ) );
  }

  @Override
  public Function<T, T> replace( @NonNull List<Pair<String, String>> replacements ) {
    return replace( replacements, true );
  }

  @Override
  public Function<T, T> replace( @NonNull List<Pair<String, String>> replacements, boolean caseSensitive ) {
    return Functions.nullSafe( new KeyValuesReplacer<>( facade, replacements, caseSensitive ) );
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

  @Override
  public Function<T, T> toLowerCase() {
    return Functions.nullSafe( new CharacterCase<>( facade, false ) );
  }

  @Override
  public Function<T, T> toUpperCase() {
    return Functions.nullSafe( new CharacterCase<>( facade, true ) );
  }

  @Override
  public Function<T, T> replaceAll( @NonNull Pattern pattern, String replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, null ) );
  }

  @Override
  public Function<T, T> replaceAll( @NonNull Pattern pattern, Function<String, String> replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, null ) );
  }

  @Override
  public Function<T, T> replaceFirst( @NonNull Pattern pattern, String replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, Boolean.TRUE ) );
  }

  @Override
  public Function<T, T> replaceFirst( @NonNull Pattern pattern, Function<String, String> replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, Boolean.TRUE ) );
  }

  @Override
  public Function<T, T> replaceLast( @NonNull Pattern pattern, String replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, Boolean.FALSE ) );
  }

  @Override
  public Function<T, T> replaceLast( @NonNull Pattern pattern, Function<String, String> replacement ) {
    return Functions.nullSafe( new RegexReplacer<>( facade, pattern, replacement, Boolean.FALSE ) );
  }
  
} /* ENDCLASS */
