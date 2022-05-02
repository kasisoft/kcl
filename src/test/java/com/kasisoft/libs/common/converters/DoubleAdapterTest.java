package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

/**
 * Tests for the type 'DoubleAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DoubleAdapterTest {

  private DoubleAdapter adapter = new DoubleAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
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

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Double expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null, null},
      {Double.valueOf(0.0), "0.0"},
      {Double.valueOf(1.3), "1.3"},
      {Double.valueOf(2.4e8), "2.4E8"},
      {Double.valueOf(Double.NaN), "NaN"},
      {Double.valueOf(Double.POSITIVE_INFINITY), "+INF"},
      {Double.valueOf(Double.NEGATIVE_INFINITY), "-INF"},
      {Double.valueOf(Double.MIN_VALUE), "MIN"},
      {Double.valueOf(Double.MAX_VALUE), "MAX"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Double value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      {"3,7", 3.7},
    };
  }

  @Test(dataProvider = "data_invalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Double expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

} /* ENDCLASS */
