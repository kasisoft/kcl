/**
 * Name........: ChangeEventDispatcher
 * Description.: Basic implementation of a dispatcher for ChangeEvent's. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.event;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.event.*;
import javax.swing.*;

import java.util.*;

/**
 * Basic implementation of a dispatcher for ChangeEvent's. Listeners called using this
 * dispatcher will be executed within the EventDispatchThread.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ChangeEventDispatcher {

  private Set<ChangeListener>   listeners;
  private SimpleErrorHandler    errorhandler;
  
  /**
   * Initialises this dispatcher to deliver ChangeEvent's.
   */
  public ChangeEventDispatcher() {
    listeners     = new HashSet<ChangeListener>();
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
   * Returns <code>true</code> if this dispatcher has some listeners to be informed.
   * 
   * @return   <code>true</code> <=> Some listeners require to be informed.
   */
  public boolean gotListeners() {
    return ! listeners.isEmpty();
  }
  
  /**
   * Registers the supplied listener to become informed upon changes.
   * 
   * @param l   The listener which becomes informed upon changes. Not <code>null</code>.
   */
  public synchronized void addChangeListener( @KNotNull(name="l") ChangeListener l ) {
    listeners.add(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public synchronized void removeChangeListener( @KNotNull(name="l") ChangeListener l ) {
    listeners.remove(l);
  }

  /**
   * Fires the supplied ChangeEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ChangeEvent that will be delivered. Not <code>null</code>.
   */
  public synchronized void fireChangeEvent( @KNotNull(name="evt") final ChangeEvent evt ) {
    if( ! listeners.isEmpty() ) {
      final ChangeListener[] ls = new ChangeListener[ listeners.size() ];
      listeners.toArray( ls );
      Runnable execution = new Runnable() {
        public void run() {
          for( ChangeListener listener : ls ) {
            try {
              listener.stateChanged( evt );
            } catch( RuntimeException ex ) {
              if( errorhandler != null ) {
                errorhandler.failure( ChangeEventDispatcher.this, ex.getMessage(), ex );
              }
            }
          }
        }
      };
      SwingUtilities.invokeLater( execution );
    }
  }
  
} /* ENDCLASS */
