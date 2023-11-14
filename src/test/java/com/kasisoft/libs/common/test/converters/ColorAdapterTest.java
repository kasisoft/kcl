package com.kasisoft.libs.common.test.converters;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

import java.util.*;

import java.awt.*;

/**
 * Tests for the type 'ColorAdapter'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ColorAdapterTest {

  private ColorAdapter adapter = new ColorAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null            , null        ),
      Arguments.of("yellow"        , Color.yellow),
      Arguments.of("blue"          , Color.BLUE  ),
      Arguments.of("#ffffff00"     , Color.yellow),
      Arguments.of("#ff0000ff"     , Color.BLUE  ),
      Arguments.of("#ffff00"       , Color.yellow),
      Arguments.of("#0000ff"       , Color.BLUE  ),
      Arguments.of("rgb(255,255,0)", Color.yellow),
      Arguments.of("rgb(0,0,255)"  , Color.BLUE  )
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Color expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_invalidDecode() {
    return Arrays.asList("12:-13:42", "0:", "a:b", "#", "#zzzzzz", "rgb", "rgb(", "rgb)", "rgb()", "rgb(a,b,c)").stream()
      .map(Arguments::of)
      ;
  }

  @ParameterizedTest
  @MethodSource("data_invalidDecode")
  public void invalidDecode(String value) throws Exception {
    assertThrows(KclException.class, () -> {
      adapter.decode(value);
    });
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null        , null),
      Arguments.of(Color.yellow, "#ffffff00"),
      Arguments.of(Color.BLUE  , "#ff0000ff")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Color value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
