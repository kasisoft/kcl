package com.kasisoft.libs.common.old.xml;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.test.framework.Utilities;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
    URL        xmlschemadtd = getClass().getResource( "/old/dtds/XMLSchema.dtd" ); 
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

  @Test(groups="all")
  public void test() throws MalformedURLException {
    
    XmlCatalog catalog1     = new XmlCatalog();
    URL        xmlschemadtd = getClass().getResource( "/old/dtds/XMLSchema.dtd" ); 
    assertThat( xmlschemadtd, is( notNullValue() ) );
    catalog1.registerSystemID( xmlschemadtd );
    
    assertTrue  ( catalog1.test( xmlschemadtd.toExternalForm() ) );
    assertFalse ( catalog1.test( httpxsd.toURI().toURL().toExternalForm() ) );

  }

} /* ENDCLASS */
