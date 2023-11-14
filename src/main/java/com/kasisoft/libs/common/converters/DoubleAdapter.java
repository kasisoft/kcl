package com.kasisoft.libs.common.converters;

import jakarta.validation.constraints.*;

/**
 * Adapter for double values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DoubleAdapter extends AbstractConverter<String, Double> {

    private static final String NAN      = "NaN";

    private static final String NEG_INF  = "-INF";

    private static final String POS_INF1 = "+INF";

    private static final String POS_INF2 = "INF";

    private static final String MAX      = "MAX";

    private static final String MIN      = "MIN";

    @Override
    public String encodeImpl(@NotNull Double v) {
        if (Double.isNaN(v)) {
            return NAN;
        }
        if (Double.isInfinite(v)) {
            if (v < 0) {
                return NEG_INF;
            } else {
                return POS_INF1;
            }
        }
        if (v == Double.MAX_VALUE) {
            return MAX;
        }
        if (v == Double.MIN_VALUE) {
            return MIN;
        }
        return v.toString();
    }

    @Override
    public Double decodeImpl(@NotNull String v) {
        if (NAN.equalsIgnoreCase(v)) {
            return Double.NaN;
        }
        if (POS_INF1.equalsIgnoreCase(v) || POS_INF2.equalsIgnoreCase(v)) {
            return Double.POSITIVE_INFINITY;
        }
        if (NEG_INF.equalsIgnoreCase(v)) {
            return Double.NEGATIVE_INFINITY;
        }
        if (MAX.equalsIgnoreCase(v)) {
            return Double.MAX_VALUE;
        }
        if (MIN.equalsIgnoreCase(v)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(v);
    }

} /* ENDCLASS */
