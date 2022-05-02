package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the type 'DoubleArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DoubleArrayAdapterTest {

  private DoubleArrayAdapter adapter = new DoubleArrayAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      {null      , null},
      {"3.1"     , new double[] {3.1}},
      {"3.2,+INF", new double[] {3.2, Double.POSITIVE_INFINITY}},
      {"4.2,MAX" , new double[] {4.2, Double.MAX_VALUE}},
      {"-1.2,MIN", new double[] {-1.2, Double.MIN_VALUE}},
      {"9.1,2.1" , new double[] {9.1, 2.1}},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null              , null},
      {new double[] {7.9 , Double.MAX_VALUE}, "7.9,MAX"},
      {new double[] {-3.1, Double.NaN}, "-3.1,NaN"}, 
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, double[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(double[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
