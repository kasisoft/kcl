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
public class PairTest {

    public static Stream<Arguments> data_processPairs() {
        return Stream.of(Arguments.of(new Pair<String, Boolean>("A", true), "A", true), Arguments.of(new Pair<String, Boolean>("A", false), "A", false), Arguments.of(new Pair<String, Boolean>("B", true), "B", true));
    }

    @ParameterizedTest
    @MethodSource("data_processPairs")
    public void processPairs(Pair<String, Boolean> pair, String key, boolean value) {
        assertThat(pair.getFirst(), is(key));
        assertThat(pair.getLast(), is(value));
        assertThat(pair.getKey(), is(key));
        assertThat(pair.getValue(), is(value));
    }

} /* ENDCLASS */
