/**
 * Name........: ValidatingEditor
 * Description.: Each implementor supports to distinguish between a valid and an invalid state.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing;

import com.kasisoft.lgpl.libs.common.event.*;

/**
 * Each implementor supports to distinguish between a valid and an invalid state.
 */
public interface ValidatingComponent {

  /**
   * Returns <code>true</code> if the current state indicates a valid state.
   * 
   * @return   <code>true</code> <=> The current state indicates a valid state.
   */
  boolean isValid();
  
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

} /* ENDINTERFACE */
