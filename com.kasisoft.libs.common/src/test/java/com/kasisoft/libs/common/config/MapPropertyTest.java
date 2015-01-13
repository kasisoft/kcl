package com.kasisoft.libs.common.config;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import java.util.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the class 'MapProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapPropertyTest {

  static final MapProperty<Color> ColorsRequired = new MapProperty<>( "color.required", new ColorAdapter(), true  ); 
  static final MapProperty<Color> ColorsOptional = new MapProperty<>( "color.optional", new ColorAdapter(), false );
  
  ColorAdapter coloradapter = new ColorAdapter();
  
  @Test(groups="all")
  public void roundtripPropertiesForRequired() {
    
    Properties properties = new Properties();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    Map<String,Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      assertThat( ColorsRequired.getKey(), is( ex.getProperty() ) );
    }
    
    // get the color while providing a default value
    read = ColorsRequired.getValue( properties, value );
    assertThat( read, is( value ) );
    
  }

  @Test(groups="all")
  public void roundtripMapForRequired() {
    
    Map<String,String> properties = new Hashtable<>();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    Map<String,Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      assertThat( ColorsRequired.getKey(), is( ex.getProperty() ) );
    }
    
    // get the color while providing a default value
    read = ColorsRequired.getValue( properties, value );
    assertThat( read, is( value ) );
    
  }
  
  @Test(groups="all")
  public void roundtripPropertiesOptional() {
    
    Properties properties = new Properties();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    Map<String,Color> read = ColorsOptional.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    assertTrue( value.isEmpty() );
    
    // get the color while providing a default value
    read = ColorsOptional.getValue( properties, value );
    assertThat( read, is( value ) );
    
  }

  @Test(groups="all")
  public void roundtripMapOptional() {
    
    Map<String,String> properties = new Hashtable<>();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    Map<String,Color> read = ColorsOptional.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    assertTrue( value.isEmpty() );
    
    // get the color while providing a default value
    read = ColorsOptional.getValue( properties, value );
    assertThat( read, is( value ) );
    
  }

  
  
  @Test(groups="all")
  public void roundtripMapForRequiredWithEmptyValue() {
   
    Map<String,String> properties = new Hashtable<>();

    Map<String,Color> value = createValues();
    for( Map.Entry<String,Color> entry : value.entrySet() ) {
      properties.put( String.format( "%s[%s]", ColorsRequired.getKey(), entry.getKey() ), coloradapter.marshal( entry.getValue() ) );
    }
    properties.put( String.format( "%s[%s]", ColorsRequired.getKey(), "unknown" ), "" );
    
    Map<String,Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
  }

  @Test(groups="all")
  public void roundtripPropertiesForRequiredWithEmptyValue() {
   
    Properties properties = new Properties();

    Map<String,Color> value = createValues();
    for( Map.Entry<String,Color> entry : value.entrySet() ) {
      properties.setProperty( String.format( "%s[%s]", ColorsRequired.getKey(),  entry.getKey() ), coloradapter.marshal( entry.getValue() ) );
    }
    properties.setProperty( String.format( "%s[%s]", ColorsRequired.getKey(), "unknown" ), "" );
    
    Map<String,Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
  }
  
  
  private Map<String,Color> createValues() {
    Map<String,Color> result = new Hashtable<>();
    result.put( "green"  , Color.green  );
    result.put( "blue"   , Color.blue   );
    result.put( "yellow" , Color.yellow );
    result.put( "cyan"   , Color.cyan   );
    return result;
  }
  
} /* ENDCLASS */
