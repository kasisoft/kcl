package com.kasisoft.libs.common.io.impl;

import com.kasisoft.libs.common.io.IoSupport;

import java.net.URI;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.io.InputStream;
import java.io.OutputStream;

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
    return Files.newOutputStream(Paths.get(destination), StandardOpenOption.CREATE);
  }

} /* ENDCLASS */
