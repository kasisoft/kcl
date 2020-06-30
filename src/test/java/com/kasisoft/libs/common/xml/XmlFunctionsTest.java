package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.KclException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'XmlFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlFunctionsTest extends AbstractTestCase {
  
  Path   simplexml;
  Path   simplexsl;
  Path   testfile;
  Path   tempfile;
  
  @BeforeTest
  public void setup() {
    simplexml   = getResource("simple.xml"); 
    simplexsl   = getResource("simple.xsl");
    testfile    = getResource("testfile.gz");
    tempfile    = getTempPath("xmlfunctions.xml");
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void readDocumentFailure() {
    
    var config = XmlParserConfiguration.builder().validate(true).xmlnamespaces(true).build();
    XmlFunctions.readDocument(testfile, config);
    
  }
  
  private void readDocument(Document doc) {
    assertNotNull(doc);
    assertNotNull(doc.getDocumentElement());
    assertThat(doc.getDocumentElement().getTagName(), is("bookstore"));
  }
  
  @Test(groups = "all")
  public void readDocument() {
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
    readDocument(XmlFunctions.readDocument(simplexml, config));
  }

  @Test(groups = "all")
  public void readDocument__NormalizeDocument() {
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).normalize().build();
    readDocument(XmlFunctions.readDocument(simplexml, config));
  }

  @SuppressWarnings("deprecation")
  @Test(groups = "all")
  public void readDocument__URL() throws Exception {
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
    readDocument(XmlFunctions.readDocument(simplexml.toFile().toURL(), config));
    
  }

  @Test(groups = "all")
  public void readDocument__URI() throws Exception {
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
    readDocument(XmlFunctions.readDocument(simplexml.toUri(), config));
  }

  @Test(groups = "all")
  public void readDocument__Reader() throws Exception {
    IoFunctions.forReaderDo(simplexml, $ -> {
      var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
      readDocument(XmlFunctions.readDocument($, config));
    });
  }

  @Test(groups = "all")
  public void readDocument__InputStream() throws Exception {
    IoFunctions.forInputStreamDo(simplexml, $ -> {
      var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
      readDocument(XmlFunctions.readDocument($, config));
    });
  }

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void writeDocument() {
    
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
    
    var document = XmlFunctions.readDocument(simplexml, config);
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
  
  @Test(groups = "all")
  public void encodeAndDecodeString() {
    
    var encoded = XmlFunctions.escapeXml("<Bla\nBlub\r\n>", true);
    assertThat(encoded, is("&lt;Bla&#10;Blub&#13;&#10;&gt;"));
    
    var decoded = XmlFunctions.unescapeXml(encoded, true);
    assertThat(decoded, is("<Bla\nBlub\r\n>"));
  }
  
  private void newTransformer(Transformer transformer) throws Exception {
    assertNotNull(transformer);
    
    var config = XmlParserConfiguration.builder().validate(false).xmlnamespaces(true).build();
    
    var document  = XmlFunctions.readDocument(simplexml, config);
    var byteout   = new ByteArrayOutputStream();
    var streamres = new StreamResult(byteout);
    transformer.transform(new DOMSource(document), streamres);
    
    var str = Encoding.ISO88591.decode(byteout.toByteArray());
    assertThat(str, is("Blöde Schuhe"));
  }

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void newTransformer() throws Exception {
    newTransformer(XmlFunctions.newTransformer(simplexsl));
  }

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void newTransformer__File() throws Exception {
    newTransformer(XmlFunctions.newTransformer(simplexsl.toFile()));
  }

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void newTransformer__URI() throws Exception {
    newTransformer(XmlFunctions.newTransformer(simplexsl.toUri()));
  }

  @SuppressWarnings("deprecation")
  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void newTransformer__URL() throws Exception {
    newTransformer(XmlFunctions.newTransformer(simplexsl.toFile().toURL()));
  }

  @Test
  public void getChildElements() {
    
    var config = XmlParserConfiguration.builder().build();
    
    var document = XmlFunctions.readDocument(simplexml, config);
    var list     = XmlFunctions.getChildElements(document.getDocumentElement());
    assertNotNull(list);
    assertThat(list.size(), is(1));
    
    var text = list.get(0).getTextContent();
    assertThat(text, is("Blöde Schuhe"));
    
  }

  @Test
  public void getChildElements__UnknownElement() {
    
    var config = XmlParserConfiguration.builder().build();
    
    var document = XmlFunctions.readDocument(simplexml, config);
    var list     = XmlFunctions.getChildElements(document.getDocumentElement(), "crowd");
    assertNotNull(list);
    assertTrue(list.isEmpty());
    
  }

  @Test
  public void getChildElements__Selective() {
    
    var config = XmlParserConfiguration.builder().build();
    
    var document = XmlFunctions.readDocument(simplexml, config);
    var list     = XmlFunctions.getChildElements(document.getDocumentElement(), "title");
    assertNotNull(list);
    assertThat(list.size(), is(1));
    
    var text = list.get(0).getTextContent();
    assertThat(text, is("Blöde Schuhe"));
    
  }

  @Test
  public void getChildNodes() {
    
    var config   = XmlParserConfiguration.builder().build();
    var document = XmlFunctions.readDocument(simplexml, config);

    var list     = XmlFunctions.getChildNodes(document.getDocumentElement().getChildNodes());
    assertNotNull(list);
    assertThat(list.size(), is(3));
    
    assertThat(list.get(0).getTextContent(), is("\n  "));
    assertThat(list.get(1).getTextContent(), is("Blöde Schuhe"));
    assertThat(list.get(2).getTextContent(), is("\n"));
    
  }
  
  @Test
  public void getAttribute() {
   
    var config      = XmlParserConfiguration.builder().build();
    var document    = XmlFunctions.readDocument(simplexml, config);
    
    var ageValue    = XmlFunctions.getAttribute("age");
    var docElement  = document.getDocumentElement();
    
    assertThat(ageValue.apply(docElement), is("20"));
    
  }

  @Test
  public void getElementText() {
   
    var config      = XmlParserConfiguration.builder().build();
    var document    = XmlFunctions.readDocument(simplexml, config);
    
    var titleText   = XmlFunctions.getElementText("title");
    var docElement  = document.getDocumentElement();
    
    assertThat(titleText.apply(docElement), is("Blöde Schuhe"));
    
  }
  
  @Test
  public void createElement() {
    
    var config      = XmlParserConfiguration.builder().build();
    var document    = XmlFunctions.readDocument(simplexml, config);
    
    var element     = XmlFunctions.createElement(document, "bobo", "My TEXT", "fruppel", "dodo");
    document.getDocumentElement().appendChild(element);
    
    var writer = new StringWriter();
    XmlFunctions.writeDocument(writer, document, Encoding.UTF8);
    
    assertTrue(writer.toString().contains("<bobo fruppel=\"dodo\">My TEXT</bobo>"));
    
  }
  
  @Test
  public void insertFirst() {

    var config      = XmlParserConfiguration.builder().build();
    var document    = XmlFunctions.readDocument(simplexml, config);
    var docElement  = document.getDocumentElement();

    var element     = XmlFunctions.createElement(document, "bobo", "My TEXT", "fruppel", "dodo");
    XmlFunctions.insertFirst(docElement, element);
    
    var writer = new StringWriter();
    XmlFunctions.writeDocument(writer, document, Encoding.UTF8);
    
    var xmldoc = writer.toString();
    
    var idx1   = xmldoc.indexOf("<bobo fruppel=\"dodo\">My TEXT</bobo>");
    var idx2   = xmldoc.indexOf("<title>Blöde Schuhe</title>");
    
    assertTrue(idx1 < idx2);

  }

} /* ENDCLASS */
