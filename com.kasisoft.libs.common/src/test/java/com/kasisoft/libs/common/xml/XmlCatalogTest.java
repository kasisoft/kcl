package com.kasisoft.libs.common.xml;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import java.net.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Collection of testcases for the type 'XmlCatalog'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlCatalogTest {

  File    httpxsd;
  File    xmlxsd;
  
  @BeforeTest
  public void setup() {
    File testdata = Utilities.getTestdataDir();
    httpxsd       = new File( testdata, "http.xsd" );
    xmlxsd        = new File( testdata, "xml.xsd" );
    assertTrue( httpxsd . isFile () );
    assertTrue( xmlxsd  . isFile () );
  }
  
  @Test(groups="all")
  public void loadResources() throws MalformedURLException {
    
    XmlCatalog catalog1     = new XmlCatalog();
    URL        xmlschemadtd = getClass().getResource( "/dtds/XMLSchema.dtd" ); 
    assertThat( xmlschemadtd, is( notNullValue() ) );
    catalog1.registerPublicID( "-//W3C//DTD XMLSCHEMA 200102//EN", xmlschemadtd );
    byte[]     content1     = catalog1.loadResource( "-//W3C//DTD XMLSCHEMA 200102//EN" );
    assertThat( content1, is( notNullValue() ) );
    
    XmlCatalog catalog2     = new XmlCatalog();
    catalog2.registerSystemID( xmlschemadtd );
    byte[]     content2     = catalog2.loadResource( xmlschemadtd );
    assertThat( content2, is( notNullValue() ) );

    XmlCatalog catalog3     = new XmlCatalog();
    catalog3.registerSystemID( httpxsd.toURI().toURL() );
    catalog3.registerSystemID( xmlxsd.toURI().toURL() );
    byte[]     content3     = catalog3.loadResource( httpxsd.toURI().toURL() );
    assertThat( content3, is( notNullValue() ) );
    byte[]     content4     = catalog3.loadResource( xmlxsd.toURI().toURL() );
    assertThat( content4, is( notNullValue() ) );

  }
  
} /* ENDCLASS */
