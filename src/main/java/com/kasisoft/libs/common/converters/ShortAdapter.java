package com.kasisoft.libs.common.converters;

import javax.validation.constraints.*;

/**
 * Adapter for short values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ShortAdapter extends AbstractConverter<String, Short> {

  @Override
  public String encodeImpl(@NotNull Short v) {
    return Short.toString(v);
  }

  @Override
  public Short decodeImpl(@NotNull String v) {
    return Short.valueOf(v);
  }

} /* ENDCLASS */
