package com.kasisoft.libs.common.data;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.sql.*;

/**
 * Simple wrapper for various database types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Database implements Predicate<String> {

  derby       ( "org.apache.derby.jdbc.EmbeddedDriver"          , "VALUES 1" ),
  h2          ( "org.h2.Driver"                                 , "SELECT 1" ),
  hsql        ( "org.hsqldb.jdbcDriver"                         , "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS" ),
  mssql       ( "com.microsoft.jdbc.sqlserver.SQLServerDriver"  , "SELECT 1" ),
  mysql       ( "com.mysql.jdbc.Driver"                         , "SELECT 1" ),
  odbc        ( "sun.jdbc.odbc.JdbcOdbcDriver"                  , null       ),
  oracle      ( "oracle.jdbc.driver.OracleDriver"               , "SELECT 1 FROM DUAL" ),
  postgresql  ( "org.postgresql.Driver"                         , "SELECT 1" ),
  sqlite      ( "org.sqlite.JDBC"                               , "SELECT 1" );

  @Getter 
  String    driver;
  
  boolean   active;
  String    aliveQuery;

  Database( String driverclass, String query ) {
    driver      = driverclass;
    active      = false;
    aliveQuery  = query;
  }

  /**
   * Returns the alive query which allows to test a connection.
   * 
   * @return   The alive query associated with this db type. Neither <code>null</code> nor empty.
   * 
   * @throws UnsupportedOperationException for {@link #odbc}.
   */
  public String getAliveQuery() {
    if( this == odbc ) {
      // not available as the underlying db system isn't known here
      throw new UnsupportedOperationException();
    }
    return aliveQuery;
  }
  
  /**
   * Verifies that a required driver is available.
   * 
   * @throws SQLException   The driver could not be loaded.
   */
  private synchronized void activate() throws SQLException {
    if( ! active ) {
      try {
        // only required for non v4 jdbc drivers
        Class.forName( driver );
        active = true;
      } catch( ClassNotFoundException ex ) {
        throw new SQLException( ex );
      }
    }
  }

  /**
   * Accesses a Connection.
   * 
   * @param url   The URL used to access the database. Neither <code>null</code> nor empty.
   * 
   * @return   The Connection used for the database. Not <code>null</code>.
   * 
   * @throws SQLException   Something went wrong while accessing the database.
   */
  public Connection getConnection( @NonNull String url ) throws SQLException {
    activate();
    return DriverManager.getConnection( url );
  }
  
  /**
   * Accesses a Connection.
   * 
   * @param url        The URL used to access the database. Neither <code>null</code> nor empty.
   * @param username   The username to access the database. Neither <code>null</code> nor empty.
   * @param password   The password to be used. Maybe <code>null</code>.
   * 
   * @return   The Connection used for the database. Not <code>null</code>.
   * 
   * @throws SQLException   Something went wrong while accessing the database.
   */
  public Connection getConnection( @NonNull String url, @NonNull String username, String password ) throws SQLException {
    activate();
    return DriverManager.getConnection( url, username, password );
  }
  
  /**
   * Makes an attempt to connect an reports whether connecting succeeded or not.
   * 
   * @param url        The URL used to access the database. Neither <code>null</code> nor empty.
   * @param username   The username to access the database. Neither <code>null</code> nor empty.
   * @param password   The password to be used. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> Connecting suceeded, so the DB seems to be available.
   */
  public boolean test( @NonNull String url, @NonNull String username, String password ) {
    boolean result = false;
    try( Connection connection = getConnection( url, username, password ) ) {
      result = connection.prepareStatement( aliveQuery ).execute();
    } catch( SQLException ex ) {
      // our default assumption is that the db isn't available
    }
    return result;
  }

  /**
   * Makes an attempt to connect an reports whether connecting succeeded or not.
   * 
   * @param url   The URL used to access the database. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> Connecting suceeded, so the DB seems to be available.
   */
  @Override
  public boolean test( @NonNull String url ) {
    boolean result = false;
    try( Connection connection = getConnection( url ) ) {
      result = connection.prepareStatement( aliveQuery ).execute();
    } catch( SQLException ex ) {
      // our default assumption is that the db isn't available
    }
    return result;
  }

} /* ENDENUM */
