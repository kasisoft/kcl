package com.kasisoft.libs.common.internal.io;

import static com.kasisoft.libs.common.function.Functions.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
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
public class KWriterImpl<T> implements KWriter<T> {

  Encoding                            encoding;
  BiConsumer<Exception, T>            errHandler;
  BiFunction<T, Encoding, Writer>     openOutput;
  boolean                             buffered;
  
  public KWriterImpl( @NonNull Class<T> type ) {
    this();
    openOutput = selectOpener( type );
  }

  public KWriterImpl( @NonNull BiFunction<T, Encoding, Writer> opener ) {
    this();
    openOutput = opener;
  }
  
  private KWriterImpl() {
    encoding          = Encoding.UTF8;
    errHandler        = ($e, $i) -> {};
    buffered          = true;
  }
  
  private BiFunction<T, Encoding, Writer> selectOpener( Class<T> type ) {
           if( Path         . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (Path) $i, $e );
    } else if( URL          . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (URL) $i, $e );
    } else if( URI          . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (URI) $i, $e );
    } else if( File         . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (File) $i, $e );
    } else if( String       . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (String) $i, $e );
    } else if( OutputStream . class . isAssignableFrom( type ) ) { return ($i, $e) -> newWriter( (OutputStream) $i, $e );
    }
    throw new IllegalArgumentException();
  }
  
  @Override
  public <R> Optional<R> forWriter( @NonNull T output, Function<ExtWriter, R> function ) {
    return forWriter( output, null, null, adaptToTri( function ) );
  }

  @Override
  public <C1, R> Optional<R> forWriter( @NonNull T output, C1 context1, BiFunction<ExtWriter, C1, R> function ) {
    return forWriter( output, context1, null, adaptToTri( function ) );
  }
  
  @SuppressWarnings("resource")
  private ExtWriter openWriter( T output ) {
    Writer result = openOutput.apply( output, encoding );
    if( buffered && (! (result instanceof BufferedWriter)) ) {
      result = new BufferedWriter( result );
    }
    return new ExtWriter<>( result, output, errHandler );
  }
  
  @Override
  public <C1, C2, R> Optional<R> forWriter( @NonNull T output, C1 context1, C2 context2, TriFunction<ExtWriter, C1, C2, R> function ) {
    try( ExtWriter writer = openWriter( output ) ) {
      return Optional.ofNullable( function.apply( writer, context1, context2 ) );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), output );
      return Optional.empty();
    }
  }
  
  @Override
  public <R> boolean forWriterDo( @NonNull T output, Consumer<ExtWriter> consumer ) {
    return forWriterDo( output, null, null, adaptToTri( consumer ) );
  }

  @Override
  public <C1, R> boolean forWriterDo( @NonNull T output, C1 context1, BiConsumer<ExtWriter, C1> consumer ) {
    return forWriterDo( output, context1, null, adaptToTri( consumer ) );
  }
  
  @Override
  public <C1, C2> boolean forWriterDo( @NonNull T output, C1 context1, C2 context2, TriConsumer<ExtWriter, C1, C2> function ) {
    try( ExtWriter writer = openWriter( output ) ) {
      function.accept( writer, context1, context2 );
      return true;
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), output );
      return false;
    }
  }
  
  @Override
  public Optional<ExtWriter> open( @NonNull T output ) {
    try {
      return Optional.of( openWriter( output ) );
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
      return Optional.empty();
    }
  }
  
  private Writer newWriter( @NonNull String dest, Encoding encoding ) {
    return newWriter( Paths.get( dest ), encoding );
  }

  private Writer newWriter( @NonNull Path dest, Encoding encoding ) {
    try {
      return newWriter( Files.newOutputStream( dest ), encoding );
    } catch( Exception ex ) {
      throw FailureCode.IO.newException(ex);
    }
  }

  @SuppressWarnings("resource")
  private Writer newWriter( @NonNull File dest, Encoding encoding ) {
    return newWriter( dest.toPath(), encoding );
  }

  private Writer newWriter( @NonNull URI source, Encoding encoding ) {
    return newWriter( Paths.get( source ), encoding );
  }

  private Writer newWriter( @NonNull URL dest, Encoding encoding ) {
    try {
      Path path = Paths.get( dest.toURI() );
      if( Files.isWritable( path ) ) {
        return newWriter( path, encoding );
      }
    } catch( Exception ex ) {
      throw FailureCode.IO.newException( ex );
    }
    throw FailureCode.IO.newException( new IOException( String.valueOf( dest ) ) );
  }

  private Writer newWriter( @NonNull OutputStream dest, Encoding encoding ) {
    return Encoding.openWriter( dest, encoding );
  }
  
  @Override
  public boolean writeAll( @NonNull T output, char[] data ) {
    return forWriterDo( output, $ -> { 
      CharArrayReader charin = new CharArrayReader( data );
      IoFunctions.copy( charin, $, $ex -> errHandler.accept( $ex, output ) );
    } );
  }
  
} /* ENDCLASS */
