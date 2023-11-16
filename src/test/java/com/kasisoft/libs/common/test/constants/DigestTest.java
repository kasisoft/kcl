package com.kasisoft.libs.common.test.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.constants.*;

import java.util.stream.*;

/**
 * Tests for the constants 'Digest'.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class DigestTest {

    public static Stream<Arguments> data_digest() {
        return Stream.of(Arguments.of("This is my test phrase", Digest.MD2), Arguments.of("This is my test phrase", Digest.MD5), Arguments.of("This is my test phrase", Digest.SHA1), Arguments.of("This is my test phrase", Digest.SHA256), Arguments.of("This is my test phrase", Digest.SHA384), Arguments.of("This is my test phrase", Digest.SHA512));
    }

    @ParameterizedTest
    @MethodSource("data_digest")
    public void digest(String text, Digest digest) {
        var data        = text.getBytes();
        var hash_first  = digest.digest(data);
        var hash_second = digest.digest(data);
        assertThat(hash_first, is(hash_second));
    }

    public static Stream<Arguments> data_digestToString() {
        return Stream.of(Arguments.of("This is my test phrase", Digest.MD2, "5a6ce9fb168eb92bb912be6c90102572"), Arguments.of("This is my test phrase", Digest.MD5, "76a2d4204bcd9ea5aac40ebf833f7345"), Arguments.of("This is my test phrase", Digest.SHA1, "bc73f7c09da0c9791cb38e93a4d3bf22e53a467d"), Arguments.of("This is my test phrase", Digest.SHA256, "6d73554fcfa28065a41eb49038049f848b91efc6af26ab0035e709bb4bf63cc7"), Arguments.of("This is my test phrase", Digest.SHA384, "5811a79674c3a6eb1a6243a27f9cd06244ad5b3096a5554f902652c686c89092f8a7a8891b93c09f7f2743be1b0dc83f"), Arguments.of("This is my test phrase", Digest.SHA512, "f5aa01867cd292624701b1bd56746569049314d2e940a12b1f43d98b73c97f28972856eb8832f2ac6f18f694bf252056271ad625c187d628185525a2744de4eb"));
    }

    @ParameterizedTest
    @MethodSource("data_digestToString")
    public void digestToString(String text, Digest digest, String expectedVal) {
        assertThat(digest.digestToString(text.getBytes()), is(expectedVal));
    }

} /* ENDCLASS */
