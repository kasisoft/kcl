package com.kasisoft.libs.common.util;

/**
 * Each implementor is supposed to delegate an issue that might have happened.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface SimpleErrorHandler {

  /**
   * Will be called whenever an error happened.
   * 
   * @param source    The object that invokes this message. Not <code>null</code>.
   * @param message   The message that provides further information. Neither <code>null</code> nor empty.
   * @param cause     A potential cause of the error. Maybe <code>null</code>.
   */
  void failure( Object source, String message, Exception cause );
  
} /* ENDINTERFACE */
