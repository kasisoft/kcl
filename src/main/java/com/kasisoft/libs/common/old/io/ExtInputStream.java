package com.kasisoft.libs.common.old.io;

import java.util.function.BiConsumer;

import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtInputStream<T> extends InputStream {

  InputStream                     instream;
  T                               input;
  BiConsumer<Exception, T>        errHandler;
  
  public int eRead() {
    try {
      return instream.read();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return -1;
    }
  }

  public int eRead( byte[] b ) {
    try {
      return instream.read(b);
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return -1;
    }
  }

  public int eRead( byte[] b, int off, int len ) {
    try {
      return instream.read( b, off, len );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return -1;
    }
  }

  public long eSkip( long n ) {
    try {
      return instream.skip(n);
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return -1;
    }
  }

  public int eAvailable() {
    try {
      return instream.available();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return -1;
    }
  }

  public void eClose() {
    try {
      instream.close();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public synchronized void eMark( int readlimit ) {
    try {
      instream.mark( readlimit );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public synchronized void eReset() {
    try {
      instream.reset();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public boolean eMarkSupported() {
    try {
      return instream.markSupported();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
      return false;
    }
  }
  
  @Override
  public int read() throws IOException {
    return eRead();
  }

  @Override
  public int read( byte[] b ) throws IOException {
    return eRead(b);
  }

  @Override
  public int read( byte[] b, int off, int len ) throws IOException {
    return eRead( b, off, len );
  }

  @Override
  public long skip( long n ) throws IOException {
    return eSkip(n);
  }

  @Override
  public int available() throws IOException {
    return eAvailable();
  }

  @Override
  public void close() throws IOException {
    eClose();
  }

  @Override
  public synchronized void mark( int readlimit ) {
    eMark( readlimit );
  }

  @Override
  public synchronized void reset() throws IOException {
    eReset();
  }

  @Override
  public boolean markSupported() {
    return eMarkSupported();
  }
  
} /* ENDCLASS */