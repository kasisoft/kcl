package com.kasisoft.libs.common.i18n;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Helper which provides formatting capabilities to a translation.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@Data @FieldDefaults(level = AccessLevel.PRIVATE)
public class I18NString {

  String   value;
  
  /**
   * @see String#format(String, Object...)
   */
  public String format(Object ... args) {
    if ((args == null) || (args.length == 0)) {
      return value;
    } else {
      try {
        return String.format(value, args);
      } catch (Exception ex) {
        return value;
      }
    }
  }
  
  @Override
  public String toString() {
    return value;
  }

} /* ENDCLASS */
