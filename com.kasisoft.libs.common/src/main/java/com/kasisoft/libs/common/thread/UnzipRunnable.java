package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;

import java.util.*;
import java.util.zip.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A Runnable that is used to perform an unzip process.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnzipRunnable extends AbstractRunnable {

  File      zip;
  File      destination;
  Integer   buffersize;

  /**
   * Initializes this Thread used to unpack a ZIP file.
   * 
   * @param zipfile   The ZIP file to unpack. Not <code>null</code>.
   * @param destdir   The destination directory. Not <code>null</code>.
   */
  public UnzipRunnable( @NonNull File zipfile, @NonNull File destdir ) {
    this( zipfile, destdir, null );
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
    zip         = zipfile;
    destination = destdir.getAbsoluteFile();
    buffersize  = size;
  }
  
  @Override
  protected void execute() {
    
    byte[] buffer  = Primitive.PByte.<byte[]>getBuffers().allocate( buffersize );
    ZipFile zipfile = null;
    try {

      zipfile                                 = new ZipFile( zip );
      Enumeration<? extends ZipEntry> entries = zipfile.entries();
      while( (! isStopped()) && entries.hasMoreElements() ) {
        
        ZipEntry entry  = entries.nextElement();
        
        File     file   = new File( destination, entry.getName() );
        
        if( entry.isDirectory() ) {
          IoFunctions.mkdirs( file );
          continue;
        }
        
        File parent = file.getParentFile();
        if( ! parent.isDirectory() ) {
          IoFunctions.mkdirs( file.getParentFile() );
        }
        
        try(
          OutputStream outstream = IoFunctions.newOutputStream( file );
          InputStream  instream  = zipfile.getInputStream( entry );
        ) {
          IoFunctions.copy( instream, outstream, buffer );
        }
        
      }
      
    } catch( IOException  ex ) {
      handleIOFailure( ex );
    } finally {
      Primitive.PByte.<byte[]>getBuffers().release( buffer );
    }
    
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing an {@link FailureException}.
   * 
   * @param ex   The cause of the failure. Not <code>null</code>.
   */
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw FailureCode.IO.newException( ex );
  }
  
} /* ENDCLASS */
