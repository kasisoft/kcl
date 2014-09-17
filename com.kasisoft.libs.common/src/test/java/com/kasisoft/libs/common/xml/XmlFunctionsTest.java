/**
 * Name........: XmlFunctionsTest
 * Description.: Tests for the class 'XmlFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.*;

/**
 * Tests for the class 'XmlFunctions'.
 */
@Test(groups="all")
public class XmlFunctionsTest {
  
  private File   simplexml;
  private File   simplexsl;
  private File   testfile;
  private File   tempfile;
  
  @BeforeTest
  public void setup() {
    File testdata = Utilities.getTestdataDir();
    simplexml     = new File( testdata, "simple.xml" );
    simplexsl     = new File( testdata, "simple.xsl" );
    testfile      = new File( testdata, "testfile.gz" );
    tempfile      = IoFunctions.newTempFile( "xmlfunctions", ".xml" );
  }

  @Test(expectedExceptions=FailureException.class)
  public void readDocumentFailure() {
    XmlFunctions.readDocument( testfile, true, true );
  }
  
  @Test
  public void readDocument() {
    Document document = XmlFunctions.readDocument( simplexml, false, true );
    Assert.assertNotNull( document );
    Assert.assertNotNull( document.getDocumentElement() );
    Assert.assertEquals( document.getDocumentElement().getTagName(), "bookstore" );
  }

  @Test(dependsOnMethods="readDocument")
  public void writeDocument() {
    Document document = XmlFunctions.readDocument( simplexml, false, true );
    XmlFunctions.writeDocument( tempfile, document, Encoding.ISO88591 );
    Assert.assertTrue( tempfile.isFile() );
    Document reloaded = XmlFunctions.readDocument( tempfile, false, true );
    Assert.assertNotNull( reloaded );
    Assert.assertNotNull( reloaded.getDocumentElement() );
    Assert.assertEquals( reloaded.getDocumentElement().getTagName(), "bookstore" );
    NodeList children = reloaded.getDocumentElement().getElementsByTagName( "title" );
    Assert.assertNotNull( children );
    Assert.assertEquals( children.getLength(), 1 );
    Element title = (Element) children.item(0);
    Assert.assertEquals( getText( title ), "Blöde Schuhe" );
  }

  private String getText( Element element ) {
    StringBuilder builder  = new StringBuilder();
    NodeList      children = element.getChildNodes();
    if( children != null ) {
      for( int i = 0; i < children.getLength(); i++ ) {
        Node node = children.item(i);
        if( node.getNodeType() == Node.TEXT_NODE ) {
          builder.append( ((Text) node).getNodeValue() );
        }
      }
    }
    return builder.toString();
  }
  
  @Test
  public void encodeAndDecodeString() {
    String encoded = XmlFunctions.encodeString( "<Bla\nBlub\r\n>" );
    Assert.assertEquals( encoded, "&lt;Bla&#10;Blub&#13;&#10;&gt;" );
    String decoded = XmlFunctions.decodeString( encoded );
    Assert.assertEquals( decoded, "<Bla\nBlub\r\n>" );
  }

  @Test(dependsOnMethods="readDocument")
  public void newTransformer() throws TransformerException {
    
    Transformer           transformer = XmlFunctions.newTransformer( simplexsl );
    Assert.assertNotNull( transformer );
    
    Document              document    = XmlFunctions.readDocument( simplexml, false, true );
    ByteArrayOutputStream byteout     = new ByteArrayOutputStream();
    StreamResult          streamres   = new StreamResult( byteout );
    transformer.transform( new DOMSource( document ), streamres );
    
    String                str         = Encoding.ISO88591.decode( byteout.toByteArray() );
    Assert.assertEquals( str, "Blöde Schuhe" );
    
  }
  
} /* ENDCLASS */
