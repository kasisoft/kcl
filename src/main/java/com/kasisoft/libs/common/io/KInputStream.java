package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.internal.io.*;

import java.util.function.*;

import java.util.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KInputStream<T> {
  
  <R> Optional<R> forInputStream( @NonNull T input, Function<InputStream, R> function );

  <C1, R> Optional<R> forInputStream( @NonNull T input, C1 context1, BiFunction<InputStream, C1, R> function );
  
  <C1, C2, R> Optional<R> forInputStream( @NonNull T input, C1 context1, C2 context2, TriFunction<InputStream, C1, C2, R> function );
  
  <R> boolean forInputStreamDo( @NonNull T input, Consumer<InputStream> consumer );

  <C1, R> boolean forInputStreamDo( @NonNull T input, C1 context1, BiConsumer<InputStream, C1> consumer );
  
  <C1, C2> boolean forInputStreamDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<InputStream, C1, C2> function );
  
  Optional<InputStream> open( @NonNull T input );
  
  // byte[] readAll( @NonNull T input );
  
  public static <R> KInputStreamBuilder<R> builder( @NonNull Function<R, InputStream> opener ) {
    return new KInputStreamBuilder<>( opener );
  }

  public static <R> KInputStreamBuilder<R> builder( @NonNull Class<R> type ) {
    return new KInputStreamBuilder<>( type );
  }

  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class KInputStreamBuilder<T> {
    
    KInputStreamImpl<T>   instance;
    
    private KInputStreamBuilder( Class<T> type ) {
      instance = new KInputStreamImpl<>( type );
    }

    private KInputStreamBuilder( Function<T, InputStream> opener ) {
      instance = new KInputStreamImpl<>( opener );
    }

    public KInputStreamBuilder<T> noBuffering() {
      instance.setBuffered( false );
      return this;
    }

    public KInputStreamBuilder<T> errorHandler( BiConsumer<Exception, T> newErrHandler ) {
      instance.setErrHandler( newErrHandler != null ? newErrHandler : instance.getErrHandler() );
      return this;
    }
    
    public KInputStream<T> build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDINTERFACE */
