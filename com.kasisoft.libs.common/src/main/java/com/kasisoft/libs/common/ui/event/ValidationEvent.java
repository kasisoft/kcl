package com.kasisoft.libs.common.ui.event;

import javax.swing.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * This event informs about the validation of a component.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationEvent extends EventObject {

  @Getter boolean   valid;

  /**
   * Sets up this event used to deliver information regarding the validty of some input.
   * 
   * @param source    The component which can be (in)valid.
   * @param isvalid   <code>true</code> <=> The component became valid.
   */
  public ValidationEvent( @NonNull JComponent source, boolean isvalid ) {
    super( source );
    valid = isvalid;
  }

  /**
   * Returns the causing component.
   * 
   * @return   The causing component.
   */
  public JComponent getComponent() {
    return (JComponent) source;
  }
  
} /* ENDCLASS */
