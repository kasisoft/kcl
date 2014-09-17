/**
 * Name........: XmlCatalogTest
 * Description.: Collection of testcases for the type 'XmlCatalog'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml;

import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.net.*;

import java.io.*;

/**
 * Collection of testcases for the type 'XmlCatalog'.
 */
public class XmlCatalogTest {

  private File    httpxsd;
  private File    xmlxsd;
  
  @BeforeTest
  public void setup() {
    File testdata = Utilities.getTestdataDir();
    httpxsd       = new File( testdata, "http.xsd" );
    xmlxsd        = new File( testdata, "xml.xsd" );
    Assert.assertTrue( httpxsd . isFile () );
    Assert.assertTrue( xmlxsd  . isFile () );
  }
  
  @Test(groups="all")
  public void loadResources() throws MalformedURLException {
    
    XmlCatalog catalog1     = new XmlCatalog();
    URL        xmlschemadtd = getClass().getResource( "/dtds/XMLSchema.dtd" ); 
    Assert.assertNotNull( xmlschemadtd );
    catalog1.registerPublicID( "-//W3C//DTD XMLSCHEMA 200102//EN", xmlschemadtd );
    byte[]     content1     = catalog1.loadResource( "-//W3C//DTD XMLSCHEMA 200102//EN" );
    Assert.assertNotNull( content1 );
    
    XmlCatalog catalog2     = new XmlCatalog();
    catalog2.registerSystemID( xmlschemadtd );
    byte[]     content2     = catalog2.loadResource( xmlschemadtd );
    Assert.assertNotNull( content2 );

    XmlCatalog catalog3     = new XmlCatalog();
    catalog3.registerSystemID( httpxsd.toURI().toURL() );
    catalog3.registerSystemID( xmlxsd.toURI().toURL() );
    byte[]     content3     = catalog3.loadResource( httpxsd.toURI().toURL() );
    Assert.assertNotNull( content3 );
    byte[]     content4     = catalog3.loadResource( xmlxsd.toURI().toURL() );
    Assert.assertNotNull( content4 );

  }
  
} /* ENDCLASS */
