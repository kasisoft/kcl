package com.kasisoft.libs.common.xml;

import static org.testng.Assert.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import java.net.*;

import java.nio.file.*;

/**
 * Collection of testcases for the type 'XmlCatalog'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class XmlCatalogTest extends AbstractTestCase {

  private Path  httpxsd;
  private Path  xmlxsd;
  
  @BeforeSuite
  public void setup() {
    httpxsd = getResource("http.xsd");
    xmlxsd  = getResource("xml.xsd");
  }
  
  @Test(groups = "all")
  public void loadResources() throws MalformedURLException {
    
    var catalog1     = new XmlCatalog();
    var xmlschemadtd = getClass().getClassLoader().getResource("dtds/XMLSchema.dtd"); 
    assertNotNull(xmlschemadtd);
    catalog1.registerPublicID("-//W3C//DTD XMLSCHEMA 200102//EN", xmlschemadtd);
    
    var content1     = catalog1.loadResource("-//W3C//DTD XMLSCHEMA 200102//EN");
    assertNotNull(content1);
    
    var catalog2     = new XmlCatalog();
    catalog2.registerSystemID(xmlschemadtd);
    
    var content2     = catalog2.loadResource(xmlschemadtd);
    assertNotNull(content2);

    var catalog3     = new XmlCatalog();
    catalog3.registerSystemID(httpxsd.toUri().toURL());
    catalog3.registerSystemID(xmlxsd.toUri().toURL());
    
    var content3     = catalog3.loadResource(httpxsd.toUri().toURL());
    assertNotNull(content3);
    
    var content4     = catalog3.loadResource(xmlxsd.toUri().toURL());
    assertNotNull(content4);

  }

  @Test(groups = "all")
  public void test() throws MalformedURLException {
    
    var catalog1     = new XmlCatalog();
    var xmlschemadtd = getClass().getClassLoader().getResource("dtds/XMLSchema.dtd"); 
    assertNotNull(xmlschemadtd);
    catalog1.registerSystemID(xmlschemadtd);
    
    assertTrue  (catalog1.test(xmlschemadtd.toExternalForm()));
    assertFalse (catalog1.test(httpxsd.toUri().toURL().toExternalForm()));

  }

} /* ENDCLASS */
