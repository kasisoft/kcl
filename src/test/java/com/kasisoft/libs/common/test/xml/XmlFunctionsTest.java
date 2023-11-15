package com.kasisoft.libs.common.test.xml;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.xml.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.test.*;

import org.w3c.dom.*;

import org.junit.jupiter.api.MethodOrderer.*;

import org.junit.jupiter.api.*;

import javax.xml.transform.stream.*;

import javax.xml.transform.dom.*;

import javax.xml.transform.*;

import java.nio.file.*;

import java.io.*;

/**
 * Tests for the class 'XmlFunctions'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@TestMethodOrder(OrderAnnotation.class)
public class XmlFunctionsTest {

    private static final TestResources TEST_RESOURCES = TestResources.createTestResources(XmlFunctionsTest.class);

    @Test
    @Order(1)
    public void readDocumentFailure() {
        assertThrows(KclException.class, () -> {
            var testfile = TEST_RESOURCES.getResource("testfile.gz");
            var config   = XmlParserConfiguration.builder().validate(true).xmlnamespaces(true).build();
            XmlFunctions.readDocument(testfile, config);
        });
    }

    private void readDocument(Document doc) {
        assertNotNull(doc);
        assertNotNull(doc.getDocumentElement());
        assertThat(doc.getDocumentElement().getTagName(), is("bookstore"));
    }

    @Test
    @Order(2)
    public void readDocument() {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
        readDocument(XmlFunctions.readDocument(simplexml, config));
    }

    @Test
    @Order(3)
    public void readDocument__NormalizeDocument() {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).normalize().build();
        readDocument(XmlFunctions.readDocument(simplexml, config));
    }

    @SuppressWarnings("deprecation")
    @Test
    @Order(4)
    public void readDocument__URL() throws Exception {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
        readDocument(XmlFunctions.readDocument(simplexml.toFile().toURL(), config));

    }

    @Test
    @Order(5)
    public void readDocument__URI() throws Exception {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
        readDocument(XmlFunctions.readDocument(simplexml.toUri(), config));
    }

    @Test
    @Order(6)
    public void readDocument__Reader() throws Exception {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        IoSupportFunctions.forReaderDo(simplexml, $ -> {
            var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
            readDocument(XmlFunctions.readDocument($, config));
        });
    }

    @Test
    @Order(7)
    public void readDocument__InputStream() throws Exception {
        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        IoSupportFunctions.forInputStreamDo(simplexml, $ -> {
            var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
            readDocument(XmlFunctions.readDocument($, config));
        });
    }

    @Test
    @Order(8)
    public void writeDocument() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var tempfile  = TEST_RESOURCES.getTempPath("xmlfunctions.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();

        var document  = XmlFunctions.readDocument(simplexml, config);
        XmlFunctions.writeDocument(tempfile, document, Encoding.ISO88591);
        assertTrue(Files.isRegularFile(tempfile));

        var reloaded = XmlFunctions.readDocument(tempfile, config);
        assertNotNull(reloaded);
        assertNotNull(reloaded.getDocumentElement());
        assertThat(reloaded.getDocumentElement().getTagName(), is("bookstore"));

        var children = reloaded.getDocumentElement().getElementsByTagName("title");
        assertNotNull(children);
        assertThat(children.getLength(), is(1));

        var title = (Element) children.item(0);
        assertThat(reloaded.getXmlEncoding(), is(Encoding.ISO88591.getEncoding()));
        assertThat(getText(title), is("Blöde Schuhe"));

    }

    private String getText(Element element) {
        var builder  = new StringBuilder();
        var children = element.getChildNodes();
        if (children != null) {
            for (var i = 0; i < children.getLength(); i++) {
                var node = children.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    builder.append(((Text) node).getNodeValue());
                }
            }
        }
        return builder.toString();
    }

    @Test
    @Order(9)
    public void encodeAndDecodeString() {

        var encoded = XmlFunctions.escapeXml("<Bla\nBlub\r\n>", true);
        assertThat(encoded, is("&lt;Bla&#10;Blub&#13;&#10;&gt;"));

        var decoded = XmlFunctions.unescapeXml(encoded, true);
        assertThat(decoded, is("<Bla\nBlub\r\n>"));
    }

    private void newTransformer(Transformer transformer) throws Exception {

        assertNotNull(transformer);

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();

        var document  = XmlFunctions.readDocument(simplexml, config);
        var byteout   = new ByteArrayOutputStream();
        var streamres = new StreamResult(byteout);
        transformer.transform(new DOMSource(document), streamres);

        var str = Encoding.ISO88591.decode(byteout.toByteArray());
        assertThat(str, is("Blöde Schuhe"));
    }

    @Test
    @Order(10)
    public void newTransformer() throws Exception {
        var simplexsl = TEST_RESOURCES.getResource("simple.xsl");
        newTransformer(XmlFunctions.newTransformer(simplexsl));
    }

    @Test
    @Order(11)
    public void newTransformer__File() throws Exception {
        var simplexsl = TEST_RESOURCES.getResource("simple.xsl");
        newTransformer(XmlFunctions.newTransformer(simplexsl.toFile()));
    }

    @Test
    @Order(12)
    public void newTransformer__URI() throws Exception {
        var simplexsl = TEST_RESOURCES.getResource("simple.xsl");
        newTransformer(XmlFunctions.newTransformer(simplexsl.toUri()));
    }

    @SuppressWarnings("deprecation")
    @Test
    @Order(13)
    public void newTransformer__URL() throws Exception {
        var simplexsl = TEST_RESOURCES.getResource("simple.xsl");
        newTransformer(XmlFunctions.newTransformer(simplexsl.toFile().toURL()));
    }

    @Test
    @Order(14)
    public void getChildElements() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().build();

        var document  = XmlFunctions.readDocument(simplexml, config);
        var list      = XmlFunctions.getChildElements(document.getDocumentElement());
        assertNotNull(list);
        assertThat(list.size(), is(1));

        var text = list.get(0).getTextContent();
        assertThat(text, is("Blöde Schuhe"));

    }

    @Test
    @Order(15)
    public void getChildElements__UnknownElement() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().build();

        var document  = XmlFunctions.readDocument(simplexml, config);
        var list      = XmlFunctions.getChildElements(document.getDocumentElement(), "crowd");
        assertNotNull(list);
        assertTrue(list.isEmpty());

    }

    @Test
    @Order(16)
    public void getChildElements__Selective() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().build();

        var document  = XmlFunctions.readDocument(simplexml, config);
        var list      = XmlFunctions.getChildElements(document.getDocumentElement(), "title");
        assertNotNull(list);
        assertThat(list.size(), is(1));

        var text = list.get(0).getTextContent();
        assertThat(text, is("Blöde Schuhe"));

    }

    @Test
    @Order(17)
    public void getChildNodes() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().build();
        var document  = XmlFunctions.readDocument(simplexml, config);

        var list      = XmlFunctions.getChildNodes(document.getDocumentElement().getChildNodes());
        assertNotNull(list);
        assertThat(list.size(), is(3));

        assertThat(list.get(0).getTextContent(), is("\n  "));
        assertThat(list.get(1).getTextContent(), is("Blöde Schuhe"));
        assertThat(list.get(2).getTextContent(), is("\n"));

    }

    @Test
    @Order(18)
    public void getAttribute() {

        var simplexml  = TEST_RESOURCES.getResource("simple.xml");
        var config     = XmlParserConfiguration.builder().build();
        var document   = XmlFunctions.readDocument(simplexml, config);

        var ageValue   = XmlFunctions.getAttribute("age");
        var docElement = document.getDocumentElement();

        assertThat(ageValue.apply(docElement), is("20"));

    }

    @Test
    @Order(19)
    public void getElementText() {

        var simplexml  = TEST_RESOURCES.getResource("simple.xml");
        var config     = XmlParserConfiguration.builder().build();
        var document   = XmlFunctions.readDocument(simplexml, config);

        var titleText  = XmlFunctions.getElementText("title");
        var docElement = document.getDocumentElement();

        assertThat(titleText.apply(docElement), is("Blöde Schuhe"));

    }

    @Test
    @Order(20)
    public void createElement() {

        var simplexml = TEST_RESOURCES.getResource("simple.xml");
        var config    = XmlParserConfiguration.builder().build();
        var document  = XmlFunctions.readDocument(simplexml, config);

        var element   = XmlFunctions.createElement(document, "bobo", "My TEXT", "fruppel", "dodo");
        document.getDocumentElement().appendChild(element);

        var writer = new StringWriter();
        XmlFunctions.writeDocument(writer, document, Encoding.UTF8);

        assertTrue(writer.toString().contains("<bobo fruppel=\"dodo\">My TEXT</bobo>"));

    }

    @Test
    @Order(21)
    public void insertFirst() {

        var simplexml  = TEST_RESOURCES.getResource("simple.xml");
        var config     = XmlParserConfiguration.builder().build();
        var document   = XmlFunctions.readDocument(simplexml, config);
        var docElement = document.getDocumentElement();

        var element    = XmlFunctions.createElement(document, "bobo", "My TEXT", "fruppel", "dodo");
        XmlFunctions.insertFirst(docElement, element);

        var writer = new StringWriter();
        XmlFunctions.writeDocument(writer, document, Encoding.UTF8);

        var xmldoc = writer.toString();

        var idx1   = xmldoc.indexOf("<bobo fruppel=\"dodo\">My TEXT</bobo>");
        var idx2   = xmldoc.indexOf("<title>Blöde Schuhe</title>");

        assertTrue(idx1 < idx2);

    }

} /* ENDCLASS */
