package com.kasisoft.libs.common.test.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.constants.*;

import java.util.stream.*;

import java.util.*;

/**
 * Tests for the enumeration {@link MimeType}
 *
 * @author daniel.kasmeroglu@kasisoft.com
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
        assertNotNull(result1);
        assertTrue(result1.isPresent());
        assertThat(result1.get(), is(MimeType.Html));

        var result2 = MimeType.findByMimeType("text/html");
        assertNotNull(result2);
        assertTrue(result2.isPresent());
        assertThat(result2.get(), is(MimeType.Html));

        var result3 = MimeType.findByMimeType(";text/html");
        assertNotNull(result3);
        assertFalse(result3.isPresent());

    }

    public static Stream<Arguments> data_test() {
        return Arrays.asList(MimeType.values()).stream().map($ -> Arguments.of($.getMimeType(), $));
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

    public static Stream<Arguments> data_supportsSuffix() {
        return Arrays.asList(MimeType.values()).stream().map($ -> Arguments.of($.getPrimarySuffix(), $));
    }

    @ParameterizedTest
    @MethodSource("data_supportsSuffix")
    public void supportsSuffix(String suffix, MimeType mt) {
        assertTrue(mt.supportsSuffix(suffix));
    }

} /* ENDCLASS */
