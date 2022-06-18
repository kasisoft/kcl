package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class RegExpressionsTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> extractYoutubeIdData() {
    return Stream.of(
      Arguments.of("https://www.youtube.com/watch?v=LoT1DHwA7ds", "LoT1DHwA7ds"),
      Arguments.of("https://www.youtube.com/watch?v=ZedhlLvph0g", "ZedhlLvph0g"),
      Arguments.of("https://www.amiga-news.de"                  , null         ),
      Arguments.of(""                                           , null         ),
      Arguments.of(null                                         , null         )
    );
  }
  
  @ParameterizedTest
  @MethodSource("extractYoutubeIdData")
  public void extractYoutubeId(String url, String expected) {
    assertThat(RegExpressions.extractYoutubeId(url), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> isEmailData() {
    return Stream.of(
      Arguments.of("daniel.kasmeroglu@kasisoft.net", "daniel.kasmeroglu@kasisoft.net"),
      Arguments.of("daniel.kasmeroglu@.net"        , null                            ),
      Arguments.of("daniel.kasmeroglu"             , null                            ),
      Arguments.of(""                              , null                            ),
      Arguments.of(null                            , null                            )
    );
  }
  
  @ParameterizedTest
  @MethodSource("isEmailData")
  public void isEmail(String value, String expected) {
    assertThat(RegExpressions.isEmail(value), is(expected));
  }

} /* ENDCLASS */
