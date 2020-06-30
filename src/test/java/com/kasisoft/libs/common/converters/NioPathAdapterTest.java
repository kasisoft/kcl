package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.AbstractTestCase;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.nio.file.Path;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'NioPathAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NioPathAdapterTest extends AbstractTestCase {

  NioPathAdapter adapter = new NioPathAdapter();

  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                                , null                   },
      {String.format( "%s\\http.xsd", path), getResource("http.xsd")},
      {String.format( "%s/http.xsd" , path), getResource("http.xsd")},
      {String.format( "%s\\bibo.txt", path), getResource("bibo.txt")},
      {String.format( "%s/bibo.txt" , path), getResource("bibo.txt")},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Path expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                   , null                              },
      {getResource("http.xsd"), String.format("%s/http.xsd", path)},
      {getResource("bibo.txt"), String.format("%s/bibo.txt", path)},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Path value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
