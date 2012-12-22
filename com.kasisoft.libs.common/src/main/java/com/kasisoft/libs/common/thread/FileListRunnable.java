/**
 * Name........: FileListRunnable
 * Description.: Implementation allowing to traverse a directory structure. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import java.util.*;

import java.io.*;

/**
 * Implementation allowing to traverse a directory structure.
 */
public class FileListRunnable extends AbstractRunnable<FileProgress> {

  private static final File[] EMPTY_LIST = new File[0];
  
  private boolean         incdirs;
  private boolean         incfiles;
  private FileFilter      filter;
  private List<File>      dirreceiver;
  private List<File>      filereceiver;
  private File[]          roots;
  private FileProgress    progress;
  private boolean         configured;

  /**
   * Initialises this file lister allowing to collect resources selectively.
   */
  public FileListRunnable() {
    this( (File[]) null );
  }

  /**
   * Initialises this file lister allowing to collect resources selectively.
   * 
   * @param files   The list of resources to traverse initially.
   */
  public FileListRunnable( File ... files ) {
    incdirs       = true;
    incfiles      = true;
    filter        = null;
    progress      = new FileProgress();
    dirreceiver   = new ArrayList<File>();
    filereceiver  = new ArrayList<File>();
    reset();
    configure( files );
  }
  
  /**
   * Initialises the object state. This only affects the parameters which can be set using {@link #configure(File...)}
   */
  private void reset() {
    dirreceiver   . clear();
    filereceiver  . clear();
    progress.setCurrent(0);
    progress.setTotal(0);
    progress.setFile( null );
    roots       = null;
    configured  = false;
  }
  
  /**
   * Sets the resources which have to be traversed.
   * 
   * @param files   The list of resources which have to be traversed.
   */
  public void configure( File ... files ) {
    roots       = files;
    if( roots == null ) {
      roots = EMPTY_LIST;
    }
    configured  = true;
  }

  /**
   * Returns a list of all files collected by this runnable.
   * 
   * @return   A list of all files collected by this runnable. Not <code>null</code>.
   */
  public List<File> getAllFiles() {
    List<File> result = new ArrayList<File>();
    result.addAll( dirreceiver  );
    result.addAll( filereceiver );
    return result;
  }

  /**
   * Returns a list of files collected by this runnable.
   * 
   * @return   A list of files collected by this runnable. Not <code>null</code>.
   */
  public List<File> getFiles() {
    return new ArrayList<File>( filereceiver );
  }

  /**
   * Returns a list of directories collected by this runnable.
   * 
   * @return   A list of directories collected by this runnable. Not <code>null</code>.
   */
  public List<File> getDirectories() {
    return new ArrayList<File>( dirreceiver );
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
  @Override
  protected void execute() {
    
    if( ! configured ) {
      return;
    }
        
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
    
    progress.setTotal(-1);
    progress( progress );
    
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
            // add children to the queue for further processing
            for( File child : children ) {
              queue.add( child );
            }
          }
          
        } else {
          // we're ignoring everything which is neither a file nor a directory
        }
        
        progress.setFile( current );
        progress.setCurrent( progress.getCurrent() + 1 );
        progress( progress );
        
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
