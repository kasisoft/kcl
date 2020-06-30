package com.kasisoft.libs.common.io.impl;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoSupport;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileIoSupport implements IoSupport<File> {

  private static final long MB_16 = 16 * 1024 * 1024;
  
  @Override
  public InputStream newInputStreamImpl(File source) throws Exception {
    return Files.newInputStream(source.toPath());
  }
  
  @Override
  public OutputStream newOutputStreamImpl(File destination) throws Exception {
    return Files.newOutputStream(destination.toPath(), StandardOpenOption.CREATE);
  }

  @Override
  public @NotNull byte[] loadAllBytes(@NotNull File source) {
    try {
      var fileSize = source.length();
      if (fileSize <= MB_16) {
        return Files.readAllBytes(source.toPath());
      } else {
        return IoSupport.super.loadAllBytes(source);
      }
    } catch (Exception ex) {
      throw new KclException(ex, "Failed to read from '%s'!", source);
    }
  }

  @Override
  public @NotNull String readText(@NotNull File source, @Null Encoding encoding) {
    try {
      var fileSize = source.length();
      if (fileSize <= MB_16) {
        return Files.readString(source.toPath(), encoding != null ? encoding.getCharset() : StandardCharsets.UTF_8);
      } else {
        return IoSupport.super.readText(source, encoding);
      }
    } catch (Exception ex) {
      throw new KclException(ex, "Failed to read from '%s'!", source);
    }
  }
  
} /* ENDCLASS */
