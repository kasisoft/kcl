package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.buckets.Buckets;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.regex.Pattern;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for short array values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortArrayAdapter extends AbstractConverter<String, short[]> {

  char              delimiter = ',';
  Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  ShortAdapter      converter = new ShortAdapter();

  public @NotNull ShortArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (delimiter != '.') {
      this.delimiter = delimiter;
      this.pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
    }
    return this;
  }

  public @NotNull ShortArrayAdapter withConverter(@NotNull ShortAdapter converter) {
    this.converter = converter;
    return this;
  }

  @Override
  public @Null String encodeImpl(@NotNull short[] decoded) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      if (decoded.length > 0) {
        $.append(decoded[0]);
        for (int i = 1; i < decoded.length; i++) {
          $.append(delimiter);
          $.append(converter.encode(decoded[i]));
        }
      }
      return $.toString();
    });
  }

  @Override
  public @Null short[] decodeImpl(@NotNull String encoded) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(encoded);
      var values = $.splitRegex(pattern);
      var result = new short[values.length];
      for (var i = 0; i < values.length; i++) {
        result[i] = converter.decode(values[i]);
      }
      return result;
    });
  }

} /* ENDCLASS */
