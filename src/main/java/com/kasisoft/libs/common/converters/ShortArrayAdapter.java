package com.kasisoft.libs.common.converters;

import com.kasisoft.libs.common.pools.*;

import javax.validation.constraints.*;

import java.util.regex.Pattern;

/**
 * Adapter for short array values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ShortArrayAdapter extends AbstractConverter<String, short[]> {

  private char              delimiter = ',';
  private Pattern           pattern   = Pattern.compile(Pattern.quote(String.valueOf(delimiter)));
  private ShortAdapter      converter = new ShortAdapter();

  public @NotNull ShortArrayAdapter withDelimiter(@NotNull char delimiter) {
    if (this.delimiter != delimiter) {
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
  public String encodeImpl(@NotNull short[] decoded) {
    return Buckets.stringFBuilder().forInstance($ -> {
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
  public short[] decodeImpl(@NotNull String encoded) {
    return Buckets.stringFBuilder().forInstance($ -> {
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
