package com.kasisoft.libs.common.constants;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import java.util.stream.*;

import java.util.*;

/**
 * Tests for the constants 'HttpStatusCode'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class HttpStatusCodeTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_findByStatusCode() {
    return Arrays.asList(HttpStatusCode.values()).stream()
      .map($ -> Arguments.of($.getTextualCode(), $))
      ;
  };

  @ParameterizedTest
  @MethodSource("data_findByStatusCode")
  public void findByStatusCode(String textualcode, HttpStatusCode code) {
    var statusCode = HttpStatusCode.findByStatusCode(textualcode);
    assertThat(statusCode, is(code));
  }

  @Test
  public void findByStatusCode__UNKNOWN() {
    assertNull(HttpStatusCode.findByStatusCode(-1));
    assertNull(HttpStatusCode.findByStatusCode("bibo"));
    assertNull(HttpStatusCode.findByStatusCode(null));
  }

  @Test
  public void predicate() {
    for (var sc : HttpStatusCode.values()) {
      assertFalse(sc.test(null));
      assertTrue(sc.test(sc.getCode()));
      assertFalse(sc.test(sc.getCode() + 1));
    }
  }

} /* ENDCLASS */
