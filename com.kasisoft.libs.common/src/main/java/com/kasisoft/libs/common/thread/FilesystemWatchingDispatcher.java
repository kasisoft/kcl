package com.kasisoft.libs.common.thread;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.function.*;

import lombok.experimental.*;

import lombok.*;

import java.util.concurrent.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * This extension of the FilesystemWatchingRunnable allows to easily watch a directory and process the files
 * through an ExecutorService.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemWatchingDispatcher extends FilesystemWatchingRunnable {

  ThreadPoolExecutor    executor;
  int                   coreThreads;
  int                   keepAlive;
  int                   maxPoolSize;
  int                   maxQueueSize;
  int                   maxTimeOut;
  Map<URI,Future<?>>    futures;
  Consumer<Path>        consumer;
  Predicate<Path>       pathTest;
  Consumer<Exception>   errorHandler;
  
  /**
   * Initializes this dispatcher to watch a certain directory.
   * 
   * @param dir   The directory which is supposed to be watched. Not <code>null</code>.
   * @param cse   The Consumer instance that will be called through this dispatcher. The implementation
   *              must be thread safe. Not <code>null</code>.
   */
  public FilesystemWatchingDispatcher( @NonNull File dir, @NonNull Consumer<Path> cse ) {
    super( dir );
    init( cse  );
  }

  /**
   * Initializes this dispatcher to watch a certain directory.
   * 
   * @param dir   The directory which is supposed to be watched. Not <code>null</code>.
   * @param cse   The Consumer instance that will be called through this dispatcher. The implementation
   *              must be thread safe. Not <code>null</code>.
   */
  public FilesystemWatchingDispatcher( @NonNull Path dir, @NonNull Consumer<Path> cse ) {
    super( dir );
    init( cse );
  }

  /**
   * Performs some basic configurations of this dispatcher.
   * 
   * @param cse   The Consumer instance that will be called through this dispatcher. The implementation
   *              must be thread safe. Not <code>null</code>.
   */
  private void init( Consumer<Path> cse ) {
    withCoreThreads( Runtime.getRuntime().availableProcessors() / 4 );
    withKeepAlive( 30 );
    withMaxPoolSize( coreThreads * 2 );
    withMaxQueueSize( maxPoolSize * 16 );
    futures      = new Hashtable<>();
    consumer     = cse;
    pathTest     = Predicates.acceptAll();
    errorHandler = $ -> {};
  }

  @Override
  protected final void handleFailure( Exception ex ) {
    errorHandler.accept( ex );
  }

  /**
   * Changes the number of core threads usually kept alive through the whole process. Values lower than 1
   * won't be accepted.
   * 
   * Recommendation: min of 2
   * Default: available processors / 4
   * 
   * @param newThreads   The new number to be set.
   *  
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withCoreThreads( int newThreads ) {
    coreThreads = Math.max( 1, newThreads );
    return (R) this;
  }

  /**
   * Changes the number of keep alive time in seconds which is the duration used until threads will be removed
   * from the internally used thread pool.
   * 
   * Recommendation: 30
   * Default: 30
   * 
   * @param newSeconds   The duration to keep threads alive. Values lower than 1 won't be accepted.
   * 
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withKeepAlive( int newSeconds ) {
    keepAlive = Math.max( 1, newSeconds );
    return (R) this;
  }

  /**
   * Changes the maximum pool size for the thread pool.
   * 
   * Recommendation: coreThreads * 2
   * Default: available processors / 2
   * 
   * @param newMaxPoolSize   The maximum number of threads provided by the pool. Values lower than 1 won't be
   *                         accepted.
   *                         
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withMaxPoolSize( int newMaxPoolSize ) {
    maxPoolSize = Math.max( 1, newMaxPoolSize );
    return (R) this;
  }

  /**
   * Specifies the maximum queue size. This queue size specifies the number of stored tasks for the execution
   * process. If that's exceeded the queue will block to schedule the corresponding execution.
   * 
   * Recommendation: A multiple of maxPoolSize
   * Default: maxPoolSize * 16
   * 
   * @param newQueueSize   The new size for the queue. Values lower than 1 won't be accepted.
   *
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withMaxQueueSize( int newQueueSize ) {
    maxQueueSize = Math.max( 1, newQueueSize );
    return (R) this;
  }

  /**
   * Specifies the maximum duration of seconds to wait while shutting down this dispatcher.
   *  
   * @param newMaxTimeOut   The maximum number of seconds to wait while shutting down this dispatcher. Values 
   *                        lower than 1 won't be accepted.
   *                        
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withMaxTimeOut( int newMaxTimeOut ) {
    maxTimeOut = Math.max( 1, newMaxTimeOut );
    return (R) this;
  }

  /**
   * Allows to specify a predicate in order to decide whether a path shall be processed or not.
   *  
   * @param test   The predicate used to test the path. Not <code>null</code>.
   *                        
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withPathTest( @NonNull Predicate<Path> test ) {
    pathTest = test;
    return (R) this;
  }

  /**
   * Specifies the error handler to be used for this watcher.
   *  
   * @param handler   The error handler to be used for this watcher. Not <code>null</code>.
   *                        
   * @return   this
   */
  public <R extends FilesystemWatchingDispatcher> R withErrorHandler( @NonNull Consumer<Exception> handler ) {
    errorHandler = handler;
    return (R) this;
  }

  @Override
  protected final void maintenance() {
    // drop all futures that are already processed
    Set<URI> keys = futures.keySet().stream().filter( k -> futures.get(k).isDone() ).collect( Collectors.toSet() );
    futures.keySet().removeAll( keys );
  }

  @Override
  protected final void startup() {
    if( executor != null ) {
      throw new IllegalStateException( error_fswatcher_already_started );
    }
    int poolSize  = Math.max( coreThreads, maxPoolSize );
    int queueSize = Math.max( poolSize, maxQueueSize );
    executor = new ThreadPoolExecutor( coreThreads, poolSize, keepAlive, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( queueSize ) );
  }

  @Override
  protected final void shutdown() {
    if( executor != null ) {
      ThreadPoolExecutor tpe = executor;
      executor               = null;
      tpe.shutdown();
      try {
        tpe.awaitTermination( maxTimeOut, TimeUnit.SECONDS );
      } catch( InterruptedException ex ) {
        handleFailure( ex );
      }
    }
  }
  
  @Override
  protected final boolean isInterestingPath( Path path ) {
    return pathTest.test( path );
  }

  @Override
  protected final void processPath( Path path ) {
    URI       uri    = path.toUri();
    Future<?> future = futures.get( uri );
    if( (future == null) || future.isDone() || future.isCancelled() ) {
      // we need to reschedule the execution for this path
      futures.put( uri, executor.submit( new ConsumerAsRunnable( path ) ) );
    }
  }
  
  @AllArgsConstructor
  private class ConsumerAsRunnable implements Runnable {
    
    Path   path;
    
    @Override
    public void run() {
      try {
        consumer.accept( path );
      } catch( Exception ex ) {
        handleFailure( ex );
      }
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
