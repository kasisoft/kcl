package com.kasisoft.libs.common.old.thread;

import static com.kasisoft.libs.common.old.constants.Empty.NO_FILES;

import com.kasisoft.libs.common.old.io.IoFunctions;

import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileFilter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Implementation allowing to traverse a directory structure.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileListRunnable extends AbstractRunnable {

  @Getter @Setter boolean       includeDirs;
  @Getter @Setter boolean       includeFiles;
  @Getter @Setter FileFilter    filter;
  
  ProtectableList<File>         dirreceiver;
  ProtectableList<File>         filereceiver;
  File[]                        roots;
  boolean                       configured;
  Pattern                       filepattern;
  Pattern                       dirpattern;

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
    includeDirs   = true;
    includeFiles  = true;
    filter        = null;
    filepattern   = null;
    dirpattern    = null;
    dirreceiver   = new ProtectableList<>();
    filereceiver  = new ProtectableList<>();
    reset();
    configure( files );
  }
  
  /**
   * Initialises the object state. This only affects the parameters which can be set using {@link #configure(File...)}
   */
  private void reset() {
    dirreceiver   . clear();
    filereceiver  . clear();
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
      roots = NO_FILES;
    }
    configured  = true;
  }

  /**
   * Returns a list of all files collected by this runnable.
   * 
   * @return   A list of all files collected by this runnable. Not <code>null</code>.
   */
  public List<File> getAllFiles() {
    List<File> result = new ArrayList<>();
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
    return new ArrayList<>( filereceiver );
  }

  /**
   * Returns a list of directories collected by this runnable.
   * 
   * @return   A list of directories collected by this runnable. Not <code>null</code>.
   */
  public List<File> getDirectories() {
    return new ArrayList<>( dirreceiver );
  }

  @Override
  protected void execute() {
    
    if( ! configured ) {
      return;
    }

    // we're using write protected lists depending on the current settings
    filereceiver.protect  = ! includeFiles;
    dirreceiver.protect   = ! includeDirs;
    
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
        files.add( current );
      } else if( current.isDirectory() ) {
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
