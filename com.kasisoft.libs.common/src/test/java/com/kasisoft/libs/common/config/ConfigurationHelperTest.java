package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the type 'ConfigurationHelper'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ConfigurationHelperTest {
  
  private SimpleProperty<String>   property1;
  private SimpleProperty<String>   property2;
  private Map<String,String>       properties;
  
  @BeforeClass
  private void setup() {
    
    property1 = new SimpleProperty<>( "simple.property1", new StringAdapter(), false );
    property2 = new SimpleProperty<>( "simple.property2", new StringAdapter(), false );
    
    properties   = new Hashtable<>();
    property2.setValue( properties, "gollum" );
    properties.put( "unknown.property", "unknown value" );
    
  }
  
  @Test(groups="all")
  public void createReplacementMap() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>", property1, property2 );
    Assert.assertNotNull( replacements );
    Assert.assertEquals( replacements.size(), 2 );
    
    String key1 = String.format( "${%s}", property1.getKey() );
    String key2 = String.format( "${%s}", property2.getKey() );
    
    Assert.assertTrue( replacements.containsKey( key1 ) );
    Assert.assertTrue( replacements.containsKey( key2 ) );
    
    Assert.assertEquals( replacements.get( key1 ), "<null>" );
    Assert.assertEquals( replacements.get( key2 ), "gollum" );
    
  }
  
} /* ENDCLASS */
