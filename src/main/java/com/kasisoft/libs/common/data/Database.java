package com.kasisoft.libs.common.data;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.sql.*;

/**
 * Simple wrapper for various database types.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum Database implements Predicate<String> {

  derby       (false, "VALUES 1"                                     , "SELECT * FROM %s LIMIT 1", "org.apache.derby.jdbc.EmbeddedDriver"),
  h2          (false, "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "org.h2.Driver"),
  hsql        (false, "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS", "SELECT TOP 1 * FROM %s"  , "org.hsqldb.jdbcDriver"),
  mssql       (false, "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "com.microsoft.jdbc.sqlserver.SQLServerDriver", "net.sourceforge.jtds.jdbc.Driver"),
  mysql       (true , "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "com.mysql.jdbc.Driver", "com.mysql.cj.jdbc.Driver"),
  odbc        (false, null                                           , "SELECT * FROM %s LIMIT 1", "sun.jdbc.odbc.JdbcOdbcDriver"),
  oracle      (false, "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "oracle.jdbc.driver.OracleDriver"),
  postgresql  (false, "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "org.postgresql.Driver"),
  sqlite      (false, "SELECT 1"                                     , "SELECT * FROM %s LIMIT 1", "org.sqlite.JDBC");

  private String        driver;
  private String        listColumnsQuery;
  private String        selectAllQuery;
  private String        countQuery;
  private List<String>  secondaryDrivers;
  private boolean       active;
  private String        aliveQuery;

  Database(boolean spi, String alive, String listColumns, String ... driverclasses) {
    driver           = driverclasses[0];
    active           = spi;
    aliveQuery       = alive;
    listColumnsQuery = listColumns;
    selectAllQuery   = "SELECT * FROM %s";
    countQuery       = "SELECT COUNT(*) FROM %s";
    if (driverclasses.length > 1) {
      secondaryDrivers = new ArrayList<>(Arrays.asList(driverclasses));
      secondaryDrivers.remove(0);
    } else {
      secondaryDrivers = Collections.emptyList();
    }
  }

  public String getDriver() {
    return driver;
  }

  public String getListColumnsQuery() {
    return listColumnsQuery;
  }

  public String getSelectAllQuery() {
    return selectAllQuery;
  }

  public String getCountQuery() {
    return countQuery;
  }

  /**
   * Returns the alive query which allows to test a connection.
   *
   * @return   The alive query associated with this db type.
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
        throw new KclException(error_failed_to_activate_jdbc_driver, driver);
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
   * @param url   The URL used to access the database.
   *
   * @return   The Connection used for the database.
   */
  public @NotNull Connection getConnection(@NotBlank String url) {
    try {
      activate();
      return DriverManager.getConnection(url);
    } catch (Exception ex) {
      throw new KclException(ex, error_cannot_connect_to_database, url);
    }
  }

  /**
   * Accesses a Connection.
   *
   * @param url        The URL used to access the database.
   * @param username   The username to access the database.
   * @param password   The password to be used.
   *
   * @return   The Connection used for the database.
   */
  public @NotNull Connection getConnection(@NotBlank String url, @NotNull String username, String password) {
    try {
      activate();
      return DriverManager.getConnection(url, username, password);
    } catch (Exception ex) {
      throw new KclException(ex, error_cannot_connect_to_database, url);
    }
  }

  /**
   * Makes an attempt to connect an reports whether connecting succeeded or not.
   *
   * @param url        The URL used to access the database.
   * @param username   The username to access the database.
   * @param password   The password to be used.
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
   * @param url   The URL used to access the database.
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
