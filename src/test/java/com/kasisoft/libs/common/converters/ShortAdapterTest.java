package com.kasisoft.libs.common.converters;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ShortAdapterTest {

  private ShortAdapter adapter = new ShortAdapter();
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null , null),
      Arguments.of("0"  , Short.valueOf((short)0  )),
      Arguments.of("13" , Short.valueOf((short)13 )),
      Arguments.of("-23", Short.valueOf((short)-23))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Short expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                       , null ),
      Arguments.of(Short.valueOf( (short)0  ) , "0"  ),
      Arguments.of(Short.valueOf( (short)13 ) , "13" ),
      Arguments.of(Short.valueOf( (short)-23) , "-23")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Short value, String expected) throws Exception {
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
