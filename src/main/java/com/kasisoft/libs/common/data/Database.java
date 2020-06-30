package com.kasisoft.libs.common.data;

import com.kasisoft.libs.common.KclException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.function.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * Simple wrapper for various database types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Database implements Predicate<String> {

  derby       (false , "VALUES 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "org.apache.derby.jdbc.EmbeddedDriver"),
  h2          (false , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "org.h2.Driver"),
  hsql        (false , "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS" , "SELECT TOP 1 * FROM %s"   , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "org.hsqldb.jdbcDriver"),
  mssql       (false , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "com.microsoft.jdbc.sqlserver.SQLServerDriver", "net.sourceforge.jtds.jdbc.Driver"),
  mysql       (true  , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver"),
  odbc        (false , null                                            , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "sun.jdbc.odbc.JdbcOdbcDriver"),
  oracle      (false , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "oracle.jdbc.driver.OracleDriver"),
  postgresql  (false , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "org.postgresql.Driver"),
  sqlite      (false , "SELECT 1"                                      , "SELECT * FROM %s LIMIT 1" , "SELECT * FROM %s" , "SELECT COUNT(*) FROM %s" , "org.sqlite.JDBC");

  @Getter 
  String        driver;
  
  @Getter
  String        listColumnsQuery;
  
  @Getter
  String        selectAllQuery;

  @Getter
  String        countQuery;

  List<String>  secondaryDrivers;
  
  boolean       active;
  String        aliveQuery;
  
  Database(boolean spi, String query, String listColumns, String selectAll, String count, String ... driverclasses) {
    driver           = driverclasses[0];
    active           = spi;
    aliveQuery       = query;
    listColumnsQuery = listColumns;
    selectAllQuery   = selectAll;
    countQuery       = count;
    if (driverclasses.length > 1) {
      secondaryDrivers = new ArrayList<>(Arrays.asList(driverclasses));
      secondaryDrivers.remove(0);
    } else {
      secondaryDrivers = Collections.emptyList();
    }
  }

  /**
   * Returns the alive query which allows to test a connection.
   * 
   * @return   The alive query associated with this db type. Neither <code>null</code> nor empty.
   * 
   * @throws UnsupportedOperationException for {@link #odbc}.
   */
  public @NotBlank String getAliveQuery() {
    if (this == odbc) {
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
  private synchronized void activate() {
    if (!active) {
      // only required for non v4 jdbc drivers
      active = activate(driver);
      if( (!active) && (!secondaryDrivers.isEmpty())) {
        for (var secondary : secondaryDrivers) {
          active = activate(secondary);
          if (active) {
            var oldPrimary = driver;
            driver         = secondary;
            secondaryDrivers.add( oldPrimary );
            break;
          }
        }
      }
      if (!active) {
        throw new KclException("Failed to activate driver '%s' !", driver);
      }
    }
  }
  
  private boolean activate(String classname) {
    try {
      Class.forName(classname);
      return true; 
    } catch (ClassNotFoundException ex) {
      return false;
    }
  }

  /**
   * Accesses a Connection.
   * 
   * @param url   The URL used to access the database. Neither <code>null</code> nor empty.
   * 
   * @return   The Connection used for the database. Not <code>null</code>.
   */
  public Connection getConnection(@NotBlank String url) {
    try {
      activate();
      return DriverManager.getConnection(url);
    } catch (Exception ex) {
      throw new KclException(ex, "Couldn't connect to database with url '%s'", url);
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
  public Connection getConnection(@NotBlank String url, @NotNull String username, String password) {
    try {
      activate();
      return DriverManager.getConnection(url, username, password);
    } catch (Exception ex) {
      throw new KclException(ex, "Couldn't connect to database with url '%s'", url);
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
  public boolean test(@NotBlank String url, @NotNull String username, String password) {
    try (
      var connection = getConnection(url, username, password);
      var statement  = connection.prepareStatement(aliveQuery);
    ) {
      return statement.execute();
    } catch (SQLException ex) {
      return false;
    }
  }

  /**
   * Makes an attempt to connect an reports whether connecting succeeded or not.
   * 
   * @param url   The URL used to access the database. Neither <code>null</code> nor empty.
   * 
   * @return   <code>true</code> <=> Connecting suceeded, so the DB seems to be available.
   */
  @Override
  public boolean test(@NotBlank String url) {
    try (
      var connection = getConnection(url);
      var statement  = connection.prepareStatement(aliveQuery);
    ) {
      return statement.execute();
    } catch( SQLException ex ) {
      return false;
    }
  }

} /* ENDENUM */
