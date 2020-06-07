package com.kasisoft.libs.common.data;

import com.kasisoft.libs.common.io.Closer;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.old.csv.CsvColumn;
import com.kasisoft.libs.common.types.Pair;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Simple wrapper around a jdbc {@link Connection} instance which provides some helpful utility features.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("resource")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DbConnection implements AutoCloseable {
  
  @Getter
  Connection                                connection;
  
  Database                                  database;
  Consumer<Exception>                       errorHandler;
  Map<String, PreparedStatement>            queries;
  List<String>                              tableNames;
  Map<String, List<String>>                 columnNames;
  Map<String, List<Pair<String, Integer>>>  columnTypes;
  Map<String, List<CsvColumn>>              columnSpecs;
  Closer                                    closer;

  /**
   * Sets up the connection which will be opened right away.
   * 
   * @param configuration   The configuration for the connection. Not <code>null</code>.
   */
  public DbConnection(@NotNull DbConfig configuration) {
    this(configuration, true);
  }
  
  /**
   * Sets up the connection which will be opened right away.
   * 
   * @param config   The configuration for the connection. Not <code>null</code>.
   * @param cache    <code>true</code> <=> Use a primitive internal cache (no eviction policies here).
   */
  public DbConnection(@NotNull DbConfig config, boolean cache) {
    init(config.getDb(), cache);
    try {
      if (config.getUsername() != null) {
        connection = config.getDb().getConnection(config.getUrl(), config.getUsername(), config.getPassword());
      } else {
        connection = config.getDb().getConnection(config.getUrl());
      }
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  /**
   * Sets up the connection which will be opened right away.
   * 
   * @param dataSource   The DataSource used to provide access to the database.
   * @param cache        <code>true</code> <=> Use a primitive internal cache (no eviction policies here).
   */
  public DbConnection(Database db, @NotNull DataSource dataSource, boolean cache) {
    init(db, cache);
    try {
      connection = dataSource.getConnection();
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }
  
  public DbConnection withErrorHandler(@NotNull Consumer<Exception> errorHandler) {
    this.errorHandler   = errorHandler;
    this.closer         = closer.withErrorHandler(errorHandler);
    return this;
  }

  private void init(Database db, boolean cache) {
    database        = db;
    tableNames      = new ArrayList<>();
    columnNames     = cache ? new HashMap<>() : new Uncacheable<>();
    columnTypes     = cache ? new HashMap<>() : new Uncacheable<>();
    columnSpecs     = cache ? new HashMap<>() : new Uncacheable<>();
    queries         = new HashMap<>();
    errorHandler    = this::errorHandler;
    closer          = new Closer().withErrorHandler(errorHandler);
  }
  
  /**
   * Default error handler.
   * 
   * @param ex   An Exception that occured while accessing the data. Not <code>null</code>.
   */
  private void errorHandler(@NotNull Exception ex) {
    throw KclException.wrap(ex);
  }
  
  /**
   * Clears all object caches.
   */
  public void clear() {
    tableNames  . clear();
    columnNames . clear();
    columnTypes . clear();
    columnSpecs . clear();
  }
  
  /**
   * Returns a canonical table name which means that the table is named as delivered by the jdbc driver.
   * 
   * @param tablename   The name that shall be canonical. Neither <code>null</code> nor empty.
   * 
   * @return   The canonical name unless there's no correspondingly named table. Maybe <code>null</code>.
   */
  public String canonicalTableName(@NotNull String tablename) {
    var    tables = listTables();
    String result = null;
    for (var table : tables) {
      if (table.equalsIgnoreCase(tablename)) {
        result = table;
        break;
      }
    }
    return result;
  }
  
  /**
   * Returns a list of table names.
   * 
   * @return   A list of table names (unmodifiable).
   */
  public @NotNull List<String> listTables() {
    List<String> result = Collections.unmodifiableList(tableNames);
    if (tableNames.isEmpty()) {
      ResultSet resultset = null;
      try {
        var metadata = connection.getMetaData();
        resultset    = metadata.getTables(null, null, "%", new String[] {"TABLE"});
        while (resultset.next()) {
          tableNames.add(resultset.getString(3));
        }
      } catch (Exception ex) {
        errorHandler.accept(ex);
      } finally {
        closer.close(resultset);
      }
    }
    return result;
  }
  
  /**
   * Returns a {@link PreparedStatement} instance using the supplied table.
   * 
   * @param query   The string providing the query. Neither <code>null</code> nor empty.
   * @param table   The name of the table.
   * 
   * @return   The {@link PreparedStatement} instance. Not <code>null</code>.
   * 
   * @throws SQLException   Setting up the query failed for some reason.
   */
  private PreparedStatement getQuery(@NotNull String query, @NotNull String table) throws SQLException {
    var key    = table != null ? String.format(query, table) : query;
    var result = queries.get(key);
    if (result == null) {
      result = connection.prepareStatement( key );
      queries.put(key, result);
    }
    return result;
  }
  
  /**
   * Returns a list of all column names for the supplied table.
   * 
   * @param table   The table which column names shall be returned. Neither <code>null</code> nor empty.
   * 
   * @return   A list of all column names. Not <code>null</code>.
   */
  public List<String> listColumnNames(@NotNull String table) {
    return listColumnInfos(columnNames, table, this::getColumnName);
  }
  
  /**
   * Fetches the column name from the supplied jdbc result.
   * 
   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
   * @param index      The 1-based index of the column.
   * 
   * @return   The name of the column. Maybe <code>null</code> in case of an error.
   */
  private String getColumnName(ResultSetMetaData metadata, int index) {
    try {
      return metadata.getColumnName(index);
    } catch(Exception ex) {
      errorHandler.accept(ex);
      return null;
    }
  }
  
  /**
   * Returns a list of pairs for all columns and their associated jdbc type.
   * 
   * @param table   The table which column names shall be returned. Neither <code>null</code> nor empty.
   * 
   * @return   A list of pairs providing the column name associated with the corresponding jdbc type. 
   *           Not <code>null</code>.
   */
  public List<Pair<String, Integer>> listColumnTypes(@NotNull String table) {
    return listColumnInfos(columnTypes, table, this::getColumnType);
  }

  /**
   * Fetches the column name with it's jdbc type from the supplied jdbc result.
   * 
   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
   * @param index      The 1-based index of the column.
   * 
   * @return   The pair with the column name and jdbc type. Maybe <code>null</code> in case of an error.
   */
  private Pair<String, Integer> getColumnType(ResultSetMetaData metadata, int index) {
    try {
      return new Pair<>(metadata.getColumnName(index), metadata.getColumnType(index));
    } catch (Exception ex) {
      errorHandler.accept(ex);
      return null;
    }
  }
  
  /**
   * A generic function which fetches some metadata results.
   * 
   * @param cache      A cache which is used to keep the metadata information in memory. Not <code>null</code>.
   * @param table      The table which column names shall be returned. Neither <code>null</code> nor empty.
   * @param producer   The function which turns the raw metadata result into our desired return type. Not <code>null</code>.
   * 
   * @return   A list of metadata results. Maybe <code>null</code> in case of an error.
   */
  private <T> List<T> listColumnInfos(Map<String, List<T>> cache, String table, BiFunction<ResultSetMetaData, Integer, T> producer) {
    var result = Collections.<T>emptyList();
    var name   = canonicalTableName(table);
    if (name != null) {
      if (!cache.containsKey(name)) {
        PreparedStatement query     = null;
        ResultSet         resultset = null;
        try {
          result        = new ArrayList<>();
          query         = getQuery(database.getListColumnsQuery(), name );
          resultset     = query.executeQuery();
          var metadata  = resultset.getMetaData();
          for (var i = 0; i < metadata.getColumnCount(); i++) {
            T value = producer.apply(metadata, i + 1);
            if (value != null) {
              result.add(value);
            }
          }
          cache.put(name, Collections.unmodifiableList(result));
        } catch (Exception ex) {
          // if the handler doesn't abort we're returning an empty list
          errorHandler.accept(ex);
        } finally {
          closer.close(resultset);
        }
      }
      result = cache.get(name);
    }
    return result;
  }

//  private <T> CsvColumn<T> newCsvColumn(String name, Function<String, T> adapter) {
//    CsvColumn<T> result = new CsvColumn<>();
//    result.setTitle(name);
//    result.setAdapter(adapter);
//    return result;
//  }
//  
//  /**
//   * Creates a CsvTableModel instance providing the metadata description of the supplied table.
//   * 
//   * @param table   The table which column names shall be returned. Neither <code>null</code> nor empty.
//   * 
//   * @return  The CsvTableModel instance. Not <code>null</code>.
//   */
//  public CsvTableModel createCsvMetaData(@NotNull String table) {
//
//    List<CsvColumn> specs = Arrays.asList(
//      newCsvColumn("name"        , new StringAdapter()),
//      newCsvColumn("label"       , new StringAdapter()),
//      newCsvColumn("schema"      , new StringAdapter()),
//      newCsvColumn("catalog"     , new StringAdapter()),
//      newCsvColumn("class"       , new StringAdapter()),
//      newCsvColumn("type"        , new StringAdapter()),
//      newCsvColumn("typename"    , new StringAdapter()),
//      newCsvColumn("displaysize" , new StringAdapter()),
//      newCsvColumn("precision"   , new StringAdapter()),
//      newCsvColumn("scale"       , new StringAdapter())
//    );
//
//    CsvTableModel result = new CsvTableModel(CsvOptions.builder().columns(specs).titleRow().build());
//    String        name   = canonicalTableName(table);
//    if (name != null) {
//      PreparedStatement query     = null;
//      ResultSet         resultset = null;
//      try {
//        query        = getQuery(config.getDb().getListColumnsQuery(), name);
//        resultset    = query.executeQuery();
//        var metadata = resultset.getMetaData();
//        for (var i = 1; i <= metadata.getColumnCount(); i++) {
//          result.addRow( new Object[] {
//            metadata.getColumnName(i),
//            metadata.getColumnLabel(i),
//            metadata.getSchemaName(i),
//            metadata.getCatalogName(i),
//            metadata.getColumnClassName(i),
//            metadata.getColumnType(i),
//            metadata.getColumnTypeName(i),
//            metadata.getColumnDisplaySize(i),
//            metadata.getPrecision(i),
//            metadata.getScale(i)
//          });
//        }
//      } catch (Exception ex) {
//        // if the handler doesn't abort we're returning an empty list
//        errorHandler.accept(ex);
//      } finally {
//        MiscFunctions.close(resultset);
//      }
//    }
//    return result;
//  }
//
//  /**
//   * Creates a CsvTableModel instance providing the data of the supplied table.
//   * 
//   * @param table      The table which column names shall be returned. Neither <code>null</code> nor empty.
//   * 
//   * @return  The CsvTableModel instance. Not <code>null</code>.
//   */
//  public CsvTableModel createCsvModel(@NotNull String table) {
//    var specs  = listColumnInfos(columnSpecs, table, this::getCsvColumn);
//    var result = new CsvTableModel(CsvOptions.builder().columns(specs).titleRow().build());
//    importAllRows(table, $ -> rowLoader($, result));
//    return result;
//  }
//  
//  /**
//   * Creates a {@link CsvColumn} specification from the jdbc metadata.
//   * 
//   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
//   * @param index      The 1-based index of the column.
//   * 
//   * @return   The {@link CsvColumn} specification. Maybe <code>null</code> in case of an error.
//   */
//  private CsvColumn getCsvColumn(ResultSetMetaData metadata, int index) {
//    try {
//      var type   = Class.forName(metadata.getColumnClassName(index));
//      var result = new CsvColumn();
//      result.setTitle(metadata.getColumnName(index));
//      result.setType(type);
//      result.setNullable(metadata.isNullable(index) == ResultSetMetaData.columnNullable);
//      if (result.getAdapter() == null) {
//        result.setAdapter(Function.identity());
//      }
//      return result;
//    } catch (Exception ex) {
//      errorHandler.accept(ex);
//      return null;
//    }
//  }
//  
//  /**
//   * List all records within a table.
//   * 
//   * @param table      The name of the table. Neither <code>null</code> nor empty.
//   * @param consumer   The {@link Consumer} which will be invoked for each record.
//   */
//  private void importAllRows(String table, Consumer<ResultSet> consumer) {
//    var name = canonicalTableName(table);
//    if (name != null) {
//      PreparedStatement query     = null;
//      ResultSet         resultset = null;
//      try {
//        query     = getQuery(database.getSelectAllQuery(), name);
//        resultset = query.executeQuery();
//        while (resultset.next()) {
//          consumer.accept(resultset);
//        }
//      } catch (Exception ex) {
//        errorHandler.accept(ex);
//      } finally {
//        closer.close(resultset);
//      }
//    }
//  }

  /**
   * Processes some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param context     Some contextual object that shall be passed to the producer. Maybe <code>null</code>.
   * @param consumer    The {@link BiConsumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public <C> void selectDo(@NotNull String jdbcQuery, C context, @NotNull BiConsumer<ResultSet, C> consumer) {
    select(jdbcQuery, context, ($1, $2) -> {consumer.accept($1, $2); return null;});
  }

  /**
   * Processes some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param consumer    The {@link Consumer} which processes the jdbc outcome.
   */
  public void selectDo(@NotNull String jdbcQuery, @NotNull Consumer<ResultSet> consumer) {
    selectDo(jdbcQuery, null, ($1, $2) -> consumer.accept($1));
  }

  /**
   * Processes all records.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param context    Some contextual object that shall be passed to the producer. Maybe <code>null</code>.
   * @param consumer   The {@link BiConsumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public <C> void selectAllDo(@NotNull String table, C context, @NotNull BiConsumer<ResultSet, C> consumer) {
    var name = canonicalTableName(table);
    selectDo(String.format(database.getSelectAllQuery(), name), context, consumer);
  }

  /**
   * Processes all records.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param consumer   The {@link Consumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public void selectAllDo(@NotNull String table, @NotNull Consumer<ResultSet> consumer) {
    var name = canonicalTableName(table);
    selectDo(String.format(database.getSelectAllQuery(), name), consumer);
  }

  /**
   * List some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param context     Some contextual object that shall be passed to the producer. Maybe <code>null</code>.
   * @param producer    The {@link BiFunction} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T, C> List<T> select(@NotNull String jdbcQuery, C context, @NotNull BiFunction<ResultSet, C, T> producer) {
    List<T>           result    = new ArrayList<>(100);
    PreparedStatement query     = null;
    ResultSet         resultset = null;
    try {
      query     = getQuery(jdbcQuery, null);
      resultset = query.executeQuery();
      while (resultset.next()) {
        try {
          result.add(producer.apply(resultset, context));
        } catch (Exception ex) {
          errorHandler.accept(ex);
        }
      }
    } catch (Exception ex) {
      errorHandler.accept(ex);
    } finally {
      closer.close(resultset);
    }
    if (result.isEmpty()) {
      result = Collections.emptyList();
    }
    return result;
  }

  /**
   * List some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param producer    The {@link Function} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T> List<T> select(@NotNull String jdbcQuery, @NotNull Function<ResultSet, T> producer) {
    return select(jdbcQuery, null, ($1, $2) -> producer.apply($1));
  }
  
  /**
   * List all records from a certain table.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param producer   The {@link Function} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T, C> List<T> selectAll(@NotNull String table, C context, @NotNull BiFunction<ResultSet, C, T> producer) {
    var name = canonicalTableName(table);
    return select(String.format(database.getSelectAllQuery(), name), context, producer);
  }

  /**
   * List all records from a certain table.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param producer   The {@link Function} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T> List<T> selectAll(@NotNull String table, @NotNull Function<ResultSet, T> producer) {
    var name = canonicalTableName(table);
    return select(String.format(database.getSelectAllQuery(), name), producer);
  }

  /**
   * Counts all records within a table.
   * 
   * @param table   The name of the table. Neither <code>null</code> nor empty.
   * 
   * @return   The number of available records. A negative number indicates an error.
   */
  public int count(@NotNull String table) {
    var result = -1;
    var name   = canonicalTableName(table);
    if (name != null) {
      PreparedStatement query     = null;
      ResultSet         resultset = null;
      try {
        query     = getQuery(database.getCountQuery(), name);
        resultset = query.executeQuery();
        if (resultset.next()) {
          result = resultset.getInt(1);
        }
      } catch (Exception ex) {
        errorHandler.accept(ex);
      } finally {
        closer.close(resultset);
      }
    }
    return result;
  }
  
  
//  /**
//   * Derives the current row data from the jdbc source and applies it to the {@link CsvTableModel} instance.
//   * 
//   * @param source   The jdbc source. Not <code>null</code>.
//   * @param dest     The {@link CsvTableModel} instance to be filled.
//   */
//  private void rowLoader(ResultSet source, CsvTableModel dest) {
//    try {
//      var rowdata = new Object[dest.getColumnCount()];
//      for(int i = 0, j = 1; i < rowdata.length; i++, j++) {
//        rowdata[i] = source.getObject(j);
//      }
//      dest.addRow(rowdata);
//    } catch (Exception ex) {
//      errorHandler.accept(ex);
//    }
//  }
  
  @Override
  public void close() throws Exception {
    queries.values().forEach(closer::close);
    queries.clear();
    if (connection != null) {
      connection.close();
      connection = null;
    }
  }
  
  private static class Uncacheable<K, V> extends HashMap<K, V> {

    @Override
    public boolean containsKey(Object key) {
      return false;
    }

    @Override
    public V put( K key, V value) {
      return super.get(key);
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
