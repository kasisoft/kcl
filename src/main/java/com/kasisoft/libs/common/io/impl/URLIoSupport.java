package com.kasisoft.libs.common.io.impl;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.net.*;

import java.io.*;

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
