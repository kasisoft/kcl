package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.converters.*;
import com.kasisoft.libs.common.test.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

import java.nio.file.*;

/**
 * Tests for the type 'NioPathAdapter'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class NioPathAdapterTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(NioPathAdapterTest.class);

    private NioPathAdapter             adapter        = new NioPathAdapter();

    public static Stream<Arguments> data_decode() {
        String path = TEST_RESOURCES.getRootFolder().toString();
        return Stream.of(Arguments.of(null, null), Arguments.of("%s\\http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd")), Arguments.of("%s/http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd")), Arguments.of("%s\\bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt")), Arguments.of("%s/bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt")));
    }

    @ParameterizedTest
    @MethodSource("data_decode")
    public void decode(String value, Path expected) throws Exception {
        assertThat(adapter.decode(value), is(expected));
    }

    public static Stream<Arguments> data_encode() {
        String path = TEST_RESOURCES.getRootFolder().toString();
        return Stream.of(Arguments.of(null, null), Arguments.of(TEST_RESOURCES.getResource("http.xsd"), "%s/http.xsd".formatted(path)), Arguments.of(TEST_RESOURCES.getResource("bibo.txt"), "%s/bibo.txt".formatted(path)));
    }

    @ParameterizedTest
    @MethodSource("data_encode")
    public void encode(Path value, String expected) throws Exception {
        assertThat(adapter.encode(value), is(expected));
    }

} /* ENDCLASS */
