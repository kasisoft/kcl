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

  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                                , null                   },
      {String.format( "%s\\http.xsd", path), getResource("http.xsd")},
      {String.format( "%s/http.xsd" , path), getResource("http.xsd")},
      {String.format( "%s\\bibo.txt", path), getResource("bibo.txt")},
      {String.format( "%s/bibo.txt" , path), getResource("bibo.txt")},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    String path = getRootFolder().toString();
    return new Object[][] {
      {null                   , null                              },
      {getResource("http.xsd"), String.format("%s/http.xsd", path)},
      {getResource("bibo.txt"), String.format("%s/bibo.txt", path)},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Path expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Path value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
