/**
 * Name........: FlatXmlHandlerTest
 * Description.: Collection of testcases for the type 'FlatXmlHandler'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import javax.xml.parsers.*;

import java.util.*;

import java.io.*;

/**
 * Collection of testcases for the type 'FlatXmlHandler'.
 */
public class FlatXmlHandlerTest {

  private File    simplexml;
  
  @BeforeTest
  public void setup() {
    File testdata = Utilities.getTestdataDir();
    simplexml     = new File( testdata, "simple.xml" );
    Assert.assertTrue( simplexml.isFile() );
  }
  
  @Test(groups="all")
  public void flatXml() throws Exception {
    
    FlatXmlHandler        handler = new FlatXmlHandler();
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    handler.setTarget( byteout );
    
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating( false );
    factory.newSAXParser().parse( simplexml, handler );

    Reader       reader = Encoding.UTF8.openReader( new ByteArrayInputStream( byteout.toByteArray() ) );
    List<String> lines  = IoFunctions.readText( reader, false, false );
    Assert.assertEquals( lines.get(0), "bookstore/@age=20" );
    Assert.assertEquals( lines.get(1), "bookstore/@soup=40" );
    Assert.assertEquals( lines.get(2), "bookstore/title/text()=Blöde Schuhe" );
    Assert.assertEquals( lines.get(3), "bookstore/text()=" );

    // without attributes
    byteout.reset();
    handler.setAttributes( false );
    factory.newSAXParser().parse( simplexml, handler );
    reader = Encoding.UTF8.openReader( new ByteArrayInputStream( byteout.toByteArray() ) );
    lines  = IoFunctions.readText( reader, false, false );
    Assert.assertEquals( lines.get(0), "bookstore/title/text()=Blöde Schuhe" );
    Assert.assertEquals( lines.get(1), "bookstore/text()=" );
    
    
  }

} /* ENDCLASS */
