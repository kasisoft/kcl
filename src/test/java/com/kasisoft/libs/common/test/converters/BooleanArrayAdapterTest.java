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
public class BooleanArrayAdapterTest {

  private BooleanArrayAdapter adapter = new BooleanArrayAdapter();

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null       , null),
      Arguments.of("true"     , new boolean[] {true}),
      Arguments.of("false"    , new boolean[] {false}),
      Arguments.of("ja"       , new boolean[] {true}),
      Arguments.of("nein"     , new boolean[] {false}),
      Arguments.of("an"       , new boolean[] {true}),
      Arguments.of("ein"      , new boolean[] {true}),
      Arguments.of("aus"      , new boolean[] {false}),
      Arguments.of("on"       , new boolean[] {true}),
      Arguments.of("off"      , new boolean[] {false}),
      Arguments.of("0"        , new boolean[] {false}),
      Arguments.of("1"        , new boolean[] {true}),
      Arguments.of("-1"       , new boolean[] {true}),
      Arguments.of(""         , new boolean[] {false}),
      Arguments.of("true,0"   , new boolean[] {true, false}),
      Arguments.of("false,ein", new boolean[] {false, true})
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, boolean[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                       , null),
      Arguments.of(new boolean[] {true, true} , "true,true"),
      Arguments.of(new boolean[] {false, true}, "false,true")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(boolean[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
