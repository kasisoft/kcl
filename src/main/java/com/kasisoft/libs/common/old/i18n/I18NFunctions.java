package com.kasisoft.libs.common.old.i18n;

import java.util.*;

/**
 * Collection of I18n related functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class I18NFunctions {

  /**
   * Returns the current locale. Uses the default if none had been set yet.
   * 
   * @param locale   The locale to be used. Maybe <code>null</code>.
   * 
   * @return   The current locale. Not <code>null</code>.
   */
  public static Locale getLocale( Locale locale ) {
    if( locale == null ) {
      return Locale.getDefault();
    } else {
      return locale;
    }
  }

} /* ENDCLASS */
