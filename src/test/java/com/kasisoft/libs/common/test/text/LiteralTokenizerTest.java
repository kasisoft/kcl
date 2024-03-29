package com.kasisoft.libs.common.test.text;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import com.kasisoft.libs.common.text.*;

import java.util.stream.*;

import java.util.*;

/**
 * Testcases for the class {@link LiteralTokenizer}
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class LiteralTokenizerTest {

    public static Stream<Arguments> data_tokenize() {

        String input1 = "@PART@ ist @FLUPPE@ heute hier";
        String input2 = "Fred ist @FLUPPE@ heute @PART@";
        String input3 = "Fred @PART@ @FLUPPE@ heute hier";
        String input4 = "#PART# ist #FLUPPE# heute hier";
        String input5 = "";

        return Stream.of(
            Arguments.of(input1, Boolean.FALSE, new String[] {" ist ", " heute hier"}),
            Arguments.of(input1, Boolean.TRUE, new String[] {"@PART@", " ist ", "@FLUPPE@", " heute hier"}),
            Arguments.of(input2, Boolean.FALSE, new String[] {"Fred ist ", " heute "}),
            Arguments.of(input2, Boolean.TRUE, new String[] {"Fred ist ", "@FLUPPE@", " heute ", "@PART@"}),
            Arguments.of(input3, Boolean.FALSE, new String[] {"Fred ", " ", " heute hier"}),
            Arguments.of(input3, Boolean.TRUE, new String[] {"Fred ", "@PART@", " ", "@FLUPPE@", " heute hier"}),
            Arguments.of(input4, Boolean.FALSE, new String[] {"#PART# ist #FLUPPE# heute hier"}),
            Arguments.of(input4, Boolean.TRUE, new String[] {"#PART# ist #FLUPPE# heute hier"}),
            Arguments.of(input5, Boolean.FALSE, new String[] {""}),
            Arguments.of(input5, Boolean.TRUE, new String[] {""})
        );

    }

    @ParameterizedTest
    @MethodSource("data_tokenize")
    public void tokenize(String input, boolean returndelimiters, Object[] expected) {
        var tokenizer = new LiteralTokenizer(input, returndelimiters, "@PART@", "@FLUPPE@");
        var tokens    = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextElement());
        }
        assertThat(tokens.toArray(), is(expected));
    }

    public static Stream<Arguments> data_tokenizeStringLike() {
        var input1 = new StringBuilder("@PART@ ist @FLUPPE@ heute hier");
        var input2 = new StringBuilder("Fred ist @FLUPPE@ heute @PART@");
        var input3 = new StringBuilder("Fred @PART@ @FLUPPE@ heute hier");
        var input4 = new StringBuilder("#PART# ist #FLUPPE# heute hier");
        var input5 = new StringBuilder("");
        return Stream.of(
            Arguments.of(input1, Boolean.FALSE, new String[] {" ist ", " heute hier"}),
            Arguments.of(input1, Boolean.TRUE, new String[] {"@PART@", " ist ", "@FLUPPE@", " heute hier"}),
            Arguments.of(input2, Boolean.FALSE, new String[] {"Fred ist ", " heute "}),
            Arguments.of(input2, Boolean.TRUE, new String[] {"Fred ist ", "@FLUPPE@", " heute ", "@PART@"}),
            Arguments.of(input3, Boolean.FALSE, new String[] {"Fred ", " ", " heute hier"}),
            Arguments.of(input3, Boolean.TRUE, new String[] {"Fred ", "@PART@", " ", "@FLUPPE@", " heute hier"}),
            Arguments.of(input4, Boolean.FALSE, new String[] {"#PART# ist #FLUPPE# heute hier"}),
            Arguments.of(input4, Boolean.TRUE, new String[] {"#PART# ist #FLUPPE# heute hier"}),
            Arguments.of(input5, Boolean.FALSE, new String[] {""}), Arguments.of(input5, Boolean.TRUE, new String[] {""})
        );
    }

    @ParameterizedTest
    @MethodSource("data_tokenizeStringLike")
    public void tokenizeStringLike(StringBuilder input, boolean returndelimiters, Object[] expected) {
        var tokenizer = new LiteralTokenizer(input.toString(), returndelimiters, "@PART@", "@FLUPPE@");
        var tokens    = new ArrayList<>();
        while (tokenizer.hasMoreElements()) {
            tokens.add(tokenizer.nextElement());
        }
        assertThat(tokens.toArray(), is(expected));
    }

} /* ENDCLASS */
