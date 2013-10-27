/**
 * Name........: I18NFormatter
 * Description.: Helper which provides formatting capabilities to a translation.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.i18n;

import lombok.*;

/**
 * Helper which provides formatting capabilities to a translation.
 */
@AllArgsConstructor
@Data
public class I18NFormatter {

  private String   value;
  
  /**
   * @see String#format(String, Object...)
   */
  public String format( Object ... args ) {
    if( (args == null) || (args.length == 0) ) {
      return value;
    } else {
      return String.format( value, args );
    }
  }
        
  @Override
  public String toString() {
    return value;
  }
  
} /* ENDCLASS */
