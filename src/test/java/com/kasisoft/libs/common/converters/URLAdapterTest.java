package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

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

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_decode() throws Exception {
    return Stream.of(
      Arguments.of(null                       , null                                 ),
      Arguments.of("http://www.amiga-news.de" , new URL( "http://www.amiga-news.de" ))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, URL expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_encode() throws Exception {
    return Stream.of(
      Arguments.of(null                                  , null                       ),
      Arguments.of(new URL( "http://www.amiga-news.de" ) , "http://www.amiga-news.de" )
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(URL value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
