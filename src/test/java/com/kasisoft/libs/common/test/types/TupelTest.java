package com.kasisoft.libs.common.test.types;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.types.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TupelTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_processTupels() {
    return Stream.of(
      Arguments.of(new Tupel<String>(), null, null),
      Arguments.of(new Tupel<String>("A"), "A", "A"),
      Arguments.of(new Tupel<String>("A", "B"), "A", "B"),
      Arguments.of(new Tupel<String>("A", "B", "C"), "A", "C")
    );
  }

  @ParameterizedTest
  @MethodSource("data_processTupels")
  public void processTupels(Tupel<String> tupel, String first, String last) {
    assertThat(tupel.getFirst(), is(first));
    assertThat(tupel.getLast(), is(last ));
  }

} /* ENDCLASS */
