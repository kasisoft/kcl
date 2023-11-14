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
public class StringAdapterTest {

  private StringAdapter adapter = new StringAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null  , null),
      Arguments.of(""    , ""),
      Arguments.of("bla" , "bla")
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, String expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null  , null),
      Arguments.of(""    , ""),
      Arguments.of("bla" , "bla")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(String value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
