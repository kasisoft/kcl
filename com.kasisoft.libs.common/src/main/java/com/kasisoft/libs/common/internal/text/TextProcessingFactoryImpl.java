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
  
  static final Map<String, String> XML2NORMAL = new HashMap<>();
  static final Map<String, String> NORMAL2XML = new HashMap<>();

  static final Map<String, String> XML2NORMAL_LE = new HashMap<>();
  static final Map<String, String> NORMAL2XML_LE = new HashMap<>();

  static {
    
    XML2NORMAL.put( "&apos;" , "'"  );
    XML2NORMAL.put( "&gt;"   , ">"  );
    XML2NORMAL.put( "&lt;"   , "<"  );
    XML2NORMAL.put( "&amp;"  , "&"  );
    XML2NORMAL.put( "&quot;" , "\"" );
    
    XML2NORMAL_LE.putAll( XML2NORMAL );
    XML2NORMAL_LE.put( "\\n"    , "\n" );
    XML2NORMAL_LE.put( "\\r"    , "\r" );
    XML2NORMAL_LE.put( "&#10;"  , "\n" );
    XML2NORMAL_LE.put( "&#13;"  , "\r" );
    
    
    NORMAL2XML.put( "'"  , "&apos;" );
    NORMAL2XML.put( ">"  , "&gt;"   );
    NORMAL2XML.put( "<"  , "&lt;"   );
    NORMAL2XML.put( "&"  , "&amp;"  );
    NORMAL2XML.put( "\"" , "&quot;" );
    
    NORMAL2XML_LE.putAll( NORMAL2XML );
    NORMAL2XML_LE.put( "\n" , "\\n"    );
    NORMAL2XML_LE.put( "\r" , "\\r"    );
    NORMAL2XML_LE.put( "\n" , "&#10;"  );
    NORMAL2XML_LE.put( "\r" , "&#13;"  );
  
  }
  
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

  @Override
  public Function<T, T> xmlNumericalEncoder() {
    return xmlNumericalEncoder( null );
  }

  @Override
  public Function<T, T> xmlNumericalEncoder( Predicate<Integer> charTest ) {
    return Functions.nullSafe( new XmlNumerical<>( facade, charTest, false, true ) );
  }

  @Override
  public Function<T, T> xmlNumericalDecoder() {
    return xmlNumericalDecoder( null, false );
  }

  @Override
  public Function<T, T> xmlNumericalDecoder( Predicate<Integer> charTest ) {
    return xmlNumericalDecoder( charTest, false );
  }

  @Override
  public Function<T, T> xmlNumericalDecoder( boolean strict ) {
    return xmlNumericalDecoder( null, strict );
  }

  @Override
  public Function<T, T> xmlNumericalDecoder( Predicate<Integer> charTest, boolean strict ) {
    return Functions.nullSafe( new XmlNumerical<>( facade, charTest, strict, false ) );
  }

  @Override
  public Function<T, T> xmlEncoder() {
    return xmlEncoder( false );
  }

  @Override
  public Function<T, T> xmlEncoder( boolean lineEndings ) {
    return Functions.nullSafe( new KeyValuesReplacer<>( facade, lineEndings ? NORMAL2XML_LE : NORMAL2XML, false ) );
  }

  @Override
  public Function<T, T> xmlDecoder() {
    return xmlDecoder( false );
  }

  @Override
  public Function<T, T> xmlDecoder( boolean lineEndings ) {
    return Functions.nullSafe( new KeyValuesReplacer<>( facade, lineEndings ? XML2NORMAL_LE : XML2NORMAL, false ) );
  }
  
} /* ENDCLASS */
