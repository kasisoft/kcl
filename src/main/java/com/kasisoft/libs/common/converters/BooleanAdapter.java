package com.kasisoft.libs.common.converters;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Arrays;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for boolean values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BooleanAdapter extends AbstractConverter<String, Boolean> {

  List<String>   trueValues = Arrays.asList("yes", "ja", "j", "y", "on", "ein", "1", "-1", "an", "true");
  
  public BooleanAdapter clear() {
    trueValues.clear();
    return this;
  }
  
  public BooleanAdapter withValues(String ... values) {
    if ((values != null) && (values.length > 0)) {
      for (var v : values) {
        trueValues.add(v);
      }
    }
    return this;
  }
  
  @Override
  public @Null Boolean decodeImpl(@NotNull String decoded) {
    return trueValues.contains(decoded.toLowerCase());
  }

  @Override
  public @Null String encodeImpl(@NotNull Boolean encoded) {
    return String.valueOf(encoded);
  }

} /* ENDCLASS */
