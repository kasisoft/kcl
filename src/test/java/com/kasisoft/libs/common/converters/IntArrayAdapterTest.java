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
public class IntArrayAdapterTest {

  IntArrayAdapter adapter = new IntArrayAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null    , null},
      {"31"    , new int[] {31}},
      {"-47,12", new int[] {-47, 12}},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, int[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                 , null},
      {new int[] {79 , 1201}, "79,1201"},
      {new int[] {-31, -128}, "-31,-128"}, 
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(int[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
