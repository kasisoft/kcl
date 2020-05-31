package com.kasisoft.libs.common.old.text;

import com.kasisoft.libs.common.old.internal.text.CharSequenceFacades;
import com.kasisoft.libs.common.old.model.Pair;

import java.util.function.Function;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * An decoded/encoder for texts.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextCodec<T extends CharSequence> {
  
  List<Function<T, T>>   encoding;
  List<Function<T, T>>   decoding;
  
  private TextCodec() {
    encoding = new ArrayList<>(2);
    decoding = new ArrayList<>(2);
  }
  
  /**
   * Returns a function that allows to encode the text according to the current configuration.
   * 
   * @return   The function that provides the encoding capability. Not <code>null</code>.
   */
  public Function<T, T> getEncoder() {
    return this::encode;
  }

  /**
   * Returns a function that allows to decode the text according to the current configuration.
   * 
   * @return   The function that provides the decoding capability. Not <code>null</code>.
   */
  public Function<T, T> getDecoder() {
    return this::decode;
  }

  /**
   * Executes the encoding process.
   * 
   * @param input   The input to be processed. Maybe <code>null</code>.
   * 
   * @return   The encoded value. Maybe <code>null</code>.
   */
  private T encode( T input ) {
    return execute( input, encoding );
  }
  
  /**
   * Executes the decoding process.
   * 
   * @param input   The input to be processed. Maybe <code>null</code>.
   * 
   * @return   The decoded value. Maybe <code>null</code>.
   */
  private T decode( T input ) {
    return execute( input, decoding );
  }

  /**
   * Actual execution of the encoding/decoding process.
   * 
   * @param input   The input to be processed. Maybe <code>null</code>.
   * @param steps   A list of transformation steps. Not <code>null</code>.
   * 
   * @return   The transformed value. Maybe <code>null</code>.
   */
  private T execute( T input, List<Function<T, T>> steps ) {
    T result = input;
    if( result != null ) {
      for( Function<T, T> step : steps ) {
        result = step.apply( result );
        if( result == null ) {
          break;
        }
      }
    }
    return result;
  }
  
  /**
   * Returns a builder for a certain type.
   * 
   * @param type   The {@link CharSequence} type used to get the builder for.
   * 
   * @return   The builder allowing to configure the {@link TextCodec} instance. Not <code>null</code>.
   */
  public static <R extends CharSequence> TextCodecBuilder<R> builder( @NonNull Class<R> type ) {
    return new TextCodecBuilder<>( type );
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class TextCodecBuilder<R extends CharSequence> {
    
    TextCodec<R>                 instance;
    TextProcessingFactory<R>     factory;
    List<Pair<String, String>>   replacements;
    List<Function<R, R>>         encodings;
    List<Function<R, R>>         decodings;
    
    private TextCodecBuilder( Class<R> type ) {
      factory       = CharSequenceFacades.getTextProcessingFactory( type );
      replacements  = new ArrayList<>(2);
      encodings     = new ArrayList<>(2);
      decodings     = new ArrayList<>(2);
      instance      = new TextCodec<>();
    }
    
    /**
     * Registers a certain encoding/decoding function that will be definitely executed.
     *  
     * @param encoding   The encoding function. Not <code>null</code>.
     * @param decoding   The decoding function. Not <code>null</code>.
     * 
     * @return   this
     */
    public TextCodecBuilder<R> defaultCodec( @NonNull Function<R, R> encoding, @NonNull Function<R, R> decoding ) {
      encodings.add( encoding );
      decodings.add( decoding );
      return this;
    }
    
    /**
     * Registers a replacment.
     * 
     * @param key     The key of the replacement. Neither <code>null</code> not empty.
     * @param value   The value to be used for the replacement. Not <code>null</code>.
     * 
     * @return   this
     */
    public TextCodecBuilder<R> replacement( @NonNull String key, @NonNull String value ) {
      replacements.add( new Pair<>( key, value ) );
      return this;
    }

    /**
     * Registers many replacment.
     * 
     * @param pairs   The pairs providing the replacement operations. Not <code>null</code>.
     * 
     * @return   this
     */
    public TextCodecBuilder<R> replacements( @NonNull Map<String, String> pairs ) {
      for( Map.Entry<String, String> entry : pairs.entrySet() ) {
        replacements.add( new Pair<>( entry.getKey(), entry.getValue() ) );
      }
      return this;
    }

    /**
     * Registers many replacment.
     * 
     * @param pairs   The pairs providing the replacement operations. Not <code>null</code>.
     * 
     * @return   this
     */
    public TextCodecBuilder<R> replacements( @NonNull List<Pair<String, String>> pairs ) {
      replacements.addAll( pairs );
      return this;
    }

    public TextCodec<R> build() {
      List<Pair<String, String>> reverted = replacements.stream().map( $ -> new Pair<>( $.getValue2(), $.getValue1() ) ).collect( Collectors.toList() );
      instance.encoding.addAll( encodings );
      instance.decoding.addAll( decodings );
      instance.encoding.add( factory.replace( replacements ) );
      instance.decoding.add( factory.replace( reverted     ) );
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
