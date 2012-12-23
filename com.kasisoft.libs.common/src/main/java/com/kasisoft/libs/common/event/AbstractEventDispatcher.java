/**
 * Name........: AbstractEventDispatcher
 * Description.: Basic implementation of a dispatcher for various Swing UI events. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

import com.kasisoft.libs.common.util.*;

import javax.swing.*;

import java.util.*;

/**
 * Basic implementation of a dispatcher for various Swing UI events.
 */
public abstract class AbstractEventDispatcher<L,E> {

  private Set<L>               listeners;
  private SimpleErrorHandler   errorhandler;
  
  /**
   * Initialises this dispatcher to deliver Swing UI events.
   */
  public AbstractEventDispatcher() {
    listeners     = new HashSet<L>();
    errorhandler  = null;
  }

  /**
   * Changes the currently used error handler.
   * 
   * @param newhandler   The new error handler. If <code>null</code> there won't be any further notification.
   */
  public synchronized void setErrorHandler( SimpleErrorHandler newhandler ) {
    errorhandler = newhandler;
  }

  /**
   * Returns <code>true</code> if this dispatcher has some listeners to be informed.
   * 
   * @return   <code>true</code> <=> Some listeners require to be informed.
   */
  public boolean gotListeners() {
    return ! listeners.isEmpty();
  }
  
  /**
   * Registers the supplied listener to become informed upon fired events.
   * 
   * @param l   The listener which becomes informed upon fired events. Not <code>null</code>.
   */
  public synchronized void addListener( L l ) {
    listeners.add(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public synchronized void removeListener( L l ) {
    listeners.remove(l);
  }

  /**
   * Fires the supplied Event so every currently registered listener will be informed.
   * 
   * @param evt   The Event that will be delivered. Not <code>null</code>.
   */
  public synchronized void fireEvent( final E evt ) {
    if( ! listeners.isEmpty() ) {
      final Object[] ls     = listeners.toArray();
      final Object   source = this;
      Runnable execution = new Runnable() {
        public void run() {
          for( Object listener : ls ) {
            try {
              invokeEvent( (L) listener, evt );
            } catch( RuntimeException ex ) {
              if( errorhandler != null ) {
                errorhandler.failure( source, ex.getMessage(), ex );
              }
            }
          }
        }
      };
      SwingUtilities.invokeLater( execution );
    }
  }
  
  /**
   * Causes the dispatching of the supplied event.
   * 
   * @param listener   The listener that will be notified. Not <code>null</code>.
   * @param event      The event that has been fired. Not <code>null</code>.
   */
  protected abstract void invokeEvent( L listener, E event );
  
} /* ENDCLASS */