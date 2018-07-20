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
public interface KOutputStream<T> {

  <R> Optional<R> forOutputStream( @NonNull T output, Function<OutputStream, R> function );

  <C1, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, BiFunction<OutputStream, C1, R> function );
  
  <C1, C2, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, C2 context2, TriFunction<OutputStream, C1, C2, R> function );
  
  <R> boolean forOutputStreamDo( @NonNull T output, Consumer<OutputStream> consumer );

  <C1, R> boolean forOutputStreamDo( @NonNull T output, C1 context1, BiConsumer<OutputStream, C1> consumer );
  
  <C1, C2> boolean forOutputStreamDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<OutputStream, C1, C2> function );

  Optional<OutputStream> open( @NonNull T input );
  
  public static <R> KOutputStreamBuilder<R> builder( @NonNull Function<R, OutputStream> opener ) {
    return new KOutputStreamBuilder<>( opener );
  }

  public static <R> KOutputStreamBuilder<R> builder( @NonNull Class<R> type ) {
    return new KOutputStreamBuilder<>( type );
  }

  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class KOutputStreamBuilder<T> {
    
    KOutputStreamImpl<T>   instance;
    
    private KOutputStreamBuilder( Class<T> type ) {
      instance = new KOutputStreamImpl<>( type );
    }

    private KOutputStreamBuilder( Function<T, OutputStream> opener ) {
      instance = new KOutputStreamImpl<>( opener );
    }

    public KOutputStreamBuilder<T> noBuffering() {
      instance.setBuffered( false );
      return this;
    }

    public KOutputStreamBuilder<T> errorHandler( BiConsumer<Exception, T> newErrHandler ) {
      instance.setErrHandler( newErrHandler != null ? newErrHandler : instance.getErrHandler() );
      return this;
    }

    public KOutputStream<T> build() {
      return instance;
    }
    
  } /* ENDCLASS */

} /* ENDINTERFACE */
