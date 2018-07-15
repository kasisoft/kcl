package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.internal.io.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.nio.file.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KReader<T> {
  
  KReader<String>       STRING_PATH_READER  = builder(String.class).build();
  KReader<Path>         PATH_READER         = builder(Path.class).build();
  KReader<File>         FILE_READER         = builder(File.class).build();
  KReader<URI>          URI_READER          = builder(URI.class).build();
  KReader<URL>          URL_READER          = builder(URL.class).build();
  KReader<InputStream>  INPUTSTREAM_READER  = builder(InputStream.class).build();

  <R> Optional<R> forReader( @NonNull T input, Function<Reader, R> function );

  <C1, R> Optional<R> forReader( @NonNull T input, C1 context1, BiFunction<Reader, C1, R> function );
  
  <C1, C2, R> Optional<R> forReader( @NonNull T input, C1 context1, C2 context2, TriFunction<Reader, C1, C2, R> function );
  
  <R> boolean forReaderDo( @NonNull T input, Consumer<Reader> consumer );

  <C1, R> boolean forReaderDo( @NonNull T input, C1 context1, BiConsumer<Reader, C1> consumer );
  
  <C1, C2> boolean forReaderDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<Reader, C1, C2> function );

  Optional<Reader> open( @NonNull T input );
  
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
  
//  /* copy */
//
//  public static boolean copy( @NonNull Reader reader, @NonNull Writer writer, @NonNull char[] buffer ) {
//    return copy( reader, writer, buffer, $ -> {} );
//  }
//  
//  public static boolean copy( @NonNull Reader reader, @NonNull Writer writer, @NonNull char[] buffer, Consumer<Exception> errHandler ) {
//    try {
//      int read = reader.read( buffer );
//      while( read != -1 ) {
//        if( read > 0 ) {
//          writer.write( buffer, 0, read );
//        }
//        read = reader.read( buffer );
//      }
//      return true;
//    } catch( Exception ex ) {
//      if( errHandler != null ) {
//        errHandler.accept( ex );
//      } else {
//        throw FailureCode.IO.newException(ex);
//      }
//      return false;
//    }
//  }
  
} /* ENDCLASS */
