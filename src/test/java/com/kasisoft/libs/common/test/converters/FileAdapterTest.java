package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.test.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FileAdapterTest {

  private static TestResources TEST_RESOURCES = TestResources.createTestResources(FileAdapterTest.class);

  private FileAdapter adapter = new FileAdapter();

  public static Stream<Arguments> data_decode() {
    var path = TEST_RESOURCES.getRootFolder().toString();
    return Stream.of(
      Arguments.of(null                               , null),
      Arguments.of("%s\\http.xsd" .formatted(path), TEST_RESOURCES.getResourceAsFile("http.xsd")),
      Arguments.of("%s/http.xsd"  .formatted(path), TEST_RESOURCES.getResourceAsFile("http.xsd")),
      Arguments.of("%s\\bibo.txt" .formatted(path), TEST_RESOURCES.findResourceAsFile("bibo.txt").orElse(null)),
      Arguments.of("%s/bibo.txt"  .formatted(path), TEST_RESOURCES.findResourceAsFile("bibo.txt").orElse(null))
    );
  }

  @ParameterizedTest
  @MethodSource("data_decode")
  public void decode(String value, File expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  public static Stream<Arguments> data_encode() {
    var path = TEST_RESOURCES.getRootFolder().toString();
    return Stream.of(
      Arguments.of(null                                        , null),
      Arguments.of(TEST_RESOURCES.getResourceAsFile("http.xsd"), "%s/http.xsd".formatted(path)),
      Arguments.of(TEST_RESOURCES.getResourceAsFile("bibo.txt"), "%s/bibo.txt".formatted(path))
    );
  }

  @ParameterizedTest
  @MethodSource("data_encode")
  public void encode(File value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

} /* ENDCLASS */
