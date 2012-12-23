/**
 * Name........: ActionEventDispatcher
 * Description.: Basic implementation of a dispatcher for ActionEvent's. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for ActionEvent's. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 */
public class ActionEventDispatcher extends AbstractEventDispatcher<ActionListener,ActionEvent> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void invokeEvent( ActionListener listener, ActionEvent event ) {
    listener.actionPerformed( event );
  }
  
} /* ENDCLASS */