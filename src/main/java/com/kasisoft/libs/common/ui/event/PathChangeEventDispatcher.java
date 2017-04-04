package com.kasisoft.libs.common.ui.event;

/**
 * Basic implementation of a dispatcher for PathChangeEvent's. Listeners called using this dispatcher will be 
 * executed within the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PathChangeEventDispatcher extends AbstractEventDispatcher<PathChangeListener, PathChangeEvent> {

  @Override
  protected void invokeEvent( PathChangeListener listener, PathChangeEvent event ) {
    listener.pathChanged( event );
  }
  
} /* ENDCLASS */