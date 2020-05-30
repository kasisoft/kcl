package com.kasisoft.libs.common.old.ui.event;

/**
 * Will be invoked whenever a  Path has been changed.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface PathChangeListener {

  /**
   * Notifies about the change of a  Path.
   * 
   * @param evt   The event carrying the necessary information. Not <code>null</code>.
   */
  void pathChanged( PathChangeEvent evt );

} /* ENDINTERFACE */
