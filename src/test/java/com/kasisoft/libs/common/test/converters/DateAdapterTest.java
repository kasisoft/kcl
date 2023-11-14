package com.kasisoft.libs.common.test.converters;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import java.util.stream.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
public class DateAdapterTest {

  private DateAdapter adapter = new DateAdapter().withPattern("dd'.'MM'.'yyyy");

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null        , null),
      Arguments.of("12.04.1987", new Date(87, 3, 12)),
      Arguments.of("03.07.1964", new Date(64, 6, 3))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Date expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null               , null),
      Arguments.of(new Date(87, 3, 12), "12.04.1987"),
      Arguments.of(new Date(64, 6, 3) , "03.07.1964")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Date value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test
  public void decodeFailure() {
    assertThrows(KclException.class, () -> {
      adapter.decode("jfjjfjfjf");
    });
  }

} /* ENDCLASS */
