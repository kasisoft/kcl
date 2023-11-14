package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.pools.*;
import com.kasisoft.libs.common.text.*;

import jakarta.validation.constraints.*;

import java.util.regex.Pattern;

/**
 * Adapter for float array values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FloatArrayAdapter extends AbstractConverter<String, float[]> {

  private char              delimiter = ',';
  private Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  private FloatAdapter      converter = new FloatAdapter();

  public @NotNull FloatArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (delimiter != '.') {
      this.delimiter = delimiter;
      this.pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
    }
    return this;
  }

  public @NotNull FloatArrayAdapter withConverter(@NotNull FloatAdapter converter) {
    this.converter = converter;
    return this;
  }

  @Override
  public String encodeImpl(@NotNull float[] decoded) {
    return Buckets.bucketStringBuilder().forInstance($ -> {
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
  public float[] decodeImpl(@NotNull String encoded) {
    var values = StringFunctions.splitRegex(encoded, pattern);
    var result = new float[values.length];
    for (var i = 0; i < values.length; i++) {
      result[i] = converter.decode(values[i]);
    }
    return result;
  }

} /* ENDCLASS */
