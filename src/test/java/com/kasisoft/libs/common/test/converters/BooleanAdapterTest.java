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
public class BooleanAdapterTest {

  private BooleanAdapter adapter = new BooleanAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null    , null         ),
      Arguments.of("true"  , Boolean.TRUE ),
      Arguments.of("false" , Boolean.FALSE),
      Arguments.of("ja"    , Boolean.TRUE ),
      Arguments.of("nein"  , Boolean.FALSE),
      Arguments.of("an"    , Boolean.TRUE ),
      Arguments.of("ein"   , Boolean.TRUE ),
      Arguments.of("aus"   , Boolean.FALSE),
      Arguments.of("on"    , Boolean.TRUE ),
      Arguments.of("off"   , Boolean.FALSE),
      Arguments.of("0"     , Boolean.FALSE),
      Arguments.of("1"     , Boolean.TRUE ),
      Arguments.of("-1"    , Boolean.TRUE ),
      Arguments.of(""      , Boolean.FALSE)
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Boolean expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null          , null   ),
      Arguments.of(Boolean.TRUE  , "true" ),
      Arguments.of(Boolean.FALSE , "false")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Boolean value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
