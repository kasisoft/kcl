package com.kasisoft.libs.common.old.internal.io;

import static com.kasisoft.libs.common.utils.Functions.adaptToTri;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.io.ExtOutputStream;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.io.KOutputStream;
import com.kasisoft.libs.common.types.TriConsumer;
import com.kasisoft.libs.common.types.TriFunction;

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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.OutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;

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
  public <R> Optional<R> forOutputStream( @NonNull T output, Function<ExtOutputStream, R> function ) {
    return forOutputStream( output, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, BiFunction<ExtOutputStream, C1, R> function ) {
    return forOutputStream( output, context1, null, adaptToTri( function ) );
  }
  
  @SuppressWarnings("resource")
  private ExtOutputStream openOutputStream( T output ) {
    OutputStream result = openOutput.apply( output );
    if( buffered && (! (result instanceof BufferedOutputStream)) ) {
      result = new BufferedOutputStream( result );
    }
    return new ExtOutputStream<>( result, output, errHandler );
  }
  
  @Override
  public Optional<ExtOutputStream> open( T output ) {
    try {
      return Optional.of( openOutputStream( output ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
      return Optional.empty();
    }
  }
  
  @Override
  public <C1, C2, R> Optional<R> forOutputStream( @NonNull T output, C1 context1, C2 context2, TriFunction<ExtOutputStream, C1, C2, R> function ) {
    try( ExtOutputStream outstream = openOutputStream( output ) ) {
      return Optional.ofNullable( function.apply( outstream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( KclException.unwrap( ex ), output );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forOutputStreamDo( @NonNull T output, Consumer<ExtOutputStream> consumer ) {
    return forOutputStreamDo( output, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forOutputStreamDo( @NonNull T output, C1 context1, BiConsumer<ExtOutputStream, C1> consumer ) {
    return forOutputStreamDo( output, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forOutputStreamDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<ExtOutputStream, C1, C2> function ) {
    try( ExtOutputStream outstream = openOutputStream( output ) ) {
      function.accept( outstream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( KclException.unwrap( ex ), output );
      return false;
    }
  }
  
  private OutputStream newOutputStream( Path input ) {
    try {
      return Files.newOutputStream( input );
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
    }
  }

  private OutputStream newOutputStream( URL input ) {
    try {
      return newOutputStream( input.toURI() );
    } catch( Exception ex ) {
      throw KclException.wrap(ex);
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
  
  @Override
  public boolean writeAll( @NonNull T output, byte[] data ) {
    return forOutputStreamDo( output, $ -> { 
      ByteArrayInputStream bytein = new ByteArrayInputStream( data );
      IoFunctions.copy( bytein, $, $ex -> errHandler.accept( $ex, output ) );
    } );
  }
  
} /* ENDCLASS */
