package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for File values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAdapter extends AbstractConverter<String, File> {

  boolean   canonical = false;
  
  public FileAdapter withCanonical(boolean canonical) {
    this.canonical = canonical;
    return this;
  }
  
  @Override
  public String encodeImpl(@NotNull File v) {
    if (canonical) {
      try {
        v = v.getCanonicalFile();
      } catch (Exception ex) {
        throw new KclException(ex, "Cannot determine canonical file of '%s'!", v);
      }
    }
    return v.getPath().replace( '\\', '/' );
  }

  @Override
  public File decodeImpl(@NotNull String v) {
    var result = new File(v.replace('\\', '/').replace('/', File.separatorChar));
    if (canonical) {
      try {
        result = result.getCanonicalFile();
      } catch (Exception ex) {
        throw new KclException(ex, "Cannot determine canonical file of '%s'!", v);
      }
    }
    return result;
  }

} /* ENDCLASS */
