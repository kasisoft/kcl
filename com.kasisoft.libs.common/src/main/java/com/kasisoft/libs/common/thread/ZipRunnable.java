/**
 * Name........: ZipRunnable
 * Description.: A Runnable that is used to ZIP a directory.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

import java.util.zip.*;

import java.io.*;

import lombok.*;

/**
 * A Runnable that is used to ZIP a directory.
 */
public class ZipRunnable extends AbstractRunnable {

  private File      zipfile;
  private File      sourcedir;
  private Integer   buffersize;
  private byte[]    buffer;

  /**
   * Configures this Runnable to zip a directory.
   * 
   * @param zip   The ZIP archive. Not <code>null</code>.
   * @param dir   The directory which shall be zipped. Not <code>null</code>.
   */
  public ZipRunnable( @NonNull File zip, @NonNull File dir ) {
    this( zip, dir, null );
  }

  /**
   * Configures this Runnable to zip a directory.
   * 
   * @param zip    The ZIP archive. Not <code>null</code>.
   * @param dir    The directory which shall be zipped. Not <code>null</code>.
   * @param size   The size used for the internal buffers. A value of <code>null</code> means that the default 
   *               buffersize will be used.
   */
  public ZipRunnable( @NonNull File zip, @NonNull File dir, Integer size ) {
    zipfile    = zip;
    sourcedir  = dir;
    buffersize = size;
    buffer     = null;
  }
  
  @Override
  protected void execute() {
    OutputStream    fileout = null;
    ZipOutputStream zipout  = null;
    try {
      buffer  = IoFunctions.allocateBytes( buffersize );
      fileout = IoFunctions.newOutputStream ( zipfile );
      zipout  = new ZipOutputStream  ( fileout );
      zipout.setMethod( ZipOutputStream.DEFLATED );
      zipout.setLevel(9);
      packDir( zipout, "", sourcedir );
    } catch( IOException ex ) {
      handleIOFailure( ex );
    } finally {
      IoFunctions.releaseBytes( buffer );
      MiscFunctions.close( zipout  );
      MiscFunctions.close( fileout );
      buffer = null;
    }
  }

  /**
   * Packs the content of a complete directory into the ZIP file.
   * 
   * @param zipout     The OutputStream to access the ZIP file.
   * @param relative   The current relative path.
   * @param dir        The directory which shall be added.
   * 
   * @throws IOException   Some IO operation failed.
   */
  private void packDir( ZipOutputStream zipout, String relative, File dir ) throws IOException {
    File[] entries = dir.listFiles();
    if( relative.length() > 0 ) {
      relative = relative + "/";
    }
    for( int i = 0; (i < entries.length) && (! Thread.currentThread().isInterrupted()); i++ ) {
      String path   = relative + entries[i].getName();
      long   length = entries[i].isDirectory() ? 0 : entries[i].length();
      onIterationBegin( path, entries[i].isDirectory(), length );
      if( entries[i].isDirectory() ) {
        packDir( zipout, path, entries[i] );
      } else {
        packFile( zipout, path, entries[i] );
      }
      onIterationEnd( path, entries[i].isDirectory(), length );
    }
  }

  /**
   * Packs the content of a single File into the ZIP file.
   * 
   * @param zipout     The OutputStream to access the ZIP file.
   * @param relative   The current relative path.
   * @param file       The File which shall be added.
   * 
   * @throws IOException   Some IO operation failed.
   */
  private void packFile( ZipOutputStream zipout, String relative, File file ) throws IOException {
    InputStream input = null;
    try {
      input           = IoFunctions.newInputStream( file );
      ZipEntry zentry = new ZipEntry( relative );
      zipout.putNextEntry( zentry );
      IoFunctions.copy( input, zipout, buffer );
      zipout.closeEntry();
    } finally {
      MiscFunctions.close( input );
    }
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing an {@link FailureException}.
   * 
   * @param ex   The cause of the failure. Not <code>null</code>.
   */
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw new FailureException( FailureCode.IO, ex );
  }
  
  /**
   * Will be invoked whenever the decompression begins.
   * 
   * @param name   The name of the zipfile entry. Neither <code>null</code> nor empty.
   * @param dir    <code>true</code> <=> The entry is a directory.
   * @param size   If this is a file, then this is the uncompressed length of it.
   */
  protected void onIterationBegin( @NonNull String name, boolean dir, long size ) {
  }

  /**
   * Will be invoked whenever the decompression ends.
   * 
   * @param name   The name of the zipfile entry. Neither <code>null</code> nor empty.
   * @param dir    <code>true</code> <=> The entry is a directory.
   * @param size   If this is a file, then this is the uncompressed length of it.
   */
  protected void onIterationEnd( @NonNull String name, boolean dir, long size ) {
  }
  
} /* ENDCLASS */
