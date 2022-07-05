package com.kasisoft.libs.common.types;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.api.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TupelTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_processTupels() {
    return Stream.of(
      Arguments.of(new Tupel<>(new String[0]), null, null),
      Arguments.of(new Tupel<>(new String[] {"A"}), "A", "A"),
      Arguments.of(new Tupel<>(new String[] {"A", "B"}), "A", "B"),
      Arguments.of(new Tupel<>(new String[] {"A", "B", "C"}), "A", "C")
    );
  }

  @ParameterizedTest
  @MethodSource("data_processTupels")
  public void processTupels(Tupel<String> tupel, String first, String last) {
    assertThat(tupel.findFirst(), is(first));
    assertThat(tupel.findLast(), is(last ));
  }

    @Test
    public void length() {
        var t = new Tupel<>(new String[0]);
        assertThat(t.getLength(), is(0));
        t.setValues(new String[] {"A"});
        assertThat(t.getLength(), is(1));
        t.setValues(new String[] {"A", "B"});
        assertThat(t.getLength(), is(2));
    }

    @Test
    public void empty() {
        var t = new Tupel<>(new String[0]);
        assertTrue(t.getEmpty());
        t.setValues(new String[] {"A"});
        assertFalse(t.getEmpty());
        t.setValues(new String[0]);
        assertTrue(t.getEmpty());
    }

} /* ENDCLASS */
