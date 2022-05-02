package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.types.*;

import javax.validation.constraints.*;

/**
 * Adapter for Version values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class VersionAdapter extends AbstractConverter<String, Version> {

  private boolean   micro       = false;
  private boolean   qualifier   = false;
  private boolean   all         = false;
  
  public VersionAdapter withMicro(boolean micro) {
    this.micro = micro;
    return this;
  }

  public VersionAdapter withQualifier(boolean qualifier) {
    this.qualifier = qualifier;
    return this;
  }

  public VersionAdapter withAll(boolean all) {
    this.all = all;
    return this;
  }

  @Override
  public String encodeImpl(@NotNull Version v) {
    return String.valueOf(v);
  }

  @Override
  public Version decodeImpl(@NotNull String v) {
    if (all) {
      return new Version(v);
    } else {
      return new Version(v, micro, qualifier);
    }
  }

} /* ENDCLASS */
