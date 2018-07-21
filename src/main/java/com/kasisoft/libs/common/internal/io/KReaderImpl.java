package com.kasisoft.libs.common.internal.io;

import static com.kasisoft.libs.common.function.Functions.*;
import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.nio.*;
import java.nio.file.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KReaderImpl<T> implements KReader<T> {

  Encoding                            encoding;
  BiConsumer<Exception, T>            errHandler;
  BiFunction<T, Encoding, Reader>     openInput;
  boolean                             buffered;
  
  public KReaderImpl( @NonNull Class<T> type ) {
    this();
    openInput = selectOpener( type );
  }

  public KReaderImpl( @NonNull BiFunction<T, Encoding, Reader> opener ) {
    this();
    openInput = opener;
  }
  
  private KReaderImpl() {
    encoding          = Encoding.UTF8;
    errHandler        = ($e, $i) -> {};
    buffered          = true;
  }
  
  private BiFunction<T, Encoding, Reader> selectOpener( Class<T> type ) {
           if( Path        . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (Path) $i, $e );
    } else if( URL         . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (URL) $i, $e );
    } else if( URI         . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (URI) $i, $e );
    } else if( File        . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (File) $i, $e );
    } else if( String      . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (String) $i, $e );
    } else if( InputStream . class . isAssignableFrom( type ) ) { return ($i, $e) -> newReader( (InputStream) $i, $e );
    }
    throw new IllegalArgumentException();
  }
  
  @Override
  public <R> Optional<R> forReader( @NonNull T input, Function<Reader, R> function ) {
    return forReader( input, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forReader( @NonNull T input, C1 context1, BiFunction<Reader, C1, R> function ) {
    return forReader( input, context1, null, adaptToTri( function ) );
  }
  
  @Override
  public Optional<Reader> open( @NonNull T input ) {
    try {
      return Optional.of( openReader( input ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return Optional.empty();
    }
  }
  
  @SuppressWarnings("resource")
  private Reader openReader( T input ) {
    Reader result = openInput.apply( input, encoding );
    if( buffered && (! (result instanceof BufferedReader))) {
      result = new BufferedReader( result );
    }
    return new InternalReader<>( result, input, errHandler );
  }
  
  @Override
  public <C1, C2, R> Optional<R> forReader( @NonNull T input, C1 context1, C2 context2, TriFunction<Reader, C1, C2, R> function ) {
    try( Reader instream = openReader( input ) ) {
      return Optional.ofNullable( function.apply( instream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forReaderDo( @NonNull T input, Consumer<Reader> consumer ) {
    return forReaderDo( input, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forReaderDo( @NonNull T input, C1 context1, BiConsumer<Reader, C1> consumer ) {
    return forReaderDo( input, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forReaderDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<Reader, C1, C2> function ) {
    try( Reader instream = openReader( input ) ) {
      function.accept( instream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return false;
    }
  }
  
  private Reader newReader( @NonNull String source, Encoding encoding ) {
    Path path = Paths.get( source );
    if( Files.isRegularFile( path ) ) {
      return newReader( path, encoding );
    } else {
      return newReader( MiscFunctions.getResource( source ), encoding );
    }
  }
  
  private Reader newReader( @NonNull Path source, Encoding encoding ) {
    try {
      return newReader( Files.newInputStream( source ), encoding );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  @SuppressWarnings("resource")
  private Reader newReader( @NonNull File source, Encoding encoding ) {
    return newReader( source.toPath(), encoding );
  }

  private Reader newReader( @NonNull URI source, Encoding encoding ) {
    return newReader( Paths.get( source ), encoding );
  }

  private Reader newReader( @NonNull URL source, Encoding encoding ) {
    try {
      Path path = Paths.get( source.toURI() );
      if( Files.isRegularFile( path ) ) {
        return newReader( path, encoding );
      }
    } catch( Exception ex ) {
      // can be ignored here as it was an attempt to open the URL as a file
    }
    try {
      return newReader( source.openStream(), encoding );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  private Reader newReader( @NonNull InputStream source, Encoding encoding ) {
    return Encoding.openReader( source, encoding );
  }
  
  @Override
  public Optional<char[]> readAll( @NonNull T input ) {
    return forReader( input, $ -> { 
      CharArrayWriter writer = new CharArrayWriter();
      PChar.withBufferDo( $b -> {
        IoFunctions.copy( $, writer, $b, $ex -> errHandler.accept( $ex, input ) );
      } );
      return writer.toCharArray();
    } );
  }
  
  @AllArgsConstructor 
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class InternalReader<T> extends Reader {

    Reader                      reader;
    T                           input;
    BiConsumer<Exception, T>    errHandler;
    
    @Override
    public int read( CharBuffer target ) throws IOException {
      try {
        return reader.read( target );
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return -1;
      }
    }

    @Override
    public int read() throws IOException {
      try {
        return reader.read();
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return -1;
      }
    }

    @Override
    public int read( char[] cbuf ) throws IOException {
      try {
        return reader.read( cbuf );
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return -1;
      }
    }

    @Override
    public long skip( long n ) throws IOException {
      try {
        return reader.skip(n);
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return -1;
      }
    }

    @Override
    public boolean ready() throws IOException {
      try {
        return reader.ready();
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return false;
      }
    }

    @Override
    public boolean markSupported() {
      try {
        return reader.markSupported();
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return false;
      }
    }

    @Override
    public void mark( int readAheadLimit ) throws IOException {
      try {
        reader.mark( readAheadLimit );
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
      }
    }

    @Override
    public void reset() throws IOException {
      try {
        reader.reset();
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
      }
    }

    @Override
    public int read( char[] cbuf, int off, int len ) throws IOException {
      try {
        return reader.read( cbuf, off, len );
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
        return -1;
      }
    }

    @Override
    public void close() throws IOException {
      try {
        reader.close();
      } catch( Exception ex ) {
        errHandler.accept( FailureException.unwrap( ex ), input );
      }
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
