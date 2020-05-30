package com.kasisoft.libs.common.old.ui.component;

import com.kasisoft.libs.common.old.ui.event.*;

/**
 * This interface is used to identify widgets where the input is allowed to be temporarily invalid. 
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KValidationComponent {

  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param l   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  void addValidationListener( ValidationListener l );
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  void removeValidationListener( ValidationListener l );

  /**
   * Returns <code>true</code> if the current input is valid.
   * 
   * @return   <code>true</code> <=> The current input is valid.
   */
  boolean isValid();
  
} /* ENDINTERFACE */
