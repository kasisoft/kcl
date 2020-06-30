package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'FloatAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FloatAdapterTest {

  FloatAdapter adapter = new FloatAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null   , null},
      {"0.0"  , Float.valueOf(0.0f)},
      {"1.3"  , Float.valueOf(1.3f)},
      {"2.4e8", Float.valueOf(2.4e8f)},
      {"nan"  , Float.valueOf(Float.NaN)},
      {"NAN"  , Float.valueOf(Float.NaN)},
      {"inf"  , Float.valueOf(Float.POSITIVE_INFINITY)},
      {"INF"  , Float.valueOf(Float.POSITIVE_INFINITY)},
      {"+inf" , Float.valueOf(Float.POSITIVE_INFINITY)},
      {"+INF" , Float.valueOf(Float.POSITIVE_INFINITY)},
      {"-inf" , Float.valueOf(Float.NEGATIVE_INFINITY)},
      {"-INF" , Float.valueOf(Float.NEGATIVE_INFINITY)},
      {"max"  , Float.valueOf(Float.MAX_VALUE)},
      {"MAX"  , Float.valueOf(Float.MAX_VALUE)},
      {"min"  , Float.valueOf(Float.MIN_VALUE)},
      {"MIN"  , Float.valueOf(Float.MIN_VALUE)},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Float expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null, null},
      {Float.valueOf(0.0f), "0.0"},
      {Float.valueOf(1.3f), "1.3"},
      {Float.valueOf(2.4e8f), "2.4E8"},
      {Float.valueOf(Float.NaN), "NaN"},
      {Float.valueOf(Float.POSITIVE_INFINITY), "+INF"},
      {Float.valueOf(Float.NEGATIVE_INFINITY), "-INF"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Float value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      {"3,7", 3.7f},
    };
  }

  @Test(dataProvider = "data_invalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Float expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

} /* ENDCLASS */
