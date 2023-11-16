package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.utils.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.math.*;

/**
 * A basic description of a csv column.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record CsvColumn<T> (
    String                  title,
    Class<T>                type,
    boolean                 nullable,
    T                       defval,
    Function<String, T>     adapter
) {

    private static Map<Class<?>, Function<String, ?>> DEFAULT_ADAPTERS = new HashMap<>();

    static {
        DEFAULT_ADAPTERS.put(Integer.class, TypeConverters::convertStringToInteger);
        DEFAULT_ADAPTERS.put(String.class, $ -> $);
        DEFAULT_ADAPTERS.put(Boolean.class, TypeConverters::convertStringToBoolean);
        DEFAULT_ADAPTERS.put(byte[].class, Encoding.UTF8::optEncode);
        DEFAULT_ADAPTERS.put(BigDecimal.class, BigDecimal::new);
    }

    /**
     * Creates a copy of this instance.
     *
     * @return A copy of this instance.
     */
    @NotNull
    public CsvColumn<T> copy() {
        return new CsvColumn<T>(title, type, nullable, defval, adapter);
    }

    public static <R> CsvColumnBuilder<R> builder() {
        return new CsvColumnBuilder<>();
    }

    public static class CsvColumnBuilder<R> {

        private String                  title    = null;
        private Class<R>                type     = null;
        private boolean                 nullable = true;
        private R                       defval   = null;
        private Function<String, R>     adapter  = null;

        private CsvColumnBuilder() {
        }

        public CsvColumnBuilder<R> type(@NotNull Class<R> type) {
            this.type = type;
            return this;
        }

        public CsvColumnBuilder<R> nullable() {
            return nullable(true);
        }

        public CsvColumnBuilder<R> nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public CsvColumnBuilder<R> defaultValue(R defval) {
            this.defval = defval;
            return this;
        }

        public CsvColumnBuilder<R> title(String title) {
            this.title = title;
            return this;
        }

        public CsvColumnBuilder<R> adapter(Function<String, R> adapter) {
            this.adapter = adapter;
            return this;
        }

        @SuppressWarnings("unchecked")
        public CsvColumn<R> build() {
            type = type != null ? type : (Class<R>) String.class;
            if (adapter == null) {
                adapter= (Function<String, R>) DEFAULT_ADAPTERS.get(type);
            }
            return new CsvColumn<R>(title, type, nullable, defval, adapter);
        }

    } /* ENDCLASS */

} /* ENDRECORD */
