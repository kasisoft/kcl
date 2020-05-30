package com.kasisoft.libs.common.old.ui.event;

import lombok.*;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for KeyEvent's. Listeners called using this dispatcher will be executed within 
 * the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KeyEventDispatcher extends AbstractEventDispatcher<KeyListener, KeyEvent> {

  @Override
  protected void invokeEvent( @NonNull KeyListener listener, @NonNull KeyEvent evt ) {
           if( evt.getID() == KeyEvent.KEY_PRESSED  ) { listener.keyPressed   ( evt );
    } else if( evt.getID() == KeyEvent.KEY_RELEASED ) { listener.keyReleased  ( evt );
    } else if( evt.getID() == KeyEvent.KEY_TYPED    ) { listener.keyTyped     ( evt );
    }
  }
  
} /* ENDCLASS */
