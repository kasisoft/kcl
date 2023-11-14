package com.kasisoft.libs.common.test.constants;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

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

    public static Stream<Arguments> data_findByStatusCode() {
        return Arrays.asList(HttpStatusCode.values()).stream().map($ -> Arguments.of($.getTextualCode(), $));
    };

    @ParameterizedTest
    @MethodSource("data_findByStatusCode")
    public void findByStatusCode(String textualcode, HttpStatusCode code) {
        var statusCode = HttpStatusCode.findByStatusCode(textualcode);
        assertNotNull(statusCode);
        assertTrue(statusCode.isPresent());
        assertThat(statusCode.get(), is(code));
    }

    @Test
    public void findByStatusCode__UNKNOWN() {
        assertFalse(HttpStatusCode.findByStatusCode(-1).isPresent());
        assertFalse(HttpStatusCode.findByStatusCode("bibo").isPresent());
        assertFalse(HttpStatusCode.findByStatusCode(null).isPresent());
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
