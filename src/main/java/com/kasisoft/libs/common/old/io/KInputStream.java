package com.kasisoft.libs.common.old.io;

import com.kasisoft.libs.common.old.function.TriConsumer;
import com.kasisoft.libs.common.old.function.TriFunction;
import com.kasisoft.libs.common.old.internal.io.KInputStreamImpl;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Optional;

import java.io.InputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KInputStream<T> {
  
  <R> Optional<R> forInputStream( @NonNull T input, Function<ExtInputStream, R> function );

  <C1, R> Optional<R> forInputStream( @NonNull T input, C1 context1, BiFunction<ExtInputStream, C1, R> function );
  
  <C1, C2, R> Optional<R> forInputStream( @NonNull T input, C1 context1, C2 context2, TriFunction<ExtInputStream, C1, C2, R> function );
  
  <R> boolean forInputStreamDo( @NonNull T input, Consumer<ExtInputStream> consumer );

  <C1, R> boolean forInputStreamDo( @NonNull T input, C1 context1, BiConsumer<ExtInputStream, C1> consumer );
  
  <C1, C2> boolean forInputStreamDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<ExtInputStream, C1, C2> function );
  
  Optional<ExtInputStream> open( @NonNull T input );
  
  Optional<byte[]> readAll( @NonNull T input );
  
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
