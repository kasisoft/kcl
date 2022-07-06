package com.kasisoft.libs.common.types;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import java.util.stream.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class VersionTest {

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_failingVersions() {
    return Stream.of(
      Arguments.of(null              , Boolean.TRUE ),
      Arguments.of("1"               , Boolean.TRUE ),
      Arguments.of("1.1"             , Boolean.TRUE ),
      Arguments.of("1.1.1"           , Boolean.TRUE ),
      Arguments.of("1.1.qualifier"   , Boolean.TRUE ),
      Arguments.of("1q.1.1.qualifier", Boolean.TRUE ),
      Arguments.of("1.1q.1.qualifier", Boolean.TRUE ),
      Arguments.of("1.1.1q.qualifier", Boolean.TRUE ),
      Arguments.of("1.1.qualifier"   , Boolean.FALSE)
    );
  }

  @ParameterizedTest
  @MethodSource("data_failingVersions")
  public void failingVersions(String version, boolean hasqualifier) throws Exception {
    assertThrows(KclException.class, () -> {
      Version.parse(version, true, hasqualifier);
    });
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_versions() {
    return Stream.of(
      Arguments.of("1.1"            , Boolean.FALSE , Boolean.FALSE , new Version(1, 1, null, null)),
      Arguments.of("1.1.1"          , Boolean.TRUE  , Boolean.FALSE , new Version(1, 1, 1, null)),
      Arguments.of("1.1.qualifier"  , Boolean.FALSE , Boolean.TRUE  , new Version(1, 1, null, "qualifier")),
      Arguments.of("1.1.1.qualifier", Boolean.TRUE  , Boolean.TRUE  , new Version(1, 1, 1, "qualifier")),
      Arguments.of("1.1.1_qualifier", Boolean.TRUE  , Boolean.TRUE  , new Version(1, 1, 1, "qualifier"))
    );
  }

  @ParameterizedTest
  @MethodSource("data_versions")
  public void versions(String version, boolean hasmicro, boolean hasqualifier, Version expected) throws Exception {
    assertThat(Version.parse(version, hasmicro, hasqualifier), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_versionsAll() {
    return Stream.of(
      Arguments.of("1.1"            , new Version(1, 1, null, null)),
      Arguments.of("1.1.1"          , new Version(1, 1, 1, null)),
      Arguments.of("1.1.qualifier"  , new Version(1, 1, null, "qualifier")),
      Arguments.of("1.1.1.qualifier", new Version(1, 1, 1, "qualifier")),
      Arguments.of("1.1.1_qualifier", new Version(1, 1, 1, "qualifier"))
    );
  }

  @ParameterizedTest
  @MethodSource("data_versionsAll")
  public void versionsAll(String version, Version expected) throws Exception {
    assertThat(Version.parse(version, null, null), is(expected));
  }

  @SuppressWarnings("exports")
  public static Stream<Arguments> data_sort() throws Exception {

    Version[] versions  = {Version.parse("2.1", false, false), Version.parse("1.1", false, false)};
    Version[] versions1 = {Version.parse("1.1", false, false), Version.parse("2.1", false, false)};
    Version[] versions2 = {Version.parse("1.2", false, false), Version.parse("1.1", false, false)};
    Version[] versions3 = {Version.parse("1.1", false, false), Version.parse("1.2", false, false) };
    Version[] versions4 = {Version.parse("1.1.2", true, false), Version.parse("1.1.1", true, false)};
    Version[] versions5 = {Version.parse("1.1.1", true, false), Version.parse("1.1.2", true, false)};
    Version[] versions6 = {Version.parse("1.1.1.zz", true, true), Version.parse("1.1.1.aa", true, true)};
    Version[] versions7 = {Version.parse("1.1.1.aa", true, true), Version.parse("1.1.1.zz", true, true)};
    Version[] versions8 = {Version.parse("1.1.1_zz", true, true), Version.parse("1.1.1.aa", true, true)};
    Version[] versions9 = {Version.parse("1.1.1_aa", true, true), Version.parse("1.1.1.zz", true, true)};
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
    assertThat(versions, is( expected));
  }

} /* ENDCLASS */
