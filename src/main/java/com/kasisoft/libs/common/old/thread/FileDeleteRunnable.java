package com.kasisoft.libs.common.old.thread;

import static com.kasisoft.libs.common.old.base.LibConfig.cfgIoRetries;

import java.util.List;

import java.io.File;

import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A Runnable implementation used to delete a file.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@NoArgsConstructor
public class FileDeleteRunnable extends FileListRunnable {

  /**
   * Initialises this Runnable which is used to delete a bunch of filesystem resources.
   * 
   * @param files   The files that has to be deleted. Not <code>null</code> but they files are not required to be 
   *                existent. Not <code>null</code>.
   */
  public FileDeleteRunnable( @NonNull File ... files ) {
    super( files );
  }
  
  @Override
  protected void execute() {
    
    super.execute();
    
    List<File>  files       = getFiles();
    List<File>  directories = getDirectories();
    int         retries     = cfgIoRetries();
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
