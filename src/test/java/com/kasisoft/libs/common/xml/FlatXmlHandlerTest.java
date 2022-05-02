package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import javax.xml.parsers.*;

import java.nio.file.*;

import java.io.*;

/**
 * Collection of testcases for the type 'FlatXmlHandler'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FlatXmlHandlerTest extends AbstractTestCase {

  private Path    simplexml;
  
  @BeforeTest
  public void setup() {
    simplexml     = getResource("simple.xml");
    assertTrue( Files.isRegularFile(simplexml) );
  }
  
  @SuppressWarnings("resource")
  @Test(groups="all")
  public void flatXml() throws Exception {
    
    FlatXmlHandler        handler = new FlatXmlHandler();
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    handler.setTarget( byteout );
    
    SAXParserFactory factory = SAXParserFactory.newInstance();
    factory.setValidating( false );
    IoFunctions.forInputStreamDo(simplexml, $ -> {
      try {
        factory.newSAXParser().parse($, handler );
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    });

    String text = IoFunctions.readText( new ByteArrayInputStream( byteout.toByteArray() ), Encoding.UTF8 );
    String[] lines = text.split("\n");
    assertThat( lines[0], is( "bookstore/@age=20" ) );
    assertThat( lines[1], is( "bookstore/@soup=40" ) );
    assertThat( lines[2], is( "bookstore/title/text()=Blöde Schuhe" ) );
    assertThat( lines[3], is( "bookstore/text()=" ) );

    // without attributes
    byteout.reset();
    handler.setAttributes( false );
    IoFunctions.forInputStreamDo(simplexml, $ -> {
      try {
        factory.newSAXParser().parse( $, handler );
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    });
    text = IoFunctions.readText( new ByteArrayInputStream( byteout.toByteArray() ), Encoding.UTF8 );
    lines = text.split("\n");
    assertThat( lines[0], is( "bookstore/title/text()=Blöde Schuhe" ) );
    assertThat( lines[1], is( "bookstore/text()=" ) );
    
    
  }

} /* ENDCLASS */
