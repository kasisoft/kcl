/**
 * Name........: ValidationEventDispatcher
 * Description.: Basic implementation of a dispatcher for ValidationEvents .
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.event;

/**
 * Basic implementation of a dispatcher for ValidationEvents. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 */
public class ValidationEventDispatcher extends AbstractEventDispatcher<ValidationListener,ValidationEvent> {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void invokeEvent( ValidationListener listener, ValidationEvent event ) {
    listener.validationChanged( event );
  }
  
} /* ENDCLASS */
