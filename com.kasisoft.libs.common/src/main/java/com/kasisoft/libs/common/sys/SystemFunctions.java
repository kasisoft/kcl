/**
 * Name........: SystemFunctions
 * Description.: System specific functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.sys;

import java.security.*;

import lombok.*;

/**
 * System specific functions.
 */
public class SystemFunctions {

  /**
   * Executes the supplied {@link Runnable} instance while making sure that System.exit calls won't stop the VM.
   * 
   * @param runnable   The {@link Runnable} instance which has to be executed. Not <code>null</code>.
   * 
   * @return   The exitcode which had been raised.
   */
  public static int executeWithoutExit( @NonNull Runnable runnable ) {
    SecurityManager oldsecuritymanager = System.getSecurityManager();
    try {
      CustomSecurityManager sm = new CustomSecurityManager();
      System.setSecurityManager( sm );
      Thread thread = new Thread( runnable );
      thread.setUncaughtExceptionHandler( sm );
      thread.start();
      try {
        thread.join();
      } catch( InterruptedException ex ) {
      }
      return sm.exitcode;
    } finally {
      System.setSecurityManager( oldsecuritymanager );
    }
  }

  /**
   * This exception indicates that a System.exit call has been intercepted.
   */
  private static class ExitTrappedException extends SecurityException { 
  } /* ENDCLASS */
  
  /**
   * SecurityManager implementation which disables System.exit calls. 
   */
  private static class CustomSecurityManager extends SecurityManager implements Thread.UncaughtExceptionHandler {

    private int   exitcode;
    
    @Override
    public void checkExit( int exitcode ) {
      this.exitcode = exitcode;
      throw new ExitTrappedException();
    }

    @Override
    public void checkPermission( Permission permission ) {
    }

    @Override
    public void uncaughtException( Thread t, Throwable e ) {
      if( e instanceof RuntimeException ) {
        throw (RuntimeException) e;
      }
    }

  } /* ENDCLASS */

} /* ENDCLASS */
