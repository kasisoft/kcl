package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.regex.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'ConfigurationHelper'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationHelperTest {
  
  SimpleProperty<String>   property1;
  SimpleProperty<String>   property2;
  Map<String,String>       map;
  Properties               properties;
  
  @BeforeClass
  private void setup() {
    
    property1 = new SimpleProperty<>( "simple.property1", new StringAdapter(), false );
    property2 = new SimpleProperty<>( "simple.property2", new StringAdapter(), false );
    
    map   = new Hashtable<>();
    property2.setValue( map, "gollum" );
    map.put( "unknown.property", "unknown value" );

    properties = new Properties();
    property2.setValue( properties, "gollum" );
    properties.setProperty( "unknown.property", "unknown value" );
    
  }
  
  @Test(groups="all")
  public void createReplacementMapForMap() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( map, "${%s}", "<null>", property1, property2 );
    Assert.assertNotNull( replacements );
    Assert.assertEquals( replacements.size(), 2 );
    
    String key1 = String.format( "${%s}", property1.getKey() );
    String key2 = String.format( "${%s}", property2.getKey() );
    
    Assert.assertTrue( replacements.containsKey( key1 ) );
    Assert.assertTrue( replacements.containsKey( key2 ) );
    
    Assert.assertEquals( replacements.get( key1 ), "<null>" );
    Assert.assertEquals( replacements.get( key2 ), "gollum" );
    
  }

  @Test(groups="all")
  public void createReplacementMapForProperties() {
    
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

  @Test(groups="all")
  public void createReplacementMapForAllInMap() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( map, "${%s}", "<null>" );
    Assert.assertNotNull( replacements );
    Assert.assertEquals( replacements.size(), 2 );
    
    String key2 = String.format( "${%s}", property2.getKey() );
    String key3 = "${unknown.property}";
    
    Assert.assertTrue( replacements.containsKey( key2 ) );
    Assert.assertTrue( replacements.containsKey( key3 ) );
    
    Assert.assertEquals( replacements.get( key2 ), "gollum" );
    Assert.assertEquals( replacements.get( key3 ), "unknown value" );
    
  }

  @Test(groups="all")
  public void createReplacementMapForAllInProperties() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>" );
    Assert.assertNotNull( replacements );
    Assert.assertEquals( replacements.size(), 2 );
    
    String key2 = String.format( "${%s}", property2.getKey() );
    String key3 = "${unknown.property}";
    
    Assert.assertTrue( replacements.containsKey( key2 ) );
    Assert.assertTrue( replacements.containsKey( key3 ) );
    
    Assert.assertEquals( replacements.get( key2 ), "gollum" );
    Assert.assertEquals( replacements.get( key3 ), "unknown value" );
    
  }

  @Test(groups="all")
  public void quoteKeys() {
    
    Map<String,String>  replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>" );
    Map<Pattern,String> patterns     = ConfigurationHelper.quoteKeys( replacements );
    
    Assert.assertNotNull( patterns );
    Assert.assertEquals( patterns.size(), 2 );
    
    String key2 = String.format( "\\Q${%s}\\E", property2.getKey() );
    String key3 = "\\Q${unknown.property}\\E";

    Set<String> patternstr = new HashSet<>();
    for( Pattern pattern : patterns.keySet() ) {
      patternstr.add( pattern.pattern() );
    }
    
    Assert.assertTrue( patternstr.contains( key2 ) );
    Assert.assertTrue( patternstr.contains( key3 ) );
    
  }

} /* ENDCLASS */
