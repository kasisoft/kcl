package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * Tests for the type 'DoubleArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DoubleArrayAdapterTest {

  private DoubleArrayAdapter adapter = new DoubleArrayAdapter();
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null      , null),
      Arguments.of("3.1"     , new double[] {3.1}),
      Arguments.of("3.2,+INF", new double[] {3.2, Double.POSITIVE_INFINITY}),
      Arguments.of("4.2,MAX" , new double[] {4.2, Double.MAX_VALUE}),
      Arguments.of("-1.2,MIN", new double[] {-1.2, Double.MIN_VALUE}),
      Arguments.of("9.1,2.1" , new double[] {9.1, 2.1})
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, double[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null              , null),
      Arguments.of(new double[] {7.9 , Double.MAX_VALUE}, "7.9,MAX"),
      Arguments.of(new double[] {-3.1, Double.NaN}, "-3.1,NaN") 
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(double[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
