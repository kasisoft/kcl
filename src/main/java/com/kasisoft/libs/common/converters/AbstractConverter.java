package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public abstract class AbstractConverter<F, T> implements KConverter<F, T> {

  public @Null T decode(@Null F encoded) {
    if (encoded != null) {
      return KclException.execute(() -> decodeImpl(encoded), "Invalid encoded value: '%s'", encoded);
    }
    return null;
  }
  
  public @Null F encode(@Null T decoded) {
    if (decoded != null) {
      return KclException.execute(() -> encodeImpl(decoded), "Invalid decoded value: '%s'", decoded);
    }
    return null;
  }

  protected abstract @Null T decodeImpl(@NotNull F encoded);
  
  protected abstract @Null F encodeImpl(@NotNull T decoded);

} /* ENDCLASS */
