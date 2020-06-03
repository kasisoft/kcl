package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'FloatArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FloatArrayAdapterTest {

  FloatArrayAdapter adapter = new FloatArrayAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      {null      , null},
      {"3.1"     , new float[] {3.1f}},
      {"3.2,+INF", new float[] {3.2f, Float.POSITIVE_INFINITY}},
      {"4.2,MAX" , new float[] {4.2f, Float.MAX_VALUE}},
      {"-1.2,MIN", new float[] {-1.2f, Float.MIN_VALUE}},
      {"9.1,2.1" , new float[] {9.1f, 2.1f}},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null              , null},
      {new float[] {7.9f , Float.MAX_VALUE}, "7.9,MAX"},
      {new float[] {-3.1f, Float.NaN}, "-3.1,NaN"}, 
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, float[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(float[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
