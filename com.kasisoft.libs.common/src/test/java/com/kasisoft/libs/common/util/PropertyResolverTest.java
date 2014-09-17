package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Test for the class 'PropertyResolver'
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PropertyResolverTest {

  private PropertyResolver   resolver;
  
  @BeforeClass
  private void setup() {
    resolver = new PropertyResolver();
    resolver.withSystemSubstitutions();
  }

  @Test(groups="all")
  public void resolveFromProperties() {
    
    Properties properties   = new Properties();
    
    Properties sysprops     = System.getProperties();
    
    String     firstname    = (String) sysprops.propertyNames().nextElement();
    String     originalval  = String.format( "BEGIN-${%s}-END", firstname );
    properties.setProperty( "property", originalval );
    
    resolver.load( properties );
    
    String     resolvedval  = String.format( "BEGIN-%s-END", System.getProperty( firstname ) );
    Assert.assertEquals( resolver.getProperty( "property" ), resolvedval );
    
  }

  @Test(groups="all")
  public void resolveFromClasspathResource() throws Exception {
    
    resolver.load( "propertyresolver.properties" );
    
    String encoding    = SysProperty.FileEncoding.getValue( System.getProperties() );
    String resolvedval = String.format( "ENCODING: %s", encoding );
    
    Assert.assertEquals( resolver.getProperty( "my_file_encoding" ), resolvedval );
    
  }

} /* ENDCLASS */
