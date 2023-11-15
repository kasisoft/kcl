package com.kasisoft.libs.common.test.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.types.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.converters.*;

import java.time.*;

import java.util.stream.*;

import java.util.*;

import java.nio.file.*;

import java.awt.*;

import java.net.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class TypeConvertersTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(TypeConvertersTest.class);

    // string <-> color

    public static Stream<Arguments> data_decode_colors() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("yellow", Color.yellow),
            Arguments.of("blue", Color.BLUE),
            Arguments.of("#ffffff00", Color.yellow),
            Arguments.of("#ff0000ff", Color.BLUE),
            Arguments.of("#ffff00", Color.yellow),
            Arguments.of("#0000ff", Color.BLUE),
            Arguments.of("rgb(255,255,0)", Color.yellow),
            Arguments.of("rgb(0,0,255)", Color.BLUE)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_colors")
    public void decode_colors(String value, Color expected) throws Exception {
        assertThat(TypeConverters.convertStringToColor(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_colors() {
        return Arrays.asList(
            "12:-13:42",
            "0:",
            "a:b",
            "#",
            "#zzzzzz",
            "rgb",
            "rgb(",
            "rgb)",
            "rgb()",
            "rgb(a,b,c)"
        ).stream().map(Arguments::of);
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_colors")
    public void invalidDecode_colors(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToColor(value));
    }

    public static Stream<Arguments> data_encode_colors() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Color.yellow, "#ffffff00"),
            Arguments.of(Color.BLUE, "#ff0000ff")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_colors")
    public void encode_colors(Color value, String expected) throws Exception {
        assertThat(TypeConverters.convertColorToString(value), is(expected));
    }

    // string <-> enum

    enum LordOfTheRings {
        Gandalf, Bilbo, Boromir;
    }

    public static Stream<Arguments> data_decode_ci_enums() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("gandalf", LordOfTheRings.Gandalf),
            Arguments.of("bilbo", LordOfTheRings.Bilbo),
            Arguments.of("boromir", LordOfTheRings.Boromir)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_ci_enums")
    public void decode_ci_enums(String value, LordOfTheRings expected) throws Exception {
        assertThat(TypeConverters.convertStringToEnum(value, LordOfTheRings.class, true), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_ci_enums() {
        return Stream.of(Arguments.of("gollum"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_ci_enums")
    public void invalidDecode_ci_enums(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToEnum(value, LordOfTheRings.class));
    }

    public static Stream<Arguments> data_encode_ci_enums() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(LordOfTheRings.Gandalf, "Gandalf"),
            Arguments.of(LordOfTheRings.Bilbo, "Bilbo"),
            Arguments.of(LordOfTheRings.Boromir, "Boromir")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_ci_enums")
    public void encode_ci_enums(LordOfTheRings value, String expected) throws Exception {
        assertThat(TypeConverters.convertEnumToString(value), is(expected));
    }

    public static Stream<Arguments> data_decode_enums() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("Gandalf", LordOfTheRings.Gandalf),
            Arguments.of("Bilbo", LordOfTheRings.Bilbo),
            Arguments.of("Boromir", LordOfTheRings.Boromir)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_enums")
    public void decode_enums(String value, LordOfTheRings expected) throws Exception {
        assertThat(TypeConverters.convertStringToEnum(value, LordOfTheRings.class), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_enums() {
        return Stream.of(
            Arguments.of("Gollum")
        );
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_enums")
    public void invalidDecode_enums(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToEnum(value, LordOfTheRings.class));
    }

    public static Stream<Arguments> data_encode_enums() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(LordOfTheRings.Gandalf, "Gandalf"),
            Arguments.of(LordOfTheRings.Bilbo, "Bilbo"),
            Arguments.of(LordOfTheRings.Boromir, "Boromir")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_enums")
    public void encode_enums(LordOfTheRings value, String expected) throws Exception {
        assertThat(TypeConverters.convertEnumToString(value), is(expected));
    }

    // string <-> version

    public static Stream<Arguments> data_decode_versions() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("1.1.1.qualifier", new Version("1.1.1.qualifier", true, true)
        ));
  }

    @ParameterizedTest
    @MethodSource("data_decode_versions")
    public void decode_versions(String value, Version expected) throws Exception {
        assertThat(TypeConverters.convertStringToVersion(value), is(expected));
    }

    public static Stream<Arguments> data_encode_versions() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Version(1, 1, Integer.valueOf(1), "qualifier"), "1.1.1.qualifier")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_versions")
    public void encode_versions(Version value, String expected) throws Exception {
        assertThat(TypeConverters.convertVersionToString(value), is(expected));
    }

    // string <-> url

    public static Stream<Arguments> data_decode_urls() throws Exception {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("http://www.amiga-news.de", URI.create("http://www.amiga-news.de").toURL())
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_urls")
    public void decode_urls(String value, URL expected) throws Exception {
        assertThat(TypeConverters.convertStringToURL(value), is(expected));
    }

    public static Stream<Arguments> data_encode_urls() throws Exception {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(URI.create("http://www.amiga-news.de").toURL(), "http://www.amiga-news.de")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_urls")
    public void encode_urls(URL value, String expected) throws Exception {
        assertThat(TypeConverters.convertURLToString(value), is(expected));
    }

    // string <-> uri

    public static Stream<Arguments> data_decode_uris() throws Exception {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("http://www.amiga-news.de", URI.create("http://www.amiga-news.de"))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_uris")
    public void decode_uris(String value, URI expected) throws Exception {
        assertThat(TypeConverters.convertStringToURI(value), is(expected));
    }

    public static Stream<Arguments> data_encode_uris() throws Exception {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(URI.create("http://www.amiga-news.de"), "http://www.amiga-news.de")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_uris")
    public void encode_uris(URI value, String expected) throws Exception {
        assertThat(TypeConverters.convertURIToString(value), is(expected));
    }

    // string <-> nio path

    public static Stream<Arguments> data_decode_paths() {
        var path = TEST_RESOURCES.getRootFolder().toString();
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("%s\\http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd")),
            Arguments.of("%s/http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd")),
            Arguments.of("%s\\bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt")),
            Arguments.of("%s/bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt"))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_paths")
    public void decode_paths(String value, Path expected) throws Exception {
        assertThat(TypeConverters.convertStringToPath(value), is(expected));
    }

    public static Stream<Arguments> data_encode_paths() {
        var path = TEST_RESOURCES.getRootFolder().toString();
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(TEST_RESOURCES.getResource("http.xsd"), "%s/http.xsd".formatted(path)),
            Arguments.of(TEST_RESOURCES.getResource("bibo.txt"), "%s/bibo.txt".formatted(path))
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_paths")
    public void encode_paths(Path value, String expected) throws Exception {
        assertThat(TypeConverters.convertPathToString(value), is(expected));
    }

    // string <-> file

    public static Stream<Arguments> data_decode_files() {
      var path = TEST_RESOURCES.getRootFolder().toString();
      return Stream.of(
          Arguments.of(null, null),
          Arguments.of("%s\\http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd").toFile()),
          Arguments.of("%s/http.xsd".formatted(path), TEST_RESOURCES.getResource("http.xsd").toFile()),
          Arguments.of("%s\\bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt").toFile()),
          Arguments.of("%s/bibo.txt".formatted(path), TEST_RESOURCES.getResource("bibo.txt").toFile())
      );
  }

    @ParameterizedTest
    @MethodSource("data_decode_files")
    public void decode_files(String value, File expected) throws Exception {
        assertThat(TypeConverters.convertStringToFile(value), is(expected));
    }

    public static Stream<Arguments> data_encode_files() {
        var path = TEST_RESOURCES.getRootFolder().toString();
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(TEST_RESOURCES.getResource("http.xsd").toFile(), "%s/http.xsd".formatted(path)),
            Arguments.of(TEST_RESOURCES.getResource("bibo.txt").toFile(), "%s/bibo.txt".formatted(path))
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_files")
    public void encode_files(File value, String expected) throws Exception {
        assertThat(TypeConverters.convertFileToString(value), is(expected));
    }

    // string <-> date

    public static Stream<Arguments> data_decode_dates() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("12.04.1987", LocalDate.of(1987, 4, 12)),
            Arguments.of("03.07.1964", LocalDate.of(1964, 7, 3))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_dates")
    public void decode_dates(String value, LocalDate expected) throws Exception {
        assertThat(TypeConverters.convertStringToLocalDate(value, "dd'.'MM'.'yyyy"), is(expected));
    }

    public static Stream<Arguments> data_encode_dates() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(LocalDate.of(1987, 4, 12), "12.04.1987"),
            Arguments.of(LocalDate.of(1964, 7, 3), "03.07.1964")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_dates")
    public void encode_dates(LocalDate value, String expected) throws Exception {
        assertThat(TypeConverters.convertLocalDateToString(value, "dd'.'MM'.'yyyy"), is(expected));
    }

    @Test
    public void invalidDecode_dates() {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToLocalDate("jfjjfjfjf", "dd'.'MM'.'yyyy"));
    }

    // string <-> boolean

    public static Stream<Arguments> data_decode_booleans() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("true", Boolean.TRUE),
            Arguments.of("false", Boolean.FALSE),
            Arguments.of("ja", Boolean.TRUE),
            Arguments.of("nein", Boolean.FALSE),
            Arguments.of("an", Boolean.TRUE),
            Arguments.of("ein", Boolean.TRUE),
            Arguments.of("aus", Boolean.FALSE),
            Arguments.of("on", Boolean.TRUE),
            Arguments.of("off", Boolean.FALSE),
            Arguments.of("0", Boolean.FALSE),
            Arguments.of("1", Boolean.TRUE),
            Arguments.of("-1", Boolean.TRUE),
            Arguments.of("", Boolean.FALSE)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_booleans")
    public void decode(String value, Boolean expected) throws Exception {
        assertThat(TypeConverters.convertStringToBoolean(value), is(expected));
    }

    public static Stream<Arguments> data_encode_booleans() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Boolean.TRUE, "true"),
            Arguments.of(Boolean.FALSE, "false")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_booleans")
    public void encode(Boolean value, String expected) throws Exception {
        assertThat(TypeConverters.convertBooleanToString(value), is(expected));
    }

    // string <-> byte

    public static Stream<Arguments> data_decode_bytes() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0", Byte.valueOf((byte) 0)),
            Arguments.of("13", Byte.valueOf((byte) 13)),
            Arguments.of("-23", Byte.valueOf((byte) -23))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_bytes")
    public void decode_bytes(String value, Byte expected) throws Exception {
        assertThat(TypeConverters.convertStringToByte(value), is(expected));
    }

    public static Stream<Arguments> data_encode_bytes() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Byte.valueOf((byte) 0), "0"),
            Arguments.of(Byte.valueOf((byte) 13), "13"),
            Arguments.of(Byte.valueOf((byte) -23), "-23")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_bytes")
    public void encode_bytes(Byte value, String expected) throws Exception {
        assertThat(TypeConverters.convertByteToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_bytes() {
        return Stream.of(Arguments.of("3.7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_bytes")
    public void invalidDecode_bytes(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToByte(value));
    }

    // string <-> int

    public static Stream<Arguments> data_decode_ints() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0", 0),
            Arguments.of("13", 13),
            Arguments.of("-23", -23)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_ints")
    public void decode_ints(String value, Integer expected) throws Exception {
        assertThat(TypeConverters.convertStringToInteger(value), is(expected));
    }

    public static Stream<Arguments> data_encode_ints() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(0, "0"),
            Arguments.of(13, "13"),
            Arguments.of(-23, "-23"),
            Arguments.of(Integer.MAX_VALUE, "2147483647"),
            Arguments.of(Integer.MIN_VALUE, "-2147483648")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_ints")
    public void encode_ints(Integer value, String expected) throws Exception {
        assertThat(TypeConverters.convertIntegerToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_ints() {
        return Stream.of(Arguments.of("3.7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_ints")
    public void invalidDecode_ints(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToInteger(value));
    }

    // string <->  long

    public static Stream<Arguments> data_decode_longs() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0", 0L),
            Arguments.of("13", 13L),
            Arguments.of("-23", -23L)
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_longs")
    public void decode_longs(String value, Long expected) throws Exception {
        assertThat(TypeConverters.convertStringToLong(value), is(expected));
    }

    public static Stream<Arguments> data_encode_longs() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(0L, "0"),
            Arguments.of(13L, "13"),
            Arguments.of(-23L, "-23")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_longs")
    public void encode_longs(Long value, String expected) throws Exception {
        assertThat(TypeConverters.convertLongToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_longs() {
        return Stream.of(Arguments.of("3.7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_longs")
    public void invalidDecode_longs(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToLong(value));
    }

    // string <-> short

    public static Stream<Arguments> data_decode_shorts() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0", Short.valueOf((short) 0)),
            Arguments.of("13", Short.valueOf((short) 13)),
            Arguments.of("-23", Short.valueOf((short) -23))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_shorts")
    public void decode_shorts(String value, Short expected) throws Exception {
        assertThat(TypeConverters.convertStringToShort(value), is(expected));
    }

    public static Stream<Arguments> data_encode_shorts() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Short.valueOf((short) 0), "0"),
            Arguments.of(Short.valueOf((short) 13), "13"),
            Arguments.of(Short.valueOf((short) -23), "-23")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_shorts")
    public void encode_shorts(Short value, String expected) throws Exception {
        assertThat(TypeConverters.convertShortToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_shorts() {
        return Stream.of(Arguments.of("3.7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_shorts")
    public void invalidDecode_shorts(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToShort(value));
    }

    // string <-> double

    public static Stream<Arguments> data_decode_doubles() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0.0", Double.valueOf(0.0)),
            Arguments.of("1.3", Double.valueOf(1.3)),
            Arguments.of("2.4e8", Double.valueOf(2.4e8)),
            Arguments.of("nan", Double.valueOf(Double.NaN)),
            Arguments.of("NAN", Double.valueOf(Double.NaN)),
            Arguments.of("inf", Double.valueOf(Double.POSITIVE_INFINITY)),
            Arguments.of("INF", Double.valueOf(Double.POSITIVE_INFINITY)),
            Arguments.of("+inf", Double.valueOf(Double.POSITIVE_INFINITY)),
            Arguments.of("+INF", Double.valueOf(Double.POSITIVE_INFINITY)),
            Arguments.of("-inf", Double.valueOf(Double.NEGATIVE_INFINITY)),
            Arguments.of("-INF", Double.valueOf(Double.NEGATIVE_INFINITY)),
            Arguments.of("max", Double.valueOf(Double.MAX_VALUE)),
            Arguments.of("MAX", Double.valueOf(Double.MAX_VALUE)),
            Arguments.of("min", Double.valueOf(Double.MIN_VALUE)),
            Arguments.of("MIN", Double.valueOf(Double.MIN_VALUE))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_doubles")
    public void decode_doubles(String value, Double expected) throws Exception {
        assertThat(TypeConverters.convertStringToDouble(value), is(expected));
    }

    public static Stream<Arguments> data_encode_doubles() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Double.valueOf(0.0), "0.0"),
            Arguments.of(Double.valueOf(1.3), "1.3"),
            Arguments.of(Double.valueOf(2.4e8), "2.4E8"),
            Arguments.of(Double.valueOf(Double.NaN), "NaN"),
            Arguments.of(Double.valueOf(Double.POSITIVE_INFINITY), "+INF"),
            Arguments.of(Double.valueOf(Double.NEGATIVE_INFINITY), "-INF"),
            Arguments.of(Double.valueOf(Double.MIN_VALUE), "MIN"),
            Arguments.of(Double.valueOf(Double.MAX_VALUE), "MAX")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_doubles")
    public void encode_doubles(Double value, String expected) throws Exception {
        assertThat(TypeConverters.convertDoubleToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_doubles() {
        return Stream.of(Arguments.of("3,7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_doubles")
    public void invalidDecode_doubles(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToDouble(value));
    }

    // string <-> float

    public static Stream<Arguments> data_decode_floats() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("0.0", Float.valueOf(0.0f)),
            Arguments.of("1.3", Float.valueOf(1.3f)),
            Arguments.of("2.4e8", Float.valueOf(2.4e8f)),
            Arguments.of("nan", Float.valueOf(Float.NaN)),
            Arguments.of("NAN", Float.valueOf(Float.NaN)),
            Arguments.of("inf", Float.valueOf(Float.POSITIVE_INFINITY)),
            Arguments.of("INF", Float.valueOf(Float.POSITIVE_INFINITY)),
            Arguments.of("+inf", Float.valueOf(Float.POSITIVE_INFINITY)),
            Arguments.of("+INF", Float.valueOf(Float.POSITIVE_INFINITY)),
            Arguments.of("-inf", Float.valueOf(Float.NEGATIVE_INFINITY)),
            Arguments.of("-INF", Float.valueOf(Float.NEGATIVE_INFINITY)),
            Arguments.of("max", Float.valueOf(Float.MAX_VALUE)),
            Arguments.of("MAX", Float.valueOf(Float.MAX_VALUE)),
            Arguments.of("min", Float.valueOf(Float.MIN_VALUE)),
            Arguments.of("MIN", Float.valueOf(Float.MIN_VALUE))
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_floats")
    public void decode_floats(String value, Float expected) throws Exception {
        assertThat(TypeConverters.convertStringToFloat(value), is(expected));
    }

    public static Stream<Arguments> data_encode_floats() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(Float.valueOf(0.0f), "0.0"),
            Arguments.of(Float.valueOf(1.3f), "1.3"),
            Arguments.of(Float.valueOf(2.4e8f), "2.4E8"),
            Arguments.of(Float.valueOf(Float.NaN), "NaN"),
            Arguments.of(Float.valueOf(Float.POSITIVE_INFINITY), "+INF"),
            Arguments.of(Float.valueOf(Float.NEGATIVE_INFINITY), "-INF"),
            Arguments.of(Float.valueOf(Float.MIN_VALUE), "MIN"),
            Arguments.of(Float.valueOf(Float.MAX_VALUE), "MAX")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_floats")
    public void encode_floats(Float value, String expected) throws Exception {
        assertThat(TypeConverters.convertFloatToString(value), is(expected));
    }

    public static Stream<Arguments> data_invalidDecode_floats() {
        return Stream.of(Arguments.of("3,7"));
    }

    @ParameterizedTest
    @MethodSource("data_invalidDecode_floats")
    public void invalidDecode_floats(String value) throws Exception {
        assertThrows(KclException.class, () -> TypeConverters.convertStringToFloat(value));
    }

    // string <-> boolean[]

    public static Stream<Arguments> data_decode_booleanarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("true", new Boolean[] {true}),
            Arguments.of("false", new Boolean[] {false}),
            Arguments.of("ja", new Boolean[] {true}),
            Arguments.of("nein", new Boolean[] {false}),
            Arguments.of("an", new Boolean[] {true}),
            Arguments.of("ein", new Boolean[] {true}),
            Arguments.of("aus", new Boolean[] {false}),
            Arguments.of("on", new Boolean[] {true}),
            Arguments.of("off", new Boolean[] {false}),
            Arguments.of("0", new Boolean[] {false}),
            Arguments.of("1", new Boolean[] {true}),
            Arguments.of("-1", new Boolean[] {true}),
            Arguments.of("", new Boolean[] {false}),
            Arguments.of("true,0", new Boolean[] {true, false}),
            Arguments.of("false,ein", new Boolean[] {false, true})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_booleanarray")
    public void decode_booleanarray(String value, Boolean[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToBooleanArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_booleanarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Boolean[] {true, true}, "true,true"),
            Arguments.of(new Boolean[] {false, true}, "false,true")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_booleanarray")
    public void encode_booleanarray(Boolean[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertBooleanArrayToString(value), is(expected));
    }

    // string <-> int[]

    public static Stream<Arguments> data_decode_intarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("31", new Integer[] {31}),
            Arguments.of("-47,12", new Integer[] {-47, 12})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_intarray")
    public void decode_intarray(String value, Integer[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToIntegerArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_intarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Integer[] {79, 1201}, "79,1201"),
            Arguments.of(new Integer[] {-31, -128}, "-31,-128")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_intarray")
    public void encode_intarray(Integer[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertIntegerArrayToString(value), is(expected));
    }

    // string <-> long[]

    public static Stream<Arguments> data_decode_longarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("31", new Long[] {31L}),
            Arguments.of("-47,12", new Long[] {-47L, 12L})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_longarray")
    public void decode_longarray(String value, Long[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToLongArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_longarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Long[] {79L, 1201L}, "79,1201"),
            Arguments.of(new Long[] {-31L, -128L}, "-31,-128")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_longarray")
    public void encode_longarray(Long[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertLongArrayToString(value), is(expected));
    }

    // string <-> short[]

    public static Stream<Arguments> data_decode_shortarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("31", new Short[] {31}),
            Arguments.of("-47,12", new Short[] {-47, 12})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_shortarray")
    public void decode_shortarray(String value, Short[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToShortArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_shortarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Short[] {79, 1201}, "79,1201"),
            Arguments.of(new Short[] {-31, -128}, "-31,-128")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_shortarray")
    public void encode_shortarray(Short[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertShortArrayToString(value), is(expected));
    }

    // string <-> double[]

    public static Stream<Arguments> data_decode_doublearray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("3.1", new Double[] {3.1}),
            Arguments.of("3.2,+INF", new Double[] {3.2, Double.POSITIVE_INFINITY}),
            Arguments.of("4.2,MAX", new Double[] {4.2, Double.MAX_VALUE}),
            Arguments.of("-1.2,MIN", new Double[] {-1.2, Double.MIN_VALUE}),
            Arguments.of("9.1,2.1", new Double[] {9.1, 2.1})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_doublearray")
    public void decode_doublearray(String value, Double[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToDoubleArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_doublearray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Double[] {7.9, Double.MAX_VALUE}, "7.9,MAX"),
            Arguments.of(new Double[] {-3.1, Double.NaN}, "-3.1,NaN")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_doublearray")
    public void encode_doublearray(Double[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertDoubleArrayToString(value), is(expected));
    }

    // string <-> float[]

    public static Stream<Arguments> data_decode_floatarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of("3.1", new Float[] {3.1f}),
            Arguments.of("3.2,+INF", new Float[] {3.2f, Float.POSITIVE_INFINITY}),
            Arguments.of("4.2,MAX", new Float[] {4.2f, Float.MAX_VALUE}),
            Arguments.of("-1.2,MIN", new Float[] {-1.2f, Float.MIN_VALUE}),
            Arguments.of("9.1,2.1", new Float[] {9.1f, 2.1f})
        );
    }

    @ParameterizedTest
    @MethodSource("data_decode_floatarray")
    public void decode_floatarray(String value, Float[] expected) throws Exception {
        assertThat(TypeConverters.convertStringToFloatArray(value), is(expected));
    }

    public static Stream<Arguments> data_encode_floatarray() {
        return Stream.of(
            Arguments.of(null, null),
            Arguments.of(new Float[] {7.9f, Float.MAX_VALUE}, "7.9,MAX"),
            Arguments.of(new Float[] {-3.1f, Float.NaN}, "-3.1,NaN")
        );
    }

    @ParameterizedTest
    @MethodSource("data_encode_floatarray")
    public void encode_floatarray(Float[] value, String expected) throws Exception {
        assertThat(TypeConverters.convertFloatArrayToString(value), is(expected));
    }

} /* ENDCLASS */
