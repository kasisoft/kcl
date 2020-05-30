package com.kasisoft.libs.common.old.io;

import com.kasisoft.libs.common.old.constants.*;
import com.kasisoft.libs.common.old.function.*;
import com.kasisoft.libs.common.old.internal.io.*;

import java.util.function.*;

import java.util.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KReader<T> {
  
  <R> Optional<R> forReader( @NonNull T input, Function<ExtReader, R> function );

  <C1, R> Optional<R> forReader( @NonNull T input, C1 context1, BiFunction<ExtReader, C1, R> function );
  
  <C1, C2, R> Optional<R> forReader( @NonNull T input, C1 context1, C2 context2, TriFunction<ExtReader, C1, C2, R> function );
  
  <R> boolean forReaderDo( @NonNull T input, Consumer<ExtReader> consumer );

  <C1, R> boolean forReaderDo( @NonNull T input, C1 context1, BiConsumer<ExtReader, C1> consumer );
  
  <C1, C2> boolean forReaderDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<ExtReader, C1, C2> function );

  Optional<ExtReader> open( @NonNull T input );
  
  Optional<char[]> readAll( @NonNull T input );
  
  Optional<String> readText( @NonNull T input );
  
  public static <R> KReaderBuilder<R> builder( @NonNull BiFunction<R, Encoding, Reader> opener ) {
    return new KReaderBuilder<>( opener );
  }

  public static <R> KReaderBuilder<R> builder( @NonNull Class<R> type ) {
    return new KReaderBuilder<>( type );
  }

  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class KReaderBuilder<T> {
    
    KReaderImpl<T>   instance;
    
    private KReaderBuilder( Class<T> type ) {
      instance = new KReaderImpl<>( type );
    }

    private KReaderBuilder( BiFunction<T, Encoding, Reader> opener ) {
      instance = new KReaderImpl<>( opener );
    }

    public KReaderBuilder<T> noBuffering() {
      instance.setBuffered( false );
      return this;
    }
    
    public KReaderBuilder<T> encoding( Encoding newEncoding ) {
      instance.setEncoding( newEncoding != null ? newEncoding : instance.getEncoding() );
      return this;
    }
    
    public KReaderBuilder<T> errorHandler( BiConsumer<Exception, T> newErrHandler ) {
      instance.setErrHandler( newErrHandler != null ? newErrHandler : instance.getErrHandler() );
      return this;
    }

    public KReader<T> build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
