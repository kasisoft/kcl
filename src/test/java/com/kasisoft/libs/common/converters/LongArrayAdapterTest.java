package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class LongArrayAdapterTest {

  private LongArrayAdapter adapter = new LongArrayAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null    , null},
      {""      , new long[0]},
      {"31"    , new long[] {31}},
      {"-47,12", new long[] {-47, 12}},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, long[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                  , null},
      {new long[0]           , ""},
      {new long[] {79 , 1201}, "79,1201"},
      {new long[] {-31, -128}, "-31,-128"}, 
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(long[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
