package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * Tests for the type 'ShortArrayAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ShortArrayAdapterTest {

  private ShortArrayAdapter adapter = new ShortArrayAdapter();
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null    , null),
      Arguments.of("31"    , new short[] {31}),
      Arguments.of("-47,12", new short[] {-47, 12})
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, short[] expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                   , null),
      Arguments.of(new short[] {79 , 1201}, "79,1201"),
      Arguments.of(new short[] {-31, -128}, "-31,-128") 
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(short[] value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
