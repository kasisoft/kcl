package com.kasisoft.libs.common.old.data;

import com.kasisoft.libs.common.converters.StringAdapter;
import com.kasisoft.libs.common.old.csv.CsvColumn;
import com.kasisoft.libs.common.old.csv.CsvOptions;
import com.kasisoft.libs.common.old.csv.CsvTableModel;
import com.kasisoft.libs.common.old.util.MiscFunctions;
import com.kasisoft.libs.common.types.Pair;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;

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
  
  DbConfig                                  config;
  Consumer<Exception>                       ehException;
  Map<String, PreparedStatement>            queries;
  List<String>                              tableNames;
  Map<String, List<String>>                 columnNames;
  Map<String, List<Pair<String, Integer>>>  columnTypes;
  Map<String, List<CsvColumn>>              columnSpecs; 

  /**
   * Sets up the connection which will be opened right away.
   * 
   * @param configuration   The configuration for the connection. Not <code>null</code>.
   */
  public DbConnection( DbConfig configuration ) {
    this( configuration, true );
  }
  
  /**
   * Sets up the connection which will be opened right away.
   * 
   * @param configuration   The configuration for the connection. Not <code>null</code>.
   * @param cache           <code>true</code> <=> Use a primitive internal cache (no eviction policies here).
   */
  public DbConnection( @NonNull DbConfig configuration, boolean cache ) {
    config          = configuration;
    tableNames      = new ArrayList<>();
    columnNames     = cache ? new HashMap<>() : new Uncacheable<>();
    columnTypes     = cache ? new HashMap<>() : new Uncacheable<>();
    columnSpecs     = cache ? new HashMap<>() : new Uncacheable<>();
    ehException     = this::errorHandler;
    connection      = null;
    queries         = new HashMap<>();
    if( config.getUsername() != null ) {
      connection = config.getDb().getConnection( config.getUrl(), config.getUsername(), config.getPassword() );
    } else {
      connection = config.getDb().getConnection( config.getUrl() );
    }
  }
  
  /**
   * Default error handler.
   * 
   * @param ex   An Exception that occured while accessing the data. Not <code>null</code>.
   */
  private void errorHandler( Exception ex ) {
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
  public String canonicalTableName( @NonNull String tablename ) {
    List<String> tables = listTables();
    String       result = null;
    for( String table : tables ) {
      if( table.equalsIgnoreCase( tablename ) ) {
        result = table;
        break;
      }
    }
    return result;
  }
  
  /**
   * Returns a list of table names.
   * 
   * @return   A list of table names (unmodifiable). Not <code>null</code>.
   */
  public List<String> listTables() {
    List<String> result = Collections.unmodifiableList( tableNames );
    if( tableNames.isEmpty() ) {
      ResultSet resultset = null;
      try {
        DatabaseMetaData metadata  = connection.getMetaData();
        resultset = metadata.getTables( null, null, "%", new String[] { "TABLE" } );
        while( resultset.next() ) {
          tableNames.add( resultset.getString(3) );
        }
      } catch( Exception ex ) {
        ehException.accept( ex );
      } finally {
        MiscFunctions.close( resultset );
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
  private PreparedStatement getQuery( String query, String table ) throws SQLException {
    String            key    = table != null ? String.format( query, table ) : query;
    PreparedStatement result = queries.get( key );
    if( result == null ) {
      result = connection.prepareStatement( key );
      queries.put( key, result );
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
  public List<String> listColumnNames( @NonNull String table ) {
    return listColumnInfos( columnNames, table, this::getColumnName );
  }
  
  /**
   * Fetches the column name from the supplied jdbc result.
   * 
   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
   * @param index      The 1-based index of the column.
   * 
   * @return   The name of the column. Maybe <code>null</code> in case of an error.
   */
  private String getColumnName( ResultSetMetaData metadata, int index ) {
    try {
      return metadata.getColumnName( index );
    } catch( Exception ex ) {
      ehException.accept( ex );
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
  public List<Pair<String, Integer>> listColumnTypes( @NonNull String table ) {
    return listColumnInfos( columnTypes, table, this::getColumnType );
  }

  /**
   * Fetches the column name with it's jdbc type from the supplied jdbc result.
   * 
   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
   * @param index      The 1-based index of the column.
   * 
   * @return   The pair with the column name and jdbc type. Maybe <code>null</code> in case of an error.
   */
  private Pair<String, Integer> getColumnType( ResultSetMetaData metadata, int index ) {
    try {
      return new Pair<>( metadata.getColumnName( index ), metadata.getColumnType( index ) );
    } catch( Exception ex ) {
      ehException.accept( ex );
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
  private <T> List<T> listColumnInfos( Map<String, List<T>> cache, String table, BiFunction<ResultSetMetaData, Integer, T> producer ) {
    List<T>  result = Collections.emptyList();
    String   name   = canonicalTableName( table );
    if( name != null ) {
      if( ! cache.containsKey( name ) ) {
        PreparedStatement query     = null;
        ResultSet         resultset = null;
        try {
          result                     = new ArrayList<>();
          query                      = getQuery( config.getDb().getListColumnsQuery(), name );
          resultset                  = query.executeQuery();
          ResultSetMetaData metadata = resultset.getMetaData();
          for( int i = 0; i < metadata.getColumnCount(); i++ ) {
            T value = producer.apply( metadata, i + 1 );
            if( value != null ) {
              result.add( value );
            }
          }
          cache.put( name, Collections.unmodifiableList( result ) );
        } catch( Exception ex ) {
          // if the handler doesn't abort we're returning an empty list
          ehException.accept( ex );
        } finally {
          MiscFunctions.close( resultset );
        }
      }
      result = cache.get( name );
    }
    return result;
  }

  private <T> CsvColumn<T> newCsvColumn( String name, Function<String, T> adapter ) {
    CsvColumn<T> result = new CsvColumn<>();
    result.setTitle( name );
    result.setAdapter( adapter );
    return result;
  }
  
  /**
   * Creates a CsvTableModel instance providing the metadata description of the supplied table.
   * 
   * @param table   The table which column names shall be returned. Neither <code>null</code> nor empty.
   * 
   * @return  The CsvTableModel instance. Not <code>null</code>.
   */
  public CsvTableModel createCsvMetaData( @NonNull String table ) {

    List<CsvColumn> specs = Arrays.asList(
      newCsvColumn( "name"        , new StringAdapter() ),
      newCsvColumn( "label"       , new StringAdapter() ),
      newCsvColumn( "schema"      , new StringAdapter() ),
      newCsvColumn( "catalog"     , new StringAdapter() ),
      newCsvColumn( "class"       , new StringAdapter() ),
      newCsvColumn( "type"        , new StringAdapter() ),
      newCsvColumn( "typename"    , new StringAdapter() ),
      newCsvColumn( "displaysize" , new StringAdapter() ),
      newCsvColumn( "precision"   , new StringAdapter() ),
      newCsvColumn( "scale"       , new StringAdapter() )
    );

    CsvTableModel result = new CsvTableModel( CsvOptions.builder().columns( specs ).titleRow().build() );
    String        name   = canonicalTableName( table );
    if( name != null ) {
      PreparedStatement query     = null;
      ResultSet         resultset = null;
      try {
        query     = getQuery( config.getDb().getListColumnsQuery(), name );
        resultset = query.executeQuery();
        ResultSetMetaData metadata  = resultset.getMetaData();
        for( int i = 1; i <= metadata.getColumnCount(); i++ ) {
          result.addRow( new Object[] {
            metadata.getColumnName(i),
            metadata.getColumnLabel(i),
            metadata.getSchemaName(i),
            metadata.getCatalogName(i),
            metadata.getColumnClassName(i),
            metadata.getColumnType(i),
            metadata.getColumnTypeName(i),
            metadata.getColumnDisplaySize(i),
            metadata.getPrecision(i),
            metadata.getScale(i)
          });
        }
      } catch( Exception ex ) {
        // if the handler doesn't abort we're returning an empty list
        ehException.accept( ex );
      } finally {
        MiscFunctions.close( resultset );
      }
    }
    return result;
  }

  /**
   * Creates a CsvTableModel instance providing the data of the supplied table.
   * 
   * @param table      The table which column names shall be returned. Neither <code>null</code> nor empty.
   * 
   * @return  The CsvTableModel instance. Not <code>null</code>.
   */
  public CsvTableModel createCsvModel( @NonNull String table ) {
    List<CsvColumn> specs  = listColumnInfos( columnSpecs, table, this::getCsvColumn );
    CsvTableModel   result = new CsvTableModel( CsvOptions.builder().columns( specs ).titleRow().build() );
    importAllRows( table, $ -> rowLoader( $, result ) );
    return result;
  }
  
  /**
   * Creates a {@link CsvColumn} specification from the jdbc metadata.
   * 
   * @param metadata   The jdbc record providing some meta information. Not <code>null</code>.
   * @param index      The 1-based index of the column.
   * 
   * @return   The {@link CsvColumn} specification. Maybe <code>null</code> in case of an error.
   */
  private CsvColumn getCsvColumn( ResultSetMetaData metadata, int index ) {
    try {
      Class<?>  type   = Class.forName( metadata.getColumnClassName( index ) );
      CsvColumn result = new CsvColumn();
      result.setTitle( metadata.getColumnName( index ) );
      result.setType( type );
      result.setNullable( metadata.isNullable( index ) == ResultSetMetaData.columnNullable );
      if( result.getAdapter() == null ) {
        result.setAdapter( $ -> $ );
      }
      return result;
    } catch( Exception ex ) {
      ehException.accept( ex );
      return null;
    }
  }
  
  /**
   * List all records within a table.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param consumer   The {@link Consumer} which will be invoked for each record.
   */
  private void importAllRows( String table, Consumer<ResultSet> consumer ) {
    String name = canonicalTableName( table );
    if( name != null ) {
      PreparedStatement query     = null;
      ResultSet         resultset = null;
      try {
        query     = getQuery( config.getDb().getSelectAllQuery(), name );
        resultset = query.executeQuery();
        while( resultset.next() ) {
          consumer.accept( resultset );
        }
      } catch( Exception ex ) {
        ehException.accept( ex );
      } finally {
        MiscFunctions.close( resultset );
      }
    }
  }

  /**
   * Processes some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param context     Some contextual object that shall be passed to the producer. Maybe <code>null</code>.
   * @param consumer    The {@link BiConsumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public <C> void selectDo( @NonNull String jdbcQuery, C context, @NonNull BiConsumer<ResultSet, C> consumer ) {
    select( jdbcQuery, context, ($1, $2) -> { consumer.accept( $1, $2 ); return null; } );
  }

  /**
   * Processes some records.
   * 
   * @param jdbcQuery   The jdbc query used to select the records. Neither <code>null</code> nor empty.
   * @param consumer    The {@link Consumer} which processes the jdbc outcome.
   */
  public void selectDo( @NonNull String jdbcQuery, @NonNull Consumer<ResultSet> consumer ) {
    selectDo( jdbcQuery, null, ($1, $2) -> consumer.accept($1) );
  }

  /**
   * Processes all records.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param context    Some contextual object that shall be passed to the producer. Maybe <code>null</code>.
   * @param consumer   The {@link BiConsumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public <C> void selectAllDo( @NonNull String table, C context, @NonNull BiConsumer<ResultSet, C> consumer ) {
    String name = canonicalTableName( table );
    selectDo( String.format( config.getDb().getSelectAllQuery(), name ), context, consumer );
  }

  /**
   * Processes all records.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param consumer   The {@link Consumer} which processes the jdbc outcome. Not <code>null</code>.
   */
  public void selectAllDo( @NonNull String table, @NonNull Consumer<ResultSet> consumer ) {
    String name = canonicalTableName( table );
    selectDo( String.format( config.getDb().getSelectAllQuery(), name ), consumer );
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
  public <T, C> List<T> select( @NonNull String jdbcQuery, C context, @NonNull BiFunction<ResultSet, C, T> producer ) {
    List<T>           result    = new ArrayList<>(100);
    PreparedStatement query     = null;
    ResultSet         resultset = null;
    try {
      query     = getQuery( jdbcQuery, null );
      resultset = query.executeQuery();
      while( resultset.next() ) {
        try {
          result.add( producer.apply( resultset, context ) );
        } catch( Exception ex ) {
          ehException.accept( ex );
        }
      }
    } catch( Exception ex ) {
      ehException.accept( ex );
    } finally {
      MiscFunctions.close( resultset );
    }
    if( result.isEmpty() ) {
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
  public <T> List<T> select( @NonNull String jdbcQuery, @NonNull Function<ResultSet, T> producer ) {
    return select( jdbcQuery, null, ($1, $2) -> producer.apply($1) );
  }
  
  /**
   * List all records from a certain table.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param producer   The {@link Function} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T, C> List<T> selectAll( @NonNull String table, C context, @NonNull BiFunction<ResultSet, C, T> producer ) {
    String name = canonicalTableName( table );
    return select( String.format( config.getDb().getSelectAllQuery(), name ), context, producer );
  }

  /**
   * List all records from a certain table.
   * 
   * @param table      The name of the table. Neither <code>null</code> nor empty.
   * @param producer   The {@link Function} which creates a usable record from the jdbc outcome. Not <code>null</code>.
   * 
   * @return   A list with all records. Not <code>null</code>.
   */
  public <T> List<T> selectAll( @NonNull String table, @NonNull Function<ResultSet, T> producer ) {
    String name = canonicalTableName( table );
    return select( String.format( config.getDb().getSelectAllQuery(), name ), producer );
  }

  /**
   * Counts all records within a table.
   * 
   * @param table   The name of the table. Neither <code>null</code> nor empty.
   * 
   * @return   The number of available records. A negative number indicates an error.
   */
  public int count( @NonNull String table ) {
    int    result = -1;
    String name   = canonicalTableName( table );
    if( name != null ) {
      PreparedStatement query     = null;
      ResultSet         resultset = null;
      try {
        query     = getQuery( config.getDb().getCountQuery(), name );
        resultset = query.executeQuery();
        if( resultset.next() ) {
          result = resultset.getInt(1);
        }
      } catch( Exception ex ) {
        ehException.accept( ex );
      } finally {
        MiscFunctions.close( resultset );
      }
    }
    return result;
  }
  
  
  /**
   * Derives the current row data from the jdbc source and applies it to the {@link CsvTableModel} instance.
   * 
   * @param source   The jdbc source. Not <code>null</code>.
   * @param dest     The {@link CsvTableModel} instance to be filled.
   */
  private void rowLoader( ResultSet source, CsvTableModel dest ) {
    try {
      Object[] rowdata = new Object[ dest.getColumnCount() ];
      for( int i = 0, j = 1; i < rowdata.length; i++, j++ ) {
        rowdata[i] = source.getObject(j);
      }
      dest.addRow( rowdata );
    } catch( Exception ex ) {
      ehException.accept( ex );
    }
  }
  
  @Override
  public void close() throws Exception {
    queries.values().forEach( MiscFunctions::close );
    queries.clear();
    if( connection != null ) {
      connection.close();
      connection = null;
    }
  }
  
  private static class Uncacheable<K, V> extends HashMap<K, V> {

    @Override
    public boolean containsKey( Object key ) {
      return false;
    }

    @Override
    public V put( K key, V value ) {
      return super.get( key );
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
