/**
 * Name........: ValidationEventDispatcher
 * Description.: Basic implementation of a dispatcher for ValidationEvents .
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

/**
 * Basic implementation of a dispatcher for ValidationEvents .. Listeners called using this
 * dispatcher will be executed within the EventDispatchThread.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ValidationEventDispatcher {

  private Set<ValidationListener>   listeners;
  private SimpleErrorHandler        errorhandler;
  
  /**
   * Initialises this dispatch to deliver ValidationEvents .
   */
  public ValidationEventDispatcher() {
    listeners     = new HashSet<ValidationListener>();
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
  public synchronized void addValidationListener( @KNotNull(name="l") ValidationListener l ) {
    listeners.add(l);
  }
  
  /**
   * Prevents further notification for the supplied listener.
   * 
   * @param l   The listener that won't be notified anymore. Not <code>null</code>.
   */
  public synchronized void removeValidationListener( @KNotNull(name="l") ValidationListener l ) {
    listeners.remove(l);
  }

  /**
   * Fires the supplied ValidationEvent so every currently registered listener will be informed.
   * 
   * @param evt   The ValidationEvent that will be delivered. Not <code>null</code>.
   */
  public synchronized void fireValidationEvent( @KNotNull(name="evt") final ValidationEvent evt ) {
    if( ! listeners.isEmpty() ) {
      final ValidationListener[] ls = new ValidationListener[ listeners.size() ];
      listeners.toArray( ls );
      Runnable execution = new Runnable() {
        public void run() {
          for( ValidationListener listener : ls ) {
            try {
              listener.validationChanged( evt );
            } catch( RuntimeException ex ) {
              if( errorhandler != null ) {
                errorhandler.failure( ValidationEventDispatcher.this, ex.getMessage(), ex );
              }
            }
          }
        }
      };
      SwingUtilities.invokeLater( execution );
    }
  }
  
} /* ENDCLASS */
