package com.kasisoft.libs.common.converters;

import javax.validation.constraints.*;

/**
 * Adapter for long values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class LongAdapter extends AbstractConverter<String, Long> {

  @Override
  public String encodeImpl(@NotNull Long v) {
    return Long.toString(v);
  }

  @Override
  public Long decodeImpl(@NotNull String v) {
    return Long.valueOf(v);
  }

} /* ENDCLASS */
