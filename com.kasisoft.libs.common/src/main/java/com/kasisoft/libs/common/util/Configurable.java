package com.kasisoft.libs.common.util;

import java.util.Properties;

/**
 * Each implementor allows to be configured after being setup.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface Configurable {

  /**
   * Configures this instance with the supplied settings.
   * 
   * @param properties   The settings which will be used for the configuration. Not <code>null</code>.
   * 
   * @throws Exception in case anything went wrong.
   */
  void configure( Properties properties ) throws Exception;

} /* ENDINTERFACE */
