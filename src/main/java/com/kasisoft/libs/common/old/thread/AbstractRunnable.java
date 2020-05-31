package com.kasisoft.libs.common.old.thread;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Basic implementation for a Runnable.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractRunnable implements Runnable {

  boolean   stopped;
  boolean   completed;
  
  @Getter @Setter
  String    name;
  
  @Override
  public final void run() {
    if( name != null ) {
      Thread.currentThread().setName( name );
    }
    stopped   = false;
    completed = false;
    try {
      execute();
    } finally {
      completed = ! isStopped();
    }
  }

  /**
   * Returns <code>true</code> if the process has been completed.
   * 
   * @return   <code>true</code> <=> The process has been completed.
   */
  public boolean hasCompleted() {
    return completed;
  }

  /**
   * @see Runnable#run()
   */
  protected abstract void execute();

  /**
   * Stops the execution of this Runnable instance.
   */
  public void stop() {
    stopped = true;
  }

  /**
   * Returns <code>true</code> if execution of this Runnable has been stopped.
   * 
   * @return   <code>true</code> <=> Execution of this Runnable has been stopped.
   */
  public boolean isStopped() {
    return stopped || Thread.currentThread().isInterrupted();
  }

} /* ENDCLASS */
