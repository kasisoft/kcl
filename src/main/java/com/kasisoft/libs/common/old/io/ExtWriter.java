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
public class ExtWriter<T> extends Writer {

  Writer                      writer;
  T                           input;
  BiConsumer<Exception, T>    errHandler;

  public void eWrite( int c ) {
    try {
      writer.write(c);
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public void eWrite( char[] cbuf ) {
    try {
      writer.write( cbuf );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public void eWrite( char[] cbuf, int off, int len ) {
    try {
      writer.write( cbuf, off, len );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public void eWrite( String str ) {
    try {
      writer.write( str );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public void eWrite( String str, int off, int len ) {
    try {
      writer.write( str, off, len );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public Writer eAppend( CharSequence csq ) {
    try {
      writer.append( csq );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
    return this;
  }

  public Writer eAppend( CharSequence csq, int start, int end ) {
    try {
      writer.append( csq, start, end );
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
    return this;
  }

  public Writer eAppend( char c ) {
    try {
      writer.append(c);
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
    return this;
  }

  public void eFlush() {
    try {
      writer.flush();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }

  public void eClose() {
    try {
      writer.close();
    } catch( Exception ex ) {
      errHandler.accept( ex, input );
    }
  }
  
  @Override
  public void write( int c ) throws IOException {
    eWrite(c);
  }

  @Override
  public void write( char[] cbuf ) throws IOException {
    eWrite( cbuf );
  }

  @Override
  public void write( char[] cbuf, int off, int len ) throws IOException {
    eWrite( cbuf, off, len );
  }

  @Override
  public void write( String str ) throws IOException {
    eWrite( str );
  }

  @Override
  public void write( String str, int off, int len ) throws IOException {
    eWrite( str, off, len );
  }

  @Override
  public Writer append( CharSequence csq ) throws IOException {
    return eAppend( csq );
  }

  @Override
  public Writer append( CharSequence csq, int start, int end ) throws IOException {
    return eAppend( csq, start, end );
  }

  @Override
  public Writer append( char c ) throws IOException {
    return eAppend(c);
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
