package com.kasisoft.libs.common.test.data;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import org.h2.tools.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.data.*;

import java.util.concurrent.atomic.*;

import java.util.*;

import java.nio.file.*;

import java.sql.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class DbConnectionTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(DbConnectionTest.class);

    private static AtomicInteger       firstSetup     = new AtomicInteger(0);
    private Path                       dbLocation;
    private Server                     server;

    @BeforeEach
    public void prepare() throws Exception {
        if (firstSetup.getAndIncrement() == 0) {
            dbLocation = TEST_RESOURCES.getTempPath("tempdir").resolve("h2db");
            IoFunctions.mkDirs(dbLocation);
            assertTrue(Files.isDirectory(dbLocation));
            server = Server.createTcpServer(new String[] {"-tcp", "-baseDir", dbLocation.toString()}).start();
        }
    }

    @AfterEach
    public void shutDown() throws Exception {
        if (firstSetup.decrementAndGet() == 0) {
            if (server != null) {
                server.stop();
            }
            IoFunctions.deleteFile(dbLocation.toFile());
        }
    }

    private List<String> expectedCountries() {
        return Arrays.asList(
            "Argentina",
            "Australia",
            "Belgium",
            "Brazil",
            "Canada",
            "Switzerland",
            "China",
            "Germany",
            "Denmark",
            "Egypt",
            "France",
            "HongKong",
            "Israel",
            "India",
            "Italy",
            "Japan",
            "Kuwait",
            "Mexico",
            "Nigeria",
            "Netherlands",
            "Singapore",
            "United Kingdom",
            "United States of America",
            "Zambia",
            "Zimbabwe"
        );
    }

    private DbConfig newDbConfig(String dbname, String initscript) throws Exception {
        var location = TEST_RESOURCES.getResource(initscript);
        var url      = "jdbc:h2:tcp://localhost/%s/%s;MODE=MySQL;DATABASE_TO_UPPER=false;INIT=runscript from '%s'".formatted(dbLocation.toString(), dbname, location.toString());
        return new DbConfig(url, Database.h2, null, null);
    }

    @Test
    public void queryDb() throws Exception {

        var expectedTables = Arrays.asList("countries", "departments", "employees", "job_history", "jobs", "locations", "regions");
        var expectedCounts = new int[] {25, 27, 50, 10, 19, 23, 4};

        try (var connection = new DbConnection(newDbConfig("test", "script.sql"))) {

            var tables = connection.listTables();
            assertNotNull(tables);
            assertThat(tables.size(), is(7));
            assertThat(tables, is(expectedTables));

            for (int i = 0; i < expectedTables.size(); i++) {
                assertThat(connection.count(expectedTables.get(i)), is(expectedCounts[i]));
            }

            var columns = connection.listColumnNames("departments");
            assertNotNull(columns);
            assertThat(columns, is(Arrays.asList("DepartmentID", "DepartmentName", "ManagerID", "LocationID")));

        }

    }

    @Test
    public void selectAll() throws Exception {
        var expectedCountries = expectedCountries();
        try (var connection = new DbConnection(newDbConfig("test", "script.sql"))) {
            var countries = connection.selectAll("countries", $ -> getString($, "CountryName"));
            assertNotNull(countries);
            assertThat(countries.size(), is(expectedCountries.size()));
            assertThat(countries, is(expectedCountries));
        }
    }

    @Test
    public void selectAllDo() throws Exception {
        var expectedCountries = expectedCountries();
        var countries         = new ArrayList<>(expectedCountries.size());
        try (DbConnection connection = new DbConnection(newDbConfig("test", "script.sql"))) {
            connection.selectAllDo("countries", $ -> countries.add(getString($, "CountryName")));
            assertNotNull(countries);
            assertThat(countries.size(), is(expectedCountries.size()));
            assertThat(countries, is(expectedCountries));
        }

    }

    private String getString(ResultSet rs, String column) {
        try {
            return rs.getString(column);
        } catch (Exception ex) {
            throw KclException.wrap(ex);
        }
    }

} /* ENDCLASS */
