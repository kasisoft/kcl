package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;

import java.util.zip.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A Runnable that is used to ZIP a directory.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZipRunnable extends AbstractRunnable {

  File      zipfile;
  File      sourcedir;
  Integer   buffersize;
  byte[]    buffer;

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
    try( ZipOutputStream zipout = new ZipOutputStream( IoFunctions.newOutputStream( zipfile ) ) ) {
      buffer = Primitive.PByte.<byte[]>getBuffers().allocate( buffersize );
      zipout.setMethod( ZipOutputStream.DEFLATED );
      zipout.setLevel(9);
      packDir( zipout, "", sourcedir );
    } catch( IOException ex ) {
      handleIOFailure( ex );
    } finally {
      Primitive.PByte.<byte[]>getBuffers().release( buffer );
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
      if( entries[i].isDirectory() ) {
        packDir( zipout, path, entries[i] );
      } else {
        packFile( zipout, path, entries[i] );
      }
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
    try( InputStream input = IoFunctions.newInputStream( file ) ) {
      ZipEntry zentry = new ZipEntry( relative );
      zipout.putNextEntry( zentry );
      IoFunctions.copy( input, zipout, buffer );
      zipout.closeEntry();
    }
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing an {@link FailureException}.
   * 
   * @param ex   The cause of the failure. Not <code>null</code>.
   */
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw FailureException.newFailureException( FailureCode.IO, ex );
  }
  
} /* ENDCLASS */
