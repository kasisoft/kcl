package com.kasisoft.libs.common.csv;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.utils.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * A TableModel implementation that can be fed by CSV data.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CsvTableModel implements TableModel {

  private static final char CR = '\r';
  private static final char DQ = '\"';
  private static final char LF = '\n';
  private static final char SQ = '\'';

  private static final String CR_STR = "\r";
  private static final String DQ_STR = "\"";
  private static final String LF_STR = "\n";
  private static final String SQ_STR = "\'";

  private static final String CRLF_STR = "\r\n";

  private static final String   DEFVAL_STRING   = "";
  private static final Double   DEFVAL_DOUBLE   = Double  . valueOf(0);
  private static final Float    DEFVAL_FLOAT    = Float   . valueOf(0);
  private static final Long     DEFVAL_LONG     = Long    . valueOf(0);
  private static final Integer  DEFVAL_INTEGER  = Integer . valueOf(0);
  private static final Short    DEFVAL_SHORT    = Short   . valueOf( (short) 0 );
  private static final Byte     DEFVAL_BYTE     = Byte    . valueOf( (byte)  0 );
  private static final Boolean  DEFVAL_BOOLEAN  = Boolean . FALSE;

  private enum ContentType {

    LINE_DELIMITER,
    SEPARATOR,
    CONTENT;

  } /* ENDENUM */

  private CsvOptions            options;
  private DefaultTableModel     tableModel;
  private EventListenerList     listeners;

  private Consumer<String>      ehInvalidCellValue;
  private Consumer<String>      ehColumnSpecWithoutAdapter;
  private Consumer<String>      ehInconsistentColumnCount;
  private Consumer<String>      ehInvalidAddRow;

  private CsvTableModel() {
    ehInvalidCellValue          = this::ehDefault;
    ehColumnSpecWithoutAdapter  = this::ehDefault;
    ehInconsistentColumnCount   = this::ehDefault;
    ehInvalidAddRow             = this::ehDefault;
    listeners                   = new EventListenerList();
    createNewDefaultTableModel();
  }

  public CsvTableModel(@NotNull CsvOptions csvOptions) {
    this();
    options = validateOptions(csvOptions);
    consolidateColumns(csvOptions.getColumns().size(), Collections.emptyList(), getTitles( csvOptions.getColumns().size(), Collections.emptyList()));
    options.getColumns().forEach($ -> tableModel.addColumn($.getTitle()));
  }

  public @NotNull CsvOptions getOptions() {
    return options;
  }

  public synchronized void removeRow(int row) {
    tableModel.removeRow(row);
  }

  public synchronized void removeRow(Predicate<Object[]> isValid) {
    var start = options.isTitleRow() ? 1 : 0;
    for (var i = tableModel.getRowCount() - 1; i >= start; i--) {
      var row = tableModel.getDataVector().get(i);
      if (!isValid.test(row.toArray())) {
        tableModel.removeRow(i);
      }
    }
  }

  public synchronized <I> void forEach(Consumer<I> rowConsumer, String column) {
    forEach(Functions.adaptConsumerToBiConsumer(rowConsumer), null, column);
  }

  public synchronized <C, I> void forEach(BiConsumer<I, C> rowConsumer, C context, String column) {
    var start  = options.isTitleRow() ? 1 : 0;
    var mapRow = getObjects(column);
    for (var i = start; i < tableModel.getRowCount(); i++) {
      var row = tableModel.getDataVector().get(i);
      rowConsumer.accept((I) mapRow.apply(row)[0], context);
    }
  }

  public synchronized void forEach(Consumer<Object[]> rowConsumer, String ... columns) {
    forEach(Functions.adaptConsumerToBiConsumer(rowConsumer), null, columns);
  }

  public synchronized <C> void forEach(BiConsumer<Object[], C> rowConsumer, C context, String ... columns) {
    var start  = options.isTitleRow() ? 1 : 0;
    var mapRow = getObjects(columns);
    for (var i = start; i < tableModel.getRowCount(); i++) {
      var row = tableModel.getDataVector().get(i);
      rowConsumer.accept(mapRow.apply(row), context);
    }
  }

  public synchronized <R, I> R reduce(R initial, BiFunction<R, I, R> operator, String column) {
    var result = initial;
    var start  = options.isTitleRow() ? 1 : 0;
    var mapRow = getObjects( column );
    for (var i = start; i < tableModel.getRowCount(); i++) {
      var row = tableModel.getDataVector().get(i);
      result  = operator.apply( result, (I) mapRow.apply( row )[0] );
    }
    return result;
  }

  public synchronized <R> @NotNull R reduce(R initial, BiFunction<R, Object[], R> operator, String ... columns) {
    var result = initial;
    var start  = options.isTitleRow() ? 1 : 0;
    var mapRow = getObjects( columns );
    for (var i = start; i < tableModel.getRowCount(); i++) {
      var row = tableModel.getDataVector().get(i);
      result  = operator.apply(result, mapRow.apply(row));
    }
    return result;
  }

  private @NotNull Function<Vector, Object[]> getObjects(String ... columns) {
    if ((columns == null) || (columns.length == 0)) {
      return $ -> $.toArray();
    } else {
      var indices = new int[columns.length];
      for (var i = 0; i < indices.length; i++) {
        indices[i] = getColumnIndex(columns[i]);
      }
      return $ -> {
        var in  = $.toArray();
        var out = new Object[indices.length];
        for (var i = 0; i < indices.length; i++) {
          out[i] = in[indices[i]];
        }
        return out;
      };
    }
  }

  public synchronized @Min(0) int getColumnIndex(@NotBlank String columnName) {
    var result = -1;
    for (int i = 0; i < getColumnCount(); i++) {
      String colName = tableModel.getColumnName(i);
      if (columnName.equals(colName)) {
        result = i;
        break;
      }
    }
    return result;
  }

  public synchronized boolean isValidColumn(@Min(0) int column) {
    return (column >= 0) && (column < tableModel.getColumnCount());
  }

  public synchronized <C> void removeRow(@NotNull Predicate<C> rowTest, @NotBlank String columnName) {
    removeRow(rowTest, getColumnIndex(columnName));
  }

  public synchronized <C> void removeRow(@NotNull Predicate<C> isValid, @Min(0) int column) {
    if (isValidColumn(column)) {
      removeRow($ -> isValid.test((C) $[column]));
    }
  }

  public synchronized void removeColumn(@NotBlank String name) {
    removeColumn(getColumnIndex(name));
  }

  public synchronized void removeColumn(@Min(0) int column) {
    if (isValidColumn(column)) {
      for (var row = 0; row < tableModel.getRowCount(); row++) {
        var rowData = tableModel.getDataVector().get(row);
        rowData.remove( column );
      }
      options.getColumns().remove(column);
    }
  }

  public synchronized <L, R, O> void joinColumns(@NotBlank String column1, @NotBlank String column2, @NotNull BiFunction<L, R, O> joiner, @NotBlank String columnName) {
    joinColumns(getColumnIndex(column1), getColumnIndex(column2), joiner, columnName, true, null);
  }

  public synchronized <L, R, O> void joinColumns(@Min(0) int column1, @Min(0) int column2, @NotNull BiFunction<L, R, O> joiner, @NotBlank String columnName) {
    joinColumns(column1, column2, joiner, columnName, true, null);
  }

  public synchronized <L, R, O> void joinColumns(@NotBlank String column1, @NotBlank String column2, @NotNull BiFunction<L, R, O> joiner, @NotBlank String columnName, boolean nullable, O defaultVal) {
    joinColumns(getColumnIndex(column1), getColumnIndex(column2), joiner, columnName, nullable, defaultVal);
  }

  public synchronized <L, R, O> void joinColumns(@Min(0) int column1, @Min(0) int column2, @NotNull BiFunction<L, R, O> joiner, @NotBlank String columnName, boolean nullable, O defaultVal) {
    if (isValidColumn(column1) && isValidColumn(column2)) {
      var newColumn = new CsvColumn<>();
      newColumn.setNullable( nullable);
      newColumn.setTitle(columnName);
      newColumn.setDefval(defaultVal);
      options.getColumns().add(newColumn);
      var idxMax = Math.max(column1, column2);
      var idxMin = Math.min(column1, column2);
      for (var row = 0; row < tableModel.getRowCount(); row++) {
        var rowData = tableModel.getDataVector().get(row);
        var left    = (L) rowData.get(column1);
        var right   = (R) rowData.get(column2);
        var joined  = joiner.apply(left, right);
        rowData.add(joined);
        rowData.remove(idxMax);
        rowData.remove(idxMin);
      }
      options.getColumns().remove(idxMax);
      options.getColumns().remove(idxMin);
    }
  }

  /**
   * Creates a new DefaultTableModel instance used as the delegate.
   */
  private void createNewDefaultTableModel() {
    if (tableModel != null) {
      tableModel.removeTableModelListener(this::tableModelEventDelegator);
    }
    tableModel = new DefaultTableModel();
    tableModel.addTableModelListener(this::tableModelEventDelegator);
  }

  /**
   * Dispatches each event to the registered listeners while replacing the original event source.
   *
   * @param evt   The event that shall be dispatched.
   */
  private void tableModelEventDelegator(@NotNull TableModelEvent evt) {
    fireTableChanged(new TableModelEvent(this, evt.getFirstRow(), evt.getLastRow(), evt.getColumn(), evt.getType()));
  }

  /**
   * Fires the supplied event to each currently registered listener.
   *
   * @param evt   The event that is supposed to be dispatched.
   */
  private void fireTableChanged(@NotNull TableModelEvent evt) {
    var objects = listeners.getListenerList();
    for (int i = objects.length - 2, j = objects.length - 1; i >= 0; i -= 2, j -= 2) {
      if (objects[i] == TableModelListener.class) {
        ((TableModelListener) objects[j]).tableChanged(evt);
      }
    }
  }

  /**
   * Returns a validated copy of csv options.
   *
   * @param options   The options that do provide the default setup.
   *
   * @return   A validated copy of csv options.
   */
  private CsvOptions validateOptions(@NotNull CsvOptions options) {
    var result = options.deepCopy();
    for (var i = 0; i < result.getColumns().size(); i++) {
      var csvColumn = result.getColumns().get(i);
      if ((csvColumn != null) && (csvColumn.getAdapter() == null)) {
        ehColumnSpecWithoutAdapter.accept(error_missing_csv_adapter.format(i));
        // unless the error handler caused an exception we're clearing this spec, so it can be
        // calculcated afterwards
        result.getColumns().set(i, null);
      }
    }
    return result;
  }

  /**
   * Reads the csv data from the supplied {@link InputStream} source.
   *
   * @param source   The source providing the csv data
   *
   * @return   A normalized table of csv data.
   */
  private @NotNull List<List<String>> loadCellData(@NotNull InputStream source) {
    if (options.isSimpleFormat()) {
      return loadCellDataSimple(source);
    } else {
      return loadCellDataDefault(source);
    }
  }

  private @NotNull List<List<String>> loadCellDataDefault(InputStream source) {

    /* The import follows these steps:
     *
     * 1. Read the complete csv as a single text.
     * 2. Convert this text into a sequence of tokens.
     * 3. Normalize these tokens.
     * 4. Split these tokens across the lines. Drops the line separator tokens.
     * 5. Add missing column values (f.e. two succeeding separators).
     * 6. Drop all separators.
     * 7. Turn the remaining content cells into Strings again.
     */

    var text      = readContent(source);

    var tokenized = tokenize(text).parallelStream()
      .map(this::toContent)
      .map(this::normalize)
      .collect(Collectors.toList())
      ;

    // fetch all lines and add potentially missing column data
    var partitioned = partition(tokenized);
    artificialContent(partitioned);

    // remove separators and get the texts only
    return cleanup(partitioned);

  }

  private @NotNull List<List<String>> loadCellDataSimple(@NotNull InputStream source) {

    /* The import follows these steps:
     *
     * 1. Read the complete csv as a single text.
     * 2. Remember the title row if any.
     * 3. Split all lines assuming they are well formed.
     * 4. Insert the title row.
     */

    var text  = readContent(source).toString();
    var lines = new ArrayList<>(Arrays.asList(text.split("\n")));

    String titleRow = null;
    if (options.isTitleRow()) {
      titleRow = lines.remove(0);
    }

    List<List<String>> result = (options.isOrderedSimpleFormat() ? lines.stream() : lines.parallelStream())
      .map(this::tokenizeSimple)
      .collect(Collectors.toList())
      ;

    if (titleRow != null) {
      result.add(0, tokenizeSimple(titleRow));
    }

    int max = result.parallelStream()
      .map($ -> $.size())
      .reduce(result.get(0).size(), Math::max)
      .intValue()
      ;

    result.parallelStream()
      .filter($ -> $.size() < max)
      .forEach($ -> fillUp($, max))
      ;

    return result;

  }

  private void fillUp(@NotNull List<String> cells, int max) {
    while (cells.size() < max) {
      cells.add("");
    }
  }

  /**
   * Reads the content of the supplied {@link InputStream} instance into a {@link StringBuilder}.
   *
   * @param source   The {@link InputStream} instance.
   *
   * @return   The {@link StringBuilder} providing the text.
   */
  private @NotNull StringBuilder readContent(@NotNull InputStream source) {
    var writer = new CharArrayWriter();
    IoFunctions.forReaderDo(source, options.getEncoding(), $ -> IoFunctions.copy($, writer));
    return new StringBuilder(writer.toString());
  }

  /**
   * Converts the supplied text into sequence of tokens.
   *
   * @param text   The full text.
   *
   * @return   A list of tokens.
   */
  private @NotNull List<String> tokenize(@NotNull StringBuilder text) {
    var result = new ArrayList<String>(Math.min(5, text.length() / 5));
    while (text.length() > 0) {
      consume (result, text);
    }
    dropEmptySequences(result);
    return result;
  }

  private List<String> tokenizeSimple(@NotNull String text) {

    var result = new ArrayList<String>(10);
    for (int i = 0; i < text.length(); i++) {

      var first = text.charAt(i);
      if ((first == DQ) && options.isConsumeDoubleQuotes()) {
        var close = text.indexOf(DQ, i + 1);
        if (close == -1) {
          throw new KclException(error_csv_missing_closing_quote, text);
        }
        result.add(text.substring(i + 1, close));
        i = close + 1;
      } else if ((first == SQ) && options.isConsumeSingleQuotes()) {
        var close = text.indexOf(SQ, i + 1);
        if (close == -1) {
          throw new KclException(error_csv_missing_closing_quote,  text);
        }
        result.add(text.substring(i + 1, close));
        i = close + 1;
      } else if (first == options.getDelimiter()) {
        // empty cell
        result.add("");
      } else {
        var end = text.indexOf(options.getDelimiter(), i + 1);
        if (end == -1) {
          result.add(text.substring(i));
          i = text.length();
        } else {
          result.add(text.substring(i, end));
          i = end;
        }
      }
    }
    return result;
  }

  /**
   * This method replaces sequences of LF, WHITESPACE*, LF to LF. Such sequences can occure if the csv
   * contains empty lines where some lines contain whitespace characters. Those lines are of no interest
   * for the csv content.
   *
   * @param result   The list of tokens that shall be altered.
   */
  private void dropEmptySequences(@NotNull List<String> result) {
    for (int i = result.size() - 1, j = result.size() - 2, k = result.size() - 3; k >= 0; i--, j--, k--) {
      var current = result.get(i);
      var between = result.get(j);
      var before  = result.get(k);
      if ((current.length() == 1) && (before.length() == 1) && (current.charAt(0) == LF) && (before.charAt(0) == LF)) {
        if (between.trim().length() == 0) {
          result.remove(i);
          result.remove(j);
          i = k;
          j = i - 1;
          k = i - 2;
        }
      }
    }
  }

  /**
   * Consumes the next token.
   *
   * @param receiver   The receiver for the generated tokens.
   * @param content    The current text to be processed.
   */
  private void consume(@NotNull List<String> receiver, @NotNull StringBuilder content) {
    var first = content.charAt(0);
    if ((first == CR) || (first == LF)) {
      consumeCRLF(receiver, content);
    } else if ((first == DQ) && options.isConsumeDoubleQuotes()) {
      consumeQuoted(receiver, content, first, DQ_STR);
    } else if ((first == SQ) && options.isConsumeSingleQuotes()) {
      consumeQuoted (receiver, content, first, SQ_STR);
    } else if (first == options.getDelimiter()) {
      consumeCellSeparator(receiver, content);
    } else {
      consumeNormal(receiver, content);
    }
  }

  /**
   * Consumes the next token which is a sequence of quoted text.
   *
   * @param receiver     The receiver for the generated tokens.
   * @param content      The current text to be processed.
   * @param quote        The currently used quote.
   * @param quoteAsStr   Same as quote but as a String.
   */
  private void consumeQuoted(@NotNull List<String> receiver, @NotNull StringBuilder content, char quote, @NotNull String quoteAsStr) {
    var pos  = 1;
    while (true) {
      var idx1 = content.indexOf(quoteAsStr, pos);
      if (idx1 == -1) {
        // the format is invalid
        throw new KclException(error_csv_missing_closing_quote, content);
      }
      if (idx1 == content.length() - 1) {
        // this cell covers the full remaining content, so we're done here
        receiver.add(content.toString());
        content.setLength(0);
        return;
      }
      if (content.charAt(idx1 + 1) == quote) {
        // two immediately succeeding quotes, so it's not an actual closing quote (escaping quote) which
        // means we need to skip this sequence and try to look for the next quote.
        pos = idx1 + 2;
        continue;
      }
      // we've got an ending for the current cell, so add it's content
      receiver.add( content.substring(0, idx1 + 1));
      content.delete(0, idx1 + 1);
      break;
    }
  }

  /**
   * Consumes the next token which is a simple unquote sequence.
   *
   * @param receiver   The receiver for the generated tokens.
   * @param content    The current text to be processed.
   */
  private void consumeNormal(@NotNull List<String> receiver, @NotNull StringBuilder content) {
    var idx = StringFunctions.indexOf(content.toString(), options.getDelimiter(), LF, CR, DQ, SQ);
    if (idx == -1) {
      // there's no other cell following, so the remaining text covers tghe complete cell
      receiver.add(content.toString());
      content.setLength(0);
    } else {
      var part = content.substring(0, idx);
      content.delete( 0, idx );
      var ch   = content.charAt(0);
      if ((ch == DQ) || (ch == SQ)) {
        // there is some quote text which is not separated so it's part of the previous text sequence
        consumeQuoted(receiver, content, ch, String.valueOf(ch));
        // prepend the text sequence to the recently added quote text
        receiver.set(receiver.size() - 1, part + receiver.get(receiver.size() - 1));
      } else {
        // this is a new token
        receiver.add(part);
      }
    }
  }

  /**
   * Consumes the cell separator.
   *
   * @param receiver   The receiver for the generated tokens.
   * @param content    The current text to be processed.
   */
  private void consumeCellSeparator(@NotNull List<String> receiver, @NotNull StringBuilder content) {
    receiver.add(content.substring(0, 1));
    content.deleteCharAt(0);
  }

  /**
   * Consumes a seqeucne of cr and lf characters. These are represented by a lf only.
   *
   * @param receiver   The receiver for the generated tokens.
   * @param content    The current text to be processed.
   */
  private void consumeCRLF(@NotNull List<String> receiver, @NotNull StringBuilder content) {
    var idx = 1;
    for (var i = 1; i < content.length(); i++) {
      var current = content.charAt(i);
      if ((current == LF) || (current == CR)) {
        idx++;
      } else {
        break;
      }
    }
    content.delete(0, idx);
    receiver.add(LF_STR);
  }

  /**
   * This function generates a simple token which allows to deal with the data more easily.
   *
   * @param data   The current token data.
   *
   * @return   The tokenized content.
   */
  private @NotNull Content toContent(String data) {
    var type = ContentType.CONTENT;
    if ((data != null) && (data.length() < 2)) {
      if (data.charAt(0) == options.getDelimiter()) {
        type = ContentType.SEPARATOR;
      } else if( data.charAt(0) == LF ) {
        type = ContentType.LINE_DELIMITER;
      }
    }
    return new Content(data, type);
  }

  /**
   * This function normalizes the supplied content. Normalization means one/more of the following things:
   *
   * <ul>
   *   <li>the left/right side of the data will be trimmed (except for line delimiters)</li>
   *   <li>if the data is empty it will be nulled</li>
   *   <li>quotes around the content will be dropped</li>
   *   <li>duplicate double/single-quotes will be unescaped to one double/single-quote</li>
   *   <li>if cr's are discouraged they will be replaced by lf's</li>
   * </ul>
   *
   * @param content   The content that will be normalized.
   *
   * @return   The supplied {@link Content} instance.
   */
  private @NotNull Content normalize(@NotNull Content content) {
    var result = content;
    if ((result.type == ContentType.CONTENT) && (result.data != null)) {
      result.data = StringFunctions.trim(result.data, "\t ", null);
      if (result.data.length() == 0) {
        result.data = null;
      }
      if (result.data != null) {
        var ch = result.data.charAt(0);
        if ((ch == DQ) && options.isConsumeDoubleQuotes()) {
          result.data  = result.data.substring(1, result.data.length() - 1);
          result.data  = StringFunctions.replaceLiterallyAll(result.data, DQ_STR + DQ_STR, DQ_STR);
        } else if ((ch == SQ) && options.isConsumeSingleQuotes()) {
          result.data  = result.data.substring(1, result.data.length() - 1);
          result.data  = StringFunctions.replaceLiterallyAll(result.data, SQ_STR + SQ_STR, SQ_STR);
        }
      }
      if (options.isDisableCr() && (result.data != null)) {
        result.data = StringFunctions.replaceLiterallyAll(result.data, CRLF_STR, LF_STR);
        result.data = StringFunctions.replaceLiterallyAll(result.data, CR_STR, LF_STR);
      }
    }
    return result;
  }

  /**
   * Splits the sequence of content instances into separate lines while removing the line delimiter tokens.
   *
   * @param content   The full sequence of tokens.
   *
   * @return   A table structured sequence of tokens.
   */
  private @NotNull List<List<Content>> partition(@NotNull List<Content> content) {
    var             result      = new ArrayList<List<Content>>();
    var             line        = new ArrayList<Content>();
    Predicate<List> isfilled    = options.getMaxLines() != -1 ? $ -> $.size() >= options.getMaxLines() : $ -> false;
    result.add(line);
    while ((! content.isEmpty()) && (!isfilled.test(result))) {
      var current = content.remove(0);
      if (current.type == ContentType.LINE_DELIMITER) {
        if (!line.isEmpty()) {
          line = new ArrayList<Content>();
        }
        result.add(line);
      } else {
        line.add(current);
      }
    }
    for (var i = result.size() - 1; i >= 0; i--) {
      if (result.get(i).isEmpty()) {
        result.remove(i);
      }
    }
    return result;
  }

  /**
   * This function generates some artificial tokens implied by the csv data. For example:
   *
   * <ul>
   *   <li>A line always starts with a cell, so if the first token is a separator we're inserting an empty column</li>
   *   <li>A line always ends with a cell, so if the last token is a separator we're adding an empty column</li>
   *   <li>If two separator are positioned in an immediate sequence, we're inserting an empty column in between</li>
   *   <li>If configured some rows are smaller than others they can be filled up with empty columns</li>
   * </ul>
   *
   * @param lines   The current table data.
   */
  private void artificialContent(@NotNull List<List<Content>> lines) {

    // there's always a first column, so if a line starts with a separator insert one at the first position
    lines.parallelStream()
      .filter($ -> $.get(0).type == ContentType.SEPARATOR)
      .forEach($ -> $.add( 0, new Content( null, ContentType.CONTENT)))
      ;

    // there's always a last column, so if a line ends with a separator insert one at the last position
    lines.parallelStream()
      .filter($ -> $.get($.size() - 1).type == ContentType.SEPARATOR)
      // there's always a last column
      .forEach($ -> $.add( new Content(null, ContentType.CONTENT)))
      ;

    // insert a column if there are subsequence separators
    lines.parallelStream().forEach(this::extendDuplicateDelimiters);

    if (options.isFillMissingColumns()) {
      var maxcolumns = lines.parallelStream().mapToInt($ -> $.size()).reduce(0, Math::max);
      lines.parallelStream().forEach($ -> fillMissingColumns($, maxcolumns));
    }

  }

  /**
   * Inserts an empty cell between two succeeding separators.
   *
   * @param line   The line that will be processed.
   */
  private void extendDuplicateDelimiters(@NotNull List<Content> line) {
    for (int i = line.size() - 1, j = line.size() - 2; j >= 0; i--, j--) {
      var current = line.get(i);
      var before  = line.get(j);
      if ((current.type == ContentType.SEPARATOR) && (before.type == ContentType.SEPARATOR)) {
        line.add(i, new Content(null, ContentType.CONTENT));
      }
    }
  }

  /**
   * Extends the supplied row with empty columns, so the number of cells per line is consistent.
   *
   * @param line   The line that will be extended.
   * @param max    The maximum number of columns.
   */
  private void fillMissingColumns(@NotNull List<Content> line, int max) {
    while (line.size() < max) {
      var last = line.get(line.size() - 1).type;
      if (last == ContentType.CONTENT) {
        line.add(new Content(",", ContentType.SEPARATOR));
      } else {
        line.add(new Content(null, ContentType.CONTENT));
      }
    }
  }

  /**
   * This function translates the tabular structured tokens into corresponding textual cells.
   * This requires to remove all separators as they're no longer needed. Afterwards only the text of
   * each token will be reused.
   *
   * @param lines   The tabular structured tokens.
   *
   * @return   The textual tabular structure.
   */
  private @NotNull List<List<String>> cleanup(@NotNull List<List<Content>> lines) {
    lines.parallelStream().forEach(this::removeSeparators);
    return lines.stream().map(this::unwrap).collect(Collectors.toList());
  }

  /**
   * Unwraps the supplied line while just taking the textual data.
   *
   * @param line   The line providing the tokens.
   *
   * @return   The line providing the corresponding textual cell values.
   */
  private @NotNull List<String> unwrap(@NotNull List<Content> line) {
    return line.stream().map($ -> $.data).map(StringFunctions::cleanup).collect(Collectors.toList());
  }

  /**
   * Removes all cell separators from the supplied line.
   *
   * @param lines   The line that get's freed from the cell separators.
   */
  private void removeSeparators(@NotNull List<Content> lines) {
    for (var i = lines.size() - 1; i >= 0; i--) {
      if (lines.get(i).type == ContentType.SEPARATOR) {
        lines.remove(i);
      }
    }
  }

  /**
   * Loads the csv data from the supplied location into this model.
   *
   * @param source   The source for the csv data.
   */
  public void load(@NotNull Path source) {
    IoFunctions.forInputStreamDo(source, this::load);
  }

  /**
   * Loads the csv data from the supplied {@link InputStream} into this model.
   *
   * @param source       The {@link InputStream} providing the csv data.
   */
  public synchronized void load(@NotNull InputStream source) {

    createNewDefaultTableModel();

    var lines    = loadCellData(source);
    var columns  = determineColumnCount(lines);
    var equalLen = equalLengthForEachLine(lines, columns);
    if (!equalLen) {
      ehInconsistentColumnCount.accept(error_csv_inconsistent_column_counts);
      // if the error handler doesn't cause an exception we need to filter out each invalid row
      lines = lines.stream().filter($ -> $.size() == columns).collect( Collectors.toList());
    }

    consolidateColumns(columns, lines, getTitles(columns, lines));

    // register each column
    options.getColumns().forEach($ -> tableModel.addColumn($.getTitle()));

    // load the table content
    lines.forEach(this::loadLine);

    SwingUtilities.invokeLater(this::changeAll);

  }

  /**
   * Saves the csv data to the supplied location.
   *
   * @note [28-Sep-2016:KASI]   Doesn't support encoding yet.
   *
   * @param dest   The destination for the csv data.
   */
  public synchronized void save(@NotNull Path dest) {
    IoFunctions.forOutputStreamDo(dest, this::save);
  }

  /**
   * Saves the csv data to the supplied location.
   *
   * @note [28-Sep-2016:KASI]   Doesn't support encoding yet.
   *
   * @param dest   The destination for the csv data.
   */
  public synchronized void save(@NotNull Path dest, @NotNull Function<String, String> overrideName) {
    IoFunctions.forOutputStreamDo(dest, $ -> save($, overrideName));
  }

  /**
   * Saves the csv data to the supplied {@link OutputStream}.
   *
   * @param dest   The {@link OutputStream} receceiving the csv data.
   */
  public synchronized void save(@NotNull OutputStream dest) {
    save(dest, null);
  }

  /**
   * Saves the csv data to the supplied {@link OutputStream}.
   *
   * @param dest   The {@link OutputStream} receceiving the csv data.
   */
  public synchronized void save(@NotNull OutputStream dest, @NotNull Function<String, String> overrideName) {
    try (var writer = new PrintWriter(new OutputStreamWriter(dest, "UTF-8" ))) {
      writeColumnTitles(writer, overrideName);
      for (var i = 0; i < getRowCount(); i++) {
        writeRow(writer, i);
      }
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  private void writeColumnTitles(@NotNull PrintWriter writer, @NotNull Function<String, String> overrideName) {
    var                      last   = getColumnCount() - 1;
    var                      names  = new String[getColumnCount()];
    Function<String, String> change = overrideName != null ? overrideName : Function.identity();
    for (var i = 0; i < getColumnCount(); i++) {
      names[i] = change.apply(getColumnName(i));
    }
    for (var i = 0; i < names.length; i++) {
      writer.append("\"%s\"".formatted(names[i]));
      if (i < last) {
        writer.append(options.getDelimiter());
      }
    }
    writer.append("\n");
  }

  private void writeRow(@NotNull PrintWriter writer, int row) {
    var last = getColumnCount() - 1;
    for (var i = 0; i < getColumnCount(); i++) {
      writer.append("\"%s\"".formatted(getValueAt(row, i)));
      if (i < last) {
        writer.append(options.getDelimiter());
      }
    }
    writer.append("\n");
  }

  /**
   * Informs all listeners about the changed table.
   */
  private void changeAll() {
    fireTableChanged(new TableModelEvent(this));
  }

  /**
   * Determines the number of columns for the supplied csv data.
   *
   * @param lines   The current csv data.
   *
   * @return   The number of columns.
   */
  private int determineColumnCount(@NotNull List<List<String>> lines) {
    var result = options.getColumns().size();
    if (!lines.isEmpty()) {
      result = lines.parallelStream().map($ -> $.size()).reduce(0, Math::max);
    }
    return result;
  }

  /**
   * Returns <code>true</code> if each line provides the same amount of columns.
   *
   * @param lines     The current lines representing the csv data.
   * @param columns   The expected number of columns.
   *
   * @return   <code>true</code> <=> Each line provides the same amount of columns.
   */
  private boolean equalLengthForEachLine(@NotNull List<List<String>> lines, int columns ) {
    return lines.parallelStream().map($ -> $.size() == columns).reduce( true, Boolean::logicalAnd);
  }

  /**
   * Determines the actual column titles.
   *
   * @param columns   The number of columns.
   * @param content   The current csv data.
   *
   * @return   A list of titles per column.
   */
  private @NotNull List<String> getTitles(int columns, @NotNull List<List<String>> content) {

    var result = new ArrayList<String>(columns);

    if ((!content.isEmpty()) && options.isTitleRow()) {
      // we're removing the first line by intent as it provides the titles
      result.addAll(content.remove(0));
    }

    var csvColumns = options.getColumns();
    for (var i = 0; i < columns; i++) {
      var current = i < result.size() ? result.get(i) : null;
      if ((current == null) && (i < csvColumns.size()) && (csvColumns.get(i) != null)) {
        // there was no title so use the title from the column spec if there's one
        current = csvColumns.get(i).getTitle();
      }
      current = StringFunctions.cleanup(current);
      if (current == null) {
        // still no title, so we need to generate one
        current = "Column %d".formatted(i);
      }
      if (i < result.size()) {
        result.set(i, current);
      } else {
        result.add(current);
      }
    }

    return result;

  }

  /**
   * This function grants a column declaration per column.
   *
   * @param columns   The number of expected columns.
   * @param lines     The current tabular csv data.
   * @param titles    A list of configured/generated titles per column.
   */
  private void consolidateColumns(int columns, @NotNull List<List<String>> lines, @NotNull List<String> titles) {

    var columnsByName = new HashMap<String, CsvColumn>();
    var allTitles     = new ArrayList<String>( titles );

    options.getColumns().parallelStream()
      .filter($ -> $ != null)
      .forEach($ -> columnsByName.put($.getTitle(), $))
      ;

    allTitles.removeAll(columnsByName.keySet());

    var reordered = new ArrayList<CsvColumn>();
    for (var i = 0; i < titles.size(); i++) {
      var title  = titles.get(i);
      var column = columnsByName.get(title);
      if (column == null) {
        column = guessColumn(lines, i);
        column.setTitle(allTitles.remove(0));
      }
      reordered.add(column);
    }
    options.getColumns().clear();
    options.getColumns().addAll(reordered);

  }

  /**
   * This function makes an attempt to guess a column specification per column.
   *
   * @param lines    The tabular csv data.
   * @param column   The column which currently has no column specification.
   *
   * @return   A valid column specification.
   */
  private @NotNull CsvColumn<?> guessColumn(@NotNull List<List<String>> lines, int column) {

    CsvColumn<?> result   = null;
    var          nullable = true;

    if (!lines.isEmpty()) {

      var values = lines.parallelStream().map($ -> $.get(column)).collect(Collectors.toSet());

      nullable = values.contains(null);
      values.remove(null);

      result = process(values, nullable, Predicates.IS_BOOLEAN, MiscFunctions::parseBoolean, Boolean.class, DEFVAL_BOOLEAN);
      if (result == null) {
        result = process(values, nullable, Predicates.IS_BYTE, MiscFunctions::parseByte, Byte.class, DEFVAL_BYTE);
      }
      if (result == null) {
        result = process(values, nullable, Predicates.IS_SHORT, MiscFunctions::parseShort, Short.class, DEFVAL_SHORT);
      }
      if (result == null) {
        result = process(values, nullable, Predicates.IS_INTEGER, MiscFunctions::parseInt, Integer.class, DEFVAL_INTEGER);
      }
      if (result == null) {
        result = process(values, nullable, Predicates.IS_LONG, MiscFunctions::parseLong, Long.class, DEFVAL_LONG);
      }
      if (result == null) {
        result = process(values, nullable, Predicates.IS_FLOAT, MiscFunctions::parseFloat, Float.class, DEFVAL_FLOAT);
      }
      if (result == null) {
        result = process(values, nullable, Predicates.IS_DOUBLE, MiscFunctions::parseDouble, Double.class, DEFVAL_DOUBLE);
      }

    }

    if (result == null) {
      var str = new CsvColumn<String>();
      str.setAdapter(String::valueOf);
      str.setDefval(nullable ? null : DEFVAL_STRING);
      str.setNullable(nullable);
      str.setType(String.class);
      result = str;
    }

    return result;

  }

  /**
   * This function generates a column specification if possible.
   *
   * @param values      All current values for this column.
   * @param nullable    <code>true</code> <=> This column supports null values.
   * @param test        A test which allows to check whether a value matches a certain type or not.
   * @param adapter     An adapter to use in order to convert the value into a destination type.
   * @param type        The destination type itself.
   * @param defValue    A default value.
   *
   * @return   A column specification.
   */
  private <T> CsvColumn<T> process(@NotNull Set<String> values, boolean nullable, @NotNull KPredicate<String> test, @NotNull Function<String, T> adapter, @NotNull Class<T> type, T defValue) {
    var testP = test.protect();
    var is    = values.parallelStream().map($ -> testP.test($)).reduce(true, Boolean::logicalAnd);
    if (is) {
      var result = new CsvColumn<T>();
      result.setAdapter(adapter);
      result.setDefval(nullable ? null : defValue);
      result.setNullable(nullable);
      result.setType(type);
      return result;
    }
    return null;
  }

  /**
   * Loads a single line into the {@link TableModel} instance.
   *
   * @param line   A single line
   */
  private void loadLine(@NotNull List<String> line) {
    var data = new Object[line.size()];
    for (var i = 0; i < line.size(); i++) {
      var cellValue = line.get(i);
      var csvColumn = options.getColumns().get(i);
      var value     = deserialize(csvColumn.getAdapter(), i, cellValue);
      if (value == null) {
        value = csvColumn.getDefval();
      }
      data[i] = value;
    }
    tableModel.addRow(data);
  }

  /**
   * Adds the supplied row data to this model.
   *
   * @param rowData   The row data that shall be added.
   */
  public void addRow(Object[] rowData) {
    if (rowData != null) {
      try {
        for (var i = 0; i < rowData.length; i++) {
          var csvColumn = options.getColumns().get(i);
          if (rowData[i] == null) {
            rowData[i] = csvColumn.getDefval();
          }
        }
        tableModel.addRow(rowData);
      } catch (Exception ex) {
        String message = error_csv_cannot_add_row.format(getRowCount(), StringFunctions.objectToString(rowData), ex.getLocalizedMessage());
        ehInvalidAddRow.accept(message);
      }
    }
  }

  /**
   * A safe deserialization of a certain object representation.
   *
   * @param adapter   The function that is used to convert the text into a dedicated object.
   * @param idx       The index of the column value which shall be deserialized.
   * @param value     The textual value.
   *
   * @return   The deserialized object.
   */
  private Object deserialize(@NotNull Function<String, ?> adapter, int idx, String value) {
    try {
      return adapter.apply(value);
    } catch (Exception ex) {
      ehInvalidCellValue.accept(error_csv_cannot_parse_cell_value.format(value, idx));
      return null;
    }
  }

  /**
   * Default error handler for invalid cell values.
   *
   * @param message   The error message.
   */
  private void ehDefault(String message) {
    throw new KclException(message);
  }

  /**
   * Changes the error handler for invalid cell values.
   *
   * @param handler   The new error handler.
   */
  public synchronized void setErrorHandlerForInvalidCellValue(Consumer<String> handler) {
    ehInvalidCellValue = handler != null ? handler : this::ehDefault;
  }

  /**
   * Changes the error handler for column specifications without an adapter.
   *
   * @param handler   The new error handler.
   */
  public synchronized void setErrorHandlerForColumnSpecWithoutAdapter(Consumer<String> handler) {
    ehColumnSpecWithoutAdapter = handler != null ? handler : this::ehDefault;
  }

  /**
   * Changes the error handler for inconsistent column counts.
   *
   * @param handler   The new error handler.
   */
  public void setErrorHandlerForInconsistentColumnCount(@NotNull Consumer<String> handler) {
    ehInconsistentColumnCount = handler != null ? handler : this::ehDefault;
  }

  @Override
  public synchronized int getRowCount() {
    return tableModel.getRowCount();
  }

  @Override
  public synchronized int getColumnCount() {
    return tableModel.getColumnCount();
  }

  @Override
  public synchronized String getColumnName(@Min(0) int columnIndex) {
    return tableModel.getColumnName(columnIndex);
  }

  @Override
  public synchronized Class<?> getColumnClass(@Min(0) int columnIndex) {
    return options.getColumns().get(columnIndex).getType();
  }

  @Override
  public synchronized boolean isCellEditable(@Min(0) int rowIndex, @Min(0) int columnIndex) {
    return tableModel.isCellEditable(rowIndex, columnIndex);
  }

  @Override
  public synchronized Object getValueAt(@Min(0) int rowIndex, @Min(0) int columnIndex) {
    return tableModel.getValueAt(rowIndex, columnIndex);
  }

  public synchronized Object getValueAt(@Min(0)  int rowIndex, @Min(0) String columnName) {
    return tableModel.getValueAt( rowIndex, getColumnIndex( columnName ) );
  }

  @Override
  public synchronized  void setValueAt(Object aValue, @Min(0) int rowIndex, @Min(0) int columnIndex) {
    tableModel.setValueAt(aValue, rowIndex, columnIndex);
  }

  @Override
  public synchronized void addTableModelListener(@NotNull TableModelListener l) {
    listeners.add(TableModelListener.class, l);
  }

  @Override
  public synchronized void removeTableModelListener(@NotNull TableModelListener l) {
    listeners.remove(TableModelListener.class, l);
  }

  private static class Content {

    String        data;
    ContentType   type;

    public Content(String data, ContentType type) {
      this.data  = data;
      this.type = type;
    }

    @Override
    public String toString() {
      return "Content [data=" + data + ", type=" + type + "]";
    }

  } /* ENDCLASS */

} /* ENDCLASS */
