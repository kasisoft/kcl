package com.kasisoft.libs.common.spi;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SPIService4 implements SPIService {

  @Override
  public String getString() {
    return "service-4";
  }

} /* ENDCLASS */
