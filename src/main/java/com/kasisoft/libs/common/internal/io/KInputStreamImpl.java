package com.kasisoft.libs.common.internal.io;

import static com.kasisoft.libs.common.function.Functions.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

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
public class KInputStreamImpl<T> implements KInputStream<T> {

  Function<T, InputStream>        openInput;
  BiConsumer<Exception, T>        errHandler;
  boolean                         buffered;
  Bucket<ByteArrayOutputStream>   byteouts;
  
  public KInputStreamImpl( @NonNull Class<T> type ) {
    this();
    openInput = selectOpener( type );
  }

  public KInputStreamImpl( @NonNull Function<T, InputStream> opener ) {
    this();
    openInput = opener;
  }
  
  private KInputStreamImpl() {
    buffered          = true;
    errHandler        = ($e, $i) -> {};
    byteouts          = BucketFactories.newByteArrayOutputStreamBucket();
  }
  
  private Function<T, InputStream> selectOpener( Class<T> type ) {
           if( Path        . class . isAssignableFrom( type ) ) { return $i -> newInputStream( (Path) $i );
    } else if( URL         . class . isAssignableFrom( type ) ) { return $i -> newInputStream( (URL) $i  );
    } else if( URI         . class . isAssignableFrom( type ) ) { return $i -> newInputStream( (URI) $i  );
    } else if( File        . class . isAssignableFrom( type ) ) { return $i -> newInputStream( (File) $i  );
    } else if( String      . class . isAssignableFrom( type ) ) { return $i -> newInputStream( (String) $i );
    }
    throw new IllegalArgumentException();
  }
  
  @Override
  public <R> Optional<R> forInputStream( @NonNull T input, Function<InputStream, R> function ) {
    return forInputStream( input, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forInputStream( @NonNull T input, C1 context1, BiFunction<InputStream, C1, R> function ) {
    return forInputStream( input, context1, null, adaptToTri( function ) );
  }
  
  @SuppressWarnings("resource")
  private InputStream openInputStream( T input ) {
    InputStream result = openInput.apply( input );
    if( buffered && (! (result instanceof BufferedInputStream))) {
      result = new BufferedInputStream( result );
    }
    return new InternalInputStream<>( result, input, errHandler );
  }
  
  @Override
  public Optional<InputStream> open( T input ) {
    try {
      return Optional.of( openInputStream( input ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return Optional.empty();
    }
  }

  @Override
  public <C1, C2, R> Optional<R> forInputStream( @NonNull T input, C1 context1, C2 context2, TriFunction<InputStream, C1, C2, R> function ) {
    try( InputStream instream = openInputStream( input ) ) {
      return Optional.ofNullable( function.apply( instream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forInputStreamDo( @NonNull T input, Consumer<InputStream> consumer ) {
    return forInputStreamDo( input, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forInputStreamDo( @NonNull T input, C1 context1, BiConsumer<InputStream, C1> consumer ) {
    return forInputStreamDo( input, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forInputStreamDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<InputStream, C1, C2> function ) {
    try( InputStream instream = openInputStream( input ) ) {
      function.accept( instream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return false;
    }
  }
  
//  @Override
//  public byte[] readAll( @NonNull T input ) {
//    Optional<byte[]> data = forInputStream( input, $i -> {
//      return byteouts.forInstance( $byteout -> {
//        Primitive.PByte.withBufferDo( $buffer -> {
//          copy( $i, $byteout, (byte[]) $buffer );
//        } );
//        return $byteout.toByteArray();
//      } );
//    } );
//    return data.orElse( Empty.NO_BYTES );
//  }

  private InputStream newInputStream( Path input ) {
    try {
      return Files.newInputStream( input );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  private InputStream newInputStream( URL input ) {
    try {
      Path path = Paths.get( input.toURI() );
      if( Files.isRegularFile( path ) ) {
        return newInputStream( path );
      }
    } catch( Exception ex ) {
      // can be ignored here as it was an attempt to open the URL as a file
    }
    try {
      return input.openStream();
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  private InputStream newInputStream( URI input ) {
    return newInputStream( Paths.get( input ) );
  }

  private InputStream newInputStream( File input ) {
    return newInputStream( input.toPath() );
  }

  private InputStream newInputStream( String input ) {
    Path path = Paths.get( input );
    if( Files.isRegularFile( path ) ) {
      return newInputStream( Paths.get( input ) );
    } else {
      return newInputStream( MiscFunctions.getResource( input ) );
    }
  }

  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class InternalInputStream<T> extends InputStream {

    InputStream                     instream;
    T                               input;
    BiConsumer<Exception, T>        errHandler;
    
    @Override
    public int read() throws IOException {
      try {
        return instream.read();
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return -1;
      }
    }

    @Override
    public int read( byte[] b ) throws IOException {
      try {
        return instream.read(b);
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return -1;
      }
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException {
      try {
        return instream.read( b, off, len );
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return -1;
      }
    }

    @Override
    public long skip( long n ) throws IOException {
      try {
        return instream.skip(n);
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return -1;
      }
    }

    @Override
    public int available() throws IOException {
      try {
        return instream.available();
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return -1;
      }
    }

    @Override
    public void close() throws IOException {
      try {
        instream.close();
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
      }
    }

    @Override
    public synchronized void mark( int readlimit ) {
      try {
        instream.mark( readlimit );
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
      }
    }

    @Override
    public synchronized void reset() throws IOException {
      try {
        instream.reset();
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
      }
    }

    @Override
    public boolean markSupported() {
      try {
        return instream.markSupported();
      } catch( Exception ex ) {
        errHandler.accept( ex, input );
        return false;
      }
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
