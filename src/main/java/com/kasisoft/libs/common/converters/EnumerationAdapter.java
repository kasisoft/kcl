package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.util.*;

/**
 * An enumeration adapter allows to bind literals against an enumeration type. Each descendent is supposed to realise 
 * the following constraints:
 * 
 * <ul>
 *   <li>The parameter type must be an enumeration.</li>
 *   <li>The (un)marshalling is based upon the textual representation of an enumeration which
 *   means that the result of the toString() implementation is relevant here.</li>
 * </ul>
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class EnumerationAdapter<T extends Enum<T>> extends AbstractConverter<String, T> {

  private Class<T>        enumtype;
  private Map<String,T>   values;
  private boolean         ignorecase;
  
  public EnumerationAdapter(@NotNull Class<T> type) {
    enumtype    = type;
    ignorecase  = true;
    values      = new HashMap<>();
    withIgnoreCase(true);
  }
  
  public EnumerationAdapter<T> withIgnoreCase(boolean ignorecase) {
    this.ignorecase = ignorecase;
    T[] enums = enumtype.getEnumConstants();
    for (int i = 0; i < enums.length; i++) {
      String text = String.valueOf(enums[i]);
      if (ignorecase) {
        text = text.toLowerCase();
      }
      values.put(text, enums[i]); 
    }
    return this;
  }
  
  @Override
  public String encodeImpl(@NotNull T v) {
    return v.toString();
  }

  @Override
  public T decodeImpl(@NotNull String v) {
    if (ignorecase) {
      v = v.toLowerCase();
    }
    if (!values.containsKey(v)) {
      throw new KclException(error_invalid_enumeration_value, v, values.keySet().stream().reduce(($a, $b) -> $a + "," + $b));
    }
    return values.get(v);
  }
  
} /* ENDCLASS */
