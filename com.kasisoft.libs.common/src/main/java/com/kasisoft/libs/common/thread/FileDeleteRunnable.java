/**
 * Name........: FileDeleteRunnable
 * Description.: A Runnable implementation used to delete a file.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.constants.*;

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
   * @param files   The files that has to be deleted. Not <code>null</code> but they files are not required to be 
   *                existent.
   */
  public FileDeleteRunnable( File ... files ) {
    super( files );
  }
  
  @Override
  protected void execute() {
    
    super.execute();
    
    List<File>  files       = getFiles();
    List<File>  directories = getDirectories();
    int         retries     = CommonProperty.IoRetries.getValue( System.getProperties() ).intValue();
    retries                *= (files.size() + directories.size());
    
    // 1. delete all files
    retries = delete( files, retries );
    
    // 2. delete all directories
    retries = delete( directories, retries );
    
  }
  
  /**
   * Deletes all resources which are supposed to become deleted.
   * 
   * @param resources   A list of resources which is supposed to be deleted. Not <code>null</code>.
   * @param retries     The current number of retries used as an abortion criteria.
   * 
   * @return   An updated number of retries used as an abortion criteria.
   */
  private int delete( List<File> resources, int retries ) {
    while( (! isStopped()) && (! resources.isEmpty()) ) {
      for( int i = resources.size() - 1; (! isStopped()) && (i >= 0); i-- ) {
        File candidate = resources.get(i);
        if( candidate.exists() ) {
          if( candidate.delete() ) {
            // the resource could be removed without any problem
            resources.remove(i);
          } else {
            retries--;
          }
        } else {
          // the resource doesn't exist in the first place
          resources.remove(i);
        }
        if( retries == 0 ) {
          stop();
        }
      }
    }
    return retries;
  }

} /* ENDCLASS */
