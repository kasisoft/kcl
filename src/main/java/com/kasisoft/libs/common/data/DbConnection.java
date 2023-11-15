package com.kasisoft.libs.common.data;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.csv.*;

import jakarta.validation.constraints.*;

import javax.sql.*;

import java.util.function.*;

import java.util.*;

import java.sql.*;

/**
 * Simple wrapper around a jdbc {@link Connection} instance which provides some helpful utility features.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
@SuppressWarnings("rawtypes")
public class DbConnection implements AutoCloseable {

    private Connection                          connection;
    private DatabaseMetaData                    metadata;
    private Database                            database;
    private Map<String, PreparedStatement>      queries;
    private List<String>                        tableNames;
    private Map<String, List<String>>           columnNames;
    private Map<String, List<ColumnType>>       columnTypes;
    private Map<String, List<CsvColumn>>        columnSpecs;

    /**
     * Sets up the connection which will be opened right away.
     *
     * @param config
     *            The configuration for the connection.
     */
    public DbConnection(@NotNull DbConfig config) {
        init(config.db());
        try {
            if (config.username() != null) {
                connection = config.db().getConnection(config.url(), config.username(), config.password());
            } else {
                connection = config.db().getConnection(config.url());
            }
            metadata = connection.getMetaData();
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Sets up the connection which will be opened right away.
     *
     * @param dataSource
     *            The DataSource used to provide access to the database.
     */
    public DbConnection(Database db, @NotNull DataSource dataSource) {
        init(db);
        try {
            connection = dataSource.getConnection();
            metadata   = connection.getMetaData();
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    private void init(@NotNull Database db) {
        database     = db;
        tableNames   = new ArrayList<>();
        columnNames  = new HashMap<>();
        columnTypes  = new HashMap<>();
        columnSpecs  = new HashMap<>();
        queries      = new HashMap<>();
    }

    public Connection getConnection() {
        return connection;
    }

    /**
     * Clears all object caches.
     */
    public synchronized void clear() {
        tableNames.clear();
        columnNames.clear();
        columnTypes.clear();
        columnSpecs.clear();
    }

    /**
     * Returns a canonical table name which means that the table is named as delivered by the jdbc
     * driver.
     *
     * @param tablename
     *            The name that shall be canonical.
     * @return The canonical name unless there's no correspondingly named table.
     */
    public synchronized String canonicalTableName(@NotNull String tablename) {
        var tables = listTables();
        for (var table : tables) {
            if (table.equalsIgnoreCase(tablename)) {
                return table;
            }
        }
        return null;
    }

    /**
     * Returns a list of table names.
     *
     * @return A list of table names (unmodifiable).
     */
    @NotNull
    public synchronized List<String> listTables() {
        var result = Collections.unmodifiableList(tableNames);
        if (tableNames.isEmpty()) {
            try (var resultset = metadata.getTables(null, null, "%", new String[] {"TABLE"})) {
                while (resultset.next()) {
                    tableNames.add(resultset.getString(3));
                }
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
        return result;
    }

    /**
     * Returns a {@link PreparedStatement} instance using the supplied table.
     *
     * @param query
     *            The string providing the query.
     * @param table
     *            The name of the table.
     * @return The {@link PreparedStatement} instance.
     */
    @NotNull
    private PreparedStatement getQuery(@NotBlank String query, @NotNull String table) {
        try {
            var key    = table != null ? query.formatted(table) : query;
            var result = queries.get(key);
            if (result == null) {
                result = connection.prepareStatement(key);
                queries.put(key, result);
            }
            return result;
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Returns a list of all column names for the supplied table.
     *
     * @param table
     *            The table which column names shall be returned.
     * @return A list of all column names.
     */
    @NotNull
    public synchronized List<String> listColumnNames(@NotBlank String table) {
        return listColumnInfos(columnNames, table, this::getColumnName);
    }

    /**
     * Fetches the column name from the supplied jdbc result.
     *
     * @param metadata
     *            The jdbc record providing some meta information.
     * @param index
     *            The 1-based index of the column.
     * @return The name of the column.
     */
    @NotBlank
    private String getColumnName(@NotNull ResultSetMetaData metadata, @Min(1) int index) {
        try {
            return metadata.getColumnName(index);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * Returns a list of pairs for all columns and their associated jdbc type.
     *
     * @param table
     *            The table which column names shall be returned.
     * @return A list of pairs providing the column name associated with the corresponding jdbc type.
     */
    @NotNull
    public synchronized List<ColumnType> listColumnTypes(@NotBlank String table) {
        return listColumnInfos(columnTypes, table, this::getColumnType);
    }

    /**
     * Fetches the column name with it's jdbc type from the supplied jdbc result.
     *
     * @param metadata
     *            The jdbc record providing some meta information.
     * @param index
     *            The 1-based index of the column.
     * @return The pair with the column name and jdbc type or null in case of an error.
     */
    private ColumnType getColumnType(@NotNull ResultSetMetaData metadata, @Min(1) int index) {
        try {
            return new ColumnType(metadata.getColumnName(index), metadata.getColumnType(index));
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

    /**
     * A generic function which fetches some metadata results.
     *
     * @param cache
     *            A cache which is used to keep the metadata information in memory.
     * @param table
     *            The table which column names shall be returned.
     * @param producer
     *            The function which turns the raw metadata result into our desired return type.
     * @return A list of metadata results.
     */
    private <T> List<T> listColumnInfos(@NotNull Map<String, List<T>> cache, @NotBlank String table, @NotNull BiFunction<ResultSetMetaData, Integer, T> producer) {
        var name = canonicalTableName(table);
        if (name != null) {
            var query = getQuery(database.getListColumnsQuery(), name);
            return cache.computeIfAbsent(name, $name -> {
                var list  = new ArrayList<T>();
                try (var resultset = query.executeQuery()) {
                    var metadata = resultset.getMetaData();
                    for (var i = 0; i < metadata.getColumnCount(); i++) {
                        T value = producer.apply(metadata, i + 1);
                        if (value != null) {
                            list.add(value);
                        }
                    }
                } catch (Exception ex) {
                    throw KclException.wrap(ex);
                }
                return Collections.unmodifiableList(list);
            });
        }
        return Collections.<T> emptyList();
    }

    /**
     * Processes some records.
     *
     * @param jdbcQuery
     *            The jdbc query used to select the records.
     * @param context
     *            Some contextual object that shall be passed to the producer.
     * @param consumer
     *            The {@link BiConsumer} which processes the jdbc outcome.
     */
    public synchronized <C> void selectDo(@NotBlank String jdbcQuery, C context, @NotNull BiConsumer<ResultSet, C> consumer) {
        select(jdbcQuery, context, ($1, $2) -> {
            consumer.accept($1, $2);
            return null;
        });
    }

    /**
     * Processes some records.
     *
     * @param jdbcQuery
     *            The jdbc query used to select the records.
     * @param consumer
     *            The {@link Consumer} which processes the jdbc outcome.
     */
    public synchronized void selectDo(@NotBlank String jdbcQuery, @NotNull Consumer<ResultSet> consumer) {
        selectDo(jdbcQuery, null, ($1, $2) -> consumer.accept($1));
    }

    /**
     * Processes all records.
     *
     * @param table
     *            The name of the table.
     * @param context
     *            Some contextual object that shall be passed to the producer.
     * @param consumer
     *            The {@link BiConsumer} which processes the jdbc outcome.
     */
    public synchronized <C> void selectAllDo(@NotBlank String table, C context, @NotNull BiConsumer<ResultSet, C> consumer) {
        var name = canonicalTableName(table);
        selectDo(database.getSelectAllQuery().formatted(name), context, consumer);
    }

    /**
     * Processes all records.
     *
     * @param table
     *            The name of the table.
     * @param consumer
     *            The {@link Consumer} which processes the jdbc outcome.
     */
    public synchronized void selectAllDo(@NotBlank String table, @NotNull Consumer<ResultSet> consumer) {
        var name = canonicalTableName(table);
        selectDo(database.getSelectAllQuery().formatted(name), consumer);
    }

    /**
     * List some records.
     *
     * @param jdbcQuery
     *            The jdbc query used to select the records.
     * @param context
     *            Some contextual object that shall be passed to the producer.
     * @param producer
     *            The {@link BiFunction} which creates a usable record from the jdbc outcome.
     * @return A list with all records.
     */
    @NotNull
    public synchronized <T, C> List<T> select(@NotBlank String jdbcQuery, C context, @NotNull BiFunction<ResultSet, C, T> producer) {
        var result  = new ArrayList<T>(100);
        var query   = getQuery(jdbcQuery, null);
        try (var resultset = query.executeQuery()) {
            while (resultset.next()) {
                result.add(producer.apply(resultset, context));
            }
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
        return result;
    }

    /**
     * List some records.
     *
     * @param jdbcQuery
     *            The jdbc query used to select the records.
     * @param producer
     *            The {@link Function} which creates a usable record from the jdbc outcome.
     * @return A list with all records.
     */
    @NotNull
    public synchronized <T> List<T> select(@NotBlank String jdbcQuery, @NotNull Function<ResultSet, T> producer) {
        return select(jdbcQuery, null, ($1, $2) -> producer.apply($1));
    }

    /**
     * List all records from a certain table.
     *
     * @param table
     *            The name of the table.
     * @param context
     *            Some contextual object that shall be passed to the producer.
     * @param producer
     *            The {@link Function} which creates a usable record from the jdbc outcome.
     * @return A list with all records.
     */
    @NotNull
    public synchronized <T, C> List<T> selectAll(@NotBlank String table, C context, @NotNull BiFunction<ResultSet, C, T> producer) {
        var name = canonicalTableName(table);
        return select(database.getSelectAllQuery().formatted(name), context, producer);
    }

    /**
     * List all records from a certain table.
     *
     * @param table
     *            The name of the table.
     * @param producer
     *            The {@link Function} which creates a usable record from the jdbc outcome.
     * @return A list with all records.
     */
    @NotNull
    public synchronized <T> List<T> selectAll(@NotBlank String table, @NotNull Function<ResultSet, T> producer) {
        var name = canonicalTableName(table);
        return select(database.getSelectAllQuery().formatted(name), producer);
    }

    /**
     * Counts all records within a table.
     *
     * @param table
     *            The name of the table.
     * @return The number of available records. A negative number indicates an error.
     */
    public synchronized int count(@NotBlank String table) {
        var result = -1;
        var name   = canonicalTableName(table);
        if (name != null) {
            var query = getQuery(database.getCountQuery(), name);
            try (var resultset = query.executeQuery()) {
                if (resultset.next()) {
                    result = resultset.getInt(1);
                }
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        }
        return result;
    }

    @Override
    public synchronized void close() throws Exception {
        queries.values().forEach($ -> {
            try {
                $.close();
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        });
        queries.clear();
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public static record ColumnType(String columnName, int columnType) {}

} /* ENDCLASS */
