package com.kasisoft.libs.common.old.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.parsers.SAXParserFactory;

import java.util.List;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Reader;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Collection of testcases for the type 'FlatXmlHandler'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FlatXmlHandlerTest {

  File    simplexml;
  
  @BeforeTest
  public void setup() {
    File testdata = Utilities.getTestdataDir();
    simplexml     = new File( testdata, "simple.xml" );
    assertTrue( simplexml.isFile() );
  }
  
  @SuppressWarnings("resource")
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
    assertThat( lines.get(0), is( "bookstore/@age=20" ) );
    assertThat( lines.get(1), is( "bookstore/@soup=40" ) );
    assertThat( lines.get(2), is( "bookstore/title/text()=Blöde Schuhe" ) );
    assertThat( lines.get(3), is( "bookstore/text()=" ) );

    // without attributes
    byteout.reset();
    handler.setAttributes( false );
    factory.newSAXParser().parse( simplexml, handler );
    reader = Encoding.UTF8.openReader( new ByteArrayInputStream( byteout.toByteArray() ) );
    lines  = IoFunctions.readText( reader, false, false );
    assertThat( lines.get(0), is( "bookstore/title/text()=Blöde Schuhe" ) );
    assertThat( lines.get(1), is( "bookstore/text()=" ) );
    
    
  }

} /* ENDCLASS */
