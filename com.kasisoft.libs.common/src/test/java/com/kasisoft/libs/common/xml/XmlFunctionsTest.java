package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.w3c.dom.*;

import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the class 'XmlFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlFunctionsTest {
  
  File   simplexml;
  File   simplexsl;
  File   testfile;
  File   tempfile;
  
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
    assertThat( document, is( notNullValue() ) );
    assertThat( document.getDocumentElement(), is( notNullValue() ) );
    assertThat( document.getDocumentElement().getTagName(), is( "bookstore" ) );
  }

  @Test(dependsOnMethods="readDocument")
  public void writeDocument() {
    Document document = XmlFunctions.readDocument( simplexml, false, true );
    XmlFunctions.writeDocument( tempfile, document, Encoding.ISO88591 );
    assertTrue( tempfile.isFile() );
    Document reloaded = XmlFunctions.readDocument( tempfile, false, true );
    assertThat( reloaded, is( notNullValue() ) );
    assertThat( reloaded.getDocumentElement(), is( notNullValue() ) );
    assertThat( reloaded.getDocumentElement().getTagName(), is( "bookstore" ) );
    NodeList children = reloaded.getDocumentElement().getElementsByTagName( "title" );
    assertNotNull( children );
    assertThat( children.getLength(), is(1) );
    Element title = (Element) children.item(0);
    assertThat( getText( title ), is( "Blöde Schuhe" ) );
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
    assertThat( encoded, is( "&lt;Bla&#10;Blub&#13;&#10;&gt;" ) );
    String decoded = XmlFunctions.decodeString( encoded );
    assertThat( decoded, is( "<Bla\nBlub\r\n>" ) );
  }

  @Test(dependsOnMethods="readDocument")
  public void newTransformer() throws TransformerException {
    
    Transformer           transformer = XmlFunctions.newTransformer( simplexsl );
    assertNotNull( transformer );
    
    Document              document    = XmlFunctions.readDocument( simplexml, false, true );
    ByteArrayOutputStream byteout     = new ByteArrayOutputStream();
    StreamResult          streamres   = new StreamResult( byteout );
    transformer.transform( new DOMSource( document ), streamres );
    
    String                str         = Encoding.ISO88591.decode( byteout.toByteArray() );
    assertThat( str, is( "Blöde Schuhe" ) );
    
  }
  
} /* ENDCLASS */
