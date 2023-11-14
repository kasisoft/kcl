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
 * Tests for the type 'ByteAdapter'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteAdapterTest {

  private ByteAdapter adapter = new ByteAdapter();

  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null, null),
      Arguments.of("0"  , Byte.valueOf((byte) 0)),
      Arguments.of("13" , Byte.valueOf((byte) 13)),
      Arguments.of("-23", Byte.valueOf((byte) -23))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, Byte expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                    , null),
      Arguments.of(Byte.valueOf((byte) 0)  , "0"),
      Arguments.of(Byte.valueOf((byte) 13) , "13"),
      Arguments.of(Byte.valueOf((byte) -23), "-23")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Byte value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  public static Stream<Arguments> data_invalidDecode() {
    return Stream.of(
      Arguments.of("3.7")
    );
  }

  @ParameterizedTest
  @MethodSource("data_invalidDecode")
  public void invalidDecode(String value) throws Exception {
    assertThrows(KclException.class, () -> {
      assertNull(adapter.decode(value));
    });
  }

} /* ENDCLASS */
