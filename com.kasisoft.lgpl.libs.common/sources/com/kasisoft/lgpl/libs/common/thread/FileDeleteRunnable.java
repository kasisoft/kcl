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
public class FileDeleteRunnable extends AbstractRunnable {

  private List<File>   deletables;
  
  /**
   * Initialises this Runnable which is used to delete a single file.
   * 
   * @param files   The files that has to be deleted. Not <code>null</code> but they files are
   *                not required to be existent.
   */
  public FileDeleteRunnable( File ... files ) {
    deletables = new ArrayList<File>();
    for( File file : files ) {
      deletables.add( file );
    }
  }
  
  /**
   * {@inheritDoc}
   */
  protected void execute() {
    int retries = ((Integer) CommonProperty.IoRetries.getValue()).intValue();
    retries    *= deletables.size();
    while( (! isStopped()) && (! deletables.isEmpty()) ) {
      for( int i = deletables.size() - 1; i >= 0; i-- ) {
        if( deletables.get(i).exists() ) {
          if( deletables.get(i).delete() ) {
            // the resource could be removed without any problem
            deletables.remove(i);
          }
        } else {
          // the resource doesn't exist in the first place
          deletables.remove(i);
        }
        retries--;
        if( retries == 0 ) {
          stop();
        }
      }
    }
  }

} /* ENDCLASS */
