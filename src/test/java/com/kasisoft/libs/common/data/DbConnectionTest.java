package com.kasisoft.libs.common.data;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.data.Database;
import com.kasisoft.libs.common.data.DbConfig;
import com.kasisoft.libs.common.data.DbConnection;
import com.kasisoft.libs.common.old.io.IoFunctions;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.h2.tools.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.sql.ResultSet;

import lombok.extern.slf4j.Slf4j;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for 'DbConnection'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class DbConnectionTest {
  
  static Path     dbLocation;
  static Server   server;
  
  private Path getLocation( String path ) throws Exception {
    URL url = DbConnectionTest.class.getClassLoader().getResource( path );
    assertNotNull( url );
    return Paths.get( url.toURI() );
  }
  
  @BeforeClass
  public static void prepare() throws Exception {
    
    Path tempdir    = SysProperty.TempDir.getValue( System.getProperties() ).toPath();
    dbLocation      = tempdir.resolve( "h2db" );
    Files.createDirectories( dbLocation );
    assertTrue( Files.isDirectory( dbLocation ) );
    
    server = Server.createTcpServer( new String[] { "-tcp", "-baseDir", dbLocation.toString() } ).start();
    
    log.info( "Server started" );
    
  }

  @AfterClass
  public static void shutDown() throws Exception {
    if( server != null ) {
      server.stop();
    }
    IoFunctions.delete( dbLocation.toFile() );
    log.info( "Server stopped" );
  }
  
  private DbConfig newDbConfig( String dbname, String initscript ) throws Exception {
    Path     location = getLocation( initscript );
    String   url      = String.format( "jdbc:h2:tcp://localhost/%s/%s;MODE=MySQL;DATABASE_TO_UPPER=false;INIT=runscript from '%s'", dbLocation.toString(), dbname, location.toString() );
    DbConfig result   = new DbConfig();
    // we're using mysql mode on h2: because we can ;-)
    result.setDb( Database.mysql );
    result.setUrl( url );
    return result;
  }
  
  @Test
  public void queryDb() throws Exception {
    
    List<String>  expectedTables = Arrays.asList( "countries", "departments", "employees", "job_history", "jobs", "locations", "regions" );
    int[]         expectedCounts = new int[]    { 25,          27,            50,          10,            19,     23,          4         };
     
    try( DbConnection connection = new DbConnection( newDbConfig( "test", "old/mysql/script.sql" ) ) ) {
      
      List<String> tables = connection.listTables();
      assertNotNull( tables );
      assertThat( tables.size(), is(7) );
      assertThat( tables, is( expectedTables ) );

      for( int i = 0; i< expectedTables.size(); i++ ) {
        assertThat( connection.count( expectedTables.get(i) ), is( expectedCounts[i] ) );
      }

      List<String> columns = connection.listColumnNames( "departments" );
      assertNotNull( columns );
      assertThat( columns, is( Arrays.asList( "DepartmentID", "DepartmentName", "ManagerID", "LocationID" )) );
      
    }
    
  }

  @Test
  public void selectAll() throws Exception {
    
    List<String> expectedCountries = Arrays.asList(
      "Argentina"   , "Australia"       , "Belgium"                   , "Brazil"  , "Canada"      ,
      "Switzerland" , "China"           , "Germany"                   , "Denmark" , "Egypt"       ,
      "France"      , "HongKong"        , "Israel"                    , "India"   , "Italy"       ,
      "Japan"       , "Kuwait"          , "Mexico"                    , "Nigeria" , "Netherlands" ,
      "Singapore"   , "United Kingdom"  , "United States of America"  , "Zambia"  , "Zimbabwe"
    );
     
    try( DbConnection connection = new DbConnection( newDbConfig( "test", "old/mysql/script.sql" ) ) ) {
      
      List<String> countries = connection.selectAll( "countries", $ -> getString( $, "CountryName" ) );
      assertNotNull( countries );
      assertThat( countries.size(), is(expectedCountries.size()) );
      assertThat( countries, is( expectedCountries ) );

    }
    
  }

  @Test
  public void selectAllDo() throws Exception {
    
    List<String> expectedCountries = Arrays.asList(
      "Argentina"   , "Australia"       , "Belgium"                   , "Brazil"  , "Canada"      ,
      "Switzerland" , "China"           , "Germany"                   , "Denmark" , "Egypt"       ,
      "France"      , "HongKong"        , "Israel"                    , "India"   , "Italy"       ,
      "Japan"       , "Kuwait"          , "Mexico"                    , "Nigeria" , "Netherlands" ,
      "Singapore"   , "United Kingdom"  , "United States of America"  , "Zambia"  , "Zimbabwe"
    );
    
    List<String> countries = new ArrayList<>( expectedCountries.size() );
    try( DbConnection connection = new DbConnection( newDbConfig( "test", "old/mysql/script.sql" ) ) ) {
      
      connection.selectAllDo( "countries", $ -> countries.add( getString( $, "CountryName" ) ) );
      assertNotNull( countries );
      assertThat( countries.size(), is(expectedCountries.size()) );
      assertThat( countries, is( expectedCountries ) );

    }
    
  }

  private static String getString( ResultSet rs, String column ) {
    try {
      return rs.getString( column );
    } catch( Exception ex ) {
      throw KclException.wrap( ex );
    }
  }

} /* ENDCLASS */
