/**
 * Name........: AbstractRunnable
 * Description.: Basic implementation for a Runnable.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Basic implementation for a Runnable.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public abstract class AbstractRunnable implements Runnable {

  private boolean   stopped;
  private boolean   completed;
  
  /**
   * {@inheritDoc}
   */
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
