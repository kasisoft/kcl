package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StringAdapterTest {

  StringAdapter adapter = new StringAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null    , null },
      {""      , ""   },
      {"bla"   , "bla"},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, String expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null    , null },
      {""      , ""   },
      {"bla"   , "bla"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(String value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
