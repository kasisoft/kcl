package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.net.*;

/**
 * Adapter for URL values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URLAdapter extends AbstractConverter<String, URL> {

  @Override
  public String encodeImpl(@NotNull URL v) {
    return v.toExternalForm();
  }

  @Override
  public URL decodeImpl(@NotNull String v) {
    try {
      return new URL(v);
    } catch (MalformedURLException ex) {
      throw new KclException(error_invalid_url, v);
    }
  }

} /* ENDCLASS */
