package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'DoubleAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoubleAdapterTest {

  DoubleAdapter adapter = new DoubleAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      {null   , null},
      {"0.0"  , Double.valueOf(0.0)},
      {"1.3"  , Double.valueOf(1.3)},
      {"2.4e8", Double.valueOf(2.4e8)},
      {"nan"  , Double.valueOf(Double.NaN)},
      {"NAN"  , Double.valueOf(Double.NaN)},
      {"inf"  , Double.valueOf(Double.POSITIVE_INFINITY)},
      {"INF"  , Double.valueOf(Double.POSITIVE_INFINITY)},
      {"+inf" , Double.valueOf(Double.POSITIVE_INFINITY)},
      {"+INF" , Double.valueOf(Double.POSITIVE_INFINITY)},
      {"-inf" , Double.valueOf(Double.NEGATIVE_INFINITY)},
      {"-INF" , Double.valueOf(Double.NEGATIVE_INFINITY)},
      {"max"  , Double.valueOf(Double.MAX_VALUE)},
      {"MAX"  , Double.valueOf(Double.MAX_VALUE)},
      {"min"  , Double.valueOf(Double.MIN_VALUE)},
      {"MIN"  , Double.valueOf(Double.MIN_VALUE)},
    };
  }

  @DataProvider(name = "dataInvalidDecode")
  public Object[][] dataInvalidDecode() {
    return new Object[][] {
      {"3,7", 3.7},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null, null},
      {Double.valueOf(0.0), "0.0"},
      {Double.valueOf(1.3), "1.3"},
      {Double.valueOf(2.4e8), "2.4E8"},
      {Double.valueOf(Double.NaN), "NaN"},
      {Double.valueOf(Double.POSITIVE_INFINITY), "+INF"},
      {Double.valueOf(Double.NEGATIVE_INFINITY), "-INF"},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Double expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Double value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Double expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

} /* ENDCLASS */