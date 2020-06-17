package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.fail;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.utils.IoFunctions.IoSupport;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

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

  private static final String CONTENT_FOR_STREAMS = """
                                                    Dies ist mein Text mit Umlauten.
                                                    Größer wird der Kram hier aber auch nicht.
                                                    Auf dieser Zeile liest man nur Quark.
                                                    """;

  private static final String CONTENT_FOR_READERS = """
                                                    Kleener Text wirkt abgehezt.
                                                    Drum isses schön sich auszuruhen.
                                                    Huppifluppi ist ein ß.
                                                    """;

  @DataProvider(name = "data_newOutputStream")
  public Object[][] data_newOutputStream() throws Exception {
    Path file1 = getTempPath("text1.txt");
    Path file2 = getTempPath("text2.txt");
    Path file3 = getTempPath("text3.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
    
  @Test(groups = "all", dataProvider = "data_newOutputStream")
  public <T> void newOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
    byte[] asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    try (OutputStream outstream = ioSupport.newOutputStream(destination)) {
      outstream.write(asBytes);
    }
  }
  
  @Test(groups = "all", dataProvider = "data_newOutputStream", dependsOnMethods = "newOutputStream")
  public <T> void newInputStream(IoSupport<T> ioSupport, T source) throws Exception {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    try (InputStream instream = ioSupport.newInputStream(source)) {
      IoFunctions.copy(instream, byteout);
    }
    String asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }
  
  @DataProvider(name = "data_newWriter")
  public Object[][] data_newWriter() throws Exception {
    Path file1 = getTempPath("text4.txt");
    Path file2 = getTempPath("text5.txt");
    Path file3 = getTempPath("text6.txt");
    Path file4 = getTempPath("text7.txt");
    Path file5 = getTempPath("text8.txt");
    Path file6 = getTempPath("text9.txt");
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
    try (Writer writer = ioSupport.newWriter(destination)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @Test(groups = "all", dataProvider = "data_newWriter", dependsOnMethods = "newWriter")
  public <T> void newReader(IoSupport<T> ioSupport, T destination, Encoding ignoredEncoding) throws Exception {
    StringWriter writer = new StringWriter();
    try (Reader reader = ioSupport.newReader(destination)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @DataProvider(name = "data_newWriter__WithEncoding")
  public Object[][] data_newWriter__WithEncoding() throws Exception {
    Path file1 = getTempPath("text10.txt");
    Path file2 = getTempPath("text11.txt");
    Path file3 = getTempPath("text12.txt");
    Path file4 = getTempPath("text13.txt");
    Path file5 = getTempPath("text14.txt");
    Path file6 = getTempPath("text15.txt");
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
    try (Writer writer = ioSupport.newWriter(destination, encoding)) {
      writer.write(CONTENT_FOR_READERS);
    }
  }

  @Test(groups = "all", dataProvider = "data_newWriter__WithEncoding", dependsOnMethods = "newWriter__WithEncoding")
  public <T> void newReader__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    StringWriter writer = new StringWriter();
    try (Reader reader = ioSupport.newReader(destination, encoding)) {
      IoFunctions.copy(reader, writer);
    }
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }

  @DataProvider(name = "data_forOutputStreamDo")
  public Object[][] data_forOutputStreamDo() throws Exception {
    Path file1 = getTempPath("text16.txt");
    Path file2 = getTempPath("text17.txt");
    Path file3 = getTempPath("text18.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStreamDo")
  public <T> void forOutputStreamDo(IoSupport<T> ioSupport, T destination) throws Exception {
    byte[] asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
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
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    ioSupport.forInputStreamDo(source, $ -> {
      IoFunctions.copy($, byteout);
    });
    String asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
  }
  
  @DataProvider(name = "data_forOutputStreamDo__WithArg")
  public Object[][] data_forOutputStreamDo__WithArg() throws Exception {
    Path file1 = getTempPath("text19.txt");
    Path file2 = getTempPath("text20.txt");
    Path file3 = getTempPath("text21.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStreamDo__WithArg")
  public <T> void forOutputStreamDo__WithArg(IoSupport<T> ioSupport, T destination) throws Exception {
    byte[] asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    ioSupport.forOutputStreamDo(destination, "DUMMY", ($w, $arg) -> {
      try {
        $w.write(asBytes);
        $w.write(Encoding.UTF8.encode($arg));
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }

  @Test(groups = "all", dataProvider = "data_forOutputStreamDo__WithArg", dependsOnMethods = "forOutputStreamDo__WithArg")
  public <T> void forInputStreamDo__WithArg(IoSupport<T> ioSupport, T source) throws Exception {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    ioSupport.forInputStreamDo(source, "DUMMY", ($r, $arg) -> {
      IoFunctions.copy($r, byteout);
    });
    String asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS + "DUMMY"));
  }  
  
  @DataProvider(name = "data_forOutputStream")
  public Object[][] data_forOutputStream() throws Exception {
    Path file1 = getTempPath("text22.txt");
    Path file2 = getTempPath("text23.txt");
    Path file3 = getTempPath("text24.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStream")
  public <T> void forOutputStream(IoSupport<T> ioSupport, T destination) throws Exception {
    byte[] asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    String result  = ioSupport.forOutputStream(destination, $ -> {
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

  @Test(groups = "all", dataProvider = "data_forOutputStream", dependsOnMethods = "forOutputStream")
  public <T> void forInputStream(IoSupport<T> ioSupport, T source) throws Exception {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    String                result  = ioSupport.forInputStream(source, $ -> {
      IoFunctions.copy($, byteout);
      return "DUMMY";
    });
    String asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
    assertThat(result, is("DUMMY"));
  }
  
  @DataProvider(name = "data_forOutputStream__WithArg")
  public Object[][] data_forOutputStream__WithArg() throws Exception {
    Path file1 = getTempPath("text25.txt");
    Path file2 = getTempPath("text26.txt");
    Path file3 = getTempPath("text27.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forOutputStream__WithArg")
  public <T> void forOutputStream__WithArg(IoSupport<T> ioSupport, T destination) throws Exception {
    byte[] asBytes = Encoding.UTF8.encode(CONTENT_FOR_STREAMS);
    String result  = ioSupport.forOutputStream(destination, "DUMMY", ($w, $arg) -> {
      try {
        $w.write(asBytes);
        return $arg;
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
        return null;
      }
    });
    assertThat(result, is("DUMMY"));
  }

  @Test(groups = "all", dataProvider = "data_forOutputStream__WithArg", dependsOnMethods = "forOutputStream__WithArg")
  public <T> void forInputStream__WithArg(IoSupport<T> ioSupport, T source) throws Exception {
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    String                result  = ioSupport.forInputStream(source, "DUMMY", ($r, $arg) -> {
      IoFunctions.copy($r, byteout);
      return $arg;
    });
    String asText = Encoding.UTF8.decode(byteout.toByteArray());
    assertThat(asText, is(CONTENT_FOR_STREAMS));
    assertThat(result, is("DUMMY"));
  }  
  
  
  @DataProvider(name = "data_forWriterDo")
  public Object[][] data_forWriterDo() throws Exception {
    Path file1 = getTempPath("text28.txt");
    Path file2 = getTempPath("text29.txt");
    Path file3 = getTempPath("text30.txt");
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

  @Test(groups = "all", dataProvider = "data_forWriterDo", dependsOnMethods = "forWriterDo")
  public <T> void forReaderDo(IoSupport<T> ioSupport, T source) throws Exception {
    StringWriter writer = new StringWriter();
    ioSupport.forReaderDo(source, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @DataProvider(name = "data_forWriterDo__WithArg")
  public Object[][] data_forWriterDo__WithArg() throws Exception {
    Path file1 = getTempPath("text31.txt");
    Path file2 = getTempPath("text32.txt");
    Path file3 = getTempPath("text33.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo__WithArg")
  public <T> void forWriterDo__WithArg(IoSupport<T> ioSupport, T destination) throws Exception {
    ioSupport.forWriterDo(destination, "DUMMY", ($w, $arg) -> {
      try {
        $w.write(CONTENT_FOR_READERS);
        $w.write($arg);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo__WithArg", dependsOnMethods = "forWriterDo__WithArg")
  public <T> void forReaderDo__WithArg(IoSupport<T> ioSupport, T source) throws Exception {
    StringWriter writer = new StringWriter();
    ioSupport.forReaderDo(source, "DUMMY", ($r, $arg) -> {
      IoFunctions.copy($r, writer);
      IoFunctions.copy(new StringReader($arg), writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS + "DUMMY" + "DUMMY"));
  }  
  
  @DataProvider(name = "data_forWriter")
  public Object[][] data_forWriter() throws Exception {
    Path file1 = getTempPath("text34.txt");
    Path file2 = getTempPath("text35.txt");
    Path file3 = getTempPath("text36.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriter")
  public <T> void forWriter(IoSupport<T> ioSupport, T destination) throws Exception {
    String result  = ioSupport.forWriter(destination, $ -> {
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

  @Test(groups = "all", dataProvider = "data_forWriter", dependsOnMethods = "forWriter")
  public <T> void forReader(IoSupport<T> ioSupport, T source) throws Exception {
    StringWriter writer = new StringWriter();
    String       result  = ioSupport.forReader(source, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }
  
  @DataProvider(name = "data_forWriter__WithArg")
  public Object[][] data_forWriter__WithArg() throws Exception {
    Path file1 = getTempPath("text37.txt");
    Path file2 = getTempPath("text38.txt");
    Path file3 = getTempPath("text39.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1 },
      { IoFunctions.IO_FILE, file2.toFile() },
      { IoFunctions.IO_URI,  file3.toUri() },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriter__WithArg")
  public <T> void forWriter__WithArg(IoSupport<T> ioSupport, T destination) throws Exception {
    String result  = ioSupport.forWriter(destination, "DUMMY", ($w, $arg) -> {
      try {
        $w.write(CONTENT_FOR_READERS);
        return $arg;
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
        return null;
      }
    });
    assertThat(result, is("DUMMY"));
  }

  @Test(groups = "all", dataProvider = "data_forWriter__WithArg", dependsOnMethods = "forWriter__WithArg")
  public <T> void forReader__WithArg(IoSupport<T> ioSupport, T source) throws Exception {
    StringWriter writer = new StringWriter();
    String       result  = ioSupport.forReader(source, "DUMMY", ($r, $arg) -> {
      IoFunctions.copy($r, writer);
      return $arg;
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }  
  
  /*** */
  
  @DataProvider(name = "data_forWriterDo__WithEncoding")
  public Object[][] data_forWriterDo__WithEncoding() throws Exception {
    Path file1 = getTempPath("text40.txt");
    Path file2 = getTempPath("text41.txt");
    Path file3 = getTempPath("text42.txt");
    Path file4 = getTempPath("text43.txt");
    Path file5 = getTempPath("text44.txt");
    Path file6 = getTempPath("text45.txt");
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
    StringWriter writer = new StringWriter();
    ioSupport.forReaderDo(source, encoding, $ -> {
      IoFunctions.copy($, writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
  }
  
  @DataProvider(name = "data_forWriterDo__WithArg__WithEncoding")
  public Object[][] data_forWriterDo__WithArg__WithEncoding() throws Exception {
    Path file1 = getTempPath("text46.txt");
    Path file2 = getTempPath("text47.txt");
    Path file3 = getTempPath("text48.txt");
    Path file4 = getTempPath("text49.txt");
    Path file5 = getTempPath("text50.txt");
    Path file6 = getTempPath("text51.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.UTF8 },
      { IoFunctions.IO_FILE, file2.toFile(), Encoding.ISO88591 },
      { IoFunctions.IO_URI,  file3.toUri(), Encoding.UTF8 },
      { IoFunctions.IO_PATH, file4, Encoding.ISO88591 },
      { IoFunctions.IO_FILE, file5.toFile() , Encoding.UTF8},
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591 },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo__WithArg__WithEncoding")
  public <T> void forWriterDo__WithArg__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    ioSupport.forWriterDo(destination, "DUMMY", encoding, ($w, $arg) -> {
      try {
        $w.write(CONTENT_FOR_READERS);
        $w.write($arg);
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
      }
    });
  }
  
  @Test(groups = "all", dataProvider = "data_forWriterDo__WithArg__WithEncoding", dependsOnMethods = "forWriterDo__WithArg__WithEncoding")
  public <T> void forReaderDo__WithArg__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    StringWriter writer = new StringWriter();
    ioSupport.forReaderDo(source, "DUMMY", encoding, ($r, $arg) -> {
      IoFunctions.copy($r, writer);
      IoFunctions.copy(new StringReader($arg), writer);
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS + "DUMMY" + "DUMMY"));
  }  
  
  @DataProvider(name = "data_forWriter__WithEncoding")
  public Object[][] data_forWriter__WithEncoding() throws Exception {
    Path file1 = getTempPath("text52.txt");
    Path file2 = getTempPath("text53.txt");
    Path file3 = getTempPath("text54.txt");
    Path file4 = getTempPath("text55.txt");
    Path file5 = getTempPath("text56.txt");
    Path file6 = getTempPath("text57.txt");
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
    String result  = ioSupport.forWriter(destination, encoding, $ -> {
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
    StringWriter writer = new StringWriter();
    String       result  = ioSupport.forReader(source, encoding, $ -> {
      IoFunctions.copy($, writer);
      return "DUMMY";
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }
  
  @DataProvider(name = "data_forWriter__WithArg__WithEncoding")
  public Object[][] data_forWriter__WithArg__WithEncoding() throws Exception {
    Path file1 = getTempPath("text58.txt");
    Path file2 = getTempPath("text59.txt");
    Path file3 = getTempPath("text60.txt");
    Path file4 = getTempPath("text61.txt");
    Path file5 = getTempPath("text62.txt");
    Path file6 = getTempPath("text63.txt");
    return new Object[][] {
      { IoFunctions.IO_PATH, file1, Encoding.UTF8 },
      { IoFunctions.IO_PATH, file2, Encoding.ISO88591 },
      { IoFunctions.IO_FILE, file3.toFile(), Encoding.UTF8 },
      { IoFunctions.IO_FILE, file4.toFile(), Encoding.ISO88591 },
      { IoFunctions.IO_URI,  file5.toUri(), Encoding.UTF8 },
      { IoFunctions.IO_URI,  file6.toUri(), Encoding.ISO88591 },
    };
  }
  
  @Test(groups = "all", dataProvider = "data_forWriter__WithArg__WithEncoding")
  public <T> void forWriter__WithArg__WithEncoding(IoSupport<T> ioSupport, T destination, Encoding encoding) throws Exception {
    String result  = ioSupport.forWriter(destination, "DUMMY", encoding, ($w, $arg) -> {
      try {
        $w.write(CONTENT_FOR_READERS);
        return $arg;
      } catch (Exception ex) {
        fail(ex.getLocalizedMessage());
        return null;
      }
    });
    assertThat(result, is("DUMMY"));
  }

  @Test(groups = "all", dataProvider = "data_forWriter__WithArg__WithEncoding", dependsOnMethods = "forWriter__WithArg__WithEncoding")
  public <T> void forReader__WithArg__WithEncoding(IoSupport<T> ioSupport, T source, Encoding encoding) throws Exception {
    StringWriter writer = new StringWriter();
    String       result  = ioSupport.forReader(source, "DUMMY", encoding, ($r, $arg) -> {
      IoFunctions.copy($r, writer);
      return $arg;
    });
    assertThat(writer.toString(), is(CONTENT_FOR_READERS));
    assertThat(result, is("DUMMY"));
  }  

  /// WRAPPER
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
//  File   testdata;
//  File   directory;
//  File   unpackeddir;
//  File   destfile;
//  Path   testdataAsPath;
//  
//  @BeforeTest
//  public void setup() {
//    testdata        = Utilities.getTestdataDir();
//    destfile        = IoFunctions.newTempFile( "file-", ".zip" );
//    directory       = IoFunctions.newTempFile( "temp-" );
//    directory.mkdirs();
//    unpackeddir     = IoFunctions.newTempFile( "temp-" );
//    unpackeddir.mkdirs();
//    Utilities.createFileSystemStructure( directory );
//    testdataAsPath  = Paths.get( testdata.toURI() );
//  }
//
//  @Test(groups="all")
//  public void newTempFile() {
//    
//    File file1 = IoFunctions.newTempFile();
//    assertThat( file1, is( notNullValue() ) );
//    
//    File file2 = IoFunctions.newTempFile( "frog" );
//    assertThat( file2, is( notNullValue() ) );
//    assertTrue( file2.getName().startsWith( "frog" ) );
//  
//    File file3 = IoFunctions.newTempFile( "frog", ".txt" );
//    assertThat( file3, is( notNullValue() ) );
//    assertTrue( file3.getName().startsWith( "frog" ) );
//    assertTrue( file3.getName().endsWith( ".txt" ) );
//    
//  }
//  
//  @Test(groups="all")
//  public void allocateAndRelease() {
//    
//    byte[]  data1       = PByte.allocate( null );
//    assertThat( data1, is( notNullValue() ) );
//
//    PByte.release( data1 );
//    PByte.release( data1 ); // just to be sure that double release won't do any bad
//    
//    byte[]  data2       = PByte.allocate( null );
//    assertThat( data2, is( notNullValue() ) );
//    assertThat( data2, is( data1 ) );
//
//    byte[]  data3       = PByte.allocate( Integer.valueOf( 8192 ) );
//    assertThat( data3, is( notNullValue() ) );
//    assertTrue( data3.length >= 8192 );
//
//    PByte.release( data3 );
//    PByte.release( data3 ); // just to be sure that double release won't do any bad
//
//    byte[]  data4       = PByte.allocate( Integer.valueOf( 8192 ) );
//    assertThat( data4, is( notNullValue() ) );
//    assertThat( data4, is( data3 ) );
//    
//  }
//
//  @Test(groups="all")
//  public void copyStreams() {
//    
//    byte[]                data    = "MY DATA".getBytes();
//    ByteArrayInputStream  bytein  = new ByteArrayInputStream( data );
//    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
//    IoFunctions.copy( bytein, byteout, null );
//    assertThat( byteout.toByteArray(), is( data ) );
//    
//    bytein.reset();
//    byteout.reset();
//    
//    byte[]                buffer  = new byte[1024];
//    IoFunctions.copy( bytein, byteout, buffer, null );
//    assertThat( byteout.toByteArray(), is( data ) );
//
//  }
//
//  @Test(groups="all")
//  public void copyFiles() {
//    
//    File    tempfile1 = Utilities.createRandomBytesFile();
//    File    tempfile2 = IoFunctions.newTempFile();
//    IoFunctions.copy( tempfile1, tempfile2 );
//    AssertExtension.assertEquals( tempfile2, tempfile1 );
//    
//  }
//  
//  @Test(groups="all")
//  public void copyDir() {
//    
//    File tempdir1 = Utilities.createRandomDirectory();
//    File tempdir2 = IoFunctions.newTempFile();
//    IoFunctions.copyDir( tempdir1, tempdir2, true );
//    AssertExtension.assertEquals( tempdir2, tempdir1 );
//    
//  }
//  
//  @Test(groups="all")
//  public void copyReaderToWriter() throws IOException {
//    
//    char[]           data    = "MY DATA".toCharArray();
//    CharArrayReader  charin  = new CharArrayReader( data );
//    CharArrayWriter  charout = new CharArrayWriter();
//    IoFunctions.copy( charin, charout, null );
//    assertThat( charout.toCharArray(), is( data ) );
//    
//    charin.reset();
//    charout.reset();
//    
//    char[]           buffer  = new char[1024];
//    IoFunctions.copy( charin, charout, buffer, null );
//    assertThat( charout.toCharArray(), is( data ) );
//
//  }
//  
//  @Test(groups="all")
//  public void loadAndWriteBytes() {
//    
//    byte[] data     = Utilities.createByteBlock();
//    File   tempfile = IoFunctions.newTempFile();
//    FILE_OUTPUTSTREAM_EX.forOutputStreamDo( tempfile, $ -> $.eWrite( data ) );
//    
//    byte[] loaded1  = IoFunctions.loadBytes( tempfile, null );
//    assertThat( loaded1, is( data ) );
//
//    byte[] loaded2  = IoFunctions.loadBytes( tempfile, Integer.valueOf( 1024 ) );
//    assertThat( loaded2, is( data ) );
//
//    byte[] loaded3  = IoFunctions.loadBytes( new ByteArrayInputStream( data ), null );
//    assertThat( loaded3, is( data ) );
//
//    byte[] loaded4  = IoFunctions.loadBytes(  new ByteArrayInputStream( data ), Integer.valueOf( 1024 ) );
//    assertThat( loaded4, is( data ) );
//
//  }
//
//  @Test(groups="all")
//  public void loadChars() {
//    
//    char[] data     = Utilities.createCharacterBlock();
//    File   tempfile = IoFunctions.newTempFile();
//    FILE_WRITER_EX.forWriterDo( tempfile, $ -> $.eWrite( data ) );
//    
//    char[] loaded1  = IoFunctions.loadChars( tempfile, null, Encoding.UTF8 );
//    assertThat( loaded1, is( data ) );
//
//    char[] loaded2  = IoFunctions.loadChars( tempfile, Integer.valueOf( 1024 ), Encoding.UTF8 );
//    assertThat( loaded2, is( data ) );
//
//    char[] loaded3  = IoFunctions.loadChars( new CharArrayReader( data ), null );
//    assertThat( loaded3, is( data ) );
//
//    char[] loaded4  = IoFunctions.loadChars(  new CharArrayReader( data ), Integer.valueOf( 1024 ) );
//    assertThat( loaded4, is( data ) );
//
//  }
//
//  @SuppressWarnings("resource")
//  @Test(groups="all")
//  public void loadTest() {
//    
//    File          testfile  = new File( testdata, "testfile.txt" );
//  
//    List<String>  text1     = FILE_READER_EX.forReader( testfile, $ -> IoFunctions.readText( $, false, true ) ).orElse( null );
//    assertThat( text1, is( notNullValue() ) );
//    assertThat( 7, is( text1.size() ) );
//    assertThat( text1.toArray(), is( new Object[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } ) );
//
//    List<String>  text2     = FILE_READER_EX.forReader( testfile, $ -> IoFunctions.readText( $, false, false ) ).orElse( null );
//    assertThat( text2, is( notNullValue() ) );
//    assertThat( 4, is( text2.size() ) );
//    assertThat( text2.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } ) );
//
//    List<String>  text3     = FILE_READER_EX.forReader( testfile, $ -> IoFunctions.readText( $, true, false ) ).orElse( null );
//    assertThat( text3, is( notNullValue() ) );
//    assertThat( 4, is( text3.size() ) );
//    assertThat( text3.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } ) );
//
//    byte[]        textdata  = IoFunctions.loadBytes( testfile, null );
//
//    Reader        reader1   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
//    List<String>  text4     = IoFunctions.readText( reader1, false, true );
//    assertThat( text4, is( notNullValue() ) );
//    assertThat( 7, is( text4.size() ) );
//    assertThat( text4.toArray(), is( new Object[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } ) );
//
//    Reader        reader2   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
//    List<String>  text5     = IoFunctions.readText( reader2, false, false );
//    assertThat( text5, is( notNullValue() ) );
//    assertThat( 4, is( text5.size() ) );
//    assertThat( text5.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } ) );
//
//    Reader        reader3   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
//    List<String>  text6     = IoFunctions.readText( reader3, true, false );
//    assertThat( text6, is( notNullValue() ) );
//    assertThat( 4, is( text6.size() ) );
//    assertThat( text6.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } ) );
//
//  }
//  
//  @Test(groups="all")
//  public void skip() {
//    
//    String                str     = "BLA BLUB WAS HERE";
//    
//    ByteArrayInputStream  bytein  = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
//    IoFunctions.skip( bytein, 4 );
//    String                result1 = Encoding.UTF8.decode( IoFunctions.loadBytes( bytein, null ) );
//    assertThat( result1, is( "BLUB WAS HERE" ) );
//    
//    CharArrayReader       charin  = new CharArrayReader( str.toCharArray() );
//    IoFunctions.skip( charin, 4 );
//    String                result2 = new String( IoFunctions.loadChars( charin, null ) );
//    assertThat( result2, is( "BLUB WAS HERE" ) );
//    
//  }
//
//  @Test(groups="all")
//  public void loadFragment() {
//    
//    String                str       = "BLA BLUB WAS HERE";
//    
//    ByteArrayInputStream  bytein    = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
//    String                result1   = Encoding.UTF8.decode( IoFunctions.loadFragment( bytein, 4, 4 ) );
//    assertThat( result1, is( "BLUB" ) );
//    
//    File                  testfile  = new File( testdata, "testfile.txt" );
//    String                result2   = Encoding.UTF8.decode( FILE_INPUTSTREAM_EX.forInputStream( testfile, $ -> IoFunctions.loadFragment( $, 15, 6 ) ).orElse( null ) );
//    assertThat( result2, is( "LINE 1" ) );
//    
//  }
//
//  @Test(groups="all")
//  public void crc32() {
//   
//    File    testfile  = new File( testdata, "testfile.gz" );
//    
//    assertThat( FILE_INPUTSTREAM_EX.forInputStream( testfile, IoFunctions::crc32 ).orElse(0L), is( 1699530864L ) );
//    
//    byte[]  data      = IoFunctions.loadBytes( testfile, null );
//    assertThat( IoFunctions.crc32( new ByteArrayInputStream( data ) ), is( 1699530864L ) );
//    
//  }
//
//  @Test(groups="all")
//  public void delete() {
//    File        tempdir = IoFunctions.newTempFile();
//    tempdir.mkdirs();
//    List<File>  files   = Utilities.createFileSystemStructure( tempdir );
//    IoFunctions.delete( tempdir );
//    for( File file : files ) {
//      Assert.assertFalse( file.exists() );
//    }
//  }
//  
//  @SuppressWarnings("resource")
//  @Test(groups="all")
//  public void writeText() {
//    
//    List<String> lines = new ArrayList<>();
//    lines.add( "FRED" );
//    lines.add( "FLINTSTONES" );
//    lines.add( "ANIMAL" );
//    lines.add( "IS" );
//    lines.add( "NAMED" );
//    lines.add( "DINO" );
//    
//    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
//    IoFunctions.writeText( byteout, lines, Encoding.UTF8 );
//    
//    byte[]       data1      = byteout.toByteArray();
//    Reader       reader1    = Encoding.UTF8.openReader( new ByteArrayInputStream( data1 ) );
//    List<String> loaded1    = IoFunctions.readText( reader1, false, true );
//    assertThat( loaded1, is( lines ) );
//    
//    File         tempfile1  = IoFunctions.newTempFile();
//    FILE_WRITER_EX.forWriterDo( tempfile1, $ -> IoFunctions.writeText( $, lines ) );
//    List<String> loaded2    = FILE_READER_EX.forReader( tempfile1, $ -> IoFunctions.readText( $, false, true ) ).orElse( null );
//    assertThat( loaded2, is( lines ) );
//    
//    StringBuilder buffer    = new StringBuilder();
//    for( int i = 0; i < lines.size(); i++ ) {
//      buffer.append( lines.get(i) );
//      buffer.append( "\n" );
//    }
//    
//    File         tempfile2  = IoFunctions.newTempFile();
//    FILE_WRITER_EX.forWriterDo( tempfile2, $ -> IoFunctions.writeText( $, lines ) );
//    
//    List<String> loaded3    = FILE_READER_EX.forReader( tempfile2, $ -> IoFunctions.readText( $, false, true ) ).orElse( null );
//    assertThat( loaded3, is( lines ) );
//    
//  }
//
//  @Test(groups="all")
//  public void readTextAsIs() {
//    
//    StringBuilder builder = new StringBuilder();
//    builder.append( "FRED\n" );
//    builder.append( "FLINTSTONES\r\n\n" );
//    builder.append( "ANIMAL" );
//    builder.append( "IS\n\n" );
//    builder.append( "NAMED\\n" );
//    builder.append( "DINO" );
//    
//    String               text    = builder.toString();
//    ByteArrayInputStream bytein  = new ByteArrayInputStream( Encoding.UTF8.encode( text ) ); 
//    String               current = INPUTSTREAM_READER_EX.forReader( bytein, IoFunctions::readTextFully ).orElse( null );
//    assertThat( current, is( text ) );
//    
//  }
//
//  
//  @Test(groups="all")
//  public void listRecursive() {
//    FileFilter filter = new FileFilter() {
//      @Override
//      public boolean accept( File pathname ) {
//        if( pathname.isDirectory() ) {
//          return ! "_svn".equalsIgnoreCase( pathname.getName() );
//        }
//        return true;
//      }
//    };
//    
//    List<File> list1  = IoFunctions.listRecursive( testdata, filter );
//    assertThat( list1.size(), is( 26 ) );
//
//    List<File> list2  = IoFunctions.listRecursive( testdata, filter, true, false );
//    assertThat( list2.size(), is( 19 ) );
//
//    List<File> list3  = IoFunctions.listRecursive( testdata, filter, false, true );
//    assertThat( list3.size(), is( 7 ) );
//
//  }
//  
//  @Test(groups="all")
//  public void listPathes() {
//    List<String> list  = IoFunctions.listPathes( testdataAsPath, (p,r) -> ! p.endsWith( "_svn" ) );
//    list.forEach( System.err::println );
//    assertThat( list.size(), is( 22 ) );
//  }
//  
//  
//  @Test(groups="all")
//  public void zip() {
//    assertTrue( IoFunctions.zip( destfile, directory, null ) );
//  }
//  
//  @Test(dependsOnMethods="zip",groups="all")
//  public void unzip() {
//    Assert.assertTrue( IoFunctions.unzip( destfile, unpackeddir, null ) );
//  }
//
//  @Test(groups="all")
//  public void locateDirectory() throws IOException {
//    
//    File dir1      = IoFunctions.locateDirectory( Iso3166Test.class );
//    assertThat( dir1, is( notNullValue() ) );
//    File current1  = new File( "target/classes/java/test" ).getCanonicalFile();
//
//    assertTrue( dir1.equals( current1 ) );
//
//    File dir2      = IoFunctions.locateDirectory( Iso3166Test.class, "target", "classes", "java", "test" );
//    assertThat( dir2, is( notNullValue() ) );
//    File current2  = new File( "." ).getCanonicalFile();
//    System.err.println("### " + current2 );
//    System.err.println("### " + dir2 );
//    assertTrue( dir2.equals( current2 ) );
//    
//  }
//  
//  @Test(groups="all")
//  public void newOutputStream() throws IOException {
//    
//    Path dest = Files.createTempFile( "sample", ".txt" );
//    try( OutputStream outstream = IoFunctions.newOutputStream( dest ) ) {
//      outstream.write( Encoding.UTF8.encode( "Hello World with some greetings" ) );
//    }
//    String text1 = IoFunctions.readTextFully( dest );
//    assertThat( text1, is( "Hello World with some greetings" ) );
//    
//    try( OutputStream outstream = IoFunctions.newOutputStream( dest ) ) {
//      outstream.write( Encoding.UTF8.encode( "Hello World" ) );
//    }
//    String text2 = IoFunctions.readTextFully( dest );
//    assertThat( text2, is( "Hello World" ) );
//  
//  }
  
} /* ENDCLASS */
