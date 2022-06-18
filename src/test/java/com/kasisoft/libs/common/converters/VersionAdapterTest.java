package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.types.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * Tests for the type 'VersionAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class VersionAdapterTest {

  private VersionAdapter adapter = new VersionAdapter().withMicro(true).withQualifier(true);
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() {
    return Stream.of(
      Arguments.of(null             , null                                       ),
      Arguments.of("1.1.1.qualifier", new Version( "1.1.1.qualifier", true, true))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode( String value, Version expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() {
    return Stream.of(
      Arguments.of(null                                              , null             ),
      Arguments.of(new Version(1, 1, Integer.valueOf(1), "qualifier"), "1.1.1.qualifier")
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(Version value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
