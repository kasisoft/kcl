package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'BooleanArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BooleanArrayAdapterTest {

  BooleanArrayAdapter adapter = new BooleanArrayAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
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

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null                        , null},
      {new boolean[] {true, true}  , "true,true"},
      {new boolean[] {false, true} , "false,true"}, 
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, boolean[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(boolean[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
