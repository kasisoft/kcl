package com.kasisoft.libs.common.spi;

import javax.validation.constraints.NotNull;

import java.util.Map;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SPIService2 implements SPIService, Configurable {
  
  public static final String KEY_NAME = "spiservice2-value";

  String    configured = "service-2";
  
  @Override
  public String getString() {
    return configured;
  }

  @Override
  public void configure(@NotNull Map<String, String> properties) {
    if (properties.containsKey(KEY_NAME)) {
      configured = properties.get(KEY_NAME);
    }
  }

} /* ENDCLASS */
