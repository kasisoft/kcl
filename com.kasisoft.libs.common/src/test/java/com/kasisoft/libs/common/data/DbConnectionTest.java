package com.kasisoft.libs.common.data;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import org.h2.tools.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.extern.slf4j.*;

import lombok.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

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
     
    try( DbConnection connection = new DbConnection( newDbConfig( "test", "mysql/script.sql" ) ) ) {
      
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

} /* ENDCLASS */
