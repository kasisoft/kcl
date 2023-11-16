package com.kasisoft.libs.common.test.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.provider.*;

import org.junit.jupiter.params.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.xml.XmlGenerator.*;

import java.util.stream.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class XmlGeneratorTest {

    private static final String COMMENT =
        """
        <root>
          <!-- my comment -->
          <element>content</element>
        </root>
        """;

    private static final String CDATA =
        """
        <root>
        <![CDATA[my cdata]]>
          <element>content</element>
        </root>
        """;

    private static final String ML_COMMENT =
        """
        <root>
          <!-- ~~~~~~~~~~~~~~~~~
          This is my text:


          Some escaping here: 12 > 7 &

          the meaning of life is 42
          ~~~~~~~~~~~~~~~~~~ -->
          <element>content</element>
        </root>
        """;

    private static final String RESET_1 =
        """
        <root>
          <element>content</element>
        </root>
        """;

    private static final String RESET_2 =
        """
        <flanch>
          <cucumber>weird</cucumber>
        </flanch>
        """;

    private static final String ATTRIBUTE_CONVERTER =
        """
        <?xml version="1.0" encoding="UTF-8"?>
        <bibo>
          <marker stoppi="changed"/>
          <bodo flambo="stoppival"/>
        </bibo>
        """;

    private static final String EXPECTED_TAG =
        """
        <?xml version=\"1.0\" encoding=\"UTF-8\"?>
        <bibo>
          <dodo alpha=\"beta&lt;&gt;\">
            <marker>Wumpi &amp; Stumpi</marker>
          </dodo>
        </bibo>
        """;

    private static final String EXPECTED_TAG_ML =
        """
        <?xml version="1.0" encoding="UTF-8"?>
        <bibo>
          <dodo alpha="beta&lt;&gt;">
            <marker>
              Wumpi &amp; Stumpi
        Kloppi &amp; Dodo
        Frank &amp; Stoffel
            </marker>
          </dodo>
        </bibo>
        """;

    private static final String EXPECTED_INVALID_ATTRIBUTE =
        """
        <?xml version=\"1.0\" encoding=\"UTF-8\"?>
        <bibo>
          <dodo>
            <marker>Wumpi &amp; Stumpi</marker>
          </dodo>
        </bibo>
        """;

    public static Stream<Arguments> data_generators() {
        return Stream.of(
            Arguments.of(new DefaultXmlGenerator()),
            Arguments.of(new DefaultXmlGenerator(Encoding.UTF8)),
            Arguments.of(new DefaultXmlGenerator(2)),
            Arguments.of(new DefaultXmlGenerator(Encoding.UTF8, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("data_generators")
    public void tag(DefaultXmlGenerator generator) {
        generator
            .processingInstruction()
            .openTag("bibo")
                .openTagV("dodo", "alpha", "beta<>")
                    .tag("marker", "Wumpi & Stumpi")
                .closeTag()
            .closeTag()
            ;
        assertThat(generator.toXml(), is(EXPECTED_TAG));
    }

    @ParameterizedTest
    @MethodSource("data_generators")
    public void tag__withMultilineText(DefaultXmlGenerator generator) {
        generator
            .processingInstruction()
            .openTag("bibo")
                .openTagV("dodo", "alpha", "beta<>")
                    .tag("marker", "Wumpi & Stumpi\nKloppi & Dodo\nFrank & Stoffel")
                .closeTag()
            .closeTag()
            ;
        assertThat(generator.toXml(), is(EXPECTED_TAG_ML));
    }

    @ParameterizedTest
    @MethodSource("data_generators")
    public void invalidAttribute(DefaultXmlGenerator generator) {
        generator
            .processingInstruction()
            .openTag("bibo")
                .openTagV("dodo", new Object(), "beta<>") // the bad attribute will NOT be part of the tag
                    .tag("marker", "Wumpi & Stumpi")
                .closeTag()
            .closeTag()
            ;
        assertThat(generator.toXml(), is(EXPECTED_INVALID_ATTRIBUTE));

    }

    @ParameterizedTest
    @MethodSource("data_generators")
    public void invalidAttributeWithException(DefaultXmlGenerator generator) {
        assertThrows(RuntimeException.class, () ->
            generator
                .withInvalidAttributeHandler(this::throwEx)
                .processingInstruction()
                .openTag("bibo")
                    .openTagV("dodo", new Object(), "beta<>")
                        .tag("marker", "Wumpi & Stumpi")
                    .closeTag()
                .closeTag()
        );
    }

    private void throwEx(Object key, Object val) {
        throw new RuntimeException();
    }

    @Test
    public void with_attributeConverter() {
        var generator = new DefaultXmlGenerator()
            .withAttributeValueConverter(($attr, $val) -> attributeValueConverter($attr, $val))
            .processingInstruction()
            .openTag("bibo")
                .tagV("marker", null, "stoppi", "stoppival", "ignore", "ignorevalue")
                .tagV("bodo", null, "flambo", "stoppival")
            .closeTag()
            ;
        assertThat(generator.toXml(), is(ATTRIBUTE_CONVERTER));
    }

    private String attributeValueConverter(String attr, Object value) {
        if ("stoppi".equals(attr)) {
            return "changed";
        }
        if ("ignore".equals(attr)) {
            return null;
        }
        return String.valueOf(value);
    }

    @Test
    public void reset() {

        var generator = new DefaultXmlGenerator()
            .openTag("root")
                .tagV("element", "content")
            .closeTag()
            ;
        assertThat(generator.toXml(), is(RESET_1));

        generator.reset();
        generator
            .openTag("flanch")
                .tagV("cucumber", "weird")
            .closeTag()
            ;
        assertThat(generator.toXml(), is(RESET_2));

    }

    @Test
    public void multilinecomment() {

        var generator = new DefaultXmlGenerator()
            .openTag("root")
                .multilineComment(
                """
                This is my text:


                Some escaping here: 12 > 7 &

                the meaning of life is 42



                """
                )
                .tagV("element", "content")
            .closeTag()
            ;

        assertThat(generator.toXml(), is(ML_COMMENT));

        generator.reset();
        generator
            .openTag("root")
                .multilineComment(null)
                .tagV("element", "content")
            .closeTag()
            ;

        assertThat(generator.toXml(), is(RESET_1));

    }

    @Test
    public void comment() {

        var generator = new DefaultXmlGenerator()
            .openTag("root")
                .comment("my comment")
                .tagV("element", "content")
            .closeTag()
            ;

        assertThat(generator.toXml(), is(COMMENT));

        generator.reset();
        generator
            .openTag("root")
                .comment(null)
                .tagV("element", "content")
            .closeTag()
            ;

        assertThat(generator.toXml(), is(RESET_1));

    }

    @Test
    public void cdata() {

        var generator = new DefaultXmlGenerator()
            .openTag("root")
                .cdata("my cdata")
                .tagV("element", "content")
            .closeTag()
            ;

        assertThat(generator.toXml(), is(CDATA));

        generator.reset();
        generator
          .openTag("root")
              .cdata(null)
              .tagV("element", "content")
          .closeTag()
          ;

        assertThat(generator.toXml(), is(RESET_1));

    }

} /* ENDCLASS */
