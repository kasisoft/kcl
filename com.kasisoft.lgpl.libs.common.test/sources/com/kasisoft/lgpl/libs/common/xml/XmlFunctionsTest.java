/**
 * Name........: XmlFunctionsTest
 * Description.: Tests for the class 'XmlFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.base.*;

import org.testng.annotations.*;

import org.testng.*;

import org.w3c.dom.*;

import java.io.*;

/**
 * Tests for the class 'XmlFunctions'.
 */
@Test(groups="all")
public class XmlFunctionsTest {
  
  private File   simplexml;
  private File   testfile;
  private File   tempfile;
  
  @BeforeSuite
  public void setup() {
    File testdata = new File( "testdata" );
    simplexml     = new File( testdata, "simple.xml" );
    testfile      = new File( testdata, "testfile.gz" );
    tempfile      = IoFunctions.newTempFile( "xmlfunctions", ".xml" );
  }

  @Test(expectedExceptions=FailureException.class)
  public void readDocumentFailure() {
    XmlFunctions.readDocument( testfile, true, true );
  }
  
  @Test
  public void readDocument() {
    Document document = XmlFunctions.readDocument( simplexml, true, true );
    Assert.assertNotNull( document );
    Assert.assertNotNull( document.getDocumentElement() );
    Assert.assertEquals( document.getDocumentElement().getTagName(), "bookstore" );
  }

  @Test(dependsOnMethods="readDocument")
  public void writeDocument() {
    Document document = XmlFunctions.readDocument( simplexml, true, true );
    XmlFunctions.writeDocument( tempfile, document, Encoding.ISO88591 );
    Assert.assertTrue( tempfile.isFile() );
    Document reloaded = XmlFunctions.readDocument( tempfile, true, true );
    Assert.assertNotNull( reloaded );
    Assert.assertNotNull( reloaded.getDocumentElement() );
    Assert.assertEquals( reloaded.getDocumentElement().getTagName(), "bookstore" );
    NodeList children = reloaded.getDocumentElement().getElementsByTagName( "title" );
    Assert.assertNotNull( children );
    Assert.assertEquals( children.getLength(), 1 );
    Element title = (Element) children.item(0);
    Assert.assertEquals( title.getTextContent(), "Blöde Schuhe" );
  }
  
  @Test
  public void encodeAndDecodeString() {
    String encoded = XmlFunctions.encodeString( "<Bla\nBlub\r\n>" );
    Assert.assertEquals( encoded, "&lt;Bla&#10;Blub&#13;&#10;&gt;" );
    String decoded = XmlFunctions.decodeString( encoded );
    Assert.assertEquals( decoded, "<Bla\nBlub\r\n>" );
  }

} /* ENDCLASS */
