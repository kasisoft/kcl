package com.kasisoft.libs.common.data;

import com.kasisoft.libs.common.base.*;

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

  derby       ( "org.apache.derby.jdbc.EmbeddedDriver"          , "VALUES 1"                                      , "SELECT * FROM %s LIMIT 1" ),
  h2          ( "org.h2.Driver"                                 , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" ),
  hsql        ( "org.hsqldb.jdbcDriver"                         , "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS" , "SELECT TOP 1 * FROM %s"   ),
  mssql       ( "com.microsoft.jdbc.sqlserver.SQLServerDriver"  , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" ),
  mysql       ( "com.mysql.jdbc.Driver"                         , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" ),
  odbc        ( "sun.jdbc.odbc.JdbcOdbcDriver"                  , null                                            , "SELECT * FROM %s LIMIT 1" ),
  oracle      ( "oracle.jdbc.driver.OracleDriver"               , "SELECT 1 FROM DUAL"                            , "SELECT * FROM %s LIMIT 1" ),
  postgresql  ( "org.postgresql.Driver"                         , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" ),
  sqlite      ( "org.sqlite.JDBC"                               , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" );


  @Getter 
  String    driver;
  
  boolean   active;
  String    aliveQuery;
  String    listColumnQuery;

  Database( String driverclass, String query, String listColumns ) {
    driver          = driverclass;
    active          = false;
    aliveQuery      = query;
    listColumnQuery = listColumns;
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
   * Returns the query for listing columns.
   * 
   * @return   The query for listing columns. Neither <code>null</code> nor empty.
   */
  public String getListColumnsQuery() {
    if( listColumnQuery.length() == 0 ) {
      // not available yet
      throw new UnsupportedOperationException();
    }
    return listColumnQuery;
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
   */
  public Connection getConnection( @NonNull String url ) {
    try {
      activate();
      return DriverManager.getConnection( url );
    } catch( SQLException ex ) {
      throw FailureCode.SqlFailure.newException( ex );
    }
  }
  
  /**
   * Accesses a Connection.
   * 
   * @param url        The URL used to access the database. Neither <code>null</code> nor empty.
   * @param username   The username to access the database. Neither <code>null</code> nor empty.
   * @param password   The password to be used. Maybe <code>null</code>.
   * 
   * @return   The Connection used for the database. Not <code>null</code>.
   */
  public Connection getConnection( @NonNull String url, @NonNull String username, String password ) {
    try {
      activate();
      return DriverManager.getConnection( url, username, password );
    } catch( SQLException ex ) {
      throw FailureCode.SqlFailure.newException( ex );
    }
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
