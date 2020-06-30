package com.kasisoft.libs.common.spi;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SPIService3 implements SPIService {

  String    value = "service-3";
  
  @Override
  public String getString() {
    return value;
  }

} /* ENDCLASS */
