package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.base.*;

import java.util.function.*;

import java.io.*;

import java.nio.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtReader<T> extends Reader {

  Reader                      reader;
  T                           input;
  BiConsumer<Exception, T>    errHandler;

  public int eRead( CharBuffer target ) {
    try {
      return reader.read( target );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return -1;
    }
  }

  public int eRead() {
    try {
      return reader.read();
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return -1;
    }
  }

  public int eRead( char[] cbuf ) {
    try {
      return reader.read( cbuf );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return -1;
    }
  }

  public long eSkip( long n ) {
    try {
      return reader.skip(n);
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return -1;
    }
  }

  public boolean eReady() {
    try {
      return reader.ready();
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return false;
    }
  }

  public boolean eMarkSupported() {
    try {
      return reader.markSupported();
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return false;
    }
  }

  public void eMark( int readAheadLimit ) {
    try {
      reader.mark( readAheadLimit );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
    }
  }

  public void eReset() {
    try {
      reader.reset();
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
    }
  }

  public int eRead( char[] cbuf, int off, int len ) {
    try {
      return reader.read( cbuf, off, len );
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
      return -1;
    }
  }

  public void eClose() {
    try {
      reader.close();
    } catch( Exception ex ) {
      errHandler.accept( FailureException.unwrap( ex ), input );
    }
  }
  
  //
  
  @Override
  public int read( CharBuffer target ) throws IOException {
    return eRead( target );
  }

  @Override
  public int read() throws IOException {
    return eRead();
  }

  @Override
  public int read( char[] cbuf ) throws IOException {
    return eRead( cbuf );
  }

  @Override
  public long skip( long n ) throws IOException {
    return eSkip(n);
  }

  @Override
  public boolean ready() throws IOException {
    return eReady();
  }

  @Override
  public boolean markSupported() {
    return eMarkSupported();
  }

  @Override
  public void mark( int readAheadLimit ) throws IOException {
    eMark( readAheadLimit );
  }

  @Override
  public void reset() throws IOException {
    eReset();
  }

  @Override
  public int read( char[] cbuf, int off, int len ) throws IOException {
    return eRead( cbuf, off, len );
  }

  @Override
  public void close() throws IOException {
    eClose();
  }
  
} /* ENDCLASS */
