package com.kasisoft.libs.common.ui.event;

import javax.swing.event.*;

import lombok.*;

/**
 * Basic implementation of a dispatcher for ChangeEvent's. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ChangeEventDispatcher extends AbstractEventDispatcher<ChangeListener,ChangeEvent>{

  @Override
  protected void invokeEvent( @NonNull ChangeListener listener, @NonNull ChangeEvent event ) {
    listener.stateChanged( event );
  }
  
} /* ENDCLASS */
