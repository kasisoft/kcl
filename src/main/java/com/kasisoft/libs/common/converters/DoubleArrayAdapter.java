package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.pools.*;

import javax.validation.constraints.*;

import java.util.regex.Pattern;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Adapter for double array values.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoubleArrayAdapter extends AbstractConverter<String, double[]> {

  char              delimiter = ',';
  Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  DoubleAdapter     converter = new DoubleAdapter();

  public @NotNull DoubleArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (delimiter != '.') {
      this.delimiter = delimiter;
      this.pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
    }
    return this;
  }

  public @NotNull DoubleArrayAdapter withConverter(@NotNull DoubleAdapter converter) {
    this.converter = converter;
    return this;
  }

  @Override
  public String encodeImpl(@NotNull double[] decoded) {
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
  public double[] decodeImpl(@NotNull String encoded) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(encoded);
      var values = $.splitRegex(pattern);
      var result = new double[values.length];
      for (var i = 0; i < values.length; i++) {
        result[i] = converter.decode(values[i]);
      }
      return result;
    });
  }

} /* ENDCLASS */
