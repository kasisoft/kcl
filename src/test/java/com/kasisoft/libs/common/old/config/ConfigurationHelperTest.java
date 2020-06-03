package com.kasisoft.libs.common.old.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.converters.StringAdapter;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.regex.Pattern;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'ConfigurationHelper'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationHelperTest {
  
  SimpleProperty<String>   property1;
  SimpleProperty<String>   property2;
  Map<String, String>      map;
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
    assertThat( replacements, is( notNullValue() ) );
    assertThat( replacements.size(), is(2) );
    
    String key1 = String.format( "${%s}", property1.getKey() );
    String key2 = String.format( "${%s}", property2.getKey() );
    
    assertTrue( replacements.containsKey( key1 ) );
    assertTrue( replacements.containsKey( key2 ) );
    
    assertThat( replacements.get( key1 ), is( "<null>" ) );
    assertThat( replacements.get( key2 ), is( "gollum" ) );
    
  }

  @Test(groups="all")
  public void createReplacementMapForProperties() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>", property1, property2 );
    assertThat( replacements, is( notNullValue() ) );
    assertThat( replacements.size(), is(2) );
    
    String key1 = String.format( "${%s}", property1.getKey() );
    String key2 = String.format( "${%s}", property2.getKey() );
    
    assertTrue( replacements.containsKey( key1 ) );
    assertTrue( replacements.containsKey( key2 ) );
    
    assertThat( replacements.get( key1 ), is( "<null>" ) );
    assertThat( replacements.get( key2 ), is( "gollum" ) );
    
  }

  @Test(groups="all")
  public void createReplacementMapForAllInMap() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( map, "${%s}", "<null>" );
    assertThat( replacements, is( notNullValue() ) );
    assertThat( replacements.size(), is(2) );
    
    String key2 = String.format( "${%s}", property2.getKey() );
    String key3 = "${unknown.property}";
    
    assertTrue( replacements.containsKey( key2 ) );
    assertTrue( replacements.containsKey( key3 ) );
    
    assertThat( replacements.get( key2 ), is( "gollum" ) );
    assertThat( replacements.get( key3 ), is( "unknown value" ) );
    
  }

  @Test(groups="all")
  public void createReplacementMapForAllInProperties() {
    
    Map<String,String> replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>" );
    assertThat( replacements, is( notNullValue() ) );
    assertThat( replacements.size(), is(2) );
    
    String key2 = String.format( "${%s}", property2.getKey() );
    String key3 = "${unknown.property}";
    
    assertTrue( replacements.containsKey( key2 ) );
    assertTrue( replacements.containsKey( key3 ) );
    
    assertThat( replacements.get( key2 ), is( "gollum" ) );
    assertThat( replacements.get( key3 ), is( "unknown value" ) );
    
  }

  @Test(groups="all")
  public void quoteKeys() {
    
    Map<String,String>  replacements = ConfigurationHelper.createReplacementMap( properties, "${%s}", "<null>" );
    Map<Pattern,String> patterns     = ConfigurationHelper.quoteKeys( replacements );
    
    assertThat( patterns, is( notNullValue() ) );
    assertThat( patterns.size(), is(2) );
    
    String key2 = String.format( "\\Q${%s}\\E", property2.getKey() );
    String key3 = "\\Q${unknown.property}\\E";

    Set<String> patternstr = new HashSet<>();
    for( Pattern pattern : patterns.keySet() ) {
      patternstr.add( pattern.pattern() );
    }
    
    assertTrue( patternstr.contains( key2 ) );
    assertTrue( patternstr.contains( key3 ) );
    
  }

} /* ENDCLASS */
