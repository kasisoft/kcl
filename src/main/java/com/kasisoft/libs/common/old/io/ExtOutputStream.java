package com.kasisoft.libs.common.old.io;

import java.util.function.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtOutputStream<T> extends OutputStream {

  OutputStream                outstream;
  T                           output;
  BiConsumer<Exception, T>    errHandler;

  public void eWrite( int b ) {
    try {
      outstream.write(b);
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
    }
  }

  public void eWrite( byte[] b ) {
    try {
      outstream.write(b);
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
    }
  }

  public void eWrite( byte[] b, int off, int len ) {
    try {
      outstream.write( b, off, len );
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
    }
  }

  public void eFlush() {
    try {
      outstream.flush();
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
    }
  }

  public void eClose() {
    try {
      outstream.close();
    } catch( Exception ex ) {
      errHandler.accept( ex, output );
    }
  }
  
  @Override
  public void write( int b ) throws IOException {
    eWrite(b);
  }

  @Override
  public void write( byte[] b ) throws IOException {
    eWrite(b);
  }

  @Override
  public void write( byte[] b, int off, int len ) throws IOException {
    eWrite( b, off, len );
  }

  @Override
  public void flush() throws IOException {
    eFlush();
  }

  @Override
  public void close() throws IOException {
    eClose();
  }
  
} /* ENDCLASS */
