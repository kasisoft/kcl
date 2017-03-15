package com.kasisoft.libs.common.constants;

import java.text.*;

import java.util.*;

import lombok.*;

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
