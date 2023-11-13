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
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class LongAdapterTest {

  private LongAdapter adapter = new LongAdapter();

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null , null),
      Arguments.of("0"  , 0L),
      Arguments.of("13" , 13L),
      Arguments.of("-23", -23L)
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Long expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null , null),
      Arguments.of(0L   , "0"),
      Arguments.of(13L  , "13"),
      Arguments.of(-23L , "-23")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Long value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_invalidDecode() {
    return Stream.of(
      Arguments.of("3.7")
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
