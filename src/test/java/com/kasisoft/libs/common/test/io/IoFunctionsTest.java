package com.kasisoft.libs.common.test.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.test.*;
import com.kasisoft.libs.common.test.constants.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import java.util.stream.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * Tests for the class 'IoFunctions'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@TestMethodOrder(OrderAnnotation.class)
public class IoFunctionsTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(IoFunctionsTest.class);

    private static final String CONTENT_FOR_STREAMS =
        """
        Dies ist mein Text mit Umlauten.
        Größer wird der Kram hier aber auch nicht.
        Auf dieser Zeile liest man nur Quark.
        """;

    private static final String CONTENT_FOR_READERS =
        """
        Kleener Text wirkt abgehezt.
        Drum isses schön sich auszuruhen.
        Huppifluppi ist ein ß.
        """;

    public static Stream<Arguments> data_compileFileSystemPattern() {
        return Stream.of(
            Arguments.of("*", "([^/]+)"),
            Arguments.of("**", "(.+)"),
            Arguments.of("dir/**", "\\Qdir/\\E(.+)"),
            Arguments.of("dir/*", "\\Qdir/\\E([^/]+)"),
            Arguments.of("dir/*/subdir", "\\Qdir/\\E([^/]+)\\Q/subdir\\E")
        );
    }

    @ParameterizedTest
    @MethodSource("data_compileFileSystemPattern")
    @Order(10)
    public void compileFileSystemPattern(String pattern, String regex) {
        var p = IoFunctions.compileFilesystemPattern(pattern);
        assertThat(p.pattern(), is(regex));
    }

    @Test
    @Order(20)
    public void copy__Streams() throws Exception {
        var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        var bytein  = new ByteArrayInputStream(asBytes);
        var byteout = new ByteArrayOutputStream();
        IoFunctions.copy(bytein, byteout);
        assertThat(byteout.toByteArray(), is(asBytes));
    }

    @Test
    @Order(30)
    public void copy__Streams__WithBlockSizeSmall() throws Exception {
        var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        var bytein  = new ByteArrayInputStream(asBytes);
        var byteout = new ByteArrayOutputStream();
        IoFunctions.copy(bytein, byteout, 8);
        assertThat(byteout.toByteArray(), is(asBytes));
    }

    @Test
    @Order(40)
    public void copy__Streams__WithBlockSizeLarge() throws Exception {
        var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        var bytein  = new ByteArrayInputStream(asBytes);
        var byteout = new ByteArrayOutputStream();
        IoFunctions.copy(bytein, byteout, 16384);
        assertThat(byteout.toByteArray(), is(asBytes));
    }

    @Test
    @Order(50)
    public void copy__CharStreams() throws Exception {
        var reader = new StringReader(CONTENT_FOR_READERS);
        var writer = new StringWriter();
        IoFunctions.copy(reader, writer);
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    @Test
    @Order(60)
    public void copy__CharStreams__WithBlockSizeSmall() throws Exception {
        var reader = new StringReader(CONTENT_FOR_READERS);
        var writer = new StringWriter();
        IoFunctions.copy(reader, writer, 8);
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    @Test
    @Order(70)
    public void copy__CharStreams__WithBlockSizeLarge() throws Exception {
        var reader = new StringReader(CONTENT_FOR_READERS);
        var writer = new StringWriter();
        IoFunctions.copy(reader, writer, 16384);
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    @Test
    @Order(80)
    public void copyFile() {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        var file1   = TEST_RESOURCES.getTempPath("text79.txt");
        IoFunctions.copyFile(httpXsd, file1);
        assertThat(IoSupportFunctions.loadAllBytes(file1), is(IoSupportFunctions.loadAllBytes(httpXsd)));
        assertTrue(Files.isRegularFile(httpXsd));
    }

    @Test
    @Order(90)
    public void copyFile__MissingSource() {
        assertThrows(KclException.class, () -> {
            var file1 = TEST_RESOURCES.getTempPath("text80.txt");
            var file2 = TEST_RESOURCES.getTempPath("text82.txt");
            IoFunctions.copyFile(file1, file2);
        });
    }

    @Test
    @Order(100)
    public void moveFile() {

        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        var file1   = TEST_RESOURCES.getTempPath("text83.txt");
        var file2   = TEST_RESOURCES.getTempPath("text84.txt");
        IoFunctions.copyFile(httpXsd, file1);

        IoFunctions.moveFile(file1, file2);

        assertFalse(Files.isRegularFile(file1));
        assertTrue(Files.isRegularFile(file2));

        assertThat(IoSupportFunctions.loadAllBytes(file2), is(IoSupportFunctions.loadAllBytes(httpXsd)));
    }

    @Test
    @Order(110)
    public void moveFile__MissingSource() {
        assertThrows(KclException.class, () -> {
            var file1 = TEST_RESOURCES.getTempPath("text85.txt");
            var file2 = TEST_RESOURCES.getTempPath("text86.txt");
            IoFunctions.moveFile(file1, file2);
        });
    }

    @Test
    @Order(120)
    public void mkDirs() {

        var dir1 = TEST_RESOURCES.getTempPath("text87");
        assertFalse(Files.isDirectory(dir1));
        IoFunctions.mkDirs(dir1);
        assertTrue(Files.isDirectory(dir1));

        var dir2 = TEST_RESOURCES.getTempPath("text88/folder1/folder2/folder3");
        assertFalse(Files.isDirectory(dir2));
        IoFunctions.mkDirs(dir2);
        assertTrue(Files.isDirectory(dir2));

    }

    @Test
    @Order(130)
    public void gzip() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        var file1   = TEST_RESOURCES.getTempPath("text73.xsd");
        Files.copy(httpXsd, file1);
        var gzipped = IoFunctions.gzip(file1);
        assertNotNull(gzipped);
        assertTrue(Files.isRegularFile(gzipped));
    }

    @Test
    @Order(140)
    public void ungzip() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text73.xsd");
        var file3 = TEST_RESOURCES.getTempPath("text74.xsd.gz");
        Files.copy(file1.toAbsolutePath().getParent().resolve(file1.getFileName().toString() + ".gz"), file3);
        var ungzipped = IoFunctions.ungzip(file3);
        assertNotNull(ungzipped);
        assertThat(IoSupportFunctions.loadAllBytes(file1), is(IoSupportFunctions.loadAllBytes(ungzipped)));
    }

    @Test
    @Order(150)
    public void gzip__WithDestination() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        var file1   = TEST_RESOURCES.getTempPath("text75.xsd");
        var file2   = TEST_RESOURCES.getTempPath("text76.xsd.gz");
        Files.copy(httpXsd, file1);
        var gzipped = IoFunctions.gzip(file1, file2);
        assertNotNull(gzipped);
        assertThat(gzipped.normalize(), is(file2.normalize()));
        assertTrue(Files.isRegularFile(gzipped));
    }

    @Test
    @Order(160)
    public <T> void ungzip__WithDestination() {
        var httpXsd   = TEST_RESOURCES.getResource("http.xsd");
        var file1     = TEST_RESOURCES.getTempPath("text76.xsd.gz");
        var file2     = TEST_RESOURCES.getTempPath("text77.xsd");
        var ungzipped = IoFunctions.ungzip(file1, file2);
        assertNotNull(ungzipped);
        assertThat(ungzipped.normalize(), is(file2.normalize()));
        assertTrue(Files.isRegularFile(ungzipped));
        assertThat(IoSupportFunctions.loadAllBytes(httpXsd), is(IoSupportFunctions.loadAllBytes(file2)));
    }

    @Test
    @Order(170)
    public void gzip__Inline() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        var file1   = TEST_RESOURCES.getTempPath("text78");
        Files.copy(httpXsd, file1);
        var gzipped = IoFunctions.gzip(file1, file1);
        assertNotNull(gzipped);
        assertThat(gzipped.normalize(), is(file1.normalize()));
        assertTrue(Files.isRegularFile(gzipped));
    }

    @Test
    @Order(180)
    public void ungzip__Inline() throws Exception {
        var httpXsd   = TEST_RESOURCES.getResource("http.xsd");
        var file1     = TEST_RESOURCES.getTempPath("text78");
        var ungzipped = IoFunctions.ungzip(file1, file1);
        assertNotNull(ungzipped);
        assertThat(ungzipped.normalize(), is(file1.normalize()));
        assertTrue(Files.isRegularFile(ungzipped));
        assertThat(IoSupportFunctions.loadAllBytes(ungzipped), is(IoSupportFunctions.loadAllBytes(httpXsd)));
    }

    @Test
    @Order(190)
    public void zip() {
        var dir     = TEST_RESOURCES.getResource("simpleton");
        var zipFile = TEST_RESOURCES.getTempPath("zipped89.zip");
        assertFalse(Files.isRegularFile(zipFile));
        IoFunctions.zip(zipFile, dir, null);
        assertTrue(Files.isRegularFile(zipFile));
    }

    @Test
    @Order(200)
    public void unzip() {
        var dir      = TEST_RESOURCES.getResource("simpleton");
        var zipFile  = TEST_RESOURCES.getTempPath("zipped89.zip");
        var unpacked = TEST_RESOURCES.getTempPath("dir90");
        IoFunctions.unzip(zipFile, unpacked);
        var filesFromOrigin   = IoFunctions.listPathes(dir);
        var filesFromUnpacked = IoFunctions.listPathes(unpacked);
        assertThat(filesFromUnpacked, is(filesFromOrigin));
    }

    @Test
    @Order(210)
    public void findExistingPath() {

        var file1   = TEST_RESOURCES.getResource("folder1/folder2/folder3/myfile.txt");
        var result1 = IoFunctions.findExistingPath(file1);
        assertTrue(result1.isPresent());
        assertThat(result1.get().getFileName().toString(), is("folder3"));

        var file2   = TEST_RESOURCES.getResource("folder1/folder2/folder4/myfile.txt");
        var result2 = IoFunctions.findExistingPath(file2);
        assertTrue(result2.isPresent());
        assertThat(result2.get().getFileName().toString(), is("folder2"));

        var file3   = Paths.get("Q:/bibo/simpleton/dodo.txt");
        var result3 = IoFunctions.findExistingPath(file3);
        assertFalse(result3.isPresent());

    }

    @Test
    @Order(220)
    public void listPathes() {
        var dir      = TEST_RESOURCES.getResource("simpleton");
        var files    = IoFunctions.listPathes(dir, null, true);
        var expected = Arrays.asList("file1.txt", "file2.txt", "folder1/", "folder1/file1.txt", "folder1/file2.txt", "folder1/file3.txt", "folder1/subfolder1/", "folder1/subfolder1/file1.txt", "folder1/subfolder1/file2.txt", "folder1/subfolder1/subsubfolder1/", "folder1/subfolder1/subsubfolder1/file1.txt", "folder1/subfolder1/subsubfolder1/file2.png", "folder1/subfolder1/subsubfolder1/file3.tif", "folder1/subfolder1/subsubfolder1/file4.jpg", "folder1/subfolder2/", "folder1/subfolder2/file4.txt", "folder1/subfolder2/file5.txt", "folder2/", "folder2/file1.txt", "folder2/file2.jpg", "folder2/file3.mp4", "folder3/", "folder3/file1.txt");
        assertThat(files, is(expected));
    }

    @Test
    @Order(230)
    public void listZipFile() {
        var zipFile  = TEST_RESOURCES.getResource("simpleton.zip");
        var files    = IoFunctions.listZipFile(zipFile);
        var expected = Arrays.asList("simpleton/", "simpleton/file1.txt", "simpleton/file2.txt", "simpleton/folder1/", "simpleton/folder1/file1.txt", "simpleton/folder1/file2.txt", "simpleton/folder1/file3.txt", "simpleton/folder1/subfolder1/", "simpleton/folder1/subfolder1/file1.txt", "simpleton/folder1/subfolder1/file2.txt", "simpleton/folder1/subfolder1/subsubfolder1/", "simpleton/folder1/subfolder1/subsubfolder1/file1.txt", "simpleton/folder1/subfolder1/subsubfolder1/file2.png", "simpleton/folder1/subfolder1/subsubfolder1/file3.tif", "simpleton/folder1/subfolder1/subsubfolder1/file4.jpg", "simpleton/folder1/subfolder2/", "simpleton/folder1/subfolder2/file4.txt", "simpleton/folder1/subfolder2/file5.txt", "simpleton/folder2/", "simpleton/folder2/file1.txt", "simpleton/folder2/file2.jpg", "simpleton/folder2/file3.mp4", "simpleton/folder3/", "simpleton/folder3/file1.txt");
        assertThat(files, is(expected));
    }

    @Test
    @Order(240)
    public void listZipFile__FilterDirectories() {
        var zipFile  = TEST_RESOURCES.getResource("simpleton.zip");
        var files    = IoFunctions.listZipFile(zipFile, null, $ -> !$.isDirectory());
        var expected = Arrays.asList("simpleton/file1.txt", "simpleton/file2.txt", "simpleton/folder1/file1.txt", "simpleton/folder1/file2.txt", "simpleton/folder1/file3.txt", "simpleton/folder1/subfolder1/file1.txt", "simpleton/folder1/subfolder1/file2.txt", "simpleton/folder1/subfolder1/subsubfolder1/file1.txt", "simpleton/folder1/subfolder1/subsubfolder1/file2.png", "simpleton/folder1/subfolder1/subsubfolder1/file3.tif", "simpleton/folder1/subfolder1/subsubfolder1/file4.jpg", "simpleton/folder1/subfolder2/file4.txt", "simpleton/folder1/subfolder2/file5.txt", "simpleton/folder2/file1.txt", "simpleton/folder2/file2.jpg", "simpleton/folder2/file3.mp4", "simpleton/folder3/file1.txt");
        assertThat(files, is(expected));
    }

    @Test
    @Order(250)
    public void locateDirectory() throws Exception {
        var dir1 = IoFunctions.locateDirectory(Iso3166Test.class);
        assertNotNull(dir1);
        var classFile = dir1.resolve("com/kasisoft/libs/common/test/constants/Iso3166Test.class");
        assertTrue(Files.isRegularFile(classFile));
    }

} /* ENDCLASS */
