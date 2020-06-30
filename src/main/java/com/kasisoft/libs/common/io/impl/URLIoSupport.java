package com.kasisoft.libs.common.io.impl;

import static com.kasisoft.libs.common.internal.Messages.error_no_write_support_for_urls;

import com.kasisoft.libs.common.io.IoSupport;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.net.URL;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URLIoSupport implements IoSupport<URL> {

  @Override
  public InputStream newInputStreamImpl(@NotNull URL source) throws Exception {
    return source.openStream();
  }
  
  @Override
  public OutputStream newOutputStreamImpl(@NotNull URL destination) throws Exception {
    throw new KclException(error_no_write_support_for_urls, destination);
  }

} /* ENDCLASS */
