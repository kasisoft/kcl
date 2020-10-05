package com.kasisoft.libs.common.converters;

import javax.validation.constraints.*;

import java.nio.file.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for Path values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NioPathAdapter extends AbstractConverter<String, Path> {

  @Override
  public String encodeImpl(@NotNull Path v) {
    return v.normalize().toString().replace('\\', '/');
  }

  @Override
  public Path decodeImpl(@NotNull String v) {
    return Paths.get(v.replace('\\', '/')).normalize();
  }

} /* ENDCLASS */
