package com.kasisoft.libs.common.converters;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

import java.text.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for Date values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DateAdapter extends AbstractConverter<String, Date> {

  SimpleDateFormat   formatter = new SimpleDateFormat("yyyy-MM-dd");
  
  public @NotNull DateAdapter withPattern(@NotNull String pattern) {
    formatter = new SimpleDateFormat(pattern);
    return this;
  }

  @Override
  public String encodeImpl(@NotNull Date decoded) {
    synchronized (formatter) {
      return formatter.format(decoded);
    }
  }

  @Override
  public Date decodeImpl(@NotNull String encoded) {
    synchronized (formatter) {
      try {
        return formatter.parse(encoded);
      } catch (ParseException ex) {
        throw new KclException(ex, error_invalid_date_value, encoded);
      }
    }
  }
  
} /* ENDCLASS */
