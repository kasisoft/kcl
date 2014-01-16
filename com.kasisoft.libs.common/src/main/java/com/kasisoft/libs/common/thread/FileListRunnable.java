/**
 * Name........: FileListRunnable
 * Description.: Implementation allowing to traverse a directory structure. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.io.*;

import java.util.regex.*;

import java.util.*;

import java.io.*;

/**
 * Implementation allowing to traverse a directory structure.
 */
public class FileListRunnable extends AbstractRunnable<FileProgress> {

  private static final File[] EMPTY_LIST = new File[0];
  
  private boolean                 incdirs;
  private boolean                 incfiles;
  private FileFilter              filter;
  private ProtectableList<File>   dirreceiver;
  private ProtectableList<File>   filereceiver;
  private File[]                  roots;
  private FileProgress            progress;
  private boolean                 configured;
  private Pattern                 filepattern;
  private Pattern                 dirpattern;

  /**
   * Initialises this file lister allowing to collect resources selectively.
   */
  public FileListRunnable() {
    this( (File[]) null );
  }

  /**
   * Initialises this file lister allowing to collect resources selectively.
   * 
   * @param files   The list of resources to traverse initially. Maybe <code>null</code>.
   */
  public FileListRunnable( File ... files ) {
    incdirs       = true;
    incfiles      = true;
    filter        = null;
    filepattern   = null;
    dirpattern    = null;
    progress      = new FileProgress();
    dirreceiver   = new ProtectableList<File>();
    filereceiver  = new ProtectableList<File>();
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
    roots       = null;
    configured  = false;
  }
  
  /**
   * Provides an Ant like filesystem pattern which allows to filter directories.
   * 
   * @param newpattern   The new pattern which allows to filter directories. Maybe <code>null</code>.
   */
  public void setDirPattern( String newpattern ) {
    if( newpattern != null ) {
      dirpattern = IoFunctions.compileFilesystemPattern( newpattern );
    } else {
      dirpattern = null;
    }
  }

  /**
   * Provides an Ant like filesystem pattern which allows to filter files.
   * 
   * @param newpattern   The new pattern which allows to filter files. Maybe <code>null</code>.
   */
  public void setFilePattern( String newpattern ) {
    if( newpattern != null ) {
      filepattern = IoFunctions.compileFilesystemPattern( newpattern );
    } else {
      filepattern = null;
    }
  }

  /**
   * Sets the resources which have to be traversed.
   * 
   * @param files   The list of resources which have to be traversed. Maybe <code>null</code>.
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

  @Override
  protected void execute() {
    
    if( ! configured ) {
      return;
    }

    progress.setTotal(-1);
    progress( progress );
    
    // we're using write protected lists depending on the current settings
    filereceiver.protect  = ! incfiles;
    dirreceiver.protect   = ! incdirs;
    
    StringBuilder buffer = new StringBuilder();
    for( File resource : roots ) {
      
      if( isStopped() ) {
        break;
      }
      
      buffer.setLength(0);
      iterate( filereceiver, dirreceiver, buffer, resource );
        
    }
      
  }

  /**
   * Iterates through the supplied directory.
   * 
   * @param files   The receiver for file instances. Not <code>null</code>.
   * @param dirs    The receiver for directory instances. Not <code>null</code>.
   * @param path    A buffer providing the relative path. Not <code>null</code>.
   * @param dir     The directory that is supposed to be processed. Not <code>null</code>.
   */
  private void iterateDir( List<File> files, List<File> dirs, StringBuilder path, File dir ) {
    File[] children = dir.listFiles();
    if( children != null ) {
      for( File child : children ) {
        if( isStopped() ) { 
          break;
        }
        iterate( files, dirs, path, child );
      }
    }
  }
  
  /**
   * Iterates through the supplied resource.
   * 
   * @param files     The receiver for file instances. Not <code>null</code>.
   * @param dirs      The receiver for directory instances. Not <code>null</code>.
   * @param path      A buffer providing the relative path. Not <code>null</code>.
   * @param current   The resource that is supposed to be processed. Not <code>null</code>.
   */
  private void iterate( List<File> files, List<File> dirs, StringBuilder path, File current ) {
    int oldlength = path.length();
    path.append( current.getName() );
    if( accept( current, path ) ) {
      if( current.isFile() ) {
        progressUpdate( current );
        files.add( current );
      } else if( current.isDirectory() ) {
        progressUpdate( current );
        dirs.add( current );
        path.append( "/" );
        iterateDir( files, dirs, path, current );
      }
    }
    path.setLength( oldlength );
  }
  
  /**
   * Returns <code>true</code> if the supplied file is acceptable.
   * 
   * @param file   The file that is supposed to be tested. Not <code>null</code>.
   * @param path   The current relative path for the supplied file. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> The supplied file is acceptable.
   */
  private boolean accept( File file, StringBuilder path ) {
    boolean result = true;
    if( filter != null ) {
      result = filter.accept( file );
    }
    if( result ) {
      if( file.isFile() ) {
        if( filepattern != null ) {
          result = filepattern.matcher( path.toString() ).matches();
        }
      } else if( file.isDirectory() ) {
        if( dirpattern != null ) {
          result = dirpattern.matcher( path.toString() ).matches();
        }
      }
    }
    return result;
  }
  
  /**
   * Performs a small update for the supplied resource.
   * 
   * @param file   The resource that is currently being processed. Not <code>null</code>.
   */
  private void progressUpdate( File file ) {
    progress.setFile( file );
    progress.setCurrent( progress.getCurrent() + 1 );
    progress( progress );
  }
  
  private static class ProtectableList<T> extends ArrayList<T> {
    
    private boolean   protect = false;
    
    @Override
    public boolean add( T element ) {
      if( protect ) {
        return true;
      }
      return super.add( element );
    }
    
  } /* ENDCLASS */
    
} /* ENDCLASS */
