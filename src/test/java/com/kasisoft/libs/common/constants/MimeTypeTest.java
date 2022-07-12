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
 * Tests for the enumeration 'MimeType'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MimeTypeTest {

  @Test
  public void findBySuffix() {
    var result = MimeType.findBySuffix("tex");
    assertNotNull(result);
    assertThat(result.size(), is(2));
    assertTrue(result.contains(MimeType.LaTeX));
    assertTrue(result.contains(MimeType.TeX));
  }

  @Test
  public void findBySuffix__UnknownSuffix() {
    var result = MimeType.findBySuffix("kiq");
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void findByMimeType() {

    var result1 = MimeType.findByMimeType("text/html;charset=UTF-8");
    assertThat(result1, is(MimeType.Html));

    var result2 = MimeType.findByMimeType("text/html");
    assertThat(result2, is(MimeType.Html));

    var result3 = MimeType.findByMimeType(";text/html");
    assertNull(result3);

  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_test() {
    return Arrays.asList(MimeType.values()).stream()
      .map($ -> Arguments.of($.getMimeType(), $))
      ;
  }

  @ParameterizedTest
  @MethodSource("data_test")
  public void test(String type, MimeType mt) {
    assertTrue(mt.test(type));
  }

  @Test
  public void test__NullValue() {
    for (var mt : MimeType.values()) {
      assertFalse(mt.test(null));
    }
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_supportsSuffix() {
    return Arrays.asList(MimeType.values()).stream()
      .map($ -> Arguments.of($.getPrimarySuffix(), $))
      ;
  }

  @ParameterizedTest
  @MethodSource("data_supportsSuffix")
  public void supportsSuffix(String suffix, MimeType mt) {
    assertTrue(mt.supportsSuffix(suffix));
  }

} /* ENDCLASS */
