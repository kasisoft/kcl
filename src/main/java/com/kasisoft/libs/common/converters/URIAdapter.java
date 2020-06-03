package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

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
      throw new KclException("Invalid URI '%s'", v);
    }
  }

  @Override
  public URI decodeImpl(@NotNull String v) {
    try {
      return new URI(v);
    } catch (URISyntaxException ex) {
      throw new KclException("Invalid URI '%s'", v);
    }
  }

} /* ENDCLASS */
