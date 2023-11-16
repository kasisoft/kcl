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
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Iso639Test {

    public static Stream<Arguments> data_validCode() {
        return Arrays.asList(Iso639.values()).stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data_validCode")
    public void validCode(Iso639 value) {
        assertNotNull(value.getBibliography());
        assertNotNull(value.getTerminology());
        assertThat(value.getBibliography().length(), is(3));
        assertThat(value.getTerminology().length(), is(3));
        if (value.getAlpha2() != null) {
            assertThat(value.getAlpha2().length(), is(2));
        }
    }

    public static Stream<Arguments> data_findByAlpha2() {
        return Arrays.asList(Iso639.values()).stream().filter($ -> $.getAlpha2() != null).map($ -> Arguments.of($, $.getAlpha2()));
    }

    @ParameterizedTest
    @MethodSource("data_findByAlpha2")
    public void findByAlpha2(Iso639 expected, String alpha2) {
        var identified = Iso639.findByAlpha2(alpha2);
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    public static Stream<Arguments> data_findByBibliography() {
        return Arrays.asList(Iso639.values()).stream().map($ -> Arguments.of($, $.getBibliography()));
    }

    @ParameterizedTest
    @MethodSource("data_findByBibliography")
    public void findByBibliography(Iso639 expected, String bibliography) {
        var identified = Iso639.findByBibliography(bibliography);
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    public static Stream<Arguments> data_findByTerminology() {
        return Arrays.asList(Iso639.values()).stream().map($ -> Arguments.of($, $.getTerminology()));
    }

    @ParameterizedTest
    @MethodSource("data_findByTerminology")
    public void findByTerminology(Iso639 expected, String terminology) {
        var identified = Iso639.findByTerminology(terminology);
        assertNotNull(identified);
        assertTrue(identified.isPresent());
        assertThat(identified.get(), is(expected));
    }

    public static Stream<Arguments> data_test() {
        var list = new ArrayList<Arguments>();
        for (var iso : Iso639.values()) {
            if (iso.getAlpha2() != null) {
                list.add(Arguments.of(iso.getAlpha2(), iso));
            }
            list.add(Arguments.of(iso.getBibliography(), iso));
            list.add(Arguments.of(iso.getTerminology(), iso));
        }
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("data_test")
    public void test(String code, Iso639 iso639) {
        assertTrue(iso639.test(code));
    }

    @Test
    public void test__NULL_VALUE() {
        for (var iso : Iso639.values()) {
            assertFalse(iso.test(null));
        }
    }

    @Test
    public void findBy__NULL_VALUE() {

        var opt1 = Iso639.findByAlpha2(null);
        assertNotNull(opt1);
        assertFalse(opt1.isPresent());

        var opt2 = Iso639.findByAlpha3(null);
        assertNotNull(opt2);
        assertFalse(opt2.isPresent());

    }

} /* ENDCLASS */
