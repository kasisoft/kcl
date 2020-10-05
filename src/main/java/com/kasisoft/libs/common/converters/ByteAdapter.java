package com.kasisoft.libs.common.converters;

import javax.validation.constraints.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for byte values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ByteAdapter extends AbstractConverter<String, Byte> {

  @Override
  public Byte decodeImpl(@NotNull String encoded) {
    return Byte.parseByte(encoded);
  }

  @Override
  public String encodeImpl(@NotNull Byte decoded) {
    return Byte.toString(decoded);
  }

} /* ENDCLASS */
