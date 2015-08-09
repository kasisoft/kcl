package com.kasisoft.libs.common.thread;

import lombok.experimental.*;

import lombok.*;

/**
 * Basic implementation for a Runnable.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class AbstractRunnable implements Runnable {

  boolean   stopped;
  boolean   completed;
  
  @Override
  public final void run() {
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
