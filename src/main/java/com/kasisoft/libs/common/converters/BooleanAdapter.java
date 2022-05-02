package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.util.*;

/**
 * Adapter for boolean values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BooleanAdapter extends AbstractConverter<String, Boolean> {

  private List<String>   trueValues = new ArrayList<>(KclConfig.TRUE_VALUES);
  
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
