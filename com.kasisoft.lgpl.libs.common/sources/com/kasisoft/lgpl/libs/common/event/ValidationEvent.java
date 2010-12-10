/**
 * Name........: ValidationEvent
 * Description.: This event informs about the validation of a component. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.event;

import com.kasisoft.lgpl.tools.diagnostic.*;

import javax.swing.*;

import java.util.*;

/**
 * This event informs about the validation of a component.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class ValidationEvent extends EventObject {

  private boolean   valid;

  /**
   * Sets up this event used to deliver information regarding the validty of some input.
   * 
   * @param source    The component which can be (in)valid.
   * @param isvalid   <code>true</code> <=> The component became valid.
   */
  public ValidationEvent( @KNotNull(name="source") JComponent source, boolean isvalid ) {
    super( source );
    valid = isvalid;
  }

  /**
   * Returns <code>true</code> if the validity of the component has been approoved.
   * 
   * @return  <code>true</code> <=> The validity of the component has been approved.
   */
  public boolean isValid() {
    return valid;
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
