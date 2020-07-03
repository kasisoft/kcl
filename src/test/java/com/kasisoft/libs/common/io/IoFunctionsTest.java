package com.kasisoft.libs.common.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.kasisoft.libs.common.constants.Encoding;
import com.kasisoft.libs.common.constants.Iso3166Test;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.KclException;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.validation.constraints.NotNull;

import java.util.Arrays;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'IoFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("preview")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoFunctionsTest extends AbstractTestCase {

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
  
  Path  httpXsd;
  Path  tempHttpXsd;
  
  @BeforeSuite
  public void setup() throws Exception {
    httpXsd     = getResource("http.xsd");
    tempHttpXsd = getTempPath("http.xsd");
    Files.copy(httpXsd, tempHttpXsd);
  }
  
  @DataProvider(name = "data_compileFileSystemPattern")
  public Object[][] data_compileFileSystemPattern() {
    return new Object[][] {
      {"*", "([^/]+)"},
      {"**", "(.+)"},
      {"dir/**", "\\Qdir/\\E(.+)"},
      {"dir/*", "\\Qdir/\\E([^/]+)"},
      {"dir/*/subdir", "\\Qdir/\\E([^/]+)\\Q/subdir\\E"},
    };
  }
  
  @Test(groups = "all", dataProvider = "data_compileFileSystemPattern")
  public void compileFileSystemPattern(String pattern, String regex) {
    var p = IoFunctions.compileFilesystemPattern(pattern);
    assertThat(p.pattern(), is(regex));
  }
  
  @DataProvider(name = "data_newOutputStream")
  public Object[][] data_newOutputStream() throws Exception {
    var file1 = getTempPath("text1.txt");
    var file2 = getTempPath("text2.txt");
    var file3 = getTempPath("text3.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
    
  @Test(groups = "all", dataProvider = "data_newOutputStream")
  public <T> void newOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    try (var outstream = ioSupport.newOutputStream(destination)) {
      outstream.write(asBytes);
    }
  }
  
  @Test(groups = "all", dataProvider = "data_newOutputStream", dependsOnMethods = "newOutputStream")
  public <T> void newInputStream(IoSupport<T> ioSupport, T source) throws Exception {
    var byteout = new ByteArrayOutputStream();
    try (var instream = ioSupport.newInputStream(source)) {
      IoFunctions.copy(instream, byteout);
    }
    var asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }

  @DataProvider(name = "data_newWriter")
  public Object[][] data_newWriter() throws Exception {
    var file1 = getTempPath("text4.txt");
    var file2 = getTempPath("text5.txt");
    var file3 = getTempPath("text6.txt");
    var file4 = getTempPath("text7.txt");
    var file5 = getTempPath("text8.txt");
    var file6 = getTempPath("text9.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.ISO88591},
      { IoFunctions.IO_PATH, file2, Encoding.UTF8},
      { IoFunctions.IO_FILE, file3.toFile(), Encoding.ISO88591},
      { IoFunctions.IO_FILE, file4.toFile(), Encoding.UTF8},
      { IoFunctions.IO_URI,  file5.toUri(), Encoding.ISO88591},
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.UTF8},
    };
  }

  @Test(groups = "all", dataProvider = "data_newWriter")
  public <T> void newWriter(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
    try (var writer = ioSupport.newWriter(destination)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @Test(groups = "all", dataProvider = "data_newWriter", dependsOnMethods = "newWriter")
  public <T> void newReader(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
    var writer = new StringWriter();
    try (var reader = ioSupport.newReader(destination)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @DataProvider(name = "data_newWriter__WithEncoding")
  public Object[][] data_newWriter__WithEncoding() throws Exception {
    var file1 = getTempPath("text10.txt");
    var file2 = getTempPath("text11.txt");
    var file3 = getTempPath("text12.txt");
    var file4 = getTempPath("text13.txt");
    var file5 = getTempPath("text14.txt");
    var file6 = getTempPath("text15.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.ISO88591},
      { IoFunctions.IO_PATH, file2, Encoding.UTF8},
      { IoFunctions.IO_FILE, file3.toFile(), Encoding.ISO88591},
      { IoFunctions.IO_FILE, file4.toFile(), Encoding.UTF8},
      { IoFunctions.IO_URI,  file5.toUri(), Encoding.ISO88591},
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.UTF8},
    };
  }

  @Test(groups = "all", dataProvider = "data_newWriter__WithEncoding")
  public <T> void newWriter__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    try (var writer = ioSupport.newWriter(destination, encoding)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @Test(groups = "all", dataProvider = "data_newWriter__WithEncoding", dependsOnMethods = "newWriter__WithEncoding")
  public <T> void newReader__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    try (var reader = ioSupport.newReader(destination, encoding)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_forOutputStreamDo")
  public Object[][] data_forOutputStreamDo() throws Exception {
    var file1 = getTempPath("text16.txt");
    var file2 = getTempPath("text17.txt");
    var file3 = getTempPath("text18.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStreamDo")
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

  @Test(groups = "all", dataProvider = "data_forOutputStreamDo", dependsOnMethods = "forOutputStreamDo")
  public <T> void forInputStreamDo(IoSupport<T> ioSupport, T source) throws Exception {
    var byteout = new ByteArrayOutputStream();
    ioSupport.forInputStreamDo(source, $ -> {
      IoFunctions.copy($, byteout);
    });
    var asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }
  
  @DataProvider(name = "data_forOutputStream")
  public Object[][] data_forOutputStream() throws Exception {
    var file1 = getTempPath("text22.txt");
    var file2 = getTempPath("text23.txt");
    var file3 = getTempPath("text24.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStream")
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

  @Test(groups = "all", dataProvider = "data_forOutputStream", expectedExceptions = KclException.class)
  public <T> void forOutputStream__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forOutputStream(destination, $ -> { throw new KclException("error"); });
  }

  @Test(groups = "all", dataProvider = "data_forOutputStream", dependsOnMethods = "forOutputStream")
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

  @Test(groups = "all", dataProvider = "data_forOutputStream", dependsOnMethods = "forOutputStream", expectedExceptions = KclException.class)
  public <T> void forInputStream__Error(IoSupport<T> ioSupport, T source) throws Exception {
    ioSupport.forInputStream(source, $ -> { throw new KclException("error"); });
  }

  @DataProvider(name = "data_forWriterDo")
  public Object[][] data_forWriterDo() throws Exception {
    var file1 = getTempPath("text28.txt");
    var file2 = getTempPath("text29.txt");
    var file3 = getTempPath("text30.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo")
  public <T> void forWriterDo(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forWriterDo(destination, $ -> {
      try {
        $.write(CONTENT_FOR_READERS);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }

  @Test(groups = "all", dataProvider = "data_forWriterDo", expectedExceptions = KclException.class)
  public <T> void forWriterDo__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forWriterDo(destination, $ -> { throw new KclException("error"); });
  }

  @Test(groups = "all", dataProvider = "data_forWriterDo", dependsOnMethods = "forWriterDo")
  public <T> void forReaderDo(IoSupport<T> ioSupport, T source) throws Exception {
    var writer = new StringWriter();
    ioSupport.forReaderDo(source, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @Test(groups = "all", dataProvider = "data_forWriterDo", dependsOnMethods = "forWriterDo", expectedExceptions = KclException.class)
  public <T> void forReaderDo__Error(IoSupport<T> ioSupport, T source) throws Exception {
    ioSupport.forReaderDo(source, $ -> { throw new KclException("error"); });
  }

  @DataProvider(name = "data_forWriter")
  public Object[][] data_forWriter() throws Exception {
    var file1 = getTempPath("text34.txt");
    var file2 = getTempPath("text35.txt");
    var file3 = getTempPath("text36.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriter")
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

  @Test(groups = "all", dataProvider = "data_forWriter", expectedExceptions = KclException.class)
  public <T> void forWriter__Error(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forWriter(destination, $ -> { throw new KclException("error"); });
  }

  @Test(groups = "all", dataProvider = "data_forWriter", dependsOnMethods = "forWriter")
  public <T> void forReader(IoSupport<T> ioSupport, T source) throws Exception {
    var writer = new StringWriter();
    var result  = ioSupport.forReader(source, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }

  @Test(groups = "all", dataProvider = "data_forWriter", dependsOnMethods = "forWriter", expectedExceptions = KclException.class)
  public <T> void forReader__Error(IoSupport<T> ioSupport, T source) throws Exception {
    ioSupport.forReader(source, $ -> { throw new KclException("error"); });
  }

  @DataProvider(name = "data_forWriterDo__WithEncoding")
  public Object[][] data_forWriterDo__WithEncoding() throws Exception {
    var file1 = getTempPath("text40.txt");
    var file2 = getTempPath("text41.txt");
    var file3 = getTempPath("text42.txt");
    var file4 = getTempPath("text43.txt");
    var file5 = getTempPath("text44.txt");
    var file6 = getTempPath("text45.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.UTF8 },
      { IoFunctions.IO_PATH, file2, Encoding.ISO88591 },
      { IoFunctions.IO_FILE, file3.toFile(), Encoding.UTF8 },
      { IoFunctions.IO_FILE, file4.toFile(), Encoding.ISO88591 },
      { IoFunctions.IO_URI,  file5.toUri(), Encoding.UTF8  },
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591 },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo__WithEncoding")
  public <T> void forWriterDo__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    ioSupport.forWriterDo(destination, encoding, $ -> {
      try {
        $.write(CONTENT_FOR_READERS);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }

  @Test(groups = "all", dataProvider = "data_forWriterDo__WithEncoding", dependsOnMethods = "forWriterDo__WithEncoding")
  public <T> void forReaderDo__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    ioSupport.forReaderDo(source, encoding, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @DataProvider(name = "data_forWriter__WithEncoding")
  public Object[][] data_forWriter__WithEncoding() throws Exception {
    var file1 = getTempPath("text52.txt");
    var file2 = getTempPath("text53.txt");
    var file3 = getTempPath("text54.txt");
    var file4 = getTempPath("text55.txt");
    var file5 = getTempPath("text56.txt");
    var file6 = getTempPath("text57.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.UTF8 },
      { IoFunctions.IO_FILE, file2.toFile(), Encoding.ISO88591 },
      { IoFunctions.IO_URI,  file3.toUri(), Encoding.UTF8 },
      { IoFunctions.IO_PATH, file4, Encoding.ISO88591 },
      { IoFunctions.IO_FILE, file5.toFile(), Encoding.UTF8 },
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591 },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriter__WithEncoding")
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

  @Test(groups = "all", dataProvider = "data_forWriter__WithEncoding", dependsOnMethods = "forWriter__WithEncoding")
  public <T> void forReader__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    var writer = new StringWriter();
    var result  = ioSupport.forReader(source, encoding, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }
  
  @Test(groups = "all")
  public void copy__Streams() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Test(groups = "all")
  public void copy__Streams__WithBlockSizeSmall() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout, 8);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Test(groups = "all")
  public void copy__Streams__WithBlockSizeLarge() throws Exception {
    var asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    var bytein  = new ByteArrayInputStream(asBytes);
    var byteout = new ByteArrayOutputStream();
    IoFunctions.copy(bytein, byteout, 16384);
    assertThat(byteout.toByteArray(), is(asBytes));
  }
  
  @Test(groups = "all")
  public void copy__CharStreams() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @Test(groups = "all")
  public void copy__CharStreams__WithBlockSizeSmall() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer, 8);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @Test(groups = "all")
  public void copy__CharStreams__WithBlockSizeLarge() throws Exception {
    var reader = new StringReader(CONTENT_FOR_READERS);
    var writer = new StringWriter();
    IoFunctions.copy(reader, writer, 16384);
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_loadBytes")
  public Object[][] data_loadBytes() throws Exception {
    return new Object[][] {
      { IoFunctions.IO_PATH, httpXsd },
      { IoFunctions.IO_FILE, httpXsd.toFile() },
      { IoFunctions.IO_URI,  httpXsd.toUri() },
      { IoFunctions.IO_URL,  httpXsd.toFile().toURL() },
    };
  }

  @Test(groups = "all", dataProvider = "data_loadBytes")
  public <T> void loadBytes(IoSupport<T> ioSupport, T source) {
    var expected = Encoding.UTF8.encode("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    var loaded   = ioSupport.loadBytes(source, 39);
    assertThat(loaded, is(expected));
  }
    
  @Test(groups = "all", dataProvider = "data_loadBytes")
  public <T> void loadBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode("version=\"1.0\"");
    var loaded   = ioSupport.loadBytes(source, 6, 13);
    assertThat(loaded, is(expected));
  }

  
  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_loadChars")
  public Object[][] data_loadChars() throws Exception {
    return new Object[][] {
      { IoFunctions.IO_PATH, httpXsd },
      { IoFunctions.IO_FILE, httpXsd.toFile() },
      { IoFunctions.IO_URI,  httpXsd.toUri() },
      { IoFunctions.IO_URL,  httpXsd.toFile().toURL() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_loadChars")
  public <T> void loadChars(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
    var loaded   = ioSupport.loadChars(source, 39);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadChars")
  public <T> void loadChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "version=\"1.0\"".toCharArray();
    var loaded   = ioSupport.loadChars(source, 6, 13);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadChars")
  public <T> void loadChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>".toCharArray();
    var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 39);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadChars")
  public <T> void loadChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = "version=\"1.0\"".toCharArray();
    var loaded   = ioSupport.loadChars(source, Encoding.ISO88591, 6, 13);
    assertThat(loaded, is(expected));
  }
  
  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_loadAllBytes")
  public Object[][] data_loadAllBytes() throws Exception {
    return new Object[][] {
      { IoFunctions.IO_PATH, httpXsd },
      { IoFunctions.IO_FILE, httpXsd.toFile() },
      { IoFunctions.IO_URI,  httpXsd.toUri() },
      { IoFunctions.IO_URL,  httpXsd.toFile().toURL() },
    };
  }

  @Test(groups = "all", dataProvider = "data_loadAllBytes")
  public <T> void loadAllBytes(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    var loaded   = ioSupport.loadAllBytes(source);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadAllBytes")
  public <T> void loadAllBytes__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = Encoding.UTF8.encode(CONTENT_HTTP_XSD.substring(40));
    var loaded   = ioSupport.loadAllBytes(source, 40);
    assertThat(loaded, is(expected));
  }
  
  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_loadAllChars")
  public Object[][] data_loadAllChars() throws Exception {
    return new Object[][] {
      { IoFunctions.IO_PATH, httpXsd },
      { IoFunctions.IO_FILE, httpXsd.toFile() },
      { IoFunctions.IO_URI,  httpXsd.toUri() },
      { IoFunctions.IO_URL,  httpXsd.toFile().toURL() },
    };
  }

  @Test(groups = "all", dataProvider = "data_loadAllChars")
  public <T> void loadAllChars(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.toCharArray();
    var loaded   = ioSupport.loadAllChars(source);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadAllChars")
  public <T> void loadAllChars__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
    var loaded   = ioSupport.loadAllChars(source, 40);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadAllChars")
  public <T> void loadAllChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.toCharArray();
    var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591);
    assertThat(loaded, is(expected));
  }
  
  @Test(groups = "all", dataProvider = "data_loadAllChars")
  public <T> void loadAllChars__WithEncoding__WithOffset(IoSupport<T> ioSupport, @NotNull T source) {
    var expected = CONTENT_HTTP_XSD.substring(40).toCharArray();
    var loaded   = ioSupport.loadAllChars(source, Encoding.ISO88591, 40);
    assertThat(loaded, is(expected));
  }
  
  @DataProvider(name = "data_saveBytes")
  public Object[][] data_saveBytes() throws Exception {
    var file1 = getTempPath("text64.txt");
    var file2 = getTempPath("text65.txt");
    var file3 = getTempPath("text66.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }

  @Test(groups = "all", dataProvider = "data_saveBytes")
  public <T> void saveBytes(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    ioSupport.saveBytes(destination, toSave);
    var loaded = ioSupport.loadAllBytes(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }
  
  @DataProvider(name = "data_saveBytes__WithOffset__WithSize")
  public Object[][] data_saveBytes__WithOffset__WithSize() throws Exception {
    var file1 = getTempPath("text91.txt");
    var file2 = getTempPath("text92.txt");
    var file3 = getTempPath("text93.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }

  @Test(groups = "all", dataProvider = "data_saveBytes__WithOffset__WithSize")
  public <T> void saveBytes__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) throws Exception {
    var toSave = Encoding.UTF8.encode(CONTENT_HTTP_XSD);
    ioSupport.saveBytes(destination, toSave, 20, 100);
    var loaded = ioSupport.loadAllBytes(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
  }

  @DataProvider(name = "data_saveChars")
  public Object[][] data_saveChars() throws Exception {
    var file1 = getTempPath("text67.txt");
    var file2 = getTempPath("text68.txt");
    var file3 = getTempPath("text69.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }

  @Test(groups = "all", dataProvider = "data_saveChars")
  public <T> void saveChars(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, toSave);
    var loaded = ioSupport.loadAllChars(destination);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }

  @Test(groups = "all", dataProvider = "data_saveChars")
  public <T> void saveChars__WithEncoding(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, Encoding.ISO88591, toSave);
    var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
    assertNotNull(loaded);
    assertThat(loaded, is(toSave));
  }

  @DataProvider(name = "data_saveChars__WithEncoding__WithOffset__WithSize")
  public Object[][] data_saveChars__WithEncoding__WithOffset__WithSize() throws Exception {
    var file1 = getTempPath("text94.txt");
    var file2 = getTempPath("text95.txt");
    var file3 = getTempPath("text96.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_saveChars__WithEncoding__WithOffset__WithSize")
  public <T> void saveChars__WithEncoding__WithOffset__WithSize(IoSupport<T> ioSupport, @NotNull T destination) {
    var toSave = CONTENT_HTTP_XSD.toCharArray();
    ioSupport.saveChars(destination, Encoding.ISO88591, toSave, 20, 100);
    var loaded = ioSupport.loadAllChars(destination, Encoding.ISO88591);
    assertNotNull(loaded);
    assertThat(loaded, is(Arrays.copyOfRange(toSave, 20, 20 + 100)));
  }

  
  @SuppressWarnings("deprecation")
  @DataProvider(name = "data_readText")
  public Object[][] data_readText() throws Exception {
    return new Object[][] {
      { IoFunctions.IO_PATH, httpXsd },
      { IoFunctions.IO_FILE, httpXsd.toFile() },
      { IoFunctions.IO_URI,  httpXsd.toUri() },
      { IoFunctions.IO_URL,  httpXsd.toFile().toURL() },
    };
  }

  @Test(groups = "all", dataProvider = "data_readText")
  public <T> void readText(IoSupport<T> ioSupport, @NotNull T source) {
    String read = ioSupport.readText(source);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @Test(groups = "all", dataProvider = "data_readText")
  public <T> void readText__WithEncoding(IoSupport<T> ioSupport, @NotNull T source) {
    String read = ioSupport.readText(source, Encoding.ISO88591);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @DataProvider(name = "data_writeText")
  public Object[][] data_writeText() throws Exception {
    var file1 = getTempPath("text70.txt");
    var file2 = getTempPath("text71.txt");
    var file3 = getTempPath("text72.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }

  @Test(groups = "all", dataProvider = "data_writeText")
  public <T> void writeText(IoSupport<T> ioSupport, T destination) {
    ioSupport.writeText(destination, CONTENT_HTTP_XSD);
    var read = ioSupport.readText(destination);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }

  @Test(groups = "all", dataProvider = "data_writeText")
  public <T> void writeText__WithEncoding(IoSupport<T> ioSupport, T destination) {
    ioSupport.writeText(destination, Encoding.ISO88591, CONTENT_HTTP_XSD);
    var read = ioSupport.readText(destination, Encoding.ISO88591);
    assertThat(read, is(CONTENT_HTTP_XSD));
  }
  
  @Test(groups = "all")
  public void gzip() throws Exception {
    var file1 = getTempPath("text73.xsd");
    Files.copy(httpXsd, file1);
    var gzipped = IoFunctions.gzip(file1);
    assertNotNull(gzipped);
    assertTrue(Files.isRegularFile(gzipped));
  }
  
  @Test(groups = "all", dependsOnMethods = "gzip")
  public void ungzip() throws Exception {
    var file1       = getTempPath("text73.xsd");
    var file3       = getTempPath("text74.xsd.gz");
    Files.copy(file1.toAbsolutePath().getParent().resolve(file1.getFileName().toString() + ".gz"), file3);
    var ungzipped   = IoFunctions.ungzip(file3);
    assertNotNull(ungzipped);
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file1), is(IoFunctions.IO_PATH.loadAllBytes(ungzipped)));
  }
  
  @Test(groups = "all")
  public void gzip__WithDestination() throws Exception {
    var file1 = getTempPath("text75.xsd");
    var file2 = getTempPath("text76.xsd.gz");
    Files.copy(httpXsd, file1);
    var gzipped = IoFunctions.gzip(file1, file2);
    assertNotNull(gzipped);
    assertThat(gzipped.normalize(), is(file2.normalize()));
    assertTrue(Files.isRegularFile(gzipped));
  }

  @Test(groups = "all", dependsOnMethods = "gzip__WithDestination")
  public <T> void ungzip__WithDestination() {
    var file1 = getTempPath("text76.xsd.gz");
    var file2 = getTempPath("text77.xsd");
    var ungzipped = IoFunctions.ungzip(file1, file2);
    assertNotNull(ungzipped);
    assertThat(ungzipped.normalize(), is(file2.normalize()));
    assertTrue(Files.isRegularFile(ungzipped));
    assertThat(IoFunctions.IO_PATH.loadAllBytes(httpXsd), is(IoFunctions.IO_PATH.loadAllBytes(file2)));
  }
  
  @Test(groups = "all")
  public void gzip__Inline() throws Exception {
    var file1 = getTempPath("text78");
    Files.copy(httpXsd, file1);
    var gzipped = IoFunctions.gzip(file1, file1);
    assertNotNull(gzipped);
    assertThat(gzipped.normalize(), is(file1.normalize()));
    assertTrue(Files.isRegularFile(gzipped));
  }

  @Test(groups = "all", dependsOnMethods = "gzip__Inline")
  public void ungzip__Inline() throws Exception {
    var file1 = getTempPath("text78");
    var ungzipped = IoFunctions.ungzip(file1, file1);
    assertNotNull(ungzipped);
    assertThat(ungzipped.normalize(), is(file1.normalize()));
    assertTrue(Files.isRegularFile(ungzipped));
    assertThat(IoFunctions.IO_PATH.loadAllBytes(ungzipped), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
  }

  @Test(groups = "all")
  public void findExistingPath() {
    
    var file1       = getResource("folder1/folder2/folder3/myfile.txt");
    var result1     = IoFunctions.findExistingPath(file1);
    assertTrue(result1.isPresent());
    assertThat(result1.get().getFileName().toString(), is("folder3"));

    var file2       = getResource("folder1/folder2/folder4/myfile.txt");
    var result2     = IoFunctions.findExistingPath(file2);
    assertTrue(result2.isPresent());
    assertThat(result2.get().getFileName().toString(), is("folder2"));

    var file3       = Paths.get("Q:/bibo/simpleton/dodo.txt");
    var result3     = IoFunctions.findExistingPath(file3);
    assertFalse(result3.isPresent());

  }
  
  @Test(groups = "all")
  public void copyFile() {
    var file1 = getTempPath("text79.txt");
    IoFunctions.copyFile(httpXsd, file1);
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file1), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
    assertTrue(Files.isRegularFile(httpXsd));
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void copyFile__MissingSource() {
    var file1 = getTempPath("text80.txt");
    var file2 = getTempPath("text82.txt");
    IoFunctions.copyFile(file1, file2);
  }

  @Test(groups = "all")
  public void moveFile() {
    
    var file1 = getTempPath("text83.txt");
    var file2 = getTempPath("text84.txt");
    IoFunctions.copyFile(httpXsd, file1);
    
    IoFunctions.moveFile(file1, file2);
    
    assertFalse(Files.isRegularFile(file1));
    assertTrue(Files.isRegularFile(file2));
    
    assertThat(IoFunctions.IO_PATH.loadAllBytes(file2), is(IoFunctions.IO_PATH.loadAllBytes(httpXsd)));
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void moveFile__MissingSource() {
    var file1 = getTempPath("text85.txt");
    var file2 = getTempPath("text86.txt");
    IoFunctions.moveFile(file1, file2);
  }

  @Test(groups = "all")
  public void mkDirs() {
    
    var dir1 = getTempPath("text87");
    assertFalse(Files.isDirectory(dir1));
    IoFunctions.mkDirs(dir1);
    assertTrue(Files.isDirectory(dir1));
    
    var dir2 = getTempPath("text88/folder1/folder2/folder3");
    assertFalse(Files.isDirectory(dir2));
    IoFunctions.mkDirs(dir2);
    assertTrue(Files.isDirectory(dir2));
    
  }
  
  @Test
  public void listPathes() {
    var dir      = getResource("simpleton");
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
  
  @Test(groups = "all")
  public void listZipFile() {
    var zipFile  = getResource("simpleton.zip");
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

  @Test(groups = "all")
  public void listZipFile__FilterDirectories() {
    var zipFile  = getResource("simpleton.zip");
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

  @Test(groups = "all")
  public void zip() {
    var dir      = getResource("simpleton");
    var zipFile  = getTempPath("zipped89.zip");
    assertFalse(Files.isRegularFile(zipFile));
    IoFunctions.zip(zipFile, dir, null);
    assertTrue(Files.isRegularFile(zipFile));
  }
  
  @Test(groups = "all", dependsOnMethods = "zip")
  public void unzip() {

    var dir      = getResource("simpleton");
    var zipFile  = getTempPath("zipped89.zip");
    var unpacked = getTempPath("dir90");
    IoFunctions.unzip(zipFile, unpacked);
    
    var filesFromOrigin    = IoFunctions.listPathes(dir);
    var filesFromUnpacked  = IoFunctions.listPathes(unpacked);
    assertThat(filesFromUnpacked, is(filesFromOrigin));
    
  }
  
  @Test(groups = "all")
  public void locateDirectory() throws Exception {
    
    Path dir1 = IoFunctions.locateDirectory(Iso3166Test.class);
    assertNotNull(dir1);
    
    Path classFile = dir1.resolve("com/kasisoft/libs/common/constants/Iso3166Test.class");
    assertTrue(Files.isRegularFile(classFile));
    
  }
  
} /* ENDCLASS */
