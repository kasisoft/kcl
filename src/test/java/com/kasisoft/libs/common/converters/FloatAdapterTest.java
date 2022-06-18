package com.kasisoft.libs.common.converters;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * Tests for the type 'FloatAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FloatAdapterTest {

  private FloatAdapter adapter = new FloatAdapter();
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null   , null),
      Arguments.of("0.0"  , Float.valueOf(0.0f)),
      Arguments.of("1.3"  , Float.valueOf(1.3f)),
      Arguments.of("2.4e8", Float.valueOf(2.4e8f)),
      Arguments.of("nan"  , Float.valueOf(Float.NaN)),
      Arguments.of("NAN"  , Float.valueOf(Float.NaN)),
      Arguments.of("inf"  , Float.valueOf(Float.POSITIVE_INFINITY)),
      Arguments.of("INF"  , Float.valueOf(Float.POSITIVE_INFINITY)),
      Arguments.of("+inf" , Float.valueOf(Float.POSITIVE_INFINITY)),
      Arguments.of("+INF" , Float.valueOf(Float.POSITIVE_INFINITY)),
      Arguments.of("-inf" , Float.valueOf(Float.NEGATIVE_INFINITY)),
      Arguments.of("-INF" , Float.valueOf(Float.NEGATIVE_INFINITY)),
      Arguments.of("max"  , Float.valueOf(Float.MAX_VALUE)),
      Arguments.of("MAX"  , Float.valueOf(Float.MAX_VALUE)),
      Arguments.of("min"  , Float.valueOf(Float.MIN_VALUE)),
      Arguments.of("MIN"  , Float.valueOf(Float.MIN_VALUE))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Float expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null, null),
      Arguments.of(Float.valueOf(0.0f), "0.0"),
      Arguments.of(Float.valueOf(1.3f), "1.3"),
      Arguments.of(Float.valueOf(2.4e8f), "2.4E8"),
      Arguments.of(Float.valueOf(Float.NaN), "NaN"),
      Arguments.of(Float.valueOf(Float.POSITIVE_INFINITY), "+INF"),
      Arguments.of(Float.valueOf(Float.NEGATIVE_INFINITY), "-INF"),
      Arguments.of(Float.valueOf(Float.MIN_VALUE), "MIN"),
      Arguments.of(Float.valueOf(Float.MAX_VALUE), "MAX")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Float value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @SuppressWarnings("exports")
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
