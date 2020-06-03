package com.kasisoft.libs.common.converters;

import javax.validation.constraints.NotNull;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for int values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntAdapter extends AbstractConverter<String, Integer> {

  @Override
  public String encodeImpl(@NotNull Integer v) {
    return Integer.toString(v);
  }

  @Override
  public Integer decodeImpl(@NotNull String v) {
    return Integer.valueOf(v);
  }

} /* ENDCLASS */
