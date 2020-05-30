package com.kasisoft.libs.common.old.thread;

import lombok.experimental.*;

import lombok.*;

import static com.kasisoft.libs.common.old.constants.Primitive.*;

import com.kasisoft.libs.common.old.base.*;
import com.kasisoft.libs.common.old.io.*;

import java.util.stream.*;

import java.util.zip.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * A Runnable that is used to ZIP a directory.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ZipRunnable extends AbstractRunnable {

  Path      zipfile;
  Path      sourcedir;
  Integer   buffersize;

  /**
   * Configures this Runnable to zip a directory.
   * 
   * @param zip   The ZIP archive. Not <code>null</code>.
   * @param dir   The directory which shall be zipped. Not <code>null</code>.
   */
  public ZipRunnable( @NonNull File zip, @NonNull File dir ) {
    commonInit( zip.toPath(), dir.toPath(), null );
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
    commonInit( zip.toPath(), dir.toPath(), size );
  }

  /**
   * Configures this Runnable to zip a directory.
   * 
   * @param zip   The ZIP archive. Not <code>null</code>.
   * @param dir   The directory which shall be zipped. Not <code>null</code>.
   */
  public ZipRunnable( @NonNull Path zip, @NonNull Path dir ) {
    commonInit( zip, dir, null );
  }

  /**
   * Configures this Runnable to zip a directory.
   * 
   * @param zip    The ZIP archive. Not <code>null</code>.
   * @param dir    The directory which shall be zipped. Not <code>null</code>.
   * @param size   The size used for the internal buffers. A value of <code>null</code> means that the default 
   *               buffersize will be used.
   */
  public ZipRunnable( @NonNull Path zip, @NonNull Path dir, Integer size ) {
    commonInit( zip, dir, size );
  }

  private void commonInit( Path zip, Path dir, Integer size ) {
    zipfile    = zip;
    sourcedir  = dir;
    buffersize = size;
  }
  
  @Override
  protected void execute() {
    PByte.withBufferDo( buffersize, this::pack );
  }
  
  private void pack( byte[] buffer ) {
    try( ZipOutputStream zipout = new ZipOutputStream( IoFunctions.newOutputStream( zipfile ) ) ) {
      zipout.setMethod( ZipOutputStream.DEFLATED );
      zipout.setLevel(9);
      packDir( buffer, zipout, sourcedir, sourcedir );
    } catch( IOException ex ) {
      handleIOFailure( ex );
    }
  }

  /**
   * Packs the content of a complete directory into the ZIP file.
   * 
   * @param buffer     The buffer to be used. Not <code>null</code>.
   * @param zipout     The OutputStream to access the ZIP file. Not <code>null</code>.
   * @param basedir    The base directory. Not <code>null</code>.
   * @param dir        The directory which shall be added. Not <code>null</code>.
   * 
   * @throws IOException   Some IO operation failed.
   */
  private void packDir( byte[] buffer, ZipOutputStream zipout, Path basedir, Path dir ) throws IOException {
    List<Path> entries = Files.list( dir ).collect( Collectors.toList() );
    for( int i = 0; (i < entries.size()) && (! Thread.currentThread().isInterrupted()); i++ ) {
      Path entry = entries.get(i);
      if( Files.isDirectory( entry ) ) {
        packDir( buffer, zipout, basedir, entry );
      } else {
        packFile( buffer, zipout, basedir, entry );
      }
    }
  }
  
  /**
   * Packs the content of a single File into the ZIP file.
   * 
   * @param buffer     The buffer to be used. Not <code>null</code>.
   * @param zipout     The OutputStream to access the ZIP file.
   * @param relative   The current relative path.
   * @param file       The File which shall be added.
   * 
   * @throws IOException   Some IO operation failed.
   */
  private void packFile( byte[] buffer, ZipOutputStream zipout, Path basedir, Path file ) throws IOException {
    String relative = basedir.relativize( file ).toString();
    try( InputStream input = IoFunctions.newInputStream( file ) ) {
      ZipEntry zentry = new ZipEntry( relative );
      zipout.putNextEntry( zentry );
      IoFunctions.copy( input, zipout, buffer, null );
      zipout.closeEntry();
    }
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing an {@link FailureException}.
   * 
   * @param ex   The cause of the failure. Not <code>null</code>.
   */
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw KclException.wrap( ex );
  }
  
} /* ENDCLASS */
