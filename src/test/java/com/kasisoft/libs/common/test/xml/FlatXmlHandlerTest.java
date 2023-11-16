package com.kasisoft.libs.common.test.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.test.*;

import com.kasisoft.libs.common.xml.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import javax.xml.parsers.*;

import java.io.*;

/**
 * Collection of testcases for the type {@link FlatXmlHandler}
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class FlatXmlHandlerTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(FlatXmlHandlerTest.class);

    @Test
    public void flatXml() throws Exception {

        var simplexml = TEST_RESOURCES.getFile("simple.xml");

        var handler   = new FlatXmlHandler();
        var byteout   = new ByteArrayOutputStream();
        handler.setTarget(byteout);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        IoSupportFunctions.forInputStreamDo(simplexml, $ -> {
            try {
                factory.newSAXParser().parse($, handler);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        });

        var text  = IoFunctions.readText(new ByteArrayInputStream(byteout.toByteArray()), Encoding.UTF8);
        var lines = text.split("\n");
        assertThat(lines[0], is("bookstore/@age=20"));
        assertThat(lines[1], is("bookstore/@soup=40"));
        assertThat(lines[2], is("bookstore/title/text()=Blöde Schuhe"));
        assertThat(lines[3], is("bookstore/text()="));

        // without attributes
        byteout.reset();
        handler.setAttributes(false);
        IoSupportFunctions.forInputStreamDo(simplexml, $ -> {
            try {
                factory.newSAXParser().parse($, handler);
            } catch (Exception ex) {
                throw KclException.wrap(ex);
            }
        });
        text  = IoFunctions.readText(new ByteArrayInputStream(byteout.toByteArray()), Encoding.UTF8);
        lines = text.split("\n");
        assertThat(lines[0], is("bookstore/title/text()=Blöde Schuhe"));
        assertThat(lines[1], is("bookstore/text()="));

    }

} /* ENDCLASS */
