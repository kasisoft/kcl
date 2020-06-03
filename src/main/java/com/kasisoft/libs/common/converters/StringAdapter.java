package com.kasisoft.libs.common.converters;

import javax.validation.constraints.NotNull;

/**
 * Adapter for String values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class StringAdapter extends AbstractConverter<String, String> {

  @Override
  public String encodeImpl(@NotNull String v) {
    return v;
  }

  @Override
  public String decodeImpl(@NotNull String v) {
    return v;
  }

} /* ENDCLASS */
