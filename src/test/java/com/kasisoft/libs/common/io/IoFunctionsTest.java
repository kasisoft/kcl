package com.kasisoft.libs.common.io;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.MethodOrderer.*;

import org.junit.jupiter.api.*;

import javax.validation.constraints.*;

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

  private static final String CONTENT_FOR_STREAMS = ""
                                                    + "Dies ist mein Text mit Umlauten.\n"
                                                    + "Größer wird der Kram hier aber auch nicht.\n"
                                                    + "Auf dieser Zeile liest man nur Quark.\n"
                                                    ;

  private static final String CONTENT_FOR_READERS = ""
                                                    + "Kleener Text wirkt abgehezt.\n"
                                                    + "Drum isses schön sich auszuruhen.\n"
                                                    + "Huppifluppi ist ein ß.\n"
                                                    ;

  private static final String CONTENT_HTTP_XSD = ""
                                                 + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                                 + "<schema xmlns=\"http://www.w3.org/2001/XMLSchema\"\n"
                                                 + "        xmlns:http=\"http://schemas.xmlsoap.org/wsdl/http/\"\n"
                                                 + "        xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\"\n"
                                                 + "        targetNamespace=\"http://schemas.xmlsoap.org/wsdl/http/\">\n"
                                                 + "   <import namespace = \"http://schemas.xmlsoap.org/wsdl/\"/>\n"
                                                 + "   <element name=\"address\" type=\"http:addressType\"/>\n"
                                                 + "   <complexType name=\"addressType\">\n"
                                                 + "       <complexContent>\n"
                                                 + "       <extension base=\"wsdl:tExtensibilityElement\">\n"
                                                 + "           <sequence/>\n"
                                                 + "           <attribute name=\"location\" type=\"anyURI\" use=\"required\"/>\n"
                                                 + "       </extension>\n"
                                                 + "       </complexContent>\n"
                                                 + "   </complexType>\n"
                                                 + "</schema>\n"
                                                 ;
  
  @SuppressWarnings("exports")
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
  @Order(1)
  public void compileFileSystemPattern(String pattern, String regex) {
    var p = IoFunctions.compileFilesystemPattern(pattern);
    assertThat(p.pattern(), is(regex));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_newOutputStream() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text1.txt");
    var file2 = TEST_RESOURCES.getTempPath("text2.txt");
    var file3 = TEST_RESOURCES.getTempPath("text3.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }
    
  @ParameterizedTest
  @MethodSource("data_newOutputStream")
  @Order(2)
  public <T> void newOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    try (var outstream = ioSupport.newOutputStream(destination)) {
      outstream.write(asBytes);
    }
  }
  
  @ParameterizedTest
  @MethodSource("data_newOutputStream")
  @Order(3)
  public <T> void newInputStream(IoSupport<T> ioSupport, T source) throws Exception {
    var byteout = new ByteArrayOutputStream();
    try (var instream = ioSupport.newInputStream(source)) {
      IoFunctions.copy(instream, byteout);
    }
    var asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_newWriter() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text4.txt");
    var file2 = TEST_RESOURCES.getTempPath("text5.txt");
    var file3 = TEST_RESOURCES.getTempPath("text6.txt");
    var file4 = TEST_RESOURCES.getTempPath("text7.txt");
    var file5 = TEST_RESOURCES.getTempPath("text8.txt");
    var file6 = TEST_RESOURCES.getTempPath("text9.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1, Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_PATH, file2, Encoding.UTF8),
      Arguments.of(IoFunctions.IO_FILE, file3.toFile(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_FILE, file4.toFile(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_URI,  file5.toUri(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_URI,  file6.toUri(), Encoding.UTF8)
    );
  }

  @ParameterizedTest
  @MethodSource("data_newWriter")
  @Order(4)
  public <T> void newWriter(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
    try (var writer = ioSupport.newWriter(destination)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @ParameterizedTest
  @MethodSource("data_newWriter")
  @Order(5)
  public <T> void newReader(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
    var writer = new StringWriter();
    try (var reader = ioSupport.newReader(destination)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_newWriter__WithEncoding() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text10.txt");
    var file2 = TEST_RESOURCES.getTempPath("text11.txt");
    var file3 = TEST_RESOURCES.getTempPath("text12.txt");
    var file4 = TEST_RESOURCES.getTempPath("text13.txt");
    var file5 = TEST_RESOURCES.getTempPath("text14.txt");
    var file6 = TEST_RESOURCES.getTempPath("text15.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1, Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_PATH, file2, Encoding.UTF8),
      Arguments.of(IoFunctions.IO_FILE, file3.toFile(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_FILE, file4.toFile(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_URI,  file5.toUri(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_URI,  file6.toUri(), Encoding.UTF8)
    );
  }

  @ParameterizedTest
  @MethodSource("data_newWriter__WithEncoding")
  @Order(6)
  public <T> void newWriter__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    try (var writer = ioSupport.newWriter(destination, encoding)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @ParameterizedTest
  @MethodSource("data_newWriter__WithEncoding")
  @Order(7)
  public <T> void newReader__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    try (var reader = ioSupport.newReader(destination, encoding)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forOutputStreamDo() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text16.txt");
    var file2 = TEST_RESOURCES.getTempPath("text17.txt");
    var file3 = TEST_RESOURCES.getTempPath("text18.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_forOutputStreamDo")
  @Order(8)
  public <T> void forOutputStreamDo(IoSupport<T> ioSupport, T destination) throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    ioSupport.forOutputStreamDo(destination, $ -> {
      try {
        $.write(asBytes);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }

  @ParameterizedTest
  @MethodSource("data_forOutputStreamDo")
  @Order(9)
  public <T> void forInputStreamDo(IoSupport<T> ioSupport, T source) throws Exception {
    var byteout = new ByteArrayOutputStream();
    ioSupport.forInputStreamDo(source, $ -> {
      IoFunctions.copy($, byteout);
    });
    var asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forOutputStream() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text22.txt");
    var file2 = TEST_RESOURCES.getTempPath("text23.txt");
    var file3 = TEST_RESOURCES.getTempPath("text24.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_forOutputStream")
  @Order(10)
  public <T> void forOutputStream__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forOutputStream(destination, $ -> { throw new KclException("error"); });
    });
  }

  @ParameterizedTest
  @MethodSource("data_forOutputStream")
  @Order(11)
  public <T> void forOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var result  = ioSupport.forOutputStream(destination, $ -> {
      try {
        $.write(asBytes);
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
  @Order(12)
  public <T> void forInputStream(IoSupport<T> ioSupport, T source) throws Exception {
    var byteout = new ByteArrayOutputStream();
    var result  = ioSupport.forInputStream(source, $ -> {
      IoFunctions.copy($, byteout);
      return "DUMMY";
    });
    var asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
    assertThat(result, is("DUMMY"));
  }

  @ParameterizedTest
  @MethodSource("data_forOutputStream")
  @Order(13)
  public <T> void forInputStream__Error(IoSupport<T> ioSupport, T source) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forInputStream(source, $ -> { throw new KclException("error"); });
    });
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forWriterDo() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text28.txt");
    var file2 = TEST_RESOURCES.getTempPath("text29.txt");
    var file3 = TEST_RESOURCES.getTempPath("text30.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forReaderDo() throws Exception {
    var file1 = TEST_RESOURCES.getFile("text34.txt");
    var file2 = TEST_RESOURCES.getFile("text35.txt");
    var file3 = TEST_RESOURCES.getFile("text36.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_forWriterDo")
  @Order(14)
  public <T> void forWriterDo(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forWriterDo(destination, $ -> {
      try {
        $.write(CONTENT_FOR_READERS);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }

  @ParameterizedTest
  @MethodSource("data_forWriterDo")
  @Order(15)
  public <T> void forWriterDo__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forWriterDo(destination, $ -> { throw new KclException("error"); });
    });
  }

  @ParameterizedTest
  @MethodSource("data_forReaderDo")
  @Order(16)
  public <T> void forReaderDo(IoSupport<T> ioSupport, T source) throws Exception {
    var writer = new StringWriter();
    ioSupport.forReaderDo(source, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @ParameterizedTest
  @MethodSource("data_forWriterDo")
  @Order(17)
  public <T> void forReaderDo__Error(IoSupport<T> ioSupport, T source) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forReaderDo(source, $ -> { throw new KclException("error"); });
    });
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forWriter() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text34.txt");
    var file2 = TEST_RESOURCES.getTempPath("text35.txt");
    var file3 = TEST_RESOURCES.getTempPath("text36.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_forWriter")
  @Order(18)
  public <T> void forWriter__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forWriter(destination, $ -> { throw new KclException("error"); });
    });
  }

  @ParameterizedTest
  @MethodSource("data_forWriter")
  @Order(19)
  public <T> void forWriter(IoSupport<T> ioSupport, T destination) throws Exception {
    var result  = ioSupport.forWriter(destination, $ -> {
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
  @Order(20)
  public <T> void forReader(IoSupport<T> ioSupport, T source) throws Exception {
    var writer = new StringWriter();
    var result  = ioSupport.forReader(source, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }

  @ParameterizedTest
  @MethodSource("data_forWriter")
  @Order(21)
  public <T> void forReader__Error(IoSupport<T> ioSupport, T source) throws Exception {
    assertThrows(KclException.class, () -> {
      ioSupport.forReader(source, $ -> { throw new KclException("error"); });
    });
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forWriterDo__WithEncoding() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text40.txt");
    var file2 = TEST_RESOURCES.getTempPath("text41.txt");
    var file3 = TEST_RESOURCES.getTempPath("text42.txt");
    var file4 = TEST_RESOURCES.getTempPath("text43.txt");
    var file5 = TEST_RESOURCES.getTempPath("text44.txt");
    var file6 = TEST_RESOURCES.getTempPath("text45.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1, Encoding.UTF8),
      Arguments.of(IoFunctions.IO_PATH, file2, Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_FILE, file3.toFile(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_FILE, file4.toFile(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_URI,  file5.toUri(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591)
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_forWriterDo__WithEncoding")
  @Order(22)
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
  @Order(23)
  public <T> void forReaderDo__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    ioSupport.forReaderDo(source, encoding, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_forWriter__WithEncoding() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text52.txt");
    var file2 = TEST_RESOURCES.getTempPath("text53.txt");
    var file3 = TEST_RESOURCES.getTempPath("text54.txt");
    var file4 = TEST_RESOURCES.getTempPath("text55.txt");
    var file5 = TEST_RESOURCES.getTempPath("text56.txt");
    var file6 = TEST_RESOURCES.getTempPath("text57.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1, Encoding.UTF8),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile(), Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_PATH, file4, Encoding.ISO88591),
      Arguments.of(IoFunctions.IO_FILE, file5.toFile(), Encoding.UTF8),
      Arguments.of(IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591)
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_forWriter__WithEncoding")
  @Order(24)
  public <T> void forWriter__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    var result  = ioSupport.forWriter(destination, encoding, $ -> {
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
  @Order(25)
  public <T> void forReader__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    var result  = ioSupport.forReader(source, encoding, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }
  
  @Order(26)
  @Test
  public void copy__Streams() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Order(27)
  @Test
  public void copy__Streams__WithBlockSizeSmall() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout, 8);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Order(28)
  @Test
  public void copy__Streams__WithBlockSizeLarge() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout, 16384);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Order(29)
  @Test
  public void copy__CharStreams() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @Order(30)
  @Test
  public void copy__CharStreams__WithBlockSizeSmall() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer, 8);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @Order(31)
  @Test
  public void copy__CharStreams__WithBlockSizeLarge() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer, 16384);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @SuppressWarnings({ "deprecation", "exports" })
  public static Stream<Arguments> data_loadBytes() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, httpXsd),
      Arguments.of(IoFunctions.IO_FILE, httpXsd.toFile()),
      Arguments.of(IoFunctions.IO_URI,  httpXsd.toUri()),
      Arguments.of(IoFunctions.IO_URL,  httpXsd.toFile().toURL())
    );
  }

  @ParameterizedTest
  @MethodSource("data_loadBytes")
  @Order(32)
  public <T> void loadBytes(IoSupport<T> ioSupport, T source) {
    var expected = Encoding.UTF8.encode("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    var loaded   = ioSupport.loadBytes(source, 39);
    assertThat(loaded, is(expected));
  }
    
  @ParameterizedTest
  @MethodSource("data_loadBytes")
  @Order(33)
  public <T> void loadBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode("version=\"1.0\"");
    var loaded   = ioSupport.loadBytes(source, 6, 13);
    assertThat(loaded, is(expected));
  }

  
  @SuppressWarnings({ "deprecation", "exports" })
  public static Stream<Arguments> data_loadChars() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, httpXsd),
      Arguments.of(IoFunctions.IO_FILE, httpXsd.toFile()),
      Arguments.of(IoFunctions.IO_URI,  httpXsd.toUri()),
      Arguments.of(IoFunctions.IO_URL,  httpXsd.toFile().toURL())
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_loadChars")
  @Order(34)
  public <T> void loadChars(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
    var loaded   = ioSupport.loadChars(source, 39);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadChars")
  @Order(35)
  public <T> void loadChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "version=\"1.0\"".toCharArray();
    var loaded   = ioSupport.loadChars(source, 6, 13);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadChars")
  @Order(36)
  public <T> void loadChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
    var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 39);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadChars")
  @Order(37)
  public <T> void loadChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "version=\"1.0\"".toCharArray();
    var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 6, 13);
    assertThat(loaded, is(expected));
  }
  
  @SuppressWarnings({ "deprecation", "exports" })
  public static Stream<Arguments> data_loadAllBytes() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, httpXsd),
      Arguments.of(IoFunctions.IO_FILE, httpXsd.toFile()),
      Arguments.of(IoFunctions.IO_URI,  httpXsd.toUri()),
      Arguments.of(IoFunctions.IO_URL,  httpXsd.toFile().toURL())
    );
  }

  @ParameterizedTest
  @MethodSource("data_loadAllBytes")
  @Order(38)
  public <T> void loadAllBytes(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    var loaded   = ioSupport.loadAllBytes(source);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadAllBytes")
  @Order(39)
  public <T> void loadAllBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD.substring(40));
    var loaded   = ioSupport.loadAllBytes(source, 40);
    assertThat(loaded, is(expected));
  }
  
  @SuppressWarnings({ "deprecation", "exports" })
  public static Stream<Arguments> data_loadAllChars() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, httpXsd),
      Arguments.of(IoFunctions.IO_FILE, httpXsd.toFile()),
      Arguments.of(IoFunctions.IO_URI,  httpXsd.toUri()),
      Arguments.of(IoFunctions.IO_URL,  httpXsd.toFile().toURL())
    );
  }

  @ParameterizedTest
  @MethodSource("data_loadAllChars")
  @Order(40)
  public <T> void loadAllChars(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.toCharArray();
    var loaded   = ioSupport.loadAllChars(source);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadAllChars")
  @Order(41)
  public <T> void loadAllChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
    var loaded   = ioSupport.loadAllChars(source, 40);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadAllChars")
  @Order(42)
  public <T> void loadAllChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.toCharArray();
    var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591);
    assertThat(loaded, is(expected));
  }
  
  @ParameterizedTest
  @MethodSource("data_loadAllChars")
  @Order(43)
  public <T> void loadAllChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
    var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591, 40);
    assertThat(loaded, is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_saveBytes() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text64.txt");
    var file2 = TEST_RESOURCES.getTempPath("text65.txt");
    var file3 = TEST_RESOURCES.getTempPath("text66.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_saveBytes")
  @Order(44)
  public <T> void saveBytes(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    ioSupport.saveBytes(destination, toSave);
    var loaded = ioSupport.loadAllBytes(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_saveBytes__WithOffset__WithSize() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text91.txt");
    var file2 = TEST_RESOURCES.getTempPath("text92.txt");
    var file3 = TEST_RESOURCES.getTempPath("text93.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_saveBytes__WithOffset__WithSize")
  @Order(45)
  public <T> void saveBytes__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) throws Exception {
    var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    ioSupport.saveBytes(destination, toSave, 20, 100);
    var loaded = ioSupport.loadAllBytes(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_saveChars() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text67.txt");
    var file2 = TEST_RESOURCES.getTempPath("text68.txt");
    var file3 = TEST_RESOURCES.getTempPath("text69.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_saveChars")
  @Order(46)
  public <T> void saveChars(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, toSave);
    var loaded = ioSupport.loadAllChars(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }

  @ParameterizedTest
  @MethodSource("data_saveChars")
  @Order(47)
  public <T> void saveChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, Encoding.ISO88591, toSave);
    var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_saveChars__WithEncoding__WithOffset__WithSize() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text94.txt");
    var file2 = TEST_RESOURCES.getTempPath("text95.txt");
    var file3 = TEST_RESOURCES.getTempPath("text96.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }
  
  @ParameterizedTest
  @MethodSource("data_saveChars__WithEncoding__WithOffset__WithSize")
  @Order(48)
  public <T> void saveChars__WithEncoding__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, Encoding.ISO88591, toSave, 20, 100);
    var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
    assertNotNull(loaded);
    assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
  }

  
  @SuppressWarnings({ "deprecation", "exports" })
  public static Stream<Arguments> data_readText() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, httpXsd),
      Arguments.of(IoFunctions.IO_FILE, httpXsd.toFile()),
      Arguments.of(IoFunctions.IO_URI,  httpXsd.toUri()),
      Arguments.of(IoFunctions.IO_URL,  httpXsd.toFile().toURL())
    );
  }

  @ParameterizedTest
  @MethodSource("data_readText")
  @Order(49)
  public <T> void readText(IoSupport<T> ioSupport, @NotNull T source) {
    String read = ioSupport.readText(source);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @ParameterizedTest
  @MethodSource("data_readText")
  @Order(50)
  public <T> void readText__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    String read = ioSupport.readText(source, Encoding.ISO88591);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_writeText() throws Exception {
    var file1 = TEST_RESOURCES.getTempPath("text70.txt");
    var file2 = TEST_RESOURCES.getTempPath("text71.txt");
    var file3 = TEST_RESOURCES.getTempPath("text72.txt");
    return Stream.of(
      Arguments.of(IoFunctions.IO_PATH, file1),
      Arguments.of(IoFunctions.IO_FILE, file2.toFile()),
      Arguments.of(IoFunctions.IO_URI,  file3.toUri())
    );
  }

  @ParameterizedTest
  @MethodSource("data_writeText")
  @Order(51)
  public <T> void writeText(IoSupport<T> ioSupport, T destination) {
    ioSupport.writeText(destination, CONTENT_HTTP_XSD);
    var read = ioSupport.readText(destination);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @ParameterizedTest
  @MethodSource("data_writeText")
  @Order(52)
  public <T> void writeText__WithEncoding(IoSupport<T> ioSupport, T destination) {
    ioSupport.writeText(destination, Encoding.ISO88591, CONTENT_HTTP_XSD);
    var read = ioSupport.readText(destination, Encoding.ISO88591);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }
  
  @Test
  @Order(53)
  public void gzip() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    var file1   = TEST_RESOURCES.getTempPath("text73.xsd");
    Files.copy(httpXsd, file1);
    var gzipped = IoFunctions.gzip(file1);
    assertNotNull(gzipped);
    assertTrue(Files.isRegularFile(gzipped));
  }
  
  @Test
  @Order(54)
  public void ungzip() throws Exception {
    var file1       = TEST_RESOURCES.getTempPath("text73.xsd");
    var file3       = TEST_RESOURCES.getTempPath("text74.xsd.gz");
    Files.copy(file1.toAbsolutePath().getParent().resolve(file1.getFileName().toString() + ".gz"), file3);
    var ungzipped   = IoFunctions.ungzip(file3);
    assertNotNull(ungzipped);
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file1), is(IoFunctions.IO_PATH.loadAllBytes(ungzipped)));
  }
  
  @Test
  @Order(55)
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
  @Order(56)
  public <T> void ungzip__WithDestination() {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    var file1   = TEST_RESOURCES.getTempPath("text76.xsd.gz");
    var file2   = TEST_RESOURCES.getTempPath("text77.xsd");
    var ungzipped = IoFunctions.ungzip(file1, file2);
    assertNotNull(ungzipped);
    assertThat(ungzipped.normalize(), is(file2.normalize()));
    assertTrue(Files.isRegularFile(ungzipped));
    assertThat(IoFunctions.IO_PATH.loadAllBytes(httpXsd), is(IoFunctions.IO_PATH.loadAllBytes(file2)));
  }
  
  @Test
  @Order(57)
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
  @Order(58)
  public void ungzip__Inline() throws Exception {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    var file1   = TEST_RESOURCES.getTempPath("text78");
    var ungzipped = IoFunctions.ungzip(file1, file1);
    assertNotNull(ungzipped);
    assertThat(ungzipped.normalize(), is(file1.normalize()));
    assertTrue(Files.isRegularFile(ungzipped));
    assertThat(IoFunctions.IO_PATH.loadAllBytes(ungzipped), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
  }

  @Test
  @Order(59)
  public void findExistingPath() {
    
    var file1       = TEST_RESOURCES.getResource("folder1/folder2/folder3/myfile.txt");
    var result1     = IoFunctions.findExistingPath(file1);
    assertTrue(result1.isPresent());
    assertThat(result1.get().getFileName().toString(), is("folder3"));

    var file2       = TEST_RESOURCES.getResource("folder1/folder2/folder4/myfile.txt");
    var result2     = IoFunctions.findExistingPath(file2);
    assertTrue(result2.isPresent());
    assertThat(result2.get().getFileName().toString(), is("folder2"));

    var file3       = Paths.get("Q:/bibo/simpleton/dodo.txt");
    var result3     = IoFunctions.findExistingPath(file3);
    assertFalse(result3.isPresent());

  }
  
  @Test
  @Order(60)
  public void copyFile() {
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    var file1   = TEST_RESOURCES.getTempPath("text79.txt");
    IoFunctions.copyFile(httpXsd, file1);
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file1), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
    assertTrue(Files.isRegularFile(httpXsd));
  }

  @Test
  @Order(61)
  public void copyFile__MissingSource() {
    assertThrows(KclException.class, () -> {
      var file1 = TEST_RESOURCES.getTempPath("text80.txt");
      var file2 = TEST_RESOURCES.getTempPath("text82.txt");
      IoFunctions.copyFile(file1, file2);
    });
  }

  @Test
  @Order(62)
  public void moveFile() {
    
    var httpXsd = TEST_RESOURCES.getResource("http.xsd");
    var file1   = TEST_RESOURCES.getTempPath("text83.txt");
    var file2   = TEST_RESOURCES.getTempPath("text84.txt");
    IoFunctions.copyFile(httpXsd, file1);
    
    IoFunctions.moveFile(file1, file2);
    
    assertFalse(Files.isRegularFile(file1));
    assertTrue(Files.isRegularFile(file2));
    
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file2), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
  }

  @Test
  @Order(63)
  public void moveFile__MissingSource() {
    assertThrows(KclException.class, () -> {
      var file1 = TEST_RESOURCES.getTempPath("text85.txt");
      var file2 = TEST_RESOURCES.getTempPath("text86.txt");
      IoFunctions.moveFile(file1, file2);
    });
  }

  @Test
  @Order(64)
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
  
  @Order(65)
  @Test
  public void listPathes() {
    var dir      = TEST_RESOURCES.getResource("simpleton");
    var files    = IoFunctions.listPathes(dir, null, true);
    var expected = Arrays.asList(
      "file1.txt",
      "file2.txt",
      "folder1/",
      "folder1/file1.txt",
      "folder1/file2.txt",
      "folder1/file3.txt",
      "folder1/subfolder1/",
      "folder1/subfolder1/file1.txt",
      "folder1/subfolder1/file2.txt",
      "folder1/subfolder1/subsubfolder1/",
      "folder1/subfolder1/subsubfolder1/file1.txt",
      "folder1/subfolder1/subsubfolder1/file2.png",
      "folder1/subfolder1/subsubfolder1/file3.tif",
      "folder1/subfolder1/subsubfolder1/file4.jpg",
      "folder1/subfolder2/",
      "folder1/subfolder2/file4.txt",
      "folder1/subfolder2/file5.txt",
      "folder2/",
      "folder2/file1.txt",
      "folder2/file2.jpg",
      "folder2/file3.mp4",
      "folder3/",
      "folder3/file1.txt"
    );
    assertThat(files, is(expected));
  }
  
  @Test
  @Order(66)
  public void listZipFile() {
    var zipFile  = TEST_RESOURCES.getResource("simpleton.zip");
    var files    = IoFunctions.listZipFile(zipFile);
    var expected = Arrays.asList(
      "simpleton/",
      "simpleton/file1.txt",
      "simpleton/file2.txt",
      "simpleton/folder1/",
      "simpleton/folder1/file1.txt",
      "simpleton/folder1/file2.txt",
      "simpleton/folder1/file3.txt",
      "simpleton/folder1/subfolder1/",
      "simpleton/folder1/subfolder1/file1.txt",
      "simpleton/folder1/subfolder1/file2.txt",
      "simpleton/folder1/subfolder1/subsubfolder1/",
      "simpleton/folder1/subfolder1/subsubfolder1/file1.txt",
      "simpleton/folder1/subfolder1/subsubfolder1/file2.png",
      "simpleton/folder1/subfolder1/subsubfolder1/file3.tif",
      "simpleton/folder1/subfolder1/subsubfolder1/file4.jpg",
      "simpleton/folder1/subfolder2/",
      "simpleton/folder1/subfolder2/file4.txt",
      "simpleton/folder1/subfolder2/file5.txt",
      "simpleton/folder2/",
      "simpleton/folder2/file1.txt",
      "simpleton/folder2/file2.jpg",
      "simpleton/folder2/file3.mp4",
      "simpleton/folder3/",
      "simpleton/folder3/file1.txt"
    );
    assertThat(files, is(expected));
  }

  @Test
  @Order(67)
  public void listZipFile__FilterDirectories() {
    var zipFile  = TEST_RESOURCES.getResource("simpleton.zip");
    var files    = IoFunctions.listZipFile(zipFile, null, $ -> !$.isDirectory());
    var expected = Arrays.asList(
      "simpleton/file1.txt",
      "simpleton/file2.txt",
      "simpleton/folder1/file1.txt",
      "simpleton/folder1/file2.txt",
      "simpleton/folder1/file3.txt",
      "simpleton/folder1/subfolder1/file1.txt",
      "simpleton/folder1/subfolder1/file2.txt",
      "simpleton/folder1/subfolder1/subsubfolder1/file1.txt",
      "simpleton/folder1/subfolder1/subsubfolder1/file2.png",
      "simpleton/folder1/subfolder1/subsubfolder1/file3.tif",
      "simpleton/folder1/subfolder1/subsubfolder1/file4.jpg",
      "simpleton/folder1/subfolder2/file4.txt",
      "simpleton/folder1/subfolder2/file5.txt",
      "simpleton/folder2/file1.txt",
      "simpleton/folder2/file2.jpg",
      "simpleton/folder2/file3.mp4",
      "simpleton/folder3/file1.txt"
    );
    assertThat(files, is(expected));
  }

  @Test
  @Order(68)
  public void zip() {
    var dir      = TEST_RESOURCES.getResource("simpleton");
    var zipFile  = TEST_RESOURCES.getTempPath("zipped89.zip");
    assertFalse(Files.isRegularFile(zipFile));
    IoFunctions.zip(zipFile, dir, null);
    assertTrue(Files.isRegularFile(zipFile));
  }
  
  @Test
  @Order(69)
  public void unzip() {

    var dir      = TEST_RESOURCES.getResource("simpleton");
    var zipFile  = TEST_RESOURCES.getTempPath("zipped89.zip");
    var unpacked = TEST_RESOURCES.getTempPath("dir90");
    IoFunctions.unzip(zipFile, unpacked);
    
    var filesFromOrigin    = IoFunctions.listPathes(dir);
    var filesFromUnpacked  = IoFunctions.listPathes(unpacked);
    assertThat(filesFromUnpacked, is(filesFromOrigin));
    
  }
  
  @Test
  @Order(70)
  public void locateDirectory() throws Exception {
    
    Path dir1 = IoFunctions.locateDirectory(Iso3166Test.class);
    assertNotNull(dir1);
    
    Path classFile = dir1.resolve("com/kasisoft/libs/common/constants/Iso3166Test.class");
    assertTrue(Files.isRegularFile(classFile));
    
  }
  
} /* ENDCLASS */
