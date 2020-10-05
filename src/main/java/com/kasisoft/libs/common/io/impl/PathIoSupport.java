package com.kasisoft.libs.common.io.impl;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.nio.charset.*;
import java.nio.file.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PathIoSupport implements IoSupport<Path> {

  private static final long MB_16 = 16 * 1024 * 1024;
  
  @Override
  public InputStream newInputStreamImpl(Path source) throws Exception {
    return Files.newInputStream(source);
  }
  
  @Override
  public OutputStream newOutputStreamImpl(Path destination) throws Exception {
    return Files.newOutputStream(destination);
  }
  
  @Override
  public @NotNull byte[] loadAllBytes(@NotNull Path source) {
    try {
      var fileSize = Files.size(source);
      if (fileSize <= MB_16) {
        return Files.readAllBytes(source);
      } else {
        return IoSupport.super.loadAllBytes(source);
      }
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }
  
  @Override
  public @NotNull String readText(@NotNull Path source, Encoding encoding) {
    try {
      var fileSize = Files.size(source);
      if (fileSize <= MB_16) {
        return Files.readString(source, encoding != null ? encoding.getCharset() : StandardCharsets.UTF_8);
      } else {
        return IoSupport.super.readText(source, encoding);
      }
    } catch (Exception ex) {
      throw new KclException(ex, error_failed_to_read_from, source);
    }
  }
  
} /* ENDCLASS */
