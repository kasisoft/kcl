package com.kasisoft.libs.common.test.converters;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * Tests for the type 'DoubleAdapter'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DoubleAdapterTest {

  private DoubleAdapter adapter = new DoubleAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null   , null),
      Arguments.of("0.0"  , Double.valueOf(0.0)),
      Arguments.of("1.3"  , Double.valueOf(1.3)),
      Arguments.of("2.4e8", Double.valueOf(2.4e8)),
      Arguments.of("nan"  , Double.valueOf(Double.NaN)),
      Arguments.of("NAN"  , Double.valueOf(Double.NaN)),
      Arguments.of("inf"  , Double.valueOf(Double.POSITIVE_INFINITY)),
      Arguments.of("INF"  , Double.valueOf(Double.POSITIVE_INFINITY)),
      Arguments.of("+inf" , Double.valueOf(Double.POSITIVE_INFINITY)),
      Arguments.of("+INF" , Double.valueOf(Double.POSITIVE_INFINITY)),
      Arguments.of("-inf" , Double.valueOf(Double.NEGATIVE_INFINITY)),
      Arguments.of("-INF" , Double.valueOf(Double.NEGATIVE_INFINITY)),
      Arguments.of("max"  , Double.valueOf(Double.MAX_VALUE)),
      Arguments.of("MAX"  , Double.valueOf(Double.MAX_VALUE)),
      Arguments.of("min"  , Double.valueOf(Double.MIN_VALUE)),
      Arguments.of("MIN"  , Double.valueOf(Double.MIN_VALUE))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Double expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null, null),
      Arguments.of(Double.valueOf(0.0), "0.0"),
      Arguments.of(Double.valueOf(1.3), "1.3"),
      Arguments.of(Double.valueOf(2.4e8), "2.4E8"),
      Arguments.of(Double.valueOf(Double.NaN), "NaN"),
      Arguments.of(Double.valueOf(Double.POSITIVE_INFINITY), "+INF"),
      Arguments.of(Double.valueOf(Double.NEGATIVE_INFINITY), "-INF"),
      Arguments.of(Double.valueOf(Double.MIN_VALUE), "MIN"),
      Arguments.of(Double.valueOf(Double.MAX_VALUE), "MAX")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Double value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  public static Stream<Arguments> data_invalidDecode() {
    return Stream.of(
      Arguments.of("3,7")
    );
  }

  @ParameterizedTest
  @MethodSource("data_invalidDecode")
  public void invalidDecode(String value) throws Exception {
    assertThrows(KclException.class, () -> {
      adapter.decode(value);
    });
  }

} /* ENDCLASS */
