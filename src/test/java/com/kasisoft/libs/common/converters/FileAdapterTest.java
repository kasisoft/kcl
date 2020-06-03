package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'FileAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAdapterTest extends AbstractTestCase {

  FileAdapter adapter = new FileAdapter();

  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                               , null},
      {String.format("%s\\http.xsd", path), getResourceAsFile("http.xsd")},
      {String.format("%s/http.xsd" , path), getResourceAsFile("http.xsd")},
      {String.format("%s\\bibo.txt", path), findResourceAsFile("bibo.txt").orElse(null)},
      {String.format("%s/bibo.txt" , path), findResourceAsFile("bibo.txt").orElse(null)},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                         , null},
      {getResourceAsFile("http.xsd"), String.format("%s/http.xsd", path)},
      {getResourceAsFile("bibo.txt"), String.format("%s/bibo.txt", path)},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, File expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(File value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
