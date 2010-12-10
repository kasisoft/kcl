/**
 * Name........: ActionEventDispatcher
 * Description.: Basic implementation of a dispatcher for ActionEvent's. 
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
 * Basic implementation of a dispatcher for ActionEvent's. Listeners called using this
 * dispatcher will be executed within the EventDispatchThread.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ActionEventDispatcher {

  private Set<ActionListener>   listeners;
  private SimpleErrorHandler    errorhandler;
  
  /**
   * Initialises this dispatch to deliver ActionEvents.
   */
  public ActionEventDispatcher() {
    listeners     = new HashSet<ActionListener>();
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
   * Registers the supplied listener to become informed upon activations.
   * 
   * @param l   The listener which becomes informed upon activations. Not <code>null</code>.
   */
  public synchronized void addActionListener( @KNotNull(name="l") ActionListener l ) {
    listeners.add(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public synchronized void removeActionListener( @KNotNull(name="l") ActionListener l ) {
    listeners.remove(l);
  }

  /**
   * Fires the supplied ActionEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ActionEvent that will be delivered. Not <code>null</code>.
   */
  public synchronized void fireActionEvent(
    @KNotNull(name="evt") final ActionEvent evt 
  ) {
    if( ! listeners.isEmpty() ) {
      final ActionListener[] ls = new ActionListener[ listeners.size() ];
      listeners.toArray( ls );
      Runnable execution = new Runnable() {
        public void run() {
          for( ActionListener listener : ls ) {
            try {
              listener.actionPerformed( evt );
            } catch( RuntimeException ex ) {
              if( errorhandler != null ) {
                errorhandler.failure( ActionEventDispatcher.this, ex.getMessage(), ex );
              }
            }
          }
        }
      };
      SwingUtilities.invokeLater( execution );
    }
  }
  
} /* ENDCLASS */
