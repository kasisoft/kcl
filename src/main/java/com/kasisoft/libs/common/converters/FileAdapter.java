package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.io.*;

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
        throw new KclException(ex, error_cannot_determine_canonical_file, v);
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
        throw new KclException(ex, error_cannot_determine_canonical_file, v);
      }
    }
    return result;
  }

} /* ENDCLASS */
