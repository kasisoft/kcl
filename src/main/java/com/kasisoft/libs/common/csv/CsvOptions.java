package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * A collection of options used for the csv processing.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("rawtypes")
public final class CsvOptions {

  private boolean           titleRow            = false;
  private char              delimiter           = ',';
  private boolean           disableCr           = true;
  private boolean           fillMissingColumns  = false;
  private boolean           consumeSingleQuotes = true;
  private boolean           consumeDoubleQuotes = true;

  // Processing a simple formatted CSV is much faster as it's less robust but should be capable to read most CSV files.
  // A CSV file must fulfill the following constraints:
  //
  //   * One record per row (no records spanning multiple lines)
  //   * If cell content is quoted it's not allowed to have whitespace outside of the quoted region
  //   * All records must have the same amount of cells
  //
  private boolean           simpleFormat        = false;

  // enable/disable whether the simple loading is allowed to mixup the record order
  private boolean           orderedSimpleFormat = true;
  private int               maxLines            = -1;
  private Encoding          encoding            = Encoding.UTF8;
  private List<CsvColumn>   columns             = new ArrayList<>();

  public CsvOptions() {
  }

  public boolean isTitleRow() {
    return titleRow;
  }

  public char getDelimiter() {
    return delimiter;
  }

  public boolean isDisableCr() {
    return disableCr;
  }

  public boolean isFillMissingColumns() {
    return fillMissingColumns;
  }

  public boolean isConsumeSingleQuotes() {
    return consumeSingleQuotes;
  }

  public boolean isConsumeDoubleQuotes() {
    return consumeDoubleQuotes;
  }

  public boolean isSimpleFormat() {
    return simpleFormat;
  }

  public boolean isOrderedSimpleFormat() {
    return orderedSimpleFormat;
  }

  public int getMaxLines() {
    return maxLines;
  }

  public Encoding getEncoding() {
    return encoding;
  }

  public List<CsvColumn> getColumns() {
    return columns;
  }

  /**
   * Creates a deep copy of this instance.
   *
   * @return   A deep copy of this instance.
   */
  public @NotNull CsvOptions deepCopy() {
    var result                  = new CsvOptions();
    result.titleRow             = titleRow;
    result.delimiter            = delimiter;
    result.disableCr            = disableCr;
    result.fillMissingColumns   = fillMissingColumns;
    result.consumeSingleQuotes  = consumeSingleQuotes;
    result.consumeDoubleQuotes  = consumeDoubleQuotes;
    result.simpleFormat         = simpleFormat;
    result.orderedSimpleFormat  = orderedSimpleFormat;
    result.maxLines             = maxLines;
    result.encoding             = encoding;
    for (var column : columns) {
      if (column != null) {
        result.columns.add(column.copy());
      } else {
        result.columns.add(null);
      }
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((columns == null) ? 0 : columns.hashCode());
    result = prime * result + (consumeDoubleQuotes ? 1231 : 1237);
    result = prime * result + (consumeSingleQuotes ? 1231 : 1237);
    result = prime * result + delimiter;
    result = prime * result + (disableCr ? 1231 : 1237);
    result = prime * result + ((encoding == null) ? 0 : encoding.hashCode());
    result = prime * result + (fillMissingColumns ? 1231 : 1237);
    result = prime * result + maxLines;
    result = prime * result + (orderedSimpleFormat ? 1231 : 1237);
    result = prime * result + (simpleFormat ? 1231 : 1237);
    result = prime * result + (titleRow ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsvOptions other = (CsvOptions) obj;
    if (columns == null) {
      if (other.columns != null)
        return false;
    } else if (!columns.equals(other.columns))
      return false;
    if (consumeDoubleQuotes != other.consumeDoubleQuotes)
      return false;
    if (consumeSingleQuotes != other.consumeSingleQuotes)
      return false;
    if (delimiter != other.delimiter)
      return false;
    if (disableCr != other.disableCr)
      return false;
    if (encoding == null) {
      if (other.encoding != null)
        return false;
    } else if (!encoding.equals(other.encoding))
      return false;
    if (fillMissingColumns != other.fillMissingColumns)
      return false;
    if (maxLines != other.maxLines)
      return false;
    if (orderedSimpleFormat != other.orderedSimpleFormat)
      return false;
    if (simpleFormat != other.simpleFormat)
      return false;
    if (titleRow != other.titleRow)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CsvOptions [titleRow=" + titleRow + ", delimiter=" + delimiter + ", disableCr=" + disableCr
        + ", fillMissingColumns=" + fillMissingColumns + ", consumeSingleQuotes=" + consumeSingleQuotes
        + ", consumeDoubleQuotes=" + consumeDoubleQuotes + ", simpleFormat=" + simpleFormat + ", orderedSimpleFormat="
        + orderedSimpleFormat + ", maxLines=" + maxLines + ", encoding=" + encoding + ", columns=" + columns + "]";
  }

  public static @NotNull CsvOptionsBuilder builder() {
    return new CsvOptionsBuilder();
  }

  public static class CsvOptionsBuilder {

    private CsvOptions   instance = new CsvOptions();

    private CsvOptionsBuilder() {
    }

    public CsvOptionsBuilder simpleFormat() {
      instance.simpleFormat = true;
      return this;
    }

    public CsvOptionsBuilder maxLines(int maxLines) {
      instance.maxLines = maxLines;
      return this;
    }

    public CsvOptionsBuilder singleQuotes(boolean consumeSingleQuotes) {
      instance.consumeSingleQuotes = consumeSingleQuotes;
      return this;
    }

    public CsvOptionsBuilder doubleQuotes(boolean consumeDoubleQuotes) {
      instance.consumeDoubleQuotes = consumeDoubleQuotes;
      return this;
    }

    public CsvOptionsBuilder encoding(@NotNull Encoding encoding) {
      instance.encoding = encoding;
      return this;
    }

    public CsvOptionsBuilder delimiter(char delimiter) {
      instance.delimiter = delimiter;
      return this;
    }

    public CsvOptionsBuilder fillMissingColumns() {
      return fillMissingColumns(true);
    }

    public CsvOptionsBuilder fillMissingColumns(boolean enable) {
      instance.fillMissingColumns = enable;
      return this;
    }

    public CsvOptionsBuilder disableCr() {
      return disableCr(true);
    }

    public CsvOptionsBuilder disableCr(boolean enable) {
      instance.disableCr = enable;
      return this;
    }

    public CsvOptionsBuilder titleRow() {
      return titleRow(true);
    }

    public CsvOptionsBuilder titleRow(boolean enable) {
      instance.titleRow = enable;
      return this;
    }

    public CsvOptionsBuilder column(CsvColumn column) {
      instance.columns.add(column);
      return this;
    }

    public CsvOptionsBuilder columns(@NotNull List<CsvColumn> columns) {
      instance.columns.clear();
      instance.columns.addAll(columns);
      return this;
    }

    public CsvOptions build() {
      return instance;
    }

  } /* ENDCLASS */

} /* ENDCLASS */
