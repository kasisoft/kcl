package com.kasisoft.libs.common.test.types;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.types.*;

import java.util.stream.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class VersionTest {

    public static Stream<Arguments> data_failingVersions() {
        return Stream.of(
            Arguments.of((String) null),
            Arguments.of("1"),
            Arguments.of("1.1a"),
            Arguments.of("1.1b.1"),
            Arguments.of("1.1qualifier"),
            Arguments.of("1q.1.1.qualifier"),
            Arguments.of("1.1q.1.qualifier"),
            Arguments.of("1.1.1q.qualifier"),
            Arguments.of("1.1@qualifier")
        );
    }

    @ParameterizedTest
    @MethodSource("data_failingVersions")
    public void failingVersions(String version) throws Exception {
        assertTrue(Version.of(version).isEmpty());
    }

    public static Stream<Arguments> data_versions() {
        return Stream.of(
            Arguments.of("1.1", Version.of(1, 1)),
            Arguments.of("1.1.1", Version.of(1, 1, 1)),
            Arguments.of("1.1.qualifier", Version.of(1, 1, "qualifier")),
            Arguments.of("1.1.1.qualifier", Version.of(1, 1, 1, "qualifier")),
            Arguments.of("1.1.1_qualifier", Version.of(1, 1, 1, "qualifier"))
        );
    }

    @ParameterizedTest
    @MethodSource("data_versions")
    public void versions(String version, Version expected) throws Exception {
        var ver = Version.of(version);
        assertTrue(ver.isPresent());
        assertThat(ver.get(), is(expected));
    }

    public static Stream<Arguments> data_versionsAll() {
        return Stream.of(
            Arguments.of("1.1", Version.of(1, 1)),
            Arguments.of("1.1.1", Version.of(1, 1, 1)),
            Arguments.of("1.1.qualifier", Version.of(1, 1, "qualifier")),
            Arguments.of("1.1.1.qualifier", Version.of(1, 1, 1, "qualifier")),
            Arguments.of("1.1.1_qualifier", Version.of(1, 1, 1, "qualifier"))
        );
    }

    @ParameterizedTest
    @MethodSource("data_versionsAll")
    public void versionsAll(String version, Version expected) throws Exception {
        var ver = Version.of(version);
        assertTrue(ver.isPresent());
        assertThat(ver.get(), is(expected));
    }

    public static Stream<Arguments> data_sort() throws Exception {
        Version[] versions  = {Version.of("2.1").get(), Version.of("1.1").get()};
        Version[] versions1 = {Version.of("1.1").get(), Version.of("2.1").get()};
        Version[] versions2 = {Version.of("1.2").get(), Version.of("1.1").get()};
        Version[] versions3 = {Version.of("1.1").get(), Version.of("1.2").get()};
        Version[] versions4 = {Version.of("1.1.2").get(), Version.of("1.1.1").get()};
        Version[] versions5 = {Version.of("1.1.1").get(), Version.of("1.1.2").get()};
        Version[] versions6 = {Version.of("1.1.1.zz").get(), Version.of("1.1.1.aa").get()};
        Version[] versions7 = {Version.of("1.1.1.aa").get(), Version.of("1.1.1.zz").get()};
        Version[] versions8 = {Version.of("1.1.1_zz").get(), Version.of("1.1.1.aa").get()};
        Version[] versions9 = {Version.of("1.1.1_aa").get(), Version.of("1.1.1.zz").get()};
        return Stream.of(
            Arguments.of(Arrays.asList(versions), Arrays.asList(versions1)),
            Arguments.of(Arrays.asList(versions2), Arrays.asList(versions3)),
            Arguments.of(Arrays.asList(versions4), Arrays.asList(versions5)),
            Arguments.of(Arrays.asList(versions6), Arrays.asList(versions7)),
            Arguments.of(Arrays.asList(versions8), Arrays.asList(versions9))
        );
    }

    @ParameterizedTest
    @MethodSource("data_sort")
    public void sort(List<Version> versions, List<Version> expected) {
        Collections.sort(versions);
        assertThat(versions, is(expected));
    }

} /* ENDCLASS */
