package com.kasisoft.libs.common.old.internal.io;

import static com.kasisoft.libs.common.old.function.Functions.adaptToTri;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.constants.Encoding;
import com.kasisoft.libs.common.old.function.TriConsumer;
import com.kasisoft.libs.common.old.function.TriFunction;
import com.kasisoft.libs.common.old.io.ExtReader;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.io.KReader;
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

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;

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
  public <R> Optional<R> forReader( @NonNull T input, Function<ExtReader, R> function ) {
    return forReader( input, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forReader( @NonNull T input, C1 context1, BiFunction<ExtReader, C1, R> function ) {
    return forReader( input, context1, null, adaptToTri( function ) );
  }
  
  @Override
  public Optional<ExtReader> open( @NonNull T input ) {
    try {
      return Optional.of( openReader( input ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return Optional.empty();
    }
  }
  
  @SuppressWarnings("resource")
  private ExtReader openReader( T input ) {
    Reader result = openInput.apply( input, encoding );
    if( buffered && (! (result instanceof BufferedReader))) {
      result = new BufferedReader( result );
    }
    return new ExtReader<>( result, input, errHandler );
  }
  
  @Override
  public <C1, C2, R> Optional<R> forReader( @NonNull T input, C1 context1, C2 context2, TriFunction<ExtReader, C1, C2, R> function ) {
    try( ExtReader instream = openReader( input ) ) {
      return Optional.ofNullable( function.apply( instream, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( KclException.unwrap( ex ), input );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forReaderDo( @NonNull T input, Consumer<ExtReader> consumer ) {
    return forReaderDo( input, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forReaderDo( @NonNull T input, C1 context1, BiConsumer<ExtReader, C1> consumer ) {
    return forReaderDo( input, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forReaderDo( @NonNull T input, C1 context1, C2 context2, TriConsumer<ExtReader, C1, C2> function ) {
    try( ExtReader instream = openReader( input ) ) {
      function.accept( instream, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( KclException.unwrap( ex ), input );
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
      throw KclException.wrap(ex);
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
      throw KclException.wrap(ex);
    }
  }

  private Reader newReader( @NonNull InputStream source, Encoding encoding ) {
    return Encoding.openReader( source, encoding );
  }
  
  @Override
  public Optional<char[]> readAll( @NonNull T input ) {
    return forReader( input, $ -> { 
      CharArrayWriter writer = new CharArrayWriter();
      IoFunctions.copy( $, writer, $ex -> errHandler.accept( $ex, input ) );
      return writer.toCharArray();
    } );
  }
  
  @Override
  public Optional<String> readText( @NonNull T input ) {
    return forReader( input, $ -> { 
      StringWriter writer = new StringWriter();
      IoFunctions.copy( $, writer, $ex -> errHandler.accept( $ex, input ) );
      return writer.toString();
    } );
  }
  
} /* ENDCLASS */
