package com.kasisoft.libs.common.old.internal.io;

import static com.kasisoft.libs.common.old.function.Functions.adaptToTri;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.buckets.Bucket;
import com.kasisoft.libs.common.buckets.BucketFactories;
import com.kasisoft.libs.common.old.function.TriConsumer;
import com.kasisoft.libs.common.old.function.TriFunction;
import com.kasisoft.libs.common.old.io.ExtInputStream;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.io.KInputStream;
import com.kasisoft.libs.common.old.util.MiscFunctions;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.Optional;

import java.net.URI;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;

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
      errHandler.accept( KclException.unwrap( ex ), input );
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
      errHandler.accept( KclException.unwrap( ex ), input );
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
      throw KclException.wrap(ex);
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
      throw KclException.wrap(ex);
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
