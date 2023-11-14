package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class IntArrayAdapterTest {

    private IntArrayAdapter adapter = new IntArrayAdapter();

    public static Stream<Arguments> data_decode() {
        return Stream.of(Arguments.of(null, null), Arguments.of("31", new int[] {
            31}), Arguments.of("-47,12", new int[] {-47, 12}));
    }

    @ParameterizedTest
    @MethodSource("data_decode")
    public void decode(String value, int[] expected) throws Exception {
        assertThat(adapter.decode(value), is(expected));
    }

    public static Stream<Arguments> data_encode() {
        return Stream.of(Arguments.of(null, null), Arguments.of(new int[] {79,
            1201}, "79,1201"), Arguments.of(new int[] {-31, -128}, "-31,-128"));
    }

    @ParameterizedTest
    @MethodSource("data_encode")
    public void encode(int[] value, String expected) throws Exception {
        assertThat(adapter.encode(value), is(expected));
    }

} /* ENDCLASS */
