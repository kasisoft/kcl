package com.kasisoft.libs.common.thread;

/**
 * Basic implementation for a Runnable.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class AbstractRunnable<T> implements Runnable {

  private boolean   stopped;
  private boolean   completed;
  
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
   * Maybe used in order to provide progress information. A listener concept would also be a possibility but I suspect 
   * that the information will often be ignored, so this mechanism is the cheaper one.
   * 
   * @param progressinfo   The current progress information.
   * 
   * @deprecated [06-Oct-2014:KASI]   The progress support will be dropped with 1.5+
   */
  @Deprecated
  protected void progress( T progressinfo ) {
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
