package com.kasisoft.libs.common.converters;

import jakarta.validation.constraints.*;

/**
 * Adapter for byte values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
