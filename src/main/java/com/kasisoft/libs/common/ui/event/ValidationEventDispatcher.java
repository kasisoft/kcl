package com.kasisoft.libs.common.ui.event;

import lombok.*;

/**
 * Basic implementation of a dispatcher for ValidationEvents. Listeners called using this dispatcher will be executed 
 * within the EventDispatchThread.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ValidationEventDispatcher extends AbstractEventDispatcher<ValidationListener, ValidationEvent> {

  @Override
  protected void invokeEvent( @NonNull ValidationListener listener, @NonNull ValidationEvent event ) {
    listener.validationChanged( event );
  }
  
} /* ENDCLASS */
