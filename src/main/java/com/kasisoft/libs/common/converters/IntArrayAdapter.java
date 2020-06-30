package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.pools.Buckets;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.regex.Pattern;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for int array values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntArrayAdapter extends AbstractConverter<String, int[]> {

  char              delimiter = ',';
  Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  IntAdapter        converter = new IntAdapter();

  public @NotNull IntArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (this.delimiter != delimiter) {
      this.delimiter = delimiter;
      this.pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
    }
    return this;
  }

  public @NotNull IntArrayAdapter withConverter(@NotNull IntAdapter converter) {
    this.converter = converter;
    return this;
  }

  @Override
  public @Null String encodeImpl(@NotNull int[] decoded) {
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
  public @Null int[] decodeImpl(@NotNull String encoded) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(encoded);
      var values = $.splitRegex(pattern);
      var result = new int[values.length];
      for (var i = 0; i < values.length; i++) {
        result[i] = converter.decode(values[i]);
      }
      return result;
    });
  }

} /* ENDCLASS */
