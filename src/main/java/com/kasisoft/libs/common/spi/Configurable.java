package com.kasisoft.libs.common.spi;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.util.Map;

/**
 * Each implementor allows to be configured after being setup.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FunctionalInterface
public interface Configurable {

  /**
   * Configures this instance with the supplied settings.
   * 
   * @param properties   The settings which will be used for the configuration.
   * 
   * @throws KclException in case anything went wrong.
   */
  void configure(@NotNull Map<String, String> properties);

} /* ENDINTERFACE */
