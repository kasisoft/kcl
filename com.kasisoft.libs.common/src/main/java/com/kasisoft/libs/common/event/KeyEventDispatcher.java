/**
 * Name........: KeyEventDispatcher
 * Description.: Basic implementation of a dispatcher for KeyEvent's. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for KeyEvent's. Listeners called using this
 * dispatcher will be executed within the EventDispatchThread.
 */
public class KeyEventDispatcher extends AbstractEventDispatcher<KeyListener,KeyEvent> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void invokeEvent( KeyListener listener, KeyEvent evt ) {
           if( evt.getID() == KeyEvent.KEY_PRESSED  ) { listener.keyPressed   ( evt );
    } else if( evt.getID() == KeyEvent.KEY_RELEASED ) { listener.keyReleased  ( evt );
    } else if( evt.getID() == KeyEvent.KEY_TYPED    ) { listener.keyTyped     ( evt );
    }
  }
  
} /* ENDCLASS */
