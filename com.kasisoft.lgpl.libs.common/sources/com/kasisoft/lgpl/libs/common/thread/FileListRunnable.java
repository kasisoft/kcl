/**
 * Name........: FileListRunnable
 * Description.: Implementation allowing to traverse a directory structure. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import java.util.*;

import java.io.*;

/**
 * Implementation allowing to traverse a directory structure.
 */
public class FileListRunnable extends AbstractRunnable {

  private boolean       incdirs;
  private boolean       incfiles;
  private FileFilter    filter;
  private List<File>    dirreceiver;
  private List<File>    filereceiver;
  private File[]        roots;
  
  /**
   * Initialises this file lister allowing to collect resources selectively.
   * 
   * @param receiver   The list used to collect the file resources. Maybe <code>null</code>.
   * @param dirs       The list of resources to traverse initially.
   */
  public FileListRunnable( List<File> receiver, File ... dirs ) {
    incdirs       = true;
    incfiles      = true;
    filter        = null;
    dirreceiver   = receiver;
    filereceiver  = receiver;
    roots         = dirs;
  }

  /**
   * Initialises this file lister allowing to collect resources selectively.
   * 
   * @param dircollector    The list used to collect directories. Maybe <code>null</code>.
   * @param filecollector   The list used to collect files. Maybe <code>null</code>.
   * @param dirs            The list of resources to traverse initially.
   */
  public FileListRunnable( List<File> dircollector, List<File> filecollector, File ... dirs ) {
    incdirs       = true;
    incfiles      = true;
    filter        = null;
    dirreceiver   = dircollector;
    filereceiver  = filecollector;
    roots         = dirs;
  }

  
  /**
   * Changes the filter that has to be used.
   * 
   * @param newfilter   The new filter that has to be used. Maybe <code>null</code>.
   */
  public void setFilter( FileFilter newfilter ) {
    filter = newfilter;
  }
  
  /**
   * Returns the current filter that will be used for the traversal.
   * 
   * @return   The current filter that will be used for the traversal. Maybe <code>null</code>.
   */
  public FileFilter getFilter() {
    return filter;
  }
  
  /**
   * Enables the inclusion of directories.
   * 
   * @param enable   <code>true</code> <=> Enable the inclusion of directories.
   */
  public void setIncludeDirs( boolean enable ) {
    incdirs = enable;
  }
  
  /**
   * Returns <code>true</code> if inclusion of directories is enabled.
   * 
   * @return   <code>true</code> <=> Inclusion of directories is enabled.
   */
  public boolean isIncludeDirs() {
    return incdirs;
  }
  
  /**
   * Enables the inclusion of files.
   * 
   * @param enable   <code>true</code> <=> Enable the inclusion of files.
   */
  public void setIncludeFiles( boolean enable ) {
    incfiles = enable;
  }
  
  /**
   * Returns <code>true</code> if inclusion of files is enabled.
   * 
   * @return   <code>true</code> <=> Inclusion of files is enabled.
   */
  public boolean isIncludeFiles() {
    return incfiles;
  }
  
  /**
   * {@inheritDoc}
   */
  protected void execute() {
    List<File> queue = new ArrayList<File>();
    for( File root : roots ) {
      queue.add( root ); 
    }
    FileFilter filefilter = null;
    if( filter != null ) {
      filefilter = filter;
    } else {
      filefilter = new AcceptAllFilter();
    }
    while( (! isStopped()) && (! queue.isEmpty()) ) {
      File    current = queue.remove(0);
      boolean accept  = filefilter.accept( current );
      if( accept ) {
        if( current.isFile() ) {
          if( incfiles && (filereceiver != null) ) {
            // collect the file
            filereceiver.add( current );
          }
        } else if( current.isDirectory() ) {
          File[] children = current.listFiles( filefilter );
          if( incdirs && (dirreceiver != null) ) {
            // collect the directory
            dirreceiver.add( current );
          }
          if( children != null ) {
            for( File child : children ) {
              queue.add( child );
            }
          }
        } else {
          // we're ignoring everything which is neither a file nor a directory
        }
      }
    }
  }
  
  /**
   * Simple helper which is used to accept all File records.
   */
  private static final class AcceptAllFilter implements FileFilter {

    /**
     * {@inheritDoc}
     */
    public boolean accept( File pathname ) {
      return true;
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
