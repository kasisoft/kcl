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
public class LongArrayAdapterTest {

  private LongArrayAdapter adapter = new LongArrayAdapter();

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null    , null),
      Arguments.of(""      , new long[0]),
      Arguments.of("31"    , new long[] {31}),
      Arguments.of("-47,12", new long[] {-47, 12})
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, long[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                  , null),
      Arguments.of(new long[0]           , ""),
      Arguments.of(new long[] {79 , 1201}, "79,1201"),
      Arguments.of(new long[] {-31, -128}, "-31,-128")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(long[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
