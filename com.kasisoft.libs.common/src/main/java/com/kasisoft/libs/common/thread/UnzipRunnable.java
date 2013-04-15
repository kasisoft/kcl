/**
 * Name........: UnzipRunnable
 * Description.: A Runnable that is used to perform an unzip process. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;
import java.util.zip.*;

import java.io.*;

/**
 * A Runnable that is used to perform an unzip process.
 */
public class UnzipRunnable extends AbstractRunnable {

  private File      zip;
  private File      destination;
  private Integer   buffersize;

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack.
   * @param destdir   The destination directory.
   */
  public UnzipRunnable( File zipfile, File destdir ) {
    this( zipfile, destdir, null );
  }

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack.
   * @param destdir   The destination directory.
   * @param size      The size for an internally used buffer. A value of <code>null</code> indicates the use of a default 
   *                  value.
   */
  public UnzipRunnable( File zipfile, File destdir, Integer size ) {
    zip         = zipfile;
    destination = destdir.getAbsoluteFile();
    buffersize  = size;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  protected void execute() {
    
    byte[] buffer  = IoFunctions.allocateBytes( buffersize );
    try( ZipFile zipfile = new ZipFile( zip ) ) {

      Enumeration<? extends ZipEntry> entries = zipfile.entries();
      while( (! isStopped()) && entries.hasMoreElements() ) {
        
        ZipEntry entry  = entries.nextElement();
        
        onIterationBegin( entry.getName(), entry.isDirectory(), entry.getSize() );
        
        File     file   = new File( destination, entry.getName() );
        
        if( entry.isDirectory() ) {
          IoFunctions.mkdirs( file );
          onIterationEnd( entry.getName(), true, 0L );
          continue;
        }
        
        File parent = file.getParentFile();
        if( ! parent.isDirectory() ) {
          IoFunctions.mkdirs( file.getParentFile() );
        }
        
        OutputStream outstream = null;
        InputStream  instream  = null;
        try {
          outstream = IoFunctions.newOutputStream( file );
          instream  = zipfile.getInputStream( entry );
          IoFunctions.copy( instream, outstream, buffer );
        } finally {
          MiscFunctions.close( instream  );
          MiscFunctions.close( outstream );
        }
        
        onIterationEnd( entry.getName(), false, file.length() );
        
      }
      
    } catch( IOException  ex ) {
      handleIOFailure( ex );
    } finally {
      IoFunctions.releaseBytes( buffer );
    }
    
  }
  
  /**
   * Will be invoked whenever the decompression begins.
   * 
   * @param name   The name of the zipfile entry.
   * @param dir    <code>true</code> <=> The entry is a directory.
   * @param size   If this is a file, then this is the uncompressed length of it. A value of -1 indicates that the 
   *               length is not known.
   */
  protected void onIterationBegin( String name, boolean dir, long size ) {
  }

  /**
   * Will be invoked whenever the decompression ends.
   * 
   * @param name   The name of the zipfile entry.
   * @param dir    <code>true</code> <=> The entry is a directory.
   * @param size   If this is a file, then this is the uncompressed length of it.
   */
  protected void onIterationEnd( String name, boolean dir, long size ) {
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing an {@link FailureException}.
   * 
   * @param ex   The cause of the failure.
   */
  protected void handleIOFailure( IOException ex ) {
    throw new FailureException( FailureCode.IO, ex );
  }
  
} /* ENDCLASS */
