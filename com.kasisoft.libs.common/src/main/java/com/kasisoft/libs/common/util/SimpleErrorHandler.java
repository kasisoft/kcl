package com.kasisoft.libs.common.util;

import java.util.function.*;

/**
 * Each implementor is supposed to delegate an issue that might have happened.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [25-Oct-2015:KASI]   This interface will be removed with version 2.2 and should be replaced
 *                                  by the <code>BiConsumer&lt;Object,Exception&gt;</code> interface.
 */
@Deprecated
public interface SimpleErrorHandler extends BiConsumer<Object, Exception> {

  /**
   * Will be called whenever an error happened.
   * 
   * @param source    The object that invokes this message. Not <code>null</code>.
   * @param message   The message that provides further information. Neither <code>null</code> nor empty.
   * @param cause     A potential cause of the error. Maybe <code>null</code>.
   */
  default void failure( Object source, String message, Exception cause ) {
    accept( source, cause );
  }
  
  void accept( Object source, Exception cause );
  
} /* ENDINTERFACE */
