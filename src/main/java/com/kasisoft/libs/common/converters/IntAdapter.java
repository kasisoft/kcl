package com.kasisoft.libs.common.converters;

import jakarta.validation.constraints.*;

/**
 * Adapter for int values.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class IntAdapter extends AbstractConverter<String, Integer> {

    @Override
    public String encodeImpl(@NotNull Integer v) {
        return Integer.toString(v);
    }

    @Override
    public Integer decodeImpl(@NotNull String v) {
        return Integer.valueOf(v);
    }

} /* ENDCLASS */
