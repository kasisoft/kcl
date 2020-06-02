package com.kasisoft.libs.common.old.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.constants.SysProperty;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

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
    
    resolver.load( "old/propertyresolver.properties" );
    
    String encoding    = SysProperty.FileEncoding.getValue( System.getProperties() );
    String resolvedval = String.format( "ENCODING: %s", encoding );
    
    assertThat( resolver.getProperty( "my_file_encoding" ), is( resolvedval ) );
    
    assertNull( resolver.getProperty( "unavailable_value" ) );
    
    assertThat( resolver.getProperty( "myvariable" ), is( "-problematic_replacement\\-" ) );
    
  }
  
  @Test(groups="all", dependsOnMethods="resolveFromClasspathResource")
  public void toMap() throws Exception {
    
    String encoding    = SysProperty.FileEncoding.getValue( System.getProperties() );
    String resolvedval = String.format( "ENCODING: %s", encoding );
    
    Map<String, String> asMap = resolver.toMap();
    assertThat( asMap.size(), is(4) );
    assertTrue( asMap.containsKey( "my_file_encoding" ) );
    assertThat( asMap.get( "my_file_encoding" ), is( resolvedval ) );
    
  }

  @Test(groups="all", dependsOnMethods="resolveFromClasspathResource")
  public void toProperties() throws Exception {
    
    String encoding    = SysProperty.FileEncoding.getValue( System.getProperties() );
    String resolvedval = String.format( "ENCODING: %s", encoding );
     
    Properties asProperties = resolver.toProperties();
    assertThat( asProperties.size(), is(3) );
    assertTrue( asProperties.containsKey( "my_file_encoding" ) );
    assertThat( asProperties.get( "my_file_encoding" ), is( resolvedval ) );
    
  }

} /* ENDCLASS */