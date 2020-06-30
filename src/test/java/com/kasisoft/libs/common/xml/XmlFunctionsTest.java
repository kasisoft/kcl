package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.KclException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.ByteArrayOutputStream;

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
    var config = XmlParserConfiguration
      .builder()
      .validate(true)
      .xmlnamespaces(true)
      .build();
    XmlFunctions.readDocument(testfile, config);
  }
  
  @Test(groups = "all")
  public void readDocument() {
    
    var config = XmlParserConfiguration
      .builder()
      .validate(false)
      .xmlnamespaces(true)
      .build();
    
    var document = XmlFunctions.readDocument(simplexml, config);
    assertNotNull(document);
    assertNotNull(document.getDocumentElement());
    assertThat(document.getDocumentElement().getTagName(), is("bookstore"));
    
  }

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void writeDocument() {
    
    var config = XmlParserConfiguration
      .builder()
      .validate(false)
      .xmlnamespaces(true)
      .build();
    
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

  @Test(groups = "all", dependsOnMethods = "readDocument")
  public void newTransformer() throws TransformerException {
    
    var transformer = XmlFunctions.newTransformer(simplexsl);
    assertNotNull(transformer);
    
    var config = XmlParserConfiguration
      .builder()
      .validate(false)
      .xmlnamespaces(true)
      .build();
    
    var document  = XmlFunctions.readDocument(simplexml, config);
    var byteout   = new ByteArrayOutputStream();
    var streamres = new StreamResult(byteout);
    transformer.transform(new DOMSource(document), streamres);
    
    var str = Encoding.ISO88591.decode(byteout.toByteArray());
    assertThat(str, is("Blöde Schuhe"));
    
  }
  
} /* ENDCLASS */
