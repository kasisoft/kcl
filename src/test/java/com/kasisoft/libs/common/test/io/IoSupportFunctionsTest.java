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

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.util.stream.*;

import java.util.*;

import java.nio.file.*;

import java.net.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
@TestMethodOrder(OrderAnnotation.class)
@SuppressWarnings("unchecked")
public class IoSupportFunctionsTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(IoSupportFunctionsTest.class);

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

    private static final String CONTENT_HTTP_XSD  =
      """
      <?xml version="1.0" encoding="UTF-8" ?>
      <schema xmlns="http://www.w3.org/2001/XMLSchema"
              xmlns:http="http://schemas.xmlsoap.org/wsdl/http/"
              xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
              targetNamespace="http://schemas.xmlsoap.org/wsdl/http/">
         <import namespace = "http://schemas.xmlsoap.org/wsdl/"/>
         <element name="address" type="http:addressType"/>
         <complexType name="addressType">
             <complexContent>
             <extension base="wsdl:tExtensibilityElement">
                 <sequence/>
                 <attribute name="location" type="anyURI" use="required"/>
             </extension>
             </complexContent>
         </complexType>
      </schema>
      """;

    private static IoSupport<Path>     ioPath;
    private static IoSupport<File>     ioFile;
    private static IoSupport<URI>      ioURI;
    private static IoSupport<URL>      ioURL;

    @BeforeAll
    public static void setup() {
        ioPath = IoSupportFunctions.ioSupport(Path.class);
        ioFile = IoSupportFunctions.ioSupport(File.class);
        ioURI  = IoSupportFunctions.ioSupport(URI.class);
        ioURL  = IoSupportFunctions.ioSupport(URL.class);
    }

    public static Stream<Arguments> data_forOutputStream() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text22.txt");
        var file2 = TEST_RESOURCES.getTempPath("text23.txt");
        var file3 = TEST_RESOURCES.getTempPath("text24.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStream")
    @Order(10)
    public <T> void forOutputStream__Error(IoSupport<T> ioSupport, T destination) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forOutputStream(destination, $out -> {
                throw new KclException("error");
            });
        });
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStream")
    @Order(20)
    public <T> void forOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
        var asBytes   = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        var result    = ioSupport.forOutputStream(destination, $out -> {
            try {
                $out.write(asBytes);
                return "DUMMY";
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
                return null;
            }
        });
        assertThat(result, is("DUMMY"));
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStream")
    @Order(30)
    public <T> void forInputStream(IoSupport<T> ioSupport, T source) throws Exception {
        var byteout = new ByteArrayOutputStream();
        var result  = ioSupport.forInputStream(source, $in -> {
            IoFunctions.copy($in, byteout);
            return "DUMMY";
        });
        var asText  = Encoding.UTF8.decode(byteout.toByteArray());
        assertThat(asText, is(CONTENT_FOR_STREAMS));
        assertThat(result, is("DUMMY"));
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStream")
    @Order(40)
    public <T> void forInputStream__Error(IoSupport<T> ioSupport, T source) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forInputStream(source, $in -> {
                throw new KclException("error");
            });
        });
    }

    public static Stream<Arguments> data_forOutputStreamDo() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text16.txt");
        var file2 = TEST_RESOURCES.getTempPath("text17.txt");
        var file3 = TEST_RESOURCES.getTempPath("text18.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStreamDo")
    @Order(50)
    public <T> void forOutputStreamDo(IoSupport<T> ioSupport, T destination) throws Exception {
        var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        ioSupport.forOutputStreamDo(destination, $out -> {
            try {
                $out.write(asBytes);
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
            }
        });
    }

    @ParameterizedTest
    @MethodSource("data_forOutputStreamDo")
    @Order(60)
    public <T> void forInputStreamDo(IoSupport<T> ioSupport, T source) throws Exception {
        var byteout = new ByteArrayOutputStream();
        ioSupport.forInputStreamDo(source, $in -> IoFunctions.copy($in, byteout));
        var asText = Encoding.UTF8.decode(byteout.toByteArray());
        assertThat(asText, is(CONTENT_FOR_STREAMS));
    }

    public static Stream<Arguments> data_forWriterDo() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text28.txt");
        var file2 = TEST_RESOURCES.getTempPath("text29.txt");
        var file3 = TEST_RESOURCES.getTempPath("text30.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
  }

    @ParameterizedTest
    @MethodSource("data_forWriterDo")
    @Order(70)
    public <T> void forWriterDo(IoSupport<T> ioSupport, T destination) throws Exception {
        ioSupport.forWriterDo(destination, $writer -> {
            try {
                $writer.write(CONTENT_FOR_READERS);
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
            }
        });
    }

    @ParameterizedTest
    @MethodSource("data_forWriterDo")
    @Order(80)
    public <T> void forWriterDo__Error(IoSupport<T> ioSupport, T destination) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forWriterDo(destination, $writer -> {
                throw new KclException("error");
            });
        });
    }

    @ParameterizedTest
    @MethodSource("data_forWriterDo")
    @Order(81)
    public <T> void forReaderDo__Error(IoSupport<T> ioSupport, T source) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forReaderDo(source, $ -> {
                throw new KclException("error");
            });
        });
    }

    public static Stream<Arguments> data_newOutputStream() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text1.txt");
        var file2 = TEST_RESOURCES.getTempPath("text2.txt");
        var file3 = TEST_RESOURCES.getTempPath("text3.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_newOutputStream")
    @Order(90)
    public <T> void newOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
        var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
        try (var outstream = ioSupport.newOutputStream(destination)) {
            outstream.write(asBytes);
        }
    }

    @ParameterizedTest
    @MethodSource("data_newOutputStream")
    @Order(100)
    public <T> void newInputStream(IoSupport<T> ioSupport, T source) throws Exception {
        var byteout = new ByteArrayOutputStream();
        try (var instream = ioSupport.newInputStream(source)) {
            IoFunctions.copy(instream, byteout);
        }
        var asText = Encoding.UTF8.decode(byteout.toByteArray());
        assertThat(asText, is(CONTENT_FOR_STREAMS));
    }

    public static Stream<Arguments> data_newWriter() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text4.txt");
        var file2 = TEST_RESOURCES.getTempPath("text5.txt");
        var file3 = TEST_RESOURCES.getTempPath("text6.txt");
        var file4 = TEST_RESOURCES.getTempPath("text7.txt");
        var file5 = TEST_RESOURCES.getTempPath("text8.txt");
        var file6 = TEST_RESOURCES.getTempPath("text9.txt");
        return Stream.of(
            Arguments.of(ioPath, file1, Encoding.ISO88591),
            Arguments.of(ioPath, file2, Encoding.UTF8),
            Arguments.of(ioFile, file3.toFile(), Encoding.ISO88591),
            Arguments.of(ioFile, file4.toFile(), Encoding.UTF8),
            Arguments.of(ioURI, file5.toUri(), Encoding.ISO88591),
            Arguments.of(ioURI, file6.toUri(), Encoding.UTF8)
        );
    }

    @ParameterizedTest
    @MethodSource("data_newWriter")
    @Order(110)
    public <T> void newWriter(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
        try (var writer = ioSupport.newWriter(destination)) {
            writer.write(CONTENT_FOR_READERS);
        }
    }

    @ParameterizedTest
    @MethodSource("data_newWriter")
    @Order(120)
    public <T> void newReader(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
        var writer = new StringWriter();
        try (var reader = ioSupport.newReader(destination)) {
            IoFunctions.copy(reader, writer);
        }
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    public static Stream<Arguments> data_newWriter__WithEncoding() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text10.txt");
        var file2 = TEST_RESOURCES.getTempPath("text11.txt");
        var file3 = TEST_RESOURCES.getTempPath("text12.txt");
        var file4 = TEST_RESOURCES.getTempPath("text13.txt");
        var file5 = TEST_RESOURCES.getTempPath("text14.txt");
        var file6 = TEST_RESOURCES.getTempPath("text15.txt");
        return Stream.of(
            Arguments.of(ioPath, file1, Encoding.ISO88591),
            Arguments.of(ioPath, file2, Encoding.UTF8),
            Arguments.of(ioFile, file3.toFile(), Encoding.ISO88591),
            Arguments.of(ioFile, file4.toFile(), Encoding.UTF8),
            Arguments.of(ioURI, file5.toUri(), Encoding.ISO88591),
            Arguments.of(ioURI, file6.toUri(), Encoding.UTF8)
        );
    }

    @ParameterizedTest
    @MethodSource("data_newWriter__WithEncoding")
    @Order(130)
    public <T> void newWriter__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
        try (var writer = ioSupport.newWriter(destination, encoding)) {
            writer.write(CONTENT_FOR_READERS);
        }
    }

    @ParameterizedTest
    @MethodSource("data_newWriter__WithEncoding")
    @Order(140)
    public <T> void newReader__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
        var writer = new StringWriter();
        try (var reader = ioSupport.newReader(destination, encoding)) {
            IoFunctions.copy(reader, writer);
        }
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    public static Stream<Arguments> data_forReaderDo() throws Exception {
        var file1 = TEST_RESOURCES.getFile("text34.txt");
        var file2 = TEST_RESOURCES.getFile("text35.txt");
        var file3 = TEST_RESOURCES.getFile("text36.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_forReaderDo")
    @Order(150)
    public <T> void forReaderDo(IoSupport<T> ioSupport, T source) throws Exception {
        var writer = new StringWriter();
        ioSupport.forReaderDo(source, $ -> IoFunctions.copy($, writer));
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    public static Stream<Arguments> data_forWriter() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text34.txt");
        var file2 = TEST_RESOURCES.getTempPath("text35.txt");
        var file3 = TEST_RESOURCES.getTempPath("text36.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_forWriter")
    @Order(160)
    public <T> void forWriter__Error(IoSupport<T> ioSupport, T destination) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forWriter(destination, $ -> {
                throw new KclException("error");
            });
        });
    }

    @ParameterizedTest
    @MethodSource("data_forWriter")
    @Order(170)
    public <T> void forWriter(IoSupport<T> ioSupport, T destination) throws Exception {
        var result = ioSupport.forWriter(destination, $ -> {
            try {
                $.write(CONTENT_FOR_READERS);
                return "DUMMY";
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
                return null;
            }
        });
        assertThat(result, is("DUMMY"));
    }

    @ParameterizedTest
    @MethodSource("data_forWriter")
    @Order(180)
    public <T> void forReader(IoSupport<T> ioSupport, T source) throws Exception {
        var writer = new StringWriter();
        var result = ioSupport.forReader(source, $ -> {
            IoFunctions.copy($, writer);
            return "DUMMY";
        });
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
        assertThat(result, is("DUMMY"));
    }

    @ParameterizedTest
    @MethodSource("data_forWriter")
    @Order(190)
    public <T> void forReader__Error(IoSupport<T> ioSupport, T source) throws Exception {
        assertThrows(KclException.class, () -> {
            ioSupport.forReader(source, $ -> {
                throw new KclException("error");
            });
        });
    }

    public static Stream<Arguments> data_forWriterDo__WithEncoding() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text40.txt");
        var file2 = TEST_RESOURCES.getTempPath("text41.txt");
        var file3 = TEST_RESOURCES.getTempPath("text42.txt");
        var file4 = TEST_RESOURCES.getTempPath("text43.txt");
        var file5 = TEST_RESOURCES.getTempPath("text44.txt");
        var file6 = TEST_RESOURCES.getTempPath("text45.txt");
        return Stream.of(
            Arguments.of(ioPath, file1, Encoding.UTF8),
            Arguments.of(ioPath, file2, Encoding.ISO88591),
            Arguments.of(ioFile, file3.toFile(), Encoding.UTF8),
            Arguments.of(ioFile, file4.toFile(), Encoding.ISO88591),
            Arguments.of(ioURI, file5.toUri(), Encoding.UTF8),
            Arguments.of(ioURI, file6.toUri(), Encoding.ISO88591)
        );
    }

    @ParameterizedTest
    @MethodSource("data_forWriterDo__WithEncoding")
    @Order(200)
    public <T> void forWriterDo__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
        ioSupport.forWriterDo(destination, encoding, $ -> {
            try {
                $.write(CONTENT_FOR_READERS);
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
            }
        });
    }

    @ParameterizedTest
    @MethodSource("data_forWriterDo__WithEncoding")
    @Order(210)
    public <T> void forReaderDo__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
        var writer = new StringWriter();
        ioSupport.forReaderDo(source, encoding, $ -> IoFunctions.copy($, writer));
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    }

    public static Stream<Arguments> data_forWriter__WithEncoding() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text52.txt");
        var file2 = TEST_RESOURCES.getTempPath("text53.txt");
        var file3 = TEST_RESOURCES.getTempPath("text54.txt");
        var file4 = TEST_RESOURCES.getTempPath("text55.txt");
        var file5 = TEST_RESOURCES.getTempPath("text56.txt");
        var file6 = TEST_RESOURCES.getTempPath("text57.txt");
        return Stream.of(
            Arguments.of(ioPath, file1, Encoding.UTF8),
            Arguments.of(ioFile, file2.toFile(), Encoding.ISO88591),
            Arguments.of(ioURI, file3.toUri(), Encoding.UTF8),
            Arguments.of(ioPath, file4, Encoding.ISO88591),
            Arguments.of(ioFile, file5.toFile(), Encoding.UTF8),
            Arguments.of(ioURI, file6.toUri(), Encoding.ISO88591)
        );
    }

    @ParameterizedTest
    @MethodSource("data_forWriter__WithEncoding")
    @Order(220)
    public <T> void forWriter__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
        var result = ioSupport.forWriter(destination, encoding, $ -> {
            try {
                $.write(CONTENT_FOR_READERS);
                return "DUMMY";
            } catch (Exception ex) {
                fail(ex.getLocalizedMessage());
                return null;
            }
        });
        assertThat(result, is("DUMMY"));
    }

    @ParameterizedTest
    @MethodSource("data_forWriter__WithEncoding")
    @Order(230)
    public <T> void forReader__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
        var writer = new StringWriter();
        var result = ioSupport.forReader(source, encoding, $ -> {
            IoFunctions.copy($, writer);
            return "DUMMY";
        });
        assertThat(writer.toString(), is(CONTENT_FOR_READERS));
        assertThat(result, is("DUMMY"));
    }


    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_loadBytes() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        return Stream.of(
            Arguments.of(ioPath, httpXsd),
            Arguments.of(ioFile, httpXsd.toFile()),
            Arguments.of(ioURI, httpXsd.toUri()),
            Arguments.of(ioURL, httpXsd.toFile().toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_loadBytes")
    @Order(240)
    public <T> void loadBytes(IoSupport<T> ioSupport, T source) {
        var expected = Encoding.UTF8.encode("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        var loaded   = ioSupport.loadBytes(source, 39);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadBytes")
    @Order(250)
    public <T> void loadBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = Encoding.UTF8.encode("version=\"1.0\"");
        var loaded   = ioSupport.loadBytes(source, 6, 13);
        assertThat(loaded, is(expected));
    }

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_loadChars() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        return Stream.of(
            Arguments.of(ioPath, httpXsd),
            Arguments.of(ioFile, httpXsd.toFile()),
            Arguments.of(ioURI, httpXsd.toUri()),
            Arguments.of(ioURL, httpXsd.toFile().toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_loadChars")
    @Order(260)
    public <T> void loadChars(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
        var loaded   = ioSupport.loadChars(source, 39);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadChars")
    @Order(270)
    public <T> void loadChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = "version=\"1.0\"".toCharArray();
        var loaded   = ioSupport.loadChars(source, 6, 13);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadChars")
    @Order(280)
    public <T> void loadChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
        var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 39);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadChars")
    @Order(290)
    public <T> void loadChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = "version=\"1.0\"".toCharArray();
        var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 6, 13);
        assertThat(loaded, is(expected));
    }

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_loadAllBytes() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        return Stream.of(
            Arguments.of(ioPath, httpXsd),
            Arguments.of(ioFile, httpXsd.toFile()),
            Arguments.of(ioURI, httpXsd.toUri()),
            Arguments.of(ioURL, httpXsd.toFile().toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_loadAllBytes")
    @Order(300)
    public <T> void loadAllBytes(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
        var loaded   = ioSupport.loadAllBytes(source);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadAllBytes")
    @Order(310)
    public <T> void loadAllBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD.substring(40));
        var loaded   = ioSupport.loadAllBytes(source, 40);
        assertThat(loaded, is(expected));
    }

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_loadAllChars() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        return Stream.of(
            Arguments.of(ioPath, httpXsd),
            Arguments.of(ioFile, httpXsd.toFile()),
            Arguments.of(ioURI, httpXsd.toUri()),
            Arguments.of(ioURL, httpXsd.toFile().toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_loadAllChars")
    @Order(320)
    public <T> void loadAllChars(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = CONTENT_HTTP_XSD.toCharArray();
        var loaded   = ioSupport.loadAllChars(source);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadAllChars")
    @Order(330)
    public <T> void loadAllChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
        var loaded   = ioSupport.loadAllChars(source, 40);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadAllChars")
    @Order(340)
    public <T> void loadAllChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = CONTENT_HTTP_XSD.toCharArray();
        var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591);
        assertThat(loaded, is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_loadAllChars")
    @Order(350)
    public <T> void loadAllChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
        var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
        var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591, 40);
        assertThat(loaded, is(expected));
    }

    public static Stream<Arguments> data_saveBytes() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text64.txt");
        var file2 = TEST_RESOURCES.getTempPath("text65.txt");
        var file3 = TEST_RESOURCES.getTempPath("text66.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_saveBytes")
    @Order(360)
    public <T> void saveBytes(IoSupport<T> ioSupport, @NotNull T destination) {
        var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
        ioSupport.saveBytes(destination, toSave);
        var loaded = ioSupport.loadAllBytes(destination);
        assertNotNull(loaded);
        assertThat(loaded, is(toSave));
    }

    public static Stream<Arguments> data_saveBytes__WithOffset__WithSize() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text91.txt");
        var file2 = TEST_RESOURCES.getTempPath("text92.txt");
        var file3 = TEST_RESOURCES.getTempPath("text93.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_saveBytes__WithOffset__WithSize")
    @Order(370)
    public <T> void saveBytes__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) throws Exception {
        var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
        ioSupport.saveBytes(destination, toSave, 20, 100);
        var loaded = ioSupport.loadAllBytes(destination);
        assertNotNull(loaded);
        assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
    }

    public static Stream<Arguments> data_saveChars() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text67.txt");
        var file2 = TEST_RESOURCES.getTempPath("text68.txt");
        var file3 = TEST_RESOURCES.getTempPath("text69.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_saveChars")
    @Order(380)
    public <T> void saveChars(IoSupport<T> ioSupport, @NotNull T destination) {
        var toSave = CONTENT_HTTP_XSD.toCharArray();
        ioSupport.saveChars(destination, toSave);
        var loaded = ioSupport.loadAllChars(destination);
        assertNotNull(loaded);
        assertThat(loaded, is(toSave));
    }

    @ParameterizedTest
    @MethodSource("data_saveChars")
    @Order(390)
    public <T> void saveChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T destination) {
        var toSave = CONTENT_HTTP_XSD.toCharArray();
        ioSupport.saveChars(destination, Encoding.ISO88591, toSave);
        var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
        assertNotNull(loaded);
        assertThat(loaded, is(toSave));
    }

    public static Stream<Arguments> data_saveChars__WithEncoding__WithOffset__WithSize() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text94.txt");
        var file2 = TEST_RESOURCES.getTempPath("text95.txt");
        var file3 = TEST_RESOURCES.getTempPath("text96.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_saveChars__WithEncoding__WithOffset__WithSize")
    @Order(400)
    public <T> void saveChars__WithEncoding__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) {
        var toSave = CONTENT_HTTP_XSD.toCharArray();
        ioSupport.saveChars(destination, Encoding.ISO88591, toSave, 20, 100);
        var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
        assertNotNull(loaded);
        assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
    }

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_readText() throws Exception {
        var httpXsd = TEST_RESOURCES.getResource("http.xsd");
        return Stream.of(
            Arguments.of(ioPath, httpXsd),
            Arguments.of(ioFile, httpXsd.toFile()),
            Arguments.of(ioURI, httpXsd.toUri()),
            Arguments.of(ioURL, httpXsd.toFile().toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_readText")
    @Order(410)
    public <T> void readText(IoSupport<T> ioSupport, @NotNull T source) {
        var read = ioSupport.readText(source);
        assertThat(read, is(CONTENT_HTTP_XSD));
    }

    @ParameterizedTest
    @MethodSource("data_readText")
    @Order(420)
    public <T> void readText__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
        String read = ioSupport.readText(source, Encoding.ISO88591);
        assertThat(read, is(CONTENT_HTTP_XSD));
    }

    public static Stream<Arguments> data_writeText() throws Exception {
        var file1 = TEST_RESOURCES.getTempPath("text70.txt");
        var file2 = TEST_RESOURCES.getTempPath("text71.txt");
        var file3 = TEST_RESOURCES.getTempPath("text72.txt");
        return Stream.of(
            Arguments.of(ioPath, file1),
            Arguments.of(ioFile, file2.toFile()),
            Arguments.of(ioURI, file3.toUri())
        );
    }

    @ParameterizedTest
    @MethodSource("data_writeText")
    @Order(430)
    public <T> void writeText(IoSupport<T> ioSupport, T destination) {
        ioSupport.writeText(destination, CONTENT_HTTP_XSD);
        var read = ioSupport.readText(destination);
        assertThat(read, is(CONTENT_HTTP_XSD));
    }

    @ParameterizedTest
    @MethodSource("data_writeText")
    @Order(440)
    public <T> void writeText__WithEncoding(IoSupport<T> ioSupport, T destination) {
        ioSupport.writeText(destination, Encoding.ISO88591, CONTENT_HTTP_XSD);
        var read = ioSupport.readText(destination, Encoding.ISO88591);
        assertThat(read, is(CONTENT_HTTP_XSD));
    }

} /* ENDCLASS */
