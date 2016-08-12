package com.kasisoft.libs.common.config;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * Test for the class 'PropertyResolver'
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyResolverTest {

  PropertyResolver   resolver;
  
  @BeforeClass
  private void setup() {
    resolver = new PropertyResolver();
    resolver.withSystemSubstitutions();
    resolver.withSubstitution( "mykey", "problematic_replacement\\" );
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
    assertThat( resolver.getProperty( "property" ), is( resolvedval ) );
    
  }

  @Test(groups="all")
  public void resolveFromMap() {
    
    Map<String,String>  properties   = new HashMap<>();
    
    Properties          sysprops     = System.getProperties();
    
    String     firstname    = (String) sysprops.propertyNames().nextElement();
    String     originalval  = String.format( "BEGIN-${%s}-END", firstname );
    properties.put( "property", originalval );
    
    resolver.load( properties );
    
    String     resolvedval  = String.format( "BEGIN-%s-END", System.getProperty( firstname ) );
    assertThat( resolver.getProperty( "property" ), is( resolvedval ) );
    
  }

  @Test(groups="all")
  public void resolveFromClasspathResource() throws Exception {
    
    resolver.load( "propertyresolver.properties" );
    
    String encoding    = SysProperty.FileEncoding.getValue( System.getProperties() );
    String resolvedval = String.format( "ENCODING: %s", encoding );
    
    assertThat( resolver.getProperty( "my_file_encoding" ), is( resolvedval ) );
    
    assertNull( resolver.getProperty( "unavailable_value" ) );
    
    assertThat( resolver.getProperty( "myvariable" ), is( "-problematic_replacement\\-" ) );
    
  }

} /* ENDCLASS */
