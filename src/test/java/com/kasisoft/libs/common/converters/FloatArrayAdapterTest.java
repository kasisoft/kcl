package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FloatArrayAdapterTest {

  FloatArrayAdapter adapter = new FloatArrayAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null      , null},
      {"3.1"     , new float[] {3.1f}},
      {"3.2,+INF", new float[] {3.2f, Float.POSITIVE_INFINITY}},
      {"4.2,MAX" , new float[] {4.2f, Float.MAX_VALUE}},
      {"-1.2,MIN", new float[] {-1.2f, Float.MIN_VALUE}},
      {"9.1,2.1" , new float[] {9.1f, 2.1f}},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, float[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null              , null},
      {new float[] {7.9f , Float.MAX_VALUE}, "7.9,MAX"},
      {new float[] {-3.1f, Float.NaN}, "-3.1,NaN"}, 
    };
  }
  
  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(float[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
