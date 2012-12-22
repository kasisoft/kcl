/**
 * Name........: Database
 * Description.: Simple wrapper for various database types. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.db;

import java.sql.*;

/**
 * Simple wrapper for various database types.
 */
public enum Database {

  derby       ( "org.apache.derby.jdbc.EmbeddedDriver"          ),
  h2          ( "org.h2.Driver"                                 ),
  hsql        ( "org.hsqldb.jdbcDriver"                         ),
  mssql       ( "com.microsoft.jdbc.sqlserver.SQLServerDriver"  ),
  mysql       ( "com.mysql.jdbc.Driver"                         ),
  odbc        ( "sun.jdbc.odbc.JdbcOdbcDriver"                  ),
  oracle      ( "oracle.jdbc.driver.OracleDriver"               ),
  postgresql  ( "org.postgresql.Driver"                         ),
  sqlite      ( "org.sqlite.JDBC"                               );

  private String    driver;
  private boolean   active;

  Database( String driverclass ) {
    driver  = driverclass;
    active  = false;
  }

  /**
   * Verifies that a required driver is available.
   * 
   * @throws SQLException   The driver could not be loaded.
   */
  private void activate() throws SQLException {
    if( ! active ) {
      try {
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
  public Connection getConnection( String url ) throws SQLException {
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
  public Connection getConnection( String url, String username, String password ) throws SQLException {
    activate();
    return DriverManager.getConnection( url, username, password );
  }

} /* ENDENUM */
