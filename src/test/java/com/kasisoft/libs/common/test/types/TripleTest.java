package com.kasisoft.libs.common.test.types;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.types.*;

import java.util.stream.*;

/**
 * Tests for the class 'Triple'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TripleTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_processTriples() {
    return Stream.of(
      Arguments.of(new Triple<String, String, Boolean>("A", "mid0", true), "A", "mid0", true),
      Arguments.of(new Triple<String, String, Boolean>("A", "mid1", false), "A", "mid1", false),
      Arguments.of(new Triple<String, String, Boolean>("B", "mid2", true), "B", "mid2", true)
    );
  }

  @ParameterizedTest
  @MethodSource("data_processTriples")
  public void processTriples(Triple<String, String, Boolean> pair, String key, String mid, boolean value) {
    assertThat(pair.getFirst(), is(key));
    assertThat(pair.getLast(), is(value));
    assertThat(pair.getValue2(), is(mid));
  }

} /* ENDCLASS */
