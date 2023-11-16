package com.kasisoft.libs.common.test.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.utils.*;

import java.time.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

/**
 * Test for various functions of the class {@link MiscFunctions}
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class MiscFunctionsTest {

    public static Stream<Arguments> data_getGravatarLink() {
        return Stream.of(
            Arguments.of(null, null, null),
            Arguments.of(null, null, 12),
            Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d", " Daniel.KASMEROGLU@kasisoft.net \n", null),
            Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d", "daniel.kasmeroglu@kasisoft.net", null),
            Arguments.of("https://www.gravatar.com/avatar/8fea46f5bc403f6949300c3007d2f18d?s=100", "daniel.kasmeroglu@kasisoft.net", 100)
        );
    }

    @ParameterizedTest
    @MethodSource("data_getGravatarLink")
    public void getGravatarLink(String expected, String email, Integer size) {
        assertThat(MiscFunctions.getGravatarLink(email, size), is(expected));
    }

    public static Stream<Arguments> data_toUniqueList() {
        return Stream.of(
            Arguments.of(Arrays.asList("Otto", "Fred", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto")),
            Arguments.of(Arrays.asList("Otto", "Fred", "Otto", "Ginger"), Arrays.asList("Fred", "Ginger", "Otto"))
        );
    }

    @ParameterizedTest
    @MethodSource("data_toUniqueList")
    public void toUniqueList(List<String> list, List<String> expected) {

        var altered1 = MiscFunctions.toUniqueList(null);
        assertNotNull(altered1);
        assertTrue(altered1.isEmpty());

        var altered2 = MiscFunctions.toUniqueList(list);
        assertNotNull(altered2);
        assertThat(altered2.size(), is(expected.size()));
        for (var i = 0; i < altered2.size(); i++) {
            assertThat(altered2.get(i), is(expected.get(i)));
        }

    }

    private static <T> Stream<Arguments> createLeapYearTests(Function<Integer, T> year2Info) {
        return Arrays.asList(
            Arguments.of(year2Info.apply(1900), false),
            Arguments.of(year2Info.apply(1901), false),
            Arguments.of(year2Info.apply(1904), true),
            Arguments.of(year2Info.apply(2000), true),
            Arguments.of(year2Info.apply(2001), false)
        ).stream();
    }

    public static Stream<Arguments> data_isLeapYear() {
        return createLeapYearTests(Function.identity());
    }

    @ParameterizedTest
    @MethodSource("data_isLeapYear")
    public void isLeapYear(int year, boolean expected) {
        assertThat(MiscFunctions.isLeapYear(year), is(expected));
    }

    private static Date createDate(int year) {
        var calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        return calendar.getTime();
    }

    private static OffsetDateTime createOffsetDateTime(int year) {
        return OffsetDateTime.ofInstant(createDate(year).toInstant(), ZoneId.of("UTC"));
    }

    private static LocalDateTime createLocalDateTime(int year) {
        return LocalDateTime.ofInstant(createDate(year).toInstant(), ZoneId.of("UTC"));
    }

    public static Stream<Arguments> data_isLeapYear__Date() {
        return createLeapYearTests(MiscFunctionsTest::createDate);
    }

    @ParameterizedTest
    @MethodSource("data_isLeapYear__Date")
    public void isLeapYear__Date(Date year, boolean expected) {
        assertThat(MiscFunctions.isLeapYear(year), is(expected));
    }

    public static Stream<Arguments> data_isLeapYear__OffsetDateTime() {
        return createLeapYearTests(MiscFunctionsTest::createOffsetDateTime);
    }

    @ParameterizedTest
    @MethodSource("data_isLeapYear__OffsetDateTime")
    public void isLeapYear__OffsetDateTime(OffsetDateTime year, boolean expected) {
        assertThat(MiscFunctions.isLeapYear(year), is(expected));
    }

    public static Stream<Arguments> data_isLeapYear__LocalDateTime() {
        return createLeapYearTests(MiscFunctionsTest::createLocalDateTime);
    }

    @ParameterizedTest
    @MethodSource("data_isLeapYear__LocalDateTime")
    public void isLeapYear__LocalDateTime(LocalDateTime year, boolean expected) {
        assertThat(MiscFunctions.isLeapYear(year), is(expected));
    }

    @Test
    public void toPairs() {

        var noentries1 = MiscFunctions.toPairs();
        assertNotNull(noentries1);
        assertThat(noentries1.size(), is(0));

        var noentries2 = MiscFunctions.toPairs((String[]) null);
        assertNotNull(noentries2);
        assertThat(noentries2.size(), is(0));

        var incompletePair = MiscFunctions.toPairs("key");
        assertNotNull(incompletePair);
        assertThat(incompletePair.size(), is(0));

        var onePair = MiscFunctions.toPairs("key", "val");
        assertNotNull(onePair);
        assertThat(onePair.size(), is(1));

        var noPairs = MiscFunctions.toPairs("key", "val", "nextKey");
        assertNotNull(noPairs);
        assertThat(noPairs.size(), is(0));

    }

    @Test
    public void toMap() {

        var map1 = MiscFunctions.toMap("key1", "value1", "key2", "value2");
        assertNotNull(map1);
        assertThat(map1.get("key1"), is("value1"));
        assertThat(map1.get("key2"), is("value2"));
        assertNull(map1.get("key3"));

        var map2 = MiscFunctions.toMap();
        assertNotNull(map2);
        assertTrue(map2.isEmpty());

        var map3 = MiscFunctions.toMap((Object[]) null);
        assertNotNull(map3);
        assertTrue(map3.isEmpty());

    }

    @Test
    public void gcd() {
        assertThat(MiscFunctions.gcd(20, 15), is(5));
        assertThat(MiscFunctions.gcd(99, 44), is(11));
    }

    @Test
    public void trimLeading() {

        var list1    = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
        var trimmed1 = MiscFunctions.trimLeading(list1);
        assertThat(trimmed1, is(Arrays.asList("str1", "str2", null, null)));

        var list2    = new ArrayList<String>();
        var trimmed2 = MiscFunctions.trimLeading(list2);
        assertNotNull(trimmed2);
        assertTrue(trimmed2.isEmpty());

    }

    @Test
    public void trimTrailing() {

        var list1    = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
        var trimmed1 = MiscFunctions.trimTrailing(list1);
        assertThat(trimmed1, is(Arrays.asList(null, null, null, "str1", "str2")));

        var list2    = new ArrayList<String>();
        var trimmed2 = MiscFunctions.trimTrailing(list2);
        assertNotNull(trimmed2);
        assertTrue(trimmed2.isEmpty());

    }

    @Test
    public void trim() {

        var list1    = new ArrayList<String>(Arrays.asList(null, null, null, "str1", "str2", null, null));
        var trimmed1 = MiscFunctions.trim(list1);
        assertThat(trimmed1, is(Arrays.asList("str1", "str2")));

        var list2    = new ArrayList<String>();
        var trimmed2 = MiscFunctions.trim(list2);
        assertNotNull(trimmed2);
        assertTrue(trimmed2.isEmpty());

    }

    @Test
    public void propertiesToMap() {

        var props1 = MiscFunctions.propertiesToMap(null);
        assertNotNull(props1);
        assertTrue(props1.isEmpty());

        var input1 = new Properties();
        input1.setProperty("key1", "value1");
        input1.setProperty("key2", "value2");

        var props2 = MiscFunctions.propertiesToMap(input1);
        assertNotNull(props2);
        assertThat(props2.size(), is(2));
        assertThat(props2.get("key1"), is("value1"));
        assertThat(props2.get("key2"), is("value2"));

    }

} /* ENDCLASS */
