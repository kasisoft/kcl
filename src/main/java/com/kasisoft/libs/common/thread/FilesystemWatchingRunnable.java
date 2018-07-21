package com.kasisoft.libs.common.thread;

import static java.nio.file.StandardWatchEventKinds.*;

import com.kasisoft.libs.common.base.*;

import lombok.experimental.*;

import lombok.*;

import java.util.concurrent.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * This {@link Runnable} implementation allows to watch a directory for filesystem changes recursively.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemWatchingRunnable extends AbstractRunnable {

  private static final int MIN_POLLINGTIME = 300;
  
  WatchService    watcher;
  Set<WatchKey>   keys;
  boolean         recursive;
  
  @Getter
  Path            root;
  
  @Getter
  long            pollingTime;
  
  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   */
  public FilesystemWatchingRunnable( @NonNull File dir ) {
    commonInit( Paths.get( dir.toURI() ), true );
  }
  
  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   */
  public FilesystemWatchingRunnable( @NonNull Path dir ) {
    commonInit( dir, true );
  }

  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   * @param rec   <code>true</code> <=> Consider subdirectories as well. Otherwise only the directory is watched.
   */
  public FilesystemWatchingRunnable( @NonNull File dir, boolean rec ) {
    commonInit( Paths.get( dir.toURI() ), rec );
  }
  
  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   * @param rec   <code>true</code> <=> Consider subdirectories as well. Otherwise only the directory is watched.
   */
  public FilesystemWatchingRunnable( @NonNull Path dir, boolean rec ) {
    commonInit( dir, rec );
  }
  
  
  private void commonInit( Path dir, boolean rec ) {
    keys        = new HashSet<>();
    root        = dir;
    pollingTime = MIN_POLLINGTIME;
    recursive   = rec;
  }

  /**
   * Changes the current polling time which cannot be set to a value lower than 300 ms.
   * 
   * @param newPollingTime   The new polling time for the watch keys in milli seconds.
   */
  public void setPollingTime( long newPollingTime ) {
    pollingTime = Math.max( newPollingTime, MIN_POLLINGTIME );
  }
  
  @Override
  public void execute() {
    
    try( WatchService watcher = FileSystems.getDefault().newWatchService() ) {
      
      this.watcher = watcher;
      
      // register all directories
      register( root );
      if( recursive ) {
        registerChildren( root );
      }
      
      startup();
      
      // process all filesystem events
      while( ! isStopped() ) {
        maintenance();
        cancelNonExistingResources();
        removeInvalidKeys();
        processNextEvents();
      }
      
    } catch( Exception ex ) {
      
      handleFailure( ex );
      
    } finally {
      
      // cleanup
      cancelAll();
      
      shutdown();
      
    }
    
  }
  
  @Override
  public boolean isStopped() {
    // we're also done if we're not watching anything anymore
    return keys.isEmpty() || super.isStopped();
  }
  
  /**
   * Removes all watch keys that aren't valid anymore as they don't provide any events.
   */
  private void removeInvalidKeys() {
    WatchKey[] list = keys.toArray( new WatchKey[ keys.size() ] );
    for( WatchKey key : list ) {
      if( isStopped() ) {
        break;
      }
      if( ! key.isValid() ) {
        keys.remove( key );
      }
    }
  }

  /**
   * Cancel all watch keys for resources that don't exist anymore.
   */
  private void cancelNonExistingResources() {
    for( WatchKey key : keys ) {
      if( isStopped() ) {
        break;
      }
      if( ! Files.exists( (Path) key.watchable() ) ) {
        key.cancel();
      }
    }
  }

  /**
   * Cancel all request and clear the keys.
   */
  private void cancelAll() {
    for( WatchKey key : keys ) {
      key.cancel();
    }
    keys.clear();
  }
  
  /**
   * Registers all children of the supplied directory.
   *  
   * @param parent   The directory which provides the children to be watched. Not <code>null</code>.
   */
  private void registerChildren( Path parent ) {
    File[] files = parent.toFile().listFiles();
    if( files != null ) {
      for( File file : files ) {
        if( file.isDirectory() ) {
          Path child = Paths.get( file.toURI() );
          register( child );
          registerChildren( child );
        }
      }
    }
  }
  
  /**
   * Registers the supplied directory for being watched.
   * 
   * @param path   The path of the directory that shall be watched.
   */
  private void register( Path path ) {
    try {
      keys.add( path.register( watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY ) );
    } catch( IOException ex ) {
      errorOnPath( path, ex );
    }
  }

  /**
   * Process the next events. 
   */
  private void processNextEvents() {
    WatchKey key = poll();
    if( key != null ) {
      processWatchEvents( key, key.pollEvents() );
      if( ! key.reset() ) {
        keys.remove( key );
      }
    }
  }
  
  /**
   * Polls the next key which provides a filesystem change.
   * 
   * @return   The next key which provides a filesystem change. Not <code>null</code>.
   */
  private WatchKey poll() {
    try {
      return watcher.poll( pollingTime, TimeUnit.MILLISECONDS );
    } catch( InterruptedException ex ) {
      return watcher.poll();
    }
  }
  
  /**
   * Processes all events for the supplied key.
   * 
   * @param key      The key associated with the supplied events. Not <code>null</code>.
   * @param events   A list of events coming from the filesystem. Not <code>null</code>.
   */
  private void processWatchEvents( WatchKey key, List<WatchEvent<?>> events ) {
    for( WatchEvent<?> event : events ) {
      if( isStopped() ) {
        break;
      }
      WatchEvent.Kind<?> kind = event.kind();
      if( kind == OVERFLOW ) {
        // can be ignored (it's always present)
        continue;
      }
      processWatchEvent( key, (WatchEvent<Path>) event );
    }
  }
  
  /**
   * Processes a single event for the supplied key.
   * 
   * @param key     The key associated with the supplied events. Not <code>null</code>.
   * @param event   An event coming from the filesystem. Not <code>null</code>.
   */
  private void processWatchEvent( WatchKey key, WatchEvent<Path> event ) {
    Path               filename = event.context();
    Path               path     =  ((Path) key.watchable()).resolve( filename );
    WatchEvent.Kind<?> kind     = event.kind();
    if( Files.isDirectory( path ) && (kind == ENTRY_CREATE) ) {
      if( recursive ) {
        register( path );
      }
    }
    if( isInterestingPath( path, kind == ENTRY_DELETE ) ) {
      processPath( path );
    }
  }
  
  /**
   * Returns <code>true</code> if the supplied path should be processed.
   * 
   * @param path       The path which identifies the change. Since a change might be a deletion it doesn't necessarily exist.
   *                   Not <code>null</code>.
   * @param deletion   <code>true</code> <=> We're dealing with a deletion here.
   *               
   * @return   <code>true</code> <=> The supplied path should be processed.
   */
  protected boolean isInterestingPath( Path path, boolean deletion ) {
    return isInterestingPath( path );
  }

  /**
   * Returns <code>true</code> if the supplied path should be processed.
   * 
   * @param path   The path which identifies the change. Since a change might be a deletion it doesn't necessarily exist.
   *               Not <code>null</code>.
   *               
   * @return   <code>true</code> <=> The supplied path should be processed.
   */
  protected boolean isInterestingPath( Path path ) {
    return true;
  }

  /**
   * Will be invoked whenever a path change happened.
   * 
   * @param path   The path which identifies the change. Since a change might be a deletion it doesn't necessarily exist.
   *               Not <code>null</code>.
   */
  protected void processPath( Path path ) {
  }

  /**
   * Will be invoked before the actual scheduling takes place.
   */
  protected void startup() {
  }
  
  /**
   * Will be invoked after all keys have been cleaned up so no more path will be passed to #processPath(Path).
   */
  protected void shutdown() {
  }

  /**
   * Will be invoked for each each event allowing to perform some maintenance operations.
   */
  protected void maintenance() {
  }
  
  /**
   * The registration to watch on the supplied directory failed. This function executes {@link #handleFailure(Exception)}
   * by default.
   * 
   * @param path   The directory that is supposed to be watched. Not <code>null</code>.
   * @param ex     The error that indicates the problem. Not <code>null</code>.
   */
  protected void errorOnPath( Path path, IOException ex ) {
    handleFailure( ex );
  }
  
  /**
   * This function will be invoked whenever an error occurs.
   * 
   * @param ex   The error which indicates the cause of a failure.
   */
  protected void handleFailure( Exception ex ) {
    throw KclException.wrap( ex );
  }
  
} /* ENDCLASS */
