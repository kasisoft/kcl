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
public class EnumerationAdapterTest {

  enum LordOfTheRings {

    Gandalf ,
    Bilbo   ,
    Boromir ;

  }

  private EnumerationAdapter<LordOfTheRings> adapter_ci = new EnumerationAdapter<>(LordOfTheRings.class);
  private EnumerationAdapter<LordOfTheRings> adapter    = new EnumerationAdapter<>(LordOfTheRings.class).withIgnoreCase(false);

  public static Stream<Arguments> data_decode_ci() {
    return Stream.of(
      Arguments.of(null     , null),
      Arguments.of("gandalf", LordOfTheRings.Gandalf),
      Arguments.of("bilbo"  , LordOfTheRings.Bilbo),
      Arguments.of("boromir", LordOfTheRings.Boromir)
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode_ci")
  public void decode_ci(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter_ci.decode(value), is(expected));
  }

  public static Stream<Arguments> data_invalidDecode_ci() {
    return Stream.of(
      Arguments.of("gollum")
    );
  }

  @ParameterizedTest
  @MethodSource("data_invalidDecode_ci")
  public void invalidDecode_ci(String value) throws Exception {
    assertThrows(KclException.class, () -> {
      adapter_ci.decode(value);
    });
  }

  public static Stream<Arguments> data_encode_ci() {
    return Stream.of(
      Arguments.of(null                  , null),
      Arguments.of(LordOfTheRings.Gandalf, "Gandalf"),
      Arguments.of(LordOfTheRings.Bilbo  , "Bilbo"),
      Arguments.of(LordOfTheRings.Boromir, "Boromir")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode_ci")
  public void encode_ci(LordOfTheRings value, String expected) throws Exception {
    assertThat(adapter_ci.encode(value), is(expected));
  }

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null     , null),
      Arguments.of("Gandalf", LordOfTheRings.Gandalf),
      Arguments.of("Bilbo"  , LordOfTheRings.Bilbo),
      Arguments.of("Boromir", LordOfTheRings.Boromir)
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, LordOfTheRings expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_invalidDecode() {
    return Stream.of(
      Arguments.of("Gollum")
    );
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
      Arguments.of(null                  , null),
      Arguments.of(LordOfTheRings.Gandalf, "Gandalf"),
      Arguments.of(LordOfTheRings.Bilbo  , "Bilbo"),
      Arguments.of(LordOfTheRings.Boromir, "Boromir")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(LordOfTheRings value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
