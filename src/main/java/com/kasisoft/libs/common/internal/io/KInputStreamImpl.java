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
  public <R> Optional<R> forInputStream( @NonNull T input, Function<ExtInputStream, R> function ) {
    return forInputStream( input, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forInputStream( @NonNull T input, C1 context1, BiFunction<ExtInputStream, C1, R> function ) {
    return forInputStream( input, context1, null, adaptToTri( function ) );
  }
  
  @SuppressWarnings("resource")
  private ExtInputStream openInputStream( T input ) {
    InputStream result = openInput.apply( input );
    if( buffered && (! (result instanceof BufferedInputStream))) {
      result = new BufferedInputStream( result );
    }
    return new ExtInputStream<>( result, input, errHandler );
  }
  
  @Override
  public Optional<ExtInputStream> open( T input ) {
    try {
      return Optional.of( openInputStream( input ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return Optional.empty();
    }
  }

  @Override
  public <C1, C2, R> Optional<R> forInputStream( @NonNull T input, C1 context1, C2 context2, TriFunction<ExtInputStream, C1, C2, R> function ) {
    try( ExtInputStream instream = openInputStream( input ) ) {
      return Optional.ofNullable( function.apply( instream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forInputStreamDo( @NonNull T input, Consumer<ExtInputStream> consumer ) {
    return forInputStreamDo( input, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forInputStreamDo( @NonNull T input, C1 context1, BiConsumer<ExtInputStream, C1> consumer ) {
    return forInputStreamDo( input, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forInputStreamDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<ExtInputStream, C1, C2> function ) {
    try( ExtInputStream instream = openInputStream( input ) ) {
      function.accept( instream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return false;
    }
  }
  
  @Override
  public Optional<byte[]> readAll( @NonNull T input ) {
    return forInputStream( input, $ -> { 
      ByteArrayOutputStream byteout = new ByteArrayOutputStream();
      IoFunctions.copy( $, byteout, $ex -> errHandler.accept( $ex, input ) );
      return byteout.toByteArray();
    } );
  }

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

} /* ENDCLASS */
