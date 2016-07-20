package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.internal.text.*;

import com.kasisoft.libs.common.model.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

/**
 * An decoded/encoder for texts.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextCodec<T extends CharSequence> {
  
  List<Function<T, T>>   encoding;
  List<Function<T, T>>   decoding;
  
  public TextCodec() {
    encoding = new ArrayList<>(2);
    decoding = new ArrayList<>(2);
  }
  
  public Function<T, T> getEncoder() {
    return this::encode;
  }

  public Function<T, T> getDecoder() {
    return this::decode;
  }

  private synchronized T encode( T input ) {
    return execute( input, encoding );
  }
  
  private synchronized T decode( T input ) {
    return execute( input, decoding );
  }

  private T execute( T input, List<Function<T, T>> steps ) {
    T result = input;
    for( Function<T, T> step : steps ) {
      result = step.apply( result );
      if( result == null ) {
        break;
      }
    }
    return result;
  }
  
  public static <R extends CharSequence> TextCodecBuilder<R> builder( @NonNull Class<R> type ) {
    return new TextCodecBuilder<>( type );
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class TextCodecBuilder<R extends CharSequence> {
    
    TextCodec<R>                 instance;
    TextProcessingFactory<R>     factory;
    List<Pair<String, String>>   replacements;
    
    private TextCodecBuilder( Class<R> type ) {
      factory       = CharSequenceFacades.getTextProcessingFactory( type );
      replacements  = new ArrayList<>(2);
      instance      = new TextCodec<>();
    }
    
    public TextCodecBuilder<R> replacement( @NonNull String key, @NonNull String value ) {
      replacements.add( new Pair<>( key, value ) );
      return this;
    }
    
    public TextCodec<R> build() {
      List<Pair<String, String>> reverted = replacements.stream().map( $ -> new Pair<>( $.getValue2(), $.getValue1() ) ).collect( Collectors.toList() );
      instance.encoding.add( factory.replace( replacements ) );
      instance.decoding.add( factory.replace( reverted     ) );
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
