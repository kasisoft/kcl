/**
 * Name........: ValidationListener
 * Description.: Each implementor can handle validity changes of some input.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.event;

import java.util.*;

/**
 * Each implementor can handle validation changes of some input.
 */
public interface ValidationListener extends EventListener {

  /**
   * Will be invoked whenever the validation of a component has been changed.
   * 
   * @param evt   An event providing the validation information. Not <code>null</code>.
   */
  void validationChanged( ValidationEvent evt );
  
} /* ENDINTERFACE */
