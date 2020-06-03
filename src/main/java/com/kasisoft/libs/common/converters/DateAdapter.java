package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

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
        throw new KclException(ex, "Invalid date value '%s'", encoded);
      }
    }
  }
  
} /* ENDCLASS */
