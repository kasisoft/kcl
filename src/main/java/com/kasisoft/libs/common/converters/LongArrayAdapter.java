package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.pools.*;

import jakarta.validation.constraints.*;

import java.util.regex.Pattern;

/**
 * Adapter for long array values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class LongArrayAdapter extends AbstractConverter<String, long[]> {

  private char              delimiter = ',';
  private Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  private LongAdapter       converter = new LongAdapter();

  public @NotNull LongArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (this.delimiter != delimiter) {
      this.delimiter = delimiter;
      this.pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
    }
    return this;
  }

  public @NotNull LongArrayAdapter withConverter(@NotNull LongAdapter converter) {
    this.converter = converter;
    return this;
  }

  @Override
  public String encodeImpl(@NotNull long[] decoded) {
    if (decoded.length == 0) {
      return Empty.NO_STRING;
    }
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
  public long[] decodeImpl(@NotNull String encoded) {
    if (encoded.length() == 0) {
      return Empty.NO_LONGS;
    }
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(encoded);
      var values = $.splitRegex(pattern);
      var result = new long[values.length];
      for (var i = 0; i < values.length; i++) {
        result[i] = converter.decode(values[i]);
      }
      return result;
    });
  }

} /* ENDCLASS */
