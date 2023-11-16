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
public class EncodingTest {

    public static Stream<Arguments> data_performEncoding() {
        return Stream.of(Arguments.of("Flöz", Encoding.UTF8, new byte[] {(byte) 0x46, (byte) 0x6C, (byte) 0xC3,
            (byte) 0xB6, (byte) 0x7A}), Arguments.of("Flöz", Encoding.UTF16, new byte[] {(byte) 0xFE, (byte) 0xFF,
                (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00,
                (byte) 0x7A}), Arguments.of("Flöz", Encoding.UTF16BE, new byte[] {(byte) 0x00, (byte) 0x46, (byte) 0x00,
                    (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00,
                    (byte) 0x7A}), Arguments.of("Flöz", Encoding.UTF16LE, new byte[] {(byte) 0x46, (byte) 0x00,
                        (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00, (byte) 0x7A, (byte) 0x00}));
    }

    @ParameterizedTest
    @MethodSource("data_performEncoding")
    public void performEncoding(String literal, Encoding encoding, byte[] bytes) {
        byte[] encoded = encoding.encode(literal);
        assertThat(encoded, is(bytes));
    }

    @Test
    public void values() {
        assertThat(Encoding.values(), is(notNullValue()));
    }

    public static Stream<Arguments> data_valueByName() {
        return Arrays.asList(Encoding.values()).stream().map($ -> Arguments.of($.getEncoding(), $));
    }

    @ParameterizedTest
    @MethodSource("data_valueByName")
    public void valueByName(String name, Encoding expected) {
        var encoding = Encoding.findByName(name);
        assertNotNull(encoding);
        assertTrue(encoding.isPresent());
        assertThat(encoding.get(), is(expected));
    }

    @Test
    public void valueByName__UNKNOWN() {
        var encoding = Encoding.findByName("Bibo");
        assertNotNull(encoding);
        assertFalse(encoding.isPresent());
    }

} /* ENDCLASS */
