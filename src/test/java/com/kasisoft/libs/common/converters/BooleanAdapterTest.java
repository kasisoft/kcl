package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BooleanAdapterTest {

  private BooleanAdapter adapter = new BooleanAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null    , null         },
      {"true"  , Boolean.TRUE },
      {"false" , Boolean.FALSE},
      {"ja"    , Boolean.TRUE },
      {"nein"  , Boolean.FALSE},
      {"an"    , Boolean.TRUE },
      {"ein"   , Boolean.TRUE },
      {"aus"   , Boolean.FALSE},
      {"on"    , Boolean.TRUE },
      {"off"   , Boolean.FALSE},
      {"0"     , Boolean.FALSE},
      {"1"     , Boolean.TRUE },
      {"-1"    , Boolean.TRUE },
      {""      , Boolean.FALSE},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Boolean expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null          , null   },
      {Boolean.TRUE  , "true" },
      {Boolean.FALSE , "false"}, 
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Boolean value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
