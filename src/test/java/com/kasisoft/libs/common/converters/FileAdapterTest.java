package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import java.io.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAdapterTest extends AbstractTestCase {

  FileAdapter adapter = new FileAdapter();

  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    var path = getRootFolder().toString();
    return new Object[][] {
      {null                               , null},
      {String.format("%s\\http.xsd", path), getResourceAsFile("http.xsd")},
      {String.format("%s/http.xsd" , path), getResourceAsFile("http.xsd")},
      {String.format("%s\\bibo.txt", path), findResourceAsFile("bibo.txt").orElse(null)},
      {String.format("%s/bibo.txt" , path), findResourceAsFile("bibo.txt").orElse(null)},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, File expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    var path = getRootFolder().toString();
    return new Object[][] {
      {null                         , null},
      {getResourceAsFile("http.xsd"), String.format("%s/http.xsd", path)},
      {getResourceAsFile("bibo.txt"), String.format("%s/bibo.txt", path)},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(File value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
