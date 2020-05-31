package com.kasisoft.libs.common.old.thread;

import static com.kasisoft.libs.common.old.constants.Primitive.PByte;
import static com.kasisoft.libs.common.old.io.DefaultIO.PATH_OUTPUTSTREAM_EX;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.io.IoFunctions;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.NonNull;

/**
 * A Runnable that is used to perform an unzip process.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnzipRunnable extends AbstractRunnable {

  Path      zip;
  Path      destination;
  Integer   buffersize;

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack. Not <code>null</code>.
   * @param destdir   The destination directory. Not <code>null</code>.
   */
  public UnzipRunnable( @NonNull File zipfile, @NonNull File destdir ) {
    commonInit( zipfile.toPath(), destdir.toPath(), null );
  }

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack. Not <code>null</code>.
   * @param destdir   The destination directory. Not <code>null</code>.
   * @param size      The size for an internally used buffer. A value of <code>null</code> indicates the use of a default 
   *                  value.
   */
  public UnzipRunnable( @NonNull File zipfile, @NonNull File destdir, Integer size ) {
    commonInit( zipfile.toPath(), destdir.toPath(), size );
  }

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack. Not <code>null</code>.
   * @param destdir   The destination directory. Not <code>null</code>.
   */
  public UnzipRunnable( @NonNull Path zipfile, @NonNull Path destdir ) {
    commonInit( zipfile, destdir, null );
  }

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack. Not <code>null</code>.
   * @param destdir   The destination directory. Not <code>null</code>.
   * @param size      The size for an internally used buffer. A value of <code>null</code> indicates the use of a default 
   *                  value.
   */
  public UnzipRunnable( @NonNull Path zipfile, @NonNull Path destdir, Integer size ) {
    commonInit( zipfile, destdir, size );
  }
  
  private void commonInit( Path zipfile, Path destdir, Integer size ) {
    zip         = zipfile.normalize();
    destination = destdir.normalize();
    buffersize  = size;
  }
  
  @Override
  protected void execute() {
    PByte.withBufferDo( buffersize, this::unpack );
  }
  
  private void unpack( byte[] buffer ) {
    
    try( ZipFile zipfile = new ZipFile( zip.toFile() ) ) {

      Enumeration<? extends ZipEntry> entries = zipfile.entries();
      while( (! isStopped()) && entries.hasMoreElements() ) {
        
        ZipEntry entry  = entries.nextElement();
        
        Path     file   = destination.resolve( entry.getName() );
        
        if( entry.isDirectory() ) {
          IoFunctions.mkdirs( file );
          continue;
        }
        
        Path parent = file.getParent();
        if( ! Files.isDirectory( parent ) ) {
          IoFunctions.mkdirs( parent );
        }
        
        
        try( InputStream instream = zipfile.getInputStream( entry ) ) {
          PATH_OUTPUTSTREAM_EX.forOutputStreamDo( file, $ -> IoFunctions.copy( instream, $, null ) );
        }
        
      }
      
    } catch( IOException ex ) {
      handleIOFailure( ex );
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
