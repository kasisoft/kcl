package com.kasisoft.libs.common.old.ui.event;

import java.util.*;

/**
 * Each implementor can handle validation changes of some input.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface ValidationListener extends EventListener {

  /**
   * Will be invoked whenever the validation of a component has been changed.
   * 
   * @param evt   An event providing the validation information. Not <code>null</code>.
   */
  void validationChanged( ValidationEvent evt );
  
} /* ENDINTERFACE */
