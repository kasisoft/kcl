package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.constants.*;
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
public interface KWriter<T> {
  
  <R> Optional<R> forWriter( @NonNull T output, Function<Writer, R> function );

  <C1, R> Optional<R> forWriter( @NonNull T output, C1 context1, BiFunction<Writer, C1, R> function );
  
  <C1, C2, R> Optional<R> forWriter( @NonNull T output, C1 context1, C2 context2, TriFunction<Writer, C1, C2, R> function );
  
  <R> boolean forWriterDo( @NonNull T output, Consumer<Writer> consumer );

  <C1, R> boolean forWriterDo( @NonNull T output, C1 context1, BiConsumer<Writer, C1> consumer );
  
  <C1, C2> boolean forWriterDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<Writer, C1, C2> function );
  
  Optional<Writer> open( @NonNull T output );

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

//  
//  /* writeChars */
//  
//  public static void writeChars( @NonNull Writer out, @NonNull char[] data ) {
//    writeChars( out, data, 0, data.length, null );
//  }
//
//  public static void writeChars( @NonNull char[] data, @NonNull Writer out ) {
//    writeChars( out, data, 0, data.length, $ -> {} );
//  }
//
//  public static void writeChars( @NonNull Writer out, @NonNull char[] data, Consumer<Exception> errHandler ) {
//    writeChars( out, data, 0, data.length, errHandler );
//  }
//
//  public static void writeChars( @NonNull char[] data, int offset, int length, @NonNull Writer out ) {
//    writeChars( out, data, offset, length, $ -> {} );
//  }
//
//  public static void writeChars( @NonNull Writer out, @NonNull char[] data, int offset, int length ) {
//    writeChars( out, data, offset, length, null );
//  }
//  
//  public static void writeChars( @NonNull Writer out, @NonNull char[] data, int offset, int length, Consumer<Exception> errHandler ) {
//    try {
//      out.write( data, offset, length );
//    } catch( Exception ex ) {
//      if( errHandler != null ) {
//        errHandler.accept(ex);
//      } else {
//        throw FailureCode.IO.newException(ex);
//      }
//    }
//  }
//
//  /* readChars */
//  
//  public static int readChars( @NonNull Reader in, @NonNull char[] buffer ) {
//    return readChars( in, buffer, 0, buffer.length, null );
//  }
//
//  public static int readChars( @NonNull char[] buffer, @NonNull Reader in ) {
//    return readChars( in, buffer, 0, buffer.length, $ -> {} );
//  }
//
//  public static int readChars( @NonNull Reader in, @NonNull char[] buffer, Consumer<Exception> errHandler ) {
//    return readChars( in, buffer, 0, buffer.length, errHandler );
//  }
//
//  public static int readChars( @NonNull Reader in, @NonNull char[] buffer, int offset, int length ) {
//    return readChars( in, buffer, offset, length, null );
//  }
//
//  public static int readChars( @NonNull char[] buffer, int offset, int length, @NonNull Reader in ) {
//    return readChars( in, buffer, offset, length, $ ->  {} );
//  }
//
//  public static int readChars( @NonNull Reader in, @NonNull char[] buffer, int offset, int length, Consumer<Exception> errHandler ) {
//    try {
//      return in.read( buffer, offset, length );
//    } catch( Exception ex ) {
//      if( errHandler != null ) {
//        errHandler.accept( ex );
//      } else {
//        throw FailureCode.IO.newException(ex);
//      }
//      return -1;
//    }
//  }
//
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
