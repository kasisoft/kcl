package com.kasisoft.libs.common.thread;

import java.util.*;

import java.io.*;

import java.nio.file.*;

import lombok.*;
import lombok.experimental.*;

/**
 * This {@link Runnable} implementation allows to watch a directory for filesystem changes recursively. It's only purpose
 * is to recognize changes and not the changed resources themselves.
 *  
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilesystemChangeRunnable extends FilesystemWatchingRunnable {

  TimerTask   timerTask;
  Timer       timer;
  long        lastinvocation;
  boolean     processed;

  @Getter
  long        duration;

  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   */
  public FilesystemChangeRunnable( @NonNull File dir ) {
    super( dir );
    init();
  }

  /**
   * Prepare to watch the supplied directory;
   *  
   * @param dir   The directory that is supposed to be watched. Not <code>null</code>.
   */
  public FilesystemChangeRunnable( @NonNull Path dir ) {
    super( dir );
    init();
  }

  /**
   * Performs some common initializations.
   */
  private void init() {
    processed       = false;
    lastinvocation  = 0L;
    duration        = 1000;
    timer           = new Timer( "fileSystemChange" );
    timerTask       = newTimerTask();
    timer.scheduleAtFixedRate( timerTask, 1000, 1000 );
  }
  
  public void setDuration( long newduration ) {
    duration = Math.max( newduration, 500 );
  }

  
  @Override
  protected final void processPath( Path path ) {
    processed      = true;
    lastinvocation = System.currentTimeMillis();
    executeTick();
  }

  /**
   * Executes a single tick which means to be calling the method {@link #tick()} when it's time for it.
   */
  private void executeTick() {
    if( lastinvocation == 0L ) {
      lastinvocation = System.currentTimeMillis();
    } else if( (System.currentTimeMillis() - lastinvocation) > duration ) {
      if( processed ) {
        processed = false;
        tick();
      }
      lastinvocation = 0L;
    }
  }

  /**
   * Will be invoked whenever at least one filesystem change happend in the last {@link #duration} milli seconds.
   */
  protected void tick() {
  }

  private TimerTask newTimerTask() {
    return new TimerTask(){

      @Override
      public void run() {
        FilesystemChangeRunnable.this.executeTick();
      }

    };
  }

} /* ENDCLASS */
