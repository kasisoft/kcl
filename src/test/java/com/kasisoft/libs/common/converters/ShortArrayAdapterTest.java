package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the type 'ShortArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortArrayAdapterTest {

  ShortArrayAdapter adapter = new ShortArrayAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null    , null},
      {"31"    , new short[] {31}},
      {"-47,12", new short[] {-47, 12}},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, short[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                   , null},
      {new short[] {79 , 1201}, "79,1201"},
      {new short[] {-31, -128}, "-31,-128"}, 
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(short[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
