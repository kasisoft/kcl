package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.KclConfig;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

  List<String>   trueValues = new ArrayList<>(KclConfig.TRUE_VALUES);
  
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
  public Boolean decodeImpl(@NotNull String decoded) {
    return trueValues.contains(decoded.toLowerCase());
  }

  @Override
  public String encodeImpl(@NotNull Boolean encoded) {
    return String.valueOf(encoded);
  }

} /* ENDCLASS */
