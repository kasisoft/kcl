package com.kasisoft.libs.common.test.utils;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.utils.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.io.*;

import java.util.stream.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BlacklistTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(BlacklistTest.class);

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_csblacklist() throws Exception {
        var csblacklistPath = new Blacklist(TEST_RESOURCES.getResource("blacklist_01.txt"), Encoding.UTF8);
        var csblacklistURL  = new Blacklist(TEST_RESOURCES.getResource("blacklist_01.txt").toFile().toURL(), Encoding.UTF8);
        var csblacklistFile = new Blacklist(TEST_RESOURCES.getResource("blacklist_01.txt").toFile(), Encoding.UTF8);
        var csblacklistURI  = new Blacklist(TEST_RESOURCES.getResource("blacklist_01.txt").toUri(), Encoding.UTF8);
        return Stream.of(Arguments.of(csblacklistPath), Arguments.of(csblacklistURL), Arguments.of(csblacklistFile), Arguments.of(csblacklistURI));
    }

    @SuppressWarnings("deprecation")
    public static Stream<Arguments> data_ciblacklist() throws Exception {
        var ciblacklistPath = new Blacklist(TEST_RESOURCES.getResource("blacklist_02.txt"), Encoding.UTF8);
        var ciblacklistURL  = new Blacklist(TEST_RESOURCES.getResource("blacklist_02.txt").toFile().toURL(), Encoding.UTF8);
        var ciblacklistFile = new Blacklist(TEST_RESOURCES.getResource("blacklist_02.txt").toFile(), Encoding.UTF8);
        var ciblacklistURI  = new Blacklist(TEST_RESOURCES.getResource("blacklist_02.txt").toUri(), Encoding.UTF8);
        return Stream.of(Arguments.of(ciblacklistPath), Arguments.of(ciblacklistURL), Arguments.of(ciblacklistFile), Arguments.of(ciblacklistURI));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void commentPrefix(Blacklist csblacklist) {

        var prefixed = new Blacklist();
        prefixed.setCommentPrefix("$%");
        prefixed.load(TEST_RESOURCES.getResource("blacklist_03.txt"));

        var original = csblacklist.getBlacklisted();
        var other    = prefixed.getBlacklisted();
        assertNotNull(original);
        assertNotNull(other);
        assertThat(other, is(original));

    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void simpleCaseSensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.test("blacklisted"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void simpleCaseInsensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.testIgnoreCase().test("blackLISTED"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void startsWithCaseSensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.startsWith().test("blacklisted TEXT"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void startsWithCaseInsensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.startsWith(true).test("blackLISTED TEXT"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void endsWithCaseSensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.endsWith().test("TEXT blacklisted"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void endsWithCaseInsensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.endsWith(true).test("TEXT blackLISTED"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void containsCaseSensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.contains().test("TEXTA blacklisted TEXTB"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void containsCaseInsensitiveMatch(Blacklist csblacklist) {
        assertTrue(csblacklist.contains(true).test("TEXTA blackLISTEDTEXTB"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void cleanTextCaseSensitive(Blacklist csblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_01.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertFalse(csblacklist.test(text));
        assertThat(csblacklist.cleanup().apply(text).toString(), is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_ciblacklist")
    public void cleanTextCaseInsensitive(Blacklist ciblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_02.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertTrue(ciblacklist.contains(true).test(text));
        assertThat(ciblacklist.cleanup(true).apply(text).toString(), is(expected));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void cleanAndListTextCaseSensitive(Blacklist csblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_01.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertFalse(csblacklist.test(text));
        var collector = new HashSet<String>();
        assertThat(csblacklist.cleanup(collector::add).apply(text).toString(), is(expected));
        assertThat(collector.size(), is(2));
        assertTrue(collector.contains("blacklisted"));
        assertTrue(collector.contains("blacklisted2"));
    }

    @ParameterizedTest
    @MethodSource("data_ciblacklist")
    public void cleanAndListTextCaseInsensitive(Blacklist ciblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_02.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertTrue(ciblacklist.contains(true).test(text));
        var collector = new HashSet<String>();
        assertThat(ciblacklist.cleanup(true, collector::add).apply(text).toString(), is(expected));
        assertThat(collector.size(), is(3));
        assertTrue(collector.contains("blackliSTed"));
        assertTrue(collector.contains("BLacklistED2"));
        assertTrue(collector.contains("blACKListed"));
    }

    @ParameterizedTest
    @MethodSource("data_csblacklist")
    public void cleanAndCountTextCaseSensitive(Blacklist csblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_01.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertFalse(csblacklist.test(text));
        var counts = new HashMap<String, Integer>();
        assertThat(csblacklist.cleanup($ -> statistic(counts, $)).apply(text).toString(), is(expected));
        assertThat(counts.size(), is(2));
        assertTrue(counts.containsKey("blacklisted"));
        assertTrue(counts.containsKey("blacklisted2"));
        assertThat(counts.get("blacklisted"), is(Integer.valueOf(2)));
        assertThat(counts.get("blacklisted2"), is(Integer.valueOf(1)));
    }

    @ParameterizedTest
    @MethodSource("data_ciblacklist")
    public void cleanAndCountTextCaseInsensitive(Blacklist ciblacklist) {
        var expected = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_base.txt"));
        var text     = IoSupportFunctions.readText(TEST_RESOURCES.getResource("text_02.txt"));
        assertNotNull(text);
        assertFalse(text.equals(expected));
        assertTrue(ciblacklist.contains(true).test(text));
        var counts = new HashMap<String, Integer>();
        assertThat(ciblacklist.cleanup(true, $ -> statistic(counts, $)).apply(text).toString(), is(expected));
        assertThat(counts.size(), is(2));
        assertTrue(counts.containsKey("blacklisted"));
        assertTrue(counts.containsKey("blacklisted2"));
        assertThat(counts.get("blacklisted"), is(Integer.valueOf(1)));
        assertThat(counts.get("blacklisted2"), is(Integer.valueOf(1)));
    }

    private void statistic(Map<String, Integer> values, String literal) {
        if (values.containsKey(literal)) {
            values.put(literal.toLowerCase(), Integer.valueOf(values.get(literal).intValue() + 1));
        } else {
            values.put(literal.toLowerCase(), Integer.valueOf(1));
        }
    }

} /* ENDCLASS */
