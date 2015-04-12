package com.kasisoft.libs.common.workspace;

import java.awt.*;

/**
 * An interface which allows to configure a component.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface WSListener<T extends Component> {

  /**
   * Configures the supplied component.
   * 
   * @param component   The component that will be configured. Not <code>null</code>.
   */
  void configure( T component );
  
} /* ENDINTERFACE */
