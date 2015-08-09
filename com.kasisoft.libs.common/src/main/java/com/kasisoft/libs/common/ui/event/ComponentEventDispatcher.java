package com.kasisoft.libs.common.ui.event;

import lombok.*;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for ComponentEvent's. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ComponentEventDispatcher extends AbstractEventDispatcher<ComponentListener,ComponentEvent>  {

  @Override
  protected void invokeEvent( @NonNull ComponentListener listener, @NonNull ComponentEvent event ) {
    switch( event.getID() ) {
    case ComponentEvent.COMPONENT_HIDDEN  : listener.componentHidden  ( event ); break;
    case ComponentEvent.COMPONENT_MOVED   : listener.componentMoved   ( event ); break;
    case ComponentEvent.COMPONENT_RESIZED : listener.componentResized ( event ); break;
    case ComponentEvent.COMPONENT_SHOWN   : listener.componentShown   ( event ); break;
    }
  }

} /* ENDCLASS */
