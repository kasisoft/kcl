/**
 * Name........: ChangeEventDispatcher
 * Description.: Basic implementation of a dispatcher for ChangeEvent's. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

import javax.swing.event.*;

/**
 * Basic implementation of a dispatcher for ChangeEvent's. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 */
public class ChangeEventDispatcher extends AbstractEventDispatcher<ChangeListener,ChangeEvent>{

  /**
   * {@inheritDoc}
   */
  @Override
  protected void invokeEvent( ChangeListener listener, ChangeEvent event ) {
    listener.stateChanged( event );
  }
  
} /* ENDCLASS */
