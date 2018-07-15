package com.kasisoft.libs.common.internal.io;

import static com.kasisoft.libs.common.function.Functions.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.io.*;

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
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KOutputStreamImpl<T> implements KOutputStream<T> {

  Function<T, OutputStream>       openOutput;
  BiConsumer<Exception, T>        errHandler;
  boolean                         buffered;
  
  public KOutputStreamImpl( @NonNull Class<T> type ) {
    this();
    openOutput = selectOpener( type );
  }

  public KOutputStreamImpl( @NonNull Function<T, OutputStream> opener ) {
    this();
    openOutput = opener;
  }
  
  private KOutputStreamImpl() {
    errHandler        = ($e, $i) -> {};
    buffered          = true;
  }
  
  private Function<T, OutputStream> selectOpener( Class<T> type ) {
           if( Path         . class . isAssignableFrom( type ) ) { return $i -> newOutputStream( (Path) $i );
    } else if( URL          . class . isAssignableFrom( type ) ) { return $i -> newOutputStream( (URL) $i );
    } else if( URI          . class . isAssignableFrom( type ) ) { return $i -> newOutputStream( (URI) $i );
    } else if( File         . class . isAssignableFrom( type ) ) { return $i -> newOutputStream( (File) $i );
    } else if( String       . class . isAssignableFrom( type ) ) { return $i -> newOutputStream( (String) $i );
    }
    throw new IllegalArgumentException();
  }
  
  @Override
  public <R> Optional<R> forOutputStream( @NonNull T output, Function<OutputStream, R> function ) {
    return forOutputStream( output, null, null, adapt( function ) );
  }

  @Override
  public <C1, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, BiFunction<OutputStream, C1, R> function ) {
    return forOutputStream( output, context1, null, adapt( function ) );
  }
  
  @SuppressWarnings("resource")
  private OutputStream openOutputStream( T output ) {
    OutputStream result = openOutput.apply( output );
    if( buffered && (! (result instanceof BufferedOutputStream)) ) {
      result = new BufferedOutputStream( result );
    }
    return new InternalOutputStream<>( result, output, errHandler );
  }
  
  @Override
  public Optional<OutputStream> open( T output ) {
    try {
      return Optional.of( openOutputStream( output ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
      return Optional.empty();
    }
  }
  
  @Override
  public <C1, C2, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, C2 context2, TriFunction<OutputStream, C1, C2, R> function ) {
    try( OutputStream outstream = openOutputStream( output ) ) {
      return Optional.ofNullable( function.apply( outstream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), output );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forOutputStreamDo( @NonNull T output, Consumer<OutputStream> consumer ) {
    return forOutputStreamDo( output, null, null, adapt( consumer ) );
  }

  @Override
  public <C1, R> boolean forOutputStreamDo( @NonNull T output, C1 context1, BiConsumer<OutputStream, C1> consumer ) {
    return forOutputStreamDo( output, context1, null, adapt( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forOutputStreamDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<OutputStream, C1, C2> function ) {
    try( OutputStream outstream = openOutputStream( output ) ) {
      function.accept( outstream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), output );
      return false;
    }
  }
  
  private OutputStream newOutputStream( Path input ) {
    try {
      return Files.newOutputStream( input );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  private OutputStream newOutputStream( URL input ) {
    try {
      return newOutputStream( input.toURI() );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  private OutputStream newOutputStream( URI input ) {
    return newOutputStream( Paths.get( input ) );
  }

  private OutputStream newOutputStream( File input ) {
    return newOutputStream( input.toPath() );
  }

  private OutputStream newOutputStream( String input ) {
    return newOutputStream( Paths.get( input ) );
  }
  
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class InternalOutputStream<T> extends OutputStream {

    OutputStream                outstream;
    T                           output;
    BiConsumer<Exception, T>    errHandler;
    
    @Override
    public void write( int b ) throws IOException {
      try {
        outstream.write(b);
      } catch( Exception ex ) {
        errHandler.accept( ex, output );
      }
    }

    @Override
    public void write( byte[] b ) throws IOException {
      try {
        outstream.write(b);
      } catch( Exception ex ) {
        errHandler.accept( ex, output );
      }
    }

    @Override
    public void write( byte[] b, int off, int len ) throws IOException {
      try {
        outstream.write( b, off, len );
      } catch( Exception ex ) {
        errHandler.accept( ex, output );
      }
    }

    @Override
    public void flush() throws IOException {
      try {
        outstream.flush();
      } catch( Exception ex ) {
        errHandler.accept( ex, output );
      }
    }

    @Override
    public void close() throws IOException {
      try {
        outstream.close();
      } catch( Exception ex ) {
        errHandler.accept( ex, output );
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
