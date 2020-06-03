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
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
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

  @DataProvider(name = "dataInvalidDecode")
  public Object[][] dataInvalidDecode() {
    return new Object[][] {
      {"3,7", 3.7f},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
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

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Float expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Float value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Float expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

} /* ENDCLASS */
