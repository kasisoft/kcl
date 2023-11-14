package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

import java.net.*;

/**
 * Tests for the type 'URLAdapter'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URLAdapterTest {

  private URLAdapter adapter = new URLAdapter();

  public static Stream<Arguments> data_decode() throws Exception {
    return Stream.of(
      Arguments.of(null                       , null                                 ),
      Arguments.of("http://www.amiga-news.de" , URI.create( "http://www.amiga-news.de" ).toURL())
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, URL expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() throws Exception {
    return Stream.of(
      Arguments.of(null                                  , null                       ),
      Arguments.of(URI.create( "http://www.amiga-news.de" ).toURL() , "http://www.amiga-news.de" )
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(URL value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
