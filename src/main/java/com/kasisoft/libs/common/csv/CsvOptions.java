package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import jakarta.validation.constraints.*;

import java.util.stream.*;

import java.util.*;

/**
 * A collection of options used for the csv processing.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings("rawtypes")
public record CsvOptions(
    boolean         titleRow,
    char            delimiter,
    boolean         disableCr,
    boolean         fillMissingColumns,
    boolean         consumeSingleQuotes,
    boolean         consumeDoubleQuotes,
    // Processing a simple formatted CSV is much faster as it's less robust but should be capable to read most CSV files.
    // A CSV file must fulfill the following constraints:
    //
    //   * One record per row (no records spanning multiple lines)
    //   * If cell content is quoted it's not allowed to have whitespace outside of the quoted region
    //   * All records must have the same amount of cells
    //
    boolean         simpleFormat,
    // enable/disable whether the simple loading is allowed to mixup the record order
    boolean         orderedSimpleFormat,
    int             maxLines,
    Encoding        encoding,
    List<CsvColumn> columns
) {

    /**
     * Creates a deep copy of this instance.
     *
     * @return A deep copy of this instance.
     */
    @NotNull
    public CsvOptions deepCopy() {
        return new CsvOptions(
            titleRow,
            delimiter,
            disableCr,
            fillMissingColumns,
            consumeSingleQuotes,
            consumeDoubleQuotes,
            simpleFormat,
            orderedSimpleFormat,
            maxLines,
            encoding,
            columns.stream().map($ -> $ != null ? $.copy() : null).collect(Collectors.toList())
        );
    }

    @NotNull
    public static CsvOptionsBuilder builder() {
        return new CsvOptionsBuilder();
    }

    public static class CsvOptionsBuilder {

        private boolean         titleRow            = false;
        private char            delimiter           = ',';
        private boolean         disableCr           = true;
        private boolean         fillMissingColumns  = false;
        private boolean         consumeSingleQuotes = true;
        private boolean         consumeDoubleQuotes = true;
        private boolean         simpleFormat        = false;
        private boolean         orderedSimpleFormat = true;
        private int             maxLines            = -1;
        private Encoding        encoding            = Encoding.UTF8;
        private List<CsvColumn> columns             = new ArrayList<>();

        private CsvOptionsBuilder() {
        }

        public CsvOptionsBuilder simpleFormat() {
            this.simpleFormat = true;
            return this;
        }

        public CsvOptionsBuilder maxLines(int maxLines) {
            this.maxLines = maxLines;
            return this;
        }

        public CsvOptionsBuilder singleQuotes(boolean consumeSingleQuotes) {
            this.consumeSingleQuotes = consumeSingleQuotes;
            return this;
        }

        public CsvOptionsBuilder doubleQuotes(boolean consumeDoubleQuotes) {
            this.consumeDoubleQuotes = consumeDoubleQuotes;
            return this;
        }

        public CsvOptionsBuilder encoding(@NotNull Encoding encoding) {
            this.encoding = encoding;
            return this;
        }

        public CsvOptionsBuilder delimiter(char delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public CsvOptionsBuilder fillMissingColumns() {
            return fillMissingColumns(true);
        }

        public CsvOptionsBuilder fillMissingColumns(boolean enable) {
            this.fillMissingColumns = enable;
            return this;
        }

        public CsvOptionsBuilder disableCr() {
            return disableCr(true);
        }

        public CsvOptionsBuilder disableCr(boolean enable) {
            this.disableCr = enable;
            return this;
        }

        public CsvOptionsBuilder titleRow() {
            return titleRow(true);
        }

        public CsvOptionsBuilder titleRow(boolean enable) {
            this.titleRow = enable;
            return this;
        }

        public CsvOptionsBuilder column(CsvColumn column) {
            this.columns.add(column);
            return this;
        }

        public CsvOptionsBuilder columns(@NotNull List<CsvColumn> columns) {
            this.columns.clear();
            this.columns.addAll(columns);
            return this;
        }

        public CsvOptions build() {
            return new CsvOptions(
                titleRow,
                delimiter,
                disableCr,
                fillMissingColumns,
                consumeSingleQuotes,
                consumeDoubleQuotes,
                simpleFormat,
                orderedSimpleFormat,
                maxLines,
                encoding,
                columns
            );
        }

    } /* ENDCLASS */

} /* ENDRECORD */
