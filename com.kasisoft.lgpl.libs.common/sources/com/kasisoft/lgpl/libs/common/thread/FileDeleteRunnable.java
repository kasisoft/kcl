/**
 * Name........: FileDeleteRunnable
 * Description.: A Runnable implementation used to delete a file.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.libs.common.constants.*;

import java.util.*;

import java.io.*;

/**
 * A Runnable implementation used to delete a file.
 */
public class FileDeleteRunnable extends FileListRunnable {

  /**
   * Initialises this Runnable which is used to delete a bunch of filesystem resources.
   */
  public FileDeleteRunnable() {
    super();
  }

  /**
   * Initialises this Runnable which is used to delete a bunch of filesystem resources.
   * 
   * @param files   The files that has to be deleted. Not <code>null</code> but they files are
   *                not required to be existent.
   */
  public FileDeleteRunnable( File ... files ) {
    super( files );
  }
  
  /**
   * {@inheritDoc}
   */
  protected void execute() {
    
    super.execute();
    
    List<File>  files       = getFiles();
    List<File>  directories = getDirectories();
    int         retries     = ((Integer) CommonProperty.IoRetries.getValue()).intValue();
    retries                *= (files.size() + directories.size());
    
    // 1. delete all files
    while( (! isStopped()) && (! files.isEmpty()) ) {
      for( int i = files.size() - 1; i >= 0; i-- ) {
        File candidate = files.get(i);
        if( candidate.exists() ) {
          if( candidate.delete() ) {
            // the resource could be removed without any problem
            files.remove(i);
          }
        } else {
          // the resource doesn't exist in the first place
          files.remove(i);
        }
        retries--;
        if( retries == 0 ) {
          stop();
        }
      }
    }
    
    // 2. delete all directories
    while( (! isStopped()) && (! directories.isEmpty()) ) {
      for( int i = directories.size() - 1; i >= 0; i-- ) {
        File candidate = directories.get(i);
        if( candidate.exists() ) {
          if( candidate.delete() ) {
            // the resource could be removed without any problem
            directories.remove(i);
          }
        } else {
          // the resource doesn't exist in the first place
          directories.remove(i);
        }
        retries--;
        if( retries == 0 ) {
          stop();
        }
      }
    }
    
  }

} /* ENDCLASS */
