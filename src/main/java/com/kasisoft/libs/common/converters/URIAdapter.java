package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.net.*;

/**
 * Adapter for URI values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URIAdapter extends AbstractConverter<String, URI> {

  @Override
  public String encodeImpl(@NotNull URI v) {
    try {
      return v.toURL().toExternalForm();
    } catch (MalformedURLException ex) {
      throw new KclException(error_invalid_uri, v);
    }
  }

  @Override
  public URI decodeImpl(@NotNull String v) {
    try {
      return new URI(v);
    } catch (URISyntaxException ex) {
      throw new KclException(error_invalid_uri, v);
    }
  }

} /* ENDCLASS */
