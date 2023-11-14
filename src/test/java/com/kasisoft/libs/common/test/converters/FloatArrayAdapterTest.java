package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FloatArrayAdapterTest {

  private FloatArrayAdapter adapter = new FloatArrayAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null      , null),
      Arguments.of("3.1"     , new float[] {3.1f}),
      Arguments.of("3.2,+INF", new float[] {3.2f, Float.POSITIVE_INFINITY}),
      Arguments.of("4.2,MAX" , new float[] {4.2f, Float.MAX_VALUE}),
      Arguments.of("-1.2,MIN", new float[] {-1.2f, Float.MIN_VALUE}),
      Arguments.of("9.1,2.1" , new float[] {9.1f, 2.1f})
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, float[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null              , null),
      Arguments.of(new float[] {7.9f , Float.MAX_VALUE}, "7.9,MAX"),
      Arguments.of(new float[] {-3.1f, Float.NaN}, "-3.1,NaN")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(float[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
