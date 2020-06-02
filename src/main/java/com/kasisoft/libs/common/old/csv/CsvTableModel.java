package com.kasisoft.libs.common.old.csv;

import static com.kasisoft.libs.common.old.io.DefaultIO.PATH_INPUTSTREAM_EX;
import static com.kasisoft.libs.common.old.io.DefaultIO.PATH_OUTPUTSTREAM_EX;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.constants.Encoding;
import com.kasisoft.libs.common.old.function.Functions;
import com.kasisoft.libs.common.old.function.Predicates;
import com.kasisoft.libs.common.old.internal.Messages;
import com.kasisoft.libs.common.old.io.DefaultIO;
import com.kasisoft.libs.common.old.io.ExtReader;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.io.KReader;
import com.kasisoft.libs.common.old.util.MiscFunctions;
import com.kasisoft.libs.common.text.StringFunctions;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import javax.swing.SwingUtilities;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import java.nio.file.Path;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

/**
 * A TableModel implementation that can be fed by CSV data.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
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

  CsvOptions            options;
  DefaultTableModel     tableModel;
  EventListenerList     listeners;
  
  Consumer<String>      ehInvalidCellValue;
  Consumer<String>      ehColumnSpecWithoutAdapter;
  Consumer<String>      ehInconsistentColumnCount;
  Consumer<String>      ehInvalidAddRow;
  
  private CsvTableModel() {
    ehInvalidCellValue          = this::ehDefault;
    ehColumnSpecWithoutAdapter  = this::ehDefault;
    ehInconsistentColumnCount   = this::ehDefault;
    ehInvalidAddRow             = this::ehDefault;
    listeners                   = new EventListenerList();
    createNewDefaultTableModel();
  }
  
  public CsvTableModel( @NonNull CsvOptions csvOptions ) {
    this();
    options = validateOptions( csvOptions );
    consolidateColumns( csvOptions.getColumns().size(), Collections.emptyList(), getTitles( csvOptions.getColumns().size(), Collections.emptyList() ) );
    options.getColumns().forEach( $ -> tableModel.addColumn( $.getTitle() ) );
  }
  
  public CsvOptions getOptions() {
    return options;
  }
  
  public synchronized void removeRow( int row ) {
    tableModel.removeRow( row );
  }
  
  public synchronized void removeRow( Predicate<Object[]> isValid ) {
    int start = options.isTitleRow() ? 1 : 0;
    for( int i = tableModel.getRowCount() - 1; i >= start; i-- ) {
      Vector row = (Vector) tableModel.getDataVector().get(i);
      if( !isValid.test( row.toArray() ) ) {
        tableModel.removeRow(i);
      }
    }
  }

  public synchronized <I> void forEach( Consumer<I> rowConsumer, String column ) {
    forEach( Functions.adaptToBi( rowConsumer ), null, column );
  }
  
  public synchronized <C, I> void forEach( BiConsumer<I, C> rowConsumer, C context, String column ) {
    int                        start  = options.isTitleRow() ? 1 : 0;
    Function<Vector, Object[]> mapRow = getObjects( column );
    for( int i = start; i < tableModel.getRowCount(); i++ ) {
      Vector row = (Vector) tableModel.getDataVector().get(i);
      rowConsumer.accept( (I) mapRow.apply( row )[0], context );
    }
  }

  public synchronized void forEach( Consumer<Object[]> rowConsumer, String ... columns ) {
    forEach( Functions.adaptToBi( rowConsumer ), null, columns );
  }
  
  public synchronized <C> void forEach( BiConsumer<Object[], C> rowConsumer, C context, String ... columns ) {
    int                        start  = options.isTitleRow() ? 1 : 0;
    Function<Vector, Object[]> mapRow = getObjects( columns );
    for( int i = start; i < tableModel.getRowCount(); i++ ) {
      Vector row = (Vector) tableModel.getDataVector().get(i);
      rowConsumer.accept( mapRow.apply( row ), context );
    }
  }

  public synchronized <R, I> R reduce( R initial, BiFunction<R, I, R> operator, String column ) {
    R                          result = initial;
    int                        start  = options.isTitleRow() ? 1 : 0;
    Function<Vector, Object[]> mapRow = getObjects( column );
    for( int i = start; i < tableModel.getRowCount(); i++ ) {
      Vector row = (Vector) tableModel.getDataVector().get(i);
      result     = operator.apply( result, (I) mapRow.apply( row )[0] );
    }
    return result;
  }

  public synchronized <R> R reduce( R initial, BiFunction<R, Object[], R> operator, String ... columns ) {
    R                          result = initial;
    int                        start  = options.isTitleRow() ? 1 : 0;
    Function<Vector, Object[]> mapRow = getObjects( columns );
    for( int i = start; i < tableModel.getRowCount(); i++ ) {
      Vector row = (Vector) tableModel.getDataVector().get(i);
      result     = operator.apply( result, mapRow.apply( row ) );
    }
    return result;
  }

  private Function<Vector, Object[]> getObjects( String ... columns ) {
    if( (columns == null) || (columns.length == 0) ) {
      return $ -> $.toArray();
    } else {
      int[] indices = new int[ columns.length ];
      for( int i = 0; i < indices.length; i++ ) {
        indices[i] = getColumnIndex( columns[i] );
      }
      return $ -> {
        Object[] in  = $.toArray();
        Object[] out = new Object[ indices.length ];
        for( int i = 0; i < indices.length; i++ ) {
          out[i] = in[ indices[i] ];
        }
        return out;
      };
    }
  }
  
  public synchronized int getColumnIndex( @NonNull String columnName ) {
    int result = -1;
    for( int i = 0; i < getColumnCount(); i++ ) {
      String colName = tableModel.getColumnName(i);
      if( columnName.equals( colName ) ) {
        result = i;
        break;
      }
    }
    return result;
  }
  
  public synchronized boolean isValidColumn( int column ) {
    return (column >= 0) && (column < tableModel.getColumnCount());
  }

  public synchronized <C> void removeRow( Predicate<C> rowTest, String columnName ) {
    removeRow( rowTest, getColumnIndex( columnName ) );
  }

  public synchronized <C> void removeRow( Predicate<C> isValid, int column ) {
    if( isValidColumn( column ) ) {
      removeRow( $ -> isValid.test( (C) $[ column ] ) );
    }
  }
  
  public synchronized void removeColumn( String name ) {
    removeColumn( getColumnIndex( name ) );
  }
  
  public synchronized void removeColumn( int column ) {
    if( isValidColumn( column ) ) {
      for( int row = 0; row < tableModel.getRowCount(); row++ ) {
        Vector rowData = (Vector) tableModel.getDataVector().get( row );
        rowData.remove( column );
      }
      options.getColumns().remove( column );
    }
  }

  public synchronized <L, R, O> void joinColumns( String column1, String column2, BiFunction<L, R, O> joiner, String columnName ) {
    joinColumns( getColumnIndex( column1 ), getColumnIndex( column2 ), joiner, columnName, true, null ); 
  }

  public synchronized <L, R, O> void joinColumns( int column1, int column2, BiFunction<L, R, O> joiner, String columnName ) {
    joinColumns( column1, column2, joiner, columnName, true, null ); 
  }

  public synchronized <L, R, O> void joinColumns( String column1, String column2, BiFunction<L, R, O> joiner, String columnName, boolean nullable, O defaultVal ) {
    joinColumns( getColumnIndex( column1 ), getColumnIndex( column2 ), joiner, columnName, nullable, defaultVal );
  }
  
  public synchronized <L, R, O> void joinColumns( int column1, int column2, BiFunction<L, R, O> joiner, String columnName, boolean nullable, O defaultVal ) {
    if( isValidColumn( column1 ) && isValidColumn( column2 ) ) {
      CsvColumn<O> newColumn = new CsvColumn<>();
      newColumn.setNullable( nullable );
      newColumn.setTitle( columnName );
      newColumn.setDefval( defaultVal );
      options.getColumns().add(newColumn );
      int idxMax = Math.max( column1, column2 );
      int idxMin = Math.min( column1, column2 );
      for( int row = 0; row < tableModel.getRowCount(); row++ ) {
        Vector rowData = (Vector) tableModel.getDataVector().get( row );
        L      left    = (L) rowData.get( column1 );
        R      right   = (R) rowData.get( column2 );
        O      joined  = joiner.apply( left, right );
        rowData.add( joined );
        rowData.remove( idxMax );
        rowData.remove( idxMin );
      }
      options.getColumns().remove( idxMax );
      options.getColumns().remove( idxMin );
    }
  }
  
  /**
   * Creates a new DefaultTableModel instance used as the delegate.
   */
  private void createNewDefaultTableModel() {
    if( tableModel != null ) {
      tableModel.removeTableModelListener( this::tableModelEventDelegator );
    }
    tableModel = new DefaultTableModel();
    tableModel.addTableModelListener( this::tableModelEventDelegator );
  }
  
  /**
   * Dispatches each event to the registered listeners while replacing the original event source.
   * 
   * @param evt   The event that shall be dispatched. Not <code>null</code>.
   */
  private void tableModelEventDelegator( TableModelEvent evt ) {
    fireTableChanged( new TableModelEvent( this, evt.getFirstRow(), evt.getLastRow(), evt.getColumn(), evt.getType() ) );
  }
  
  /**
   * Fires the supplied event to each currently registered listener.
   * 
   * @param evt   The event that is supposed to be dispatched. Not <code>null</code>.
   */
  private void fireTableChanged( TableModelEvent evt ) {
    Object[] objects = listeners.getListenerList();
    for( int i = objects.length - 2, j = objects.length - 1; i >= 0; i -= 2, j -= 2 ) {
      if( objects[i] == TableModelListener.class ) {
        ((TableModelListener) objects[j]).tableChanged( evt );
      }
    }
  }
  
  /**
   * Returns a validated copy of csv options.
   * 
   * @param options   The options that do provide the default setup. Not <code>null</code>.
   * 
   * @return   A validated copy of csv options.
   */
  private CsvOptions validateOptions( CsvOptions options ) {
    CsvOptions result = options.deepCopy();
    for( int i = 0; i < result.getColumns().size(); i++ ) {
      CsvColumn csvColumn = result.getColumns().get(i);
      if( (csvColumn != null) && (csvColumn.getAdapter() == null) ) {
        ehColumnSpecWithoutAdapter.accept( Messages.error_csv_missing_adapter.format(i) );
        // unless the error handler caused an exception we're clearing this spec, so it can be
        // calculcated afterwards
        result.getColumns().set( i, null );
      }
    }
    return result;
  }
  
  /**
   * Reads the csv data from the supplied {@link InputStream} source.
   * 
   * @param source   The source providing the csv data. Not <code>null</code>.
   * 
   * @return   A normalized table of csv data. Not <code>null</code>.
   */
  private List<List<String>> loadCellData( InputStream source ) {
    if( options.isSimpleFormat() ) {
      return loadCellDataSimple( source );
    } else {
      return loadCellDataDefault( source );
    }
  }
  
  private List<List<String>> loadCellDataDefault( InputStream source ) {
    
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

    StringBuilder  text      = readContent( source );

    List<Content>  tokenized = tokenize( text ).parallelStream()
      .map( this::toContent )
      .map( this::normalize )
      .collect( Collectors.toList() );
    
    // fetch all lines and add potentially missing column data
    List<List<Content>> partitioned = partition( tokenized );
    artificialContent( partitioned );

    // remove separators and get the texts only
    return cleanup( partitioned );
    
  }
  
  private List<List<String>> loadCellDataSimple( InputStream source ) {
    
    /* The import follows these steps:
     * 
     * 1. Read the complete csv as a single text.
     * 2. Remember the title row if any.
     * 3. Split all lines assuming they are well formed.
     * 4. Insert the title row. 
     */

    String       text  = readContent( source ).toString();
    List<String> lines = StringFunctions.toLines( text );
    
    String titleRow = null;
    if( options.isTitleRow() ) {
      titleRow = lines.remove(0);
    }
    
    List<List<String>> result = (options.isOrderedSimpleFormat() ? lines.stream() : lines.parallelStream())
      .map( this::tokenizeSimple )
      .collect( Collectors.toList() );
    
    if( titleRow != null ) {
      result.add( 0, tokenizeSimple( titleRow ) );
    }
    
    int max = result.parallelStream()
      .map( $ -> $.size() )
      .reduce( result.get(0).size(), Math::max )
      .intValue();

    result.parallelStream()
      .filter( $ -> $.size() < max )
      .forEach( $ -> fillUp( $, max ) );
    
    return result;
    
  }  
  
  private void fillUp( List<String> cells, int max ) {
    while( cells.size() < max ) {
      cells.add( "" );
    }
  }
  
  /**
   * Reads the content of the supplied {@link InputStream} instance into a {@link StringBuilder}.
   * 
   * @param source   The {@link InputStream} instance. Not <code>null</code>.
   * 
   * @return   The {@link StringBuilder} providing the text. Not <code>null</code>.
   */
  private StringBuilder readContent( InputStream source ) {
    CharArrayWriter writer = new CharArrayWriter();
    forReaderDo( source, $ -> IoFunctions.copy( $, writer, null ) );
    return new StringBuilder( writer.toString() );
  }
  
  private void forReaderDo( InputStream source, Consumer<ExtReader> consumer ) {
    if( options.getEncoding() == Encoding.UTF8 ) {
      DefaultIO.INPUTSTREAM_READER_EX.forReaderDo( source, consumer );
    } else {
      KReader<InputStream> reader = KReader.builder( InputStream.class )
        .encoding( options.getEncoding() )
        .errorHandler( ($ex, $_) -> { throw KclException.wrap( $ex ); } )
        .build();
      reader.forReaderDo( source, consumer );
    }
  }
  
  /**
   * Converts the supplied text into sequence of tokens.
   * 
   * @param text   The full text. Not <code>null</code>.
   * 
   * @return   A list of tokens. Not <code>null</code>.
   */
  private List<String> tokenize( StringBuilder text ) {
    List<String> result = new ArrayList<>( Math.min( 5, text.length() / 5 ) );
    while( text.length() > 0 ) {
      consume( result, text ); 
    }
    dropEmptySequences( result );
    return result;
  }

  private List<String> tokenizeSimple( String text ) {
    List<String> result = new ArrayList<>(10);
    for( int i = 0; i < text.length(); i++ ) {
      
      char first    = text.charAt(i);
      if( (first == DQ) && options.isConsumeDoubleQuotes() ) {
        int close = text.indexOf( DQ, i + 1 );
        if( close == -1 ) {
          throw new KclException( Messages.error_csv_missing_closing_quote.format( text ) );
        }
        result.add( text.substring( i + 1, close ) );
        i = close + 1;
      } else if( (first == SQ) && options.isConsumeSingleQuotes() ) {
        int close = text.indexOf( SQ, i + 1 );
        if( close == -1 ) {
          throw new KclException( Messages.error_csv_missing_closing_quote.format( text ) );
        }
        result.add( text.substring( i + 1, close ) );
        i = close + 1;
      } else if( first == options.getDelimiter() ) {
        // empty cell
        result.add( "" );
      } else {
        int end = text.indexOf( options.getDelimiter(), i + 1 );
        if( end == -1 ) {
          result.add( text.substring(i) );
          i = text.length();
        } else {
          result.add( text.substring( i, end ) );
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
  private void dropEmptySequences( List<String> result ) {
    for( int i = result.size() - 1, j = result.size() - 2, k = result.size() - 3; k >= 0; i--, j--, k-- ) {
      String current = result.get(i);
      String between = result.get(j);
      String before  = result.get(k);
      if( (current.length() == 1) && (before.length() == 1) && (current.charAt(0) == LF) && (before.charAt(0) == LF)) {
        if( between.trim().length() == 0 ) {
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
   * @param receiver   The receiver for the generated tokens. Not <code>null</code>.
   * @param content    The current text to be processed. Not <code>null</code>.
   */
  private void consume( List<String> receiver, StringBuilder content ) {
    char first = content.charAt(0);
    if( (first == CR) || (first == LF) ) {
      consumeCRLF( receiver, content );
    } else if( (first == DQ) && options.isConsumeDoubleQuotes() ) {
      consumeQuoted( receiver, content, first, DQ_STR );
    } else if( (first == SQ) && options.isConsumeSingleQuotes() ) {
      consumeQuoted( receiver, content, first, SQ_STR );
    } else if( first == options.getDelimiter() ) {
      consumeCellSeparator( receiver, content );
    } else {
      consumeNormal( receiver, content );
    }
  }

  /**
   * Consumes the next token which is a sequence of quoted text.
   * 
   * @param receiver     The receiver for the generated tokens. Not <code>null</code>.
   * @param content      The current text to be processed. Not <code>null</code>.
   * @param quote        The currently used quote. 
   * @param quoteAsStr   Same as quote but as a String. Not <code>null</code>.
   */
  private void consumeQuoted( List<String> receiver, StringBuilder content, char quote, String quoteAsStr ) {
    int pos  = 1;
    while( true ) {
      int idx1 = content.indexOf( quoteAsStr, pos );
      if( idx1 == -1 ) {
        // the format is invalid
        throw new KclException( Messages.error_csv_missing_closing_quote.format( content ) );
      }
      if( idx1 == content.length() - 1 ) {
        // this cell covers the full remaining content, so we're done here
        receiver.add( content.toString() );
        content.setLength(0);
        return;
      }
      if( content.charAt( idx1 + 1 ) == quote ) {
        // two immediately succeeding quotes, so it's not an actual closing quote (escaping quote) which
        // means we need to skip this sequence and try to look for the next quote.
        pos = idx1 + 2;
        continue;
      }
      // we've got an ending for the current cell, so add it's content
      receiver.add( content.substring( 0, idx1 + 1 ) );
      content.delete( 0, idx1 + 1 );
      break;
    }
  }
  
  /**
   * Consumes the next token which is a simple unquote sequence.
   * 
   * @param receiver   The receiver for the generated tokens. Not <code>null</code>.
   * @param content    The current text to be processed. Not <code>null</code>.
   */
  private void consumeNormal( List<String> receiver, StringBuilder content ) {
    int idx = StringFunctions.indexOf( content, options.getDelimiter(), LF, CR, DQ, SQ );
    if( idx == -1 ) {
      // there's no other cell following, so the remaining text covers tghe complete cell
      receiver.add( content.toString() );
      content.setLength(0);
    } else {
      String part = content.substring( 0, idx );
      content.delete( 0, idx );
      char   ch   = content.charAt(0);
      if( (ch == DQ) || (ch == SQ) ) {
        // there is some quote text which is not separated so it's part of the previous text sequence
        consumeQuoted( receiver, content, ch, String.valueOf( ch ) );
        // prepend the text sequence to the recently added quote text
        receiver.set( receiver.size() - 1, part + receiver.get( receiver.size() - 1 ) );
      } else {
        // this is a new token
        receiver.add( part );
      }
    }
  }
  
  /**
   * Consumes the cell separator.
   * 
   * @param receiver   The receiver for the generated tokens. Not <code>null</code>.
   * @param content    The current text to be processed. Not <code>null</code>.
   */
  private void consumeCellSeparator( List<String> receiver, StringBuilder content ) {
    receiver.add( content.substring( 0, 1 ) );
    content.deleteCharAt(0);
  }
  
  /**
   * Consumes a seqeucne of cr and lf characters. These are represented by a lf only.
   * 
   * @param receiver   The receiver for the generated tokens. Not <code>null</code>.
   * @param content    The current text to be processed. Not <code>null</code>.
   */
  private void consumeCRLF( List<String> receiver, StringBuilder content ) {
    int idx = 1;
    for( int i = 1; i < content.length(); i++ ) {
      char current = content.charAt(i);
      if( (current == LF) || (current == CR) ) {
        idx++;
      } else {
        break;
      }
    }
    content.delete( 0, idx );
    receiver.add( LF_STR );
  }

  /**
   * This function generates a simple token which allows to deal with the data more easily.
   *  
   * @param data   The current token data. Maybe <code>null</code>.
   * 
   * @return   The tokenized content. Not <code>null</code>.
   */
  private Content toContent( String data ) {
    ContentType type = ContentType.CONTENT;
    if( (data != null) && (data.length() < 2) ) {
      if( data.charAt(0) == options.getDelimiter() ) {
        type = ContentType.SEPARATOR;
      } else if( data.charAt(0) == LF ) {
        type = ContentType.LINE_DELIMITER;
      }
    }
    return new Content( data, type );
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
   * @param content   The content that will be normalized. Not <code>null</code>.
   * 
   * @return   The supplied {@link Content} instance. Not <code>null</code>.
   */
  private Content normalize( Content content ) {
    Content result = content;
    if( (result.type == ContentType.CONTENT) && (result.data != null) ) {
      result.data = StringFunctions.trim( result.data, "\t ", null );
      if( result.data.length() == 0 ) {
        result.data = null;
      }
      if( result.data != null ) {
        char ch = result.data.charAt(0);
        if( (ch == DQ) && options.isConsumeDoubleQuotes() ) {
          result.data  = result.data.substring( 1, result.data.length() - 1 );
          result.data  = StringFunctions.replace( result.data, DQ_STR + DQ_STR, DQ_STR );
        } else if( (ch == SQ) && options.isConsumeSingleQuotes() ) {
          result.data  = result.data.substring( 1, result.data.length() - 1 );
          result.data  = StringFunctions.replace( result.data, SQ_STR + SQ_STR, SQ_STR );
        }
      }
      if( options.isDisableCr() && (result.data != null) ) {
        result.data = StringFunctions.replace( result.data, CRLF_STR, LF_STR );
        result.data = StringFunctions.replace( result.data, CR_STR, LF_STR );
      }
    }
    return result;
  }

  /**
   * Splits the sequence of content instances into separate lines while removing the line delimiter tokens.
   *  
   * @param content   The full sequence of tokens. Not <code>null</code>.
   * 
   * @return   A table structured sequence of tokens. Not <code>null</code>.
   */
  private List<List<Content>> partition( List<Content> content ) {
    List<List<Content>> result      = new ArrayList<>();
    List<Content>       line        = new ArrayList<>();
    Predicate<List>     isfilled    = options.getMaxLines() != -1 ? $ -> $.size() >= options.getMaxLines() : $ -> false;
    result.add( line );
    while( (! content.isEmpty()) && (!isfilled.test( result )) ) {
      Content current = content.remove(0);
      if( current.type == ContentType.LINE_DELIMITER ) {
        if( ! line.isEmpty() ) {
          line = new ArrayList<>();
        }
        result.add( line );
      } else {
        line.add( current );
      }
    }
    for( int i = result.size() - 1; i >= 0; i-- ) {
      if( result.get(i).isEmpty() ) {
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
   * @param lines   The current table data. Not <code>null</code>.
   */
  private void artificialContent( List<List<Content>> lines ) {
    
    // there's always a first column, so if a line starts with a separator insert one at the first position
    lines.parallelStream()
      .filter( $ -> $.get(0).type == ContentType.SEPARATOR )
      .forEach( $ -> $.add( 0, new Content( null, ContentType.CONTENT ) ) );

    // there's always a last column, so if a line ends with a separator insert one at the last position
    lines.parallelStream()
      .filter( $ -> $.get( $.size() - 1 ).type == ContentType.SEPARATOR )
      // there's always a last column
      .forEach( $ -> $.add( new Content( null, ContentType.CONTENT ) ) );

    // insert a column if there are subsequence separators
    lines.parallelStream().forEach( this::extendDuplicateDelimiters );
   
    if( options.isFillMissingColumns() ) {
      int maxcolumns = lines.parallelStream().mapToInt( $ -> $.size() ).reduce( 0, Math::max );
      lines.parallelStream().forEach( $ -> fillMissingColumns( $, maxcolumns ) );
    }
    
  }
  
  /**
   * Inserts an empty cell between two succeeding separators.
   * 
   * @param line   The line that will be processed. Not <code>null</code>.
   */
  private void extendDuplicateDelimiters( List<Content> line ) {
    for(int i = line.size() - 1, j = line.size() - 2; j >= 0; i--, j-- ) {
      Content current = line.get(i);
      Content before  = line.get(j);
      if( (current.type == ContentType.SEPARATOR) && (before.type == ContentType.SEPARATOR) ) {
        line.add( i, new Content( null, ContentType.CONTENT ) );
      }
    }
  }
  
  /**
   * Extends the supplied row with empty columns, so the number of cells per line is consistent.
   * 
   * @param line   The line that will be extended. Not <code>null</code>.
   * @param max    The maximum number of columns.
   */
  private void fillMissingColumns( List<Content> line, int max ) {
    while( line.size() < max ) {
      ContentType last = line.get( line.size() - 1 ).type;
      if( last == ContentType.CONTENT ) {
        line.add( new Content( ",", ContentType.SEPARATOR ) );
      } else {
        line.add( new Content( null, ContentType.CONTENT ) );
      }
    }
    
  }
  
  /**
   * This function translates the tabular structured tokens into corresponding textual cells.
   * This requires to remove all separators as they're no longer needed. Afterwards only the text of 
   * each token will be reused.
   * 
   * @param lines   The tabular structured tokens. Not <code>null</code>.
   * 
   * @return   The textual tabular structure. Not <code>null</code>.
   */
  private List<List<String>> cleanup( List<List<Content>> lines ) {
    lines.parallelStream().forEach( this::removeSeparators );
    return lines.stream().map( this::unwrap ).collect( Collectors.toList() );
  }

  /**
   * Unwraps the supplied line while just taking the textual data.
   *  
   * @param line   The line providing the tokens. Not <code>null</code>.
   * 
   * @return   The line providing the corresponding textual cell values. Not <code>null</code>.
   */
  private List<String> unwrap( List<Content> line ) {
    return line.stream().map( $ -> $.data ).collect( Collectors.toList() );
  }
    
  /**
   * Removes all cell separators from the supplied line.
   * 
   * @param lines   The line that get's freed from the cell separators. Not <code>null</code>.
   */
  private void removeSeparators( List<Content> lines ) {
    for( int i = lines.size() - 1; i >= 0; i-- ) {
      if( lines.get(i).type == ContentType.SEPARATOR ) {
        lines.remove(i);
      }
    }
  }
  
  /**
   * Loads the csv data from the supplied location into this model.
   * 
   * @param source   The source for the csv data. Not <code>null</code>.
   */
  public void load( @NonNull Path source ) {
    PATH_INPUTSTREAM_EX.forInputStreamDo( source, this::load );
  }
  
  /**
   * Loads the csv data from the supplied {@link InputStream} into this model. 
   * 
   * @param source       The {@link InputStream} providing the csv data. Not <code>null</code>.
   */
  public synchronized void load( @NonNull InputStream source ) {
    
    createNewDefaultTableModel();
    
    List<List<String>> lines      = loadCellData( source );
    int                columns    = determineColumnCount( lines );
    boolean            equalLen   = equalLengthForEachLine( lines, columns );
    if( ! equalLen ) {
      ehInconsistentColumnCount.accept( Messages.error_csv_inconsistent_column_count );
      // if the error handler doesn't cause an exception we need to filter out each invalid row
      lines = lines.stream().filter( $ -> $.size() == columns ).collect( Collectors.toList() );
    }
    
    consolidateColumns( columns, lines, getTitles( columns, lines ) );
    
    // register each column
    options.getColumns().forEach( $ -> tableModel.addColumn( $.getTitle() ) );
    
    // load the table content 
    lines.forEach( this::loadLine );
    
    SwingUtilities.invokeLater( this::changeAll );
    
  }

  /**
   * Saves the csv data to the supplied location.
   * 
   * @note [28-Sep-2016:KASI]   Doesn't support encoding yet.
   * 
   * @param dest   The destination for the csv data. Not <code>null</code>.
   */
  public synchronized void save( @NonNull Path dest ) {
    PATH_OUTPUTSTREAM_EX.forOutputStreamDo( dest, this::save );
  }

  /**
   * Saves the csv data to the supplied location.
   * 
   * @note [28-Sep-2016:KASI]   Doesn't support encoding yet.
   * 
   * @param dest   The destination for the csv data. Not <code>null</code>.
   */
  public synchronized void save( @NonNull Path dest, Function<String, String> overrideName ) {
    PATH_OUTPUTSTREAM_EX.forOutputStreamDo( dest, $ -> save( $, overrideName ) );
  }

  /**
   * Saves the csv data to the supplied {@link OutputStream}. 
   * 
   * @param dest   The {@link OutputStream} receceiving the csv data. Not <code>null</code>.
   */
  public synchronized void save( @NonNull OutputStream dest ) {
    save( dest, null );
  }
  
  /**
   * Saves the csv data to the supplied {@link OutputStream}. 
   * 
   * @param dest   The {@link OutputStream} receceiving the csv data. Not <code>null</code>.
   */
  public synchronized void save( @NonNull OutputStream dest, Function<String, String> overrideName ) {
    try( PrintWriter writer = new PrintWriter( new OutputStreamWriter( dest, "UTF-8" ) ) ) {
      writeColumnTitles( writer, overrideName );
      for( int i = 0; i < getRowCount(); i++ ) {
        writeRow( writer, i );
      }
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }
  
  private void writeColumnTitles( PrintWriter writer, Function<String, String> overrideName ) {
    int      last  = getColumnCount() - 1;
    String[] names = new String[ getColumnCount() ];
    Function<String, String> change = overrideName != null ? overrideName : Function.identity();
    for( int i = 0; i < getColumnCount(); i++ ) {
      names[i] = change.apply( getColumnName(i) );
    }
    for( int i = 0; i < names.length; i++ ) {
      writer.append( String.format( "\"%s\"", names[i] ) );
      if( i < last ) {
        writer.append( options.getDelimiter() );
      }
    }
    writer.append("\n");
  }

  private void writeRow( PrintWriter writer, int row ) {
    int last = getColumnCount() - 1;
    for( int i = 0; i < getColumnCount(); i++ ) {
      writer.append( String.format( "\"%s\"", getValueAt( row, i ) ) );
      if( i < last ) {
        writer.append( options.getDelimiter() );
      }
    }
    writer.append("\n");
  }
  
  /**
   * Informs all listeners about the changed table.
   */
  private void changeAll() {
    fireTableChanged( new TableModelEvent( this ) );
  }

  /**
   * Determines the number of columns for the supplied csv data.
   * 
   * @param lines   The current csv data. Not <code>null</code>.
   * 
   * @return   The number of columns.
   */
  private int determineColumnCount( List<List<String>> lines ) {
    int result = options.getColumns().size();
    if( ! lines.isEmpty() ) {
      result = lines.parallelStream().map( $ -> $.size() ).reduce( 0, Math::max );
    }
    return result;
  }
  
  /**
   * Returns <code>true</code> if each line provides the same amount of columns.
   * 
   * @param lines     The current lines representing the csv data. Not <code>null</code>.
   * @param columns   The expected number of columns.
   * 
   * @return   <code>true</code> <=> Each line provides the same amount of columns.
   */
  private boolean equalLengthForEachLine( List<List<String>> lines, int columns ) {
    return lines.parallelStream().map( $ -> $.size() == columns ).reduce( true, Boolean::logicalAnd );
  }

  /**
   * Determines the actual column titles.
   * 
   * @param columns   The number of columns.
   * @param content   The current csv data. Not <code>null</code>.
   * 
   * @return   A list of titles per column. Not <code>null</code>.
   */
  private List<String> getTitles( int columns, List<List<String>> content ) {
    List<String> result = new ArrayList<>( columns );
    if( (! content.isEmpty()) && options.isTitleRow() ) {
      // we're removing the first line by intent as it provides the titles
      result.addAll( content.remove(0) );
    }
    List<CsvColumn> csvColumns = options.getColumns();
    for( int i = 0; i < columns; i++ ) {
      String current = i < result.size() ? result.get(i) : null;
      if( (current == null) && (i < csvColumns.size()) && (csvColumns.get(i) != null) ) {
        // there was no title so use the title from the column spec if there's one
        current = csvColumns.get(i).getTitle();
      }
      current = StringFunctions.cleanup( current );
      if( current == null ) {
        // still no title, so we need to generate one
        current = String.format( "Column %d", i );
      }
      if( i < result.size() ) {
        result.set( i, current );
      } else {
        result.add( current );
      }
    }
    return result;
  }
  
  /**
   * This function grants a column declaration per column.
   * 
   * @param columns   The number of expected columns.
   * @param lines     The current tabular csv data. Not <code>null</code>.
   * @param titles    A list of configured/generated titles per column. Not <code>null</code>.
   */
  private void consolidateColumns( int columns, List<List<String>> lines, List<String> titles ) {
    
    Map<String, CsvColumn> columnsByName = new HashMap<>();
    List<String>           allTitles     = new ArrayList<>( titles );
    
    options.getColumns().parallelStream()
      .filter( $ -> $ != null )
      .forEach( $ -> columnsByName.put( $.getTitle(), $ ) );

    allTitles.removeAll( columnsByName.keySet() );
    
    List<CsvColumn> reordered = new ArrayList<>();
    for( int i = 0; i < titles.size(); i++ ) {
      String    title  = titles.get(i);
      CsvColumn column = columnsByName.get( title );
      if( column == null ) {
        column = guessColumn( lines, i );
        column.setTitle( allTitles.remove(0) );
      }
      reordered.add( column );
    }
    options.getColumns().clear();
    options.getColumns().addAll( reordered );
    
  }
  
  /**
   * This function makes an attempt to guess a column specification per column.
   * 
   * @param lines    The tabular csv data. Not <code>null</code>.
   * @param column   The column which currently has no column specification. Not <code>null</code>.
   *  
   * @return   A valid column specification. Not <code>null</code>.
   */
  private CsvColumn<?> guessColumn( List<List<String>> lines, int column ) {
    
    CsvColumn<?> result   = null;
    boolean      nullable = true;
    
    if( ! lines.isEmpty() ) {
    
      Set<String>  values   = lines.parallelStream().map( $ -> $.get( column ) ).collect( Collectors.toSet() );
      
      nullable = values.contains( null );
      values.remove( null );
      
      result = process( values, nullable, Predicates.IS_BOOLEAN, MiscFunctions::parseBoolean, Boolean.class, DEFVAL_BOOLEAN );
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_BYTE, MiscFunctions::parseByte, Byte.class, DEFVAL_BYTE );
      }
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_SHORT, MiscFunctions::parseShort, Short.class, DEFVAL_SHORT );
      }
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_INTEGER, MiscFunctions::parseInt, Integer.class, DEFVAL_INTEGER );
      }
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_LONG, MiscFunctions::parseLong, Long.class, DEFVAL_LONG );
      }
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_FLOAT, MiscFunctions::parseFloat, Float.class, DEFVAL_FLOAT );
      }
      if( result == null ) {
        result = process( values, nullable, Predicates.IS_DOUBLE, MiscFunctions::parseDouble, Double.class, DEFVAL_DOUBLE );
      }
      
    }
    if( result == null ) {
      CsvColumn<String> str = new CsvColumn<>();
      str.setAdapter( String::valueOf );
      str.setDefval( nullable ? null : DEFVAL_STRING );
      str.setNullable( nullable );
      str.setType( String.class );
      result = str;
    }
    
    return result;

  }
  
  /**
   * This function generates a column specification if possible.
   * 
   * @param values      All current values for this column. Not <code>null</code>.
   * @param nullable    <code>true</code> <=> This column supports null values.
   * @param test        A test which allows to check whether a value matches a certain type or not. Not <code>null</code>.
   * @param adapter     An adapter to use in order to convert the value into a destination type. Not <code>null</code>.
   * @param type        The destination type itself. Not <code>null</code>.
   * @param defValue    A default value. Not <code>null</code>.
   * 
   * @return   A column specification. Maybe <code>null</code> if some values don't support to be converted properly.
   */
  private <T> CsvColumn<T> process( Set<String> values, boolean nullable, Predicate<String> test, Function<String, T> adapter, Class<T> type, T defValue ) {
    boolean is = values.parallelStream().map( $ -> test.test($) ).reduce( true, Boolean::logicalAnd );
    if( is ) {
      CsvColumn<T> result = new CsvColumn<>();
      result.setAdapter( adapter );
      result.setDefval( nullable ? null : defValue );
      result.setNullable( nullable );
      result.setType( type );
      return result;
    }
    return null;
  }
  
  /**
   * Loads a single line into the {@link TableModel} instance.
   * 
   * @param line   A single line. Not <code>null</code>.
   */
  private void loadLine( List<String> line ) {
    Object[] data = new Object[ line.size() ];
    for( int i = 0; i < line.size(); i++ ) {
      String    cellValue = line.get(i);
      CsvColumn csvColumn = options.getColumns().get(i);
      Object    value     = deserialize( csvColumn.getAdapter(), i, cellValue );
      if( value == null ) {
        value = csvColumn.getDefval();
      }
      data[i] = value;
    }
    tableModel.addRow( data );
  }
  
  /**
   * Adds the supplied row data to this model.
   * 
   * @param rowData   The row data that shall be added. Maybe <code>null</code>.
   */
  public void addRow( Object[] rowData ) {
    if( rowData != null ) {
      try {
        for( int i = 0; i < rowData.length; i++ ) {
          CsvColumn csvColumn = options.getColumns().get(i);
          if( rowData[i] == null ) {
            rowData[i] = csvColumn.getDefval();
          }
        }
        tableModel.addRow( rowData );
      } catch( Exception ex ) {
        String message = Messages.error_csv_invalid_add_row.format( getRowCount(), StringFunctions.toString( rowData ), ex.getLocalizedMessage() );
        ehInvalidAddRow.accept( message );
      }
    }
  }

  /**
   * A safe deserialization of a certain object representation.
   * 
   * @param adapter   The function that is used to convert the text into a dedicated object. Not <code>null</code>.
   * @param idx       The index of the column value which shall be deserialized. 
   * @param value     The textual value. Maybe <code>null</code>.
   * 
   * @return   The deserialized object or <code>null</code>.
   */
  private Object deserialize( Function<String, ?> adapter, int idx, String value ) {
    try {
      return adapter.apply( value );
    } catch( Exception ex ) {
      ehInvalidCellValue.accept( Messages.error_csv_invalid_cell_value.format( value, idx ) );
      return null;
    }
  }
    
  /**
   * Default error handler for invalid cell values.
   * 
   * @param message   The error message.
   */
  private void ehDefault( String message ) {
    throw new KclException( message );
  }

  /**
   * Changes the error handler for invalid cell values.
   * 
   * @param handler   The new error handler.
   */
  public synchronized void setErrorHandlerForInvalidCellValue( Consumer<String> handler ) {
    ehInvalidCellValue = handler != null ? handler : this::ehDefault;
  }

  /**
   * Changes the error handler for column specifications without an adapter.
   * 
   * @param handler   The new error handler.
   */
  public synchronized void setErrorHandlerForColumnSpecWithoutAdapter( Consumer<String> handler ) {
    ehColumnSpecWithoutAdapter = handler != null ? handler : this::ehDefault;
  }

  /**
   * Changes the error handler for inconsistent column counts.
   * 
   * @param handler   The new error handler.
   */
  public void setErrorHandlerForInconsistentColumnCount( Consumer<String> handler ) {
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
  public synchronized String getColumnName( int columnIndex ) {
    return tableModel.getColumnName( columnIndex );
  }

  @Override
  public synchronized Class<?> getColumnClass( int columnIndex ) {
    return options.getColumns().get( columnIndex ).getType();
  }

  @Override
  public synchronized boolean isCellEditable( int rowIndex, int columnIndex ) {
    return tableModel.isCellEditable( rowIndex, columnIndex );
  }

  @Override
  public synchronized  Object getValueAt( int rowIndex, int columnIndex ) {
    return tableModel.getValueAt( rowIndex, columnIndex );
  }

  public synchronized  Object getValueAt( int rowIndex, String columnName ) {
    return tableModel.getValueAt( rowIndex, getColumnIndex( columnName ) );
  }

  @Override
  public synchronized  void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
    tableModel.setValueAt( aValue, rowIndex, columnIndex );
  }

  @Override
  public synchronized void addTableModelListener( TableModelListener l ) {
    listeners.add( TableModelListener.class, l );
  }

  @Override
  public synchronized void removeTableModelListener( TableModelListener l ) {
    listeners.remove( TableModelListener.class, l );
  }

  @AllArgsConstructor @ToString
  private static class Content {
    
    String        data;
    ContentType   type;
    
  } /* ENDCLASS */

} /* ENDCLASS */
