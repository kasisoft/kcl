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
 * Test for the constants 'Iso3166'.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Iso3166Test {

    public static Stream<Arguments> data_validCode() {
        return Arrays.asList(Iso3166.values()).stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data_validCode")
    public void validCode(Iso3166 value) {
        assertNotNull(value.getAlpha2());
        assertNotNull(value.getAlpha3());
        assertThat(value.getAlpha2().length(), is(2));
        assertThat(value.getAlpha3().length(), is(3));
    }

    public static Stream<Arguments> data_findByAlpha2() {
        return Arrays.asList(Iso3166.values()).stream().map($ -> Arguments.of($, $.getAlpha2()));
    }

    @ParameterizedTest
    @MethodSource("data_findByAlpha2")
    public void findByAlpha2(Iso3166 expected, String alpha2) {
        var identified = Iso3166.findByAlpha2(alpha2);
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    @Test
    public void findByAlpha2__NULL_VALUE() {
        var opt = Iso3166.findByAlpha2(null);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    public static Stream<Arguments> data_findByAlpha3() {
        return Arrays.asList(Iso3166.values()).stream().map($ -> Arguments.of($, $.getAlpha3()));
    }

    @ParameterizedTest
    @MethodSource("data_findByAlpha3")
    public void findByAlpha3(Iso3166 expected, String alpha3) {
        var identified = Iso3166.findByAlpha3(alpha3);
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    @Test
    public void findByAlpha3__NULL_VALUE() {
        var opt = Iso3166.findByAlpha3(null);
        assertNotNull(opt);
        assertFalse(opt.isPresent());
    }

    public static Stream<Arguments> data_findByNumerical() {
        return Arrays.asList(Iso3166.values()).stream().map($ -> Arguments.of($, $.getNumerical()));
    }

    @ParameterizedTest
    @MethodSource("data_findByNumerical")
    public void findByNumerical(Iso3166 expected, Integer numerical) {
        var identified = Iso3166.findByNumerical(numerical.intValue());
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    public static Stream<Arguments> data_test() {
        return Arrays.asList(Iso3166.values()).stream().map($ -> Arguments.of($.getAlpha2(), $.getAlpha3(), $));
    }

    @ParameterizedTest
    @MethodSource("data_test")
    public void test(String alpha2, String alpha3, Iso3166 iso3166) {
        assertTrue(iso3166.test(alpha2));
        assertTrue(iso3166.test(alpha3));
    }

    @Test
    public void test__NullValue() {
        for (var iso : Iso3166.values()) {
            assertFalse(iso.test(null));
        }
    }

} /* ENDCLASS */
