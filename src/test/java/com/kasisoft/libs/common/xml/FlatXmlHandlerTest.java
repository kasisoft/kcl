package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.io.IoFunctions;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.KclException;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.parsers.SAXParserFactory;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Collection of testcases for the type 'FlatXmlHandler'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlatXmlHandlerTest extends AbstractTestCase {

  Path    simplexml;
  
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
