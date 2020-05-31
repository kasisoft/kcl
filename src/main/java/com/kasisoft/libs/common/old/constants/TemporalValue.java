package com.kasisoft.libs.common.old.constants;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;

import lombok.NonNull;
import lombok.val;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
interface TemporalValue {

  @SuppressWarnings("deprecation")
  default String getPresentable( @NonNull Locale locale, String pattern, @NonNull Date date ) {
    val formatter = new SimpleDateFormat( pattern, locale );
    return formatter.format( date );
  }

} /* ENDINTERFACE */
