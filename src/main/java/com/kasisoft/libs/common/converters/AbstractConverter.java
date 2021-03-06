package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class AbstractConverter<F, T> implements KConverter<F, T> {

  @Override
  public T decode(F encoded) {
    if (encoded != null) {
      return KclException.execute(() -> decodeImpl(encoded), error_invalid_encoded_value, encoded);
    }
    return null;
  }
  
  @Override
  public F encode(T decoded) {
    if (decoded != null) {
      return KclException.execute(() -> encodeImpl(decoded), error_invalid_decoded_value, decoded);
    }
    return null;
  }

  protected abstract T decodeImpl(@NotNull F encoded);
  
  protected abstract F encodeImpl(@NotNull T decoded);

} /* ENDCLASS */
