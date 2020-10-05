package com.kasisoft.libs.common.io.impl;

import com.kasisoft.libs.common.io.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URIIoSupport implements IoSupport<URI> {

  @Override
  public InputStream newInputStreamImpl(URI source) throws Exception {
    return Files.newInputStream(Paths.get(source));
  }
  
  @Override
  public OutputStream newOutputStreamImpl(URI destination) throws Exception {
    return Files.newOutputStream(Paths.get(destination));
  }

} /* ENDCLASS */
