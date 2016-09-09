package com.kasisoft.libs.common.workspace;

import java.awt.*;

/**
 * An interface which allows to configure a component.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [08-Sep-2016:KASI]   This type will be removed with version 2.5. Use {@link WorkspacePersistent}
 *                                  instead.
*/
@Deprecated
public interface WSListener<T extends Component> {

  /**
   * Configures the supplied component.
   * 
   * @param component   The component that will be configured. Not <code>null</code>.
   */
  void configure( T component );
  
} /* ENDINTERFACE */
