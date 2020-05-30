package com.kasisoft.libs.common.old.ui.event;

import lombok.*;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for ActionEvent's. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ActionEventDispatcher extends AbstractEventDispatcher<ActionListener, ActionEvent> {

  @Override
  protected void invokeEvent( @NonNull ActionListener listener, @NonNull ActionEvent event ) {
    listener.actionPerformed( event );
  }
  
} /* ENDCLASS */
