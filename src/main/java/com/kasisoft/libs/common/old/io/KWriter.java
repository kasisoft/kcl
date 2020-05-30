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
public interface KWriter<T> {
  
  <R> Optional<R> forWriter( @NonNull T output, Function<ExtWriter, R> function );

  <C1, R> Optional<R> forWriter( @NonNull T output, C1 context1, BiFunction<ExtWriter, C1, R> function );
  
  <C1, C2, R> Optional<R> forWriter( @NonNull T output, C1 context1, C2 context2, TriFunction<ExtWriter, C1, C2, R> function );
  
  <R> boolean forWriterDo( @NonNull T output, Consumer<ExtWriter> consumer );

  <C1, R> boolean forWriterDo( @NonNull T output, C1 context1, BiConsumer<ExtWriter, C1> consumer );
  
  <C1, C2> boolean forWriterDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<ExtWriter, C1, C2> function );
  
  Optional<ExtWriter> open( @NonNull T output );

  boolean writeAll( @NonNull T output, char[] data );
  
  public static <R> KWriterBuilder<R> builder( @NonNull BiFunction<R, Encoding, Writer> opener ) {
    return new KWriterBuilder<>( opener );
  }

  public static <R> KWriterBuilder<R> builder( @NonNull Class<R> type ) {
    return new KWriterBuilder<>( type );
  }

  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class KWriterBuilder<T> {
    
    KWriterImpl<T>   instance;
    
    private KWriterBuilder( Class<T> type ) {
      instance = new KWriterImpl<>( type );
    }

    private KWriterBuilder( BiFunction<T, Encoding, Writer> opener ) {
      instance = new KWriterImpl<>( opener );
    }

    public KWriterBuilder<T> noBuffering() {
      instance.setBuffered( false );
      return this;
    }

    public KWriterBuilder<T> encoding( Encoding newEncoding ) {
      instance.setEncoding( newEncoding != null ? newEncoding : instance.getEncoding() );
      return this;
    }
    
    public KWriterBuilder<T> errorHandler( BiConsumer<Exception, T> newErrHandler ) {
      instance.setErrHandler( newErrHandler != null ? newErrHandler : instance.getErrHandler() );
      return this;
    }

    public KWriter<T> build() {
      return instance;
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
