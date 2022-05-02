package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BooleanArrayAdapterTest {

  private BooleanArrayAdapter adapter = new BooleanArrayAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null       , null},
      {"true"     , new boolean[] {true}},
      {"false"    , new boolean[] {false}},
      {"ja"       , new boolean[] {true}},
      {"nein"     , new boolean[] {false}},
      {"an"       , new boolean[] {true}},
      {"ein"      , new boolean[] {true}},
      {"aus"      , new boolean[] {false}},
      {"on"       , new boolean[] {true}},
      {"off"      , new boolean[] {false}},
      {"0"        , new boolean[] {false}},
      {"1"        , new boolean[] {true}},
      {"-1"       , new boolean[] {true}},
      {""         , new boolean[] {false}},
      {"true,0"   , new boolean[] {true, false}},
      {"false,ein", new boolean[] {false, true}},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, boolean[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                       , null},
      {new boolean[] {true, true} , "true,true"},
      {new boolean[] {false, true}, "false,true"}, 
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(boolean[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
