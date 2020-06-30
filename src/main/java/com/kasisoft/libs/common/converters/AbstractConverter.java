package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.error_invalid_decoded_value;
import static com.kasisoft.libs.common.internal.Messages.error_invalid_encoded_value;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class AbstractConverter<F, T> implements KConverter<F, T> {

  @Override
  public @Null T decode(@Null F encoded) {
    if (encoded != null) {
      return KclException.execute(() -> decodeImpl(encoded), error_invalid_encoded_value, encoded);
    }
    return null;
  }
  
  @Override
  public @Null F encode(@Null T decoded) {
    if (decoded != null) {
      return KclException.execute(() -> encodeImpl(decoded), error_invalid_decoded_value, decoded);
    }
    return null;
  }

  protected abstract @Null T decodeImpl(@NotNull F encoded);
  
  protected abstract @Null F encodeImpl(@NotNull T decoded);

} /* ENDCLASS */
