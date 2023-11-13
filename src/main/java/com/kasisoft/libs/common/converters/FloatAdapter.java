package com.kasisoft.libs.common.converters;

import jakarta.validation.constraints.*;

/**
 * Adapter for float values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FloatAdapter extends AbstractConverter<String, Float> {

  private static final String NAN       = "NaN";
  private static final String NEG_INF   = "-INF";
  private static final String POS_INF1  = "+INF";
  private static final String POS_INF2  = "INF";
  private static final String MAX       = "MAX";
  private static final String MIN       = "MIN";

  @Override
  public String encodeImpl(@NotNull Float v) {
    if (Float.isNaN(v)) {
      return NAN;
    }
    if (Float.isInfinite(v)) {
      if (v < 0) {
        return NEG_INF;
      } else {
        return POS_INF1;
      }
    }
    if (v == Float.MAX_VALUE) {
      return MAX;
    }
    if (v == Float.MIN_VALUE) {
      return MIN;
    }
    return v.toString();
  }

  @Override
  public Float decodeImpl(@NotNull String v) {
    if (NAN.equalsIgnoreCase(v)) {
      return Float.NaN;
    }
    if (POS_INF1.equalsIgnoreCase(v) || POS_INF2.equalsIgnoreCase(v)) {
      return Float.POSITIVE_INFINITY;
    }
    if (NEG_INF.equalsIgnoreCase(v)) {
      return Float.NEGATIVE_INFINITY;
    }
    if (MAX.equalsIgnoreCase(v)) {
      return Float.MAX_VALUE;
    }
    if (MIN.equalsIgnoreCase(v)) {
      return Float.MIN_VALUE;
    }
    return Float.parseFloat(v);
  }

} /* ENDCLASS */
