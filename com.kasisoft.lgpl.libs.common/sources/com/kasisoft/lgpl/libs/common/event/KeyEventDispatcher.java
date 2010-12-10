/**
 * Name........: KeyEventDispatcher
 * Description.: Basic implementation of a dispatcher for KeyEvent's. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.event;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.util.*;

import java.awt.event.*;

/**
 * Basic implementation of a dispatcher for KeyEvent's. Listeners called using this
 * dispatcher will be executed within the EventDispatchThread.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class KeyEventDispatcher {

  private Set<KeyListener>     listeners;
  private SimpleErrorHandler   errorhandler;
  
  /**
   * Initialises this dispatcher to deliver ChangeEvent's.
   */
  public KeyEventDispatcher() {
    listeners     = new HashSet<KeyListener>();
    errorhandler  = null;
  }

  /**
   * Changes the currently used error handler.
   * 
   * @param newhandler   The new error handler. If <code>null</code> there won't be any further
   *                     notification.
   */
  public synchronized void setErrorHandler( SimpleErrorHandler newhandler ) {
    errorhandler = newhandler;
  }

  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param l   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public synchronized void addKeyListener( @KNotNull(name="l") KeyListener l ) {
    listeners.add(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public synchronized void removeKeyListener( @KNotNull(name="l") KeyListener l ) {
    listeners.remove(l);
  }

  /**
   * Fires the supplied KeyEvent so every currently registered listener will be informed.
   * 
   * @param evt   The KeyEvent that will be delivered. Not <code>null</code>.
   */
  public synchronized void fireKeyEvent( @KNotNull(name="evt") final KeyEvent evt ) {
    if( ! listeners.isEmpty() ) {
      final KeyListener[] ls = new KeyListener[ listeners.size() ];
      listeners.toArray( ls );
      Runnable execution = new Runnable() {
        public void run() {
          for( KeyListener listener : ls ) {
            
            try {
              
                     if( evt.getID() == KeyEvent.KEY_PRESSED  ) { listener.keyPressed   ( evt );
              } else if( evt.getID() == KeyEvent.KEY_RELEASED ) { listener.keyReleased  ( evt );
              } else if( evt.getID() == KeyEvent.KEY_TYPED    ) { listener.keyTyped     ( evt );
              }
                     
            } catch( RuntimeException ex ) {
              if( errorhandler != null ) {
                errorhandler.failure( KeyEventDispatcher.this, ex.getMessage(), ex );
              }
            }
          }
        }
      };
      SwingUtilities.invokeLater( execution );
    }
  }
  
} /* ENDCLASS */
