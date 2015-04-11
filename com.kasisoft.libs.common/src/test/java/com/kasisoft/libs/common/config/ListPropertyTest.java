package com.kasisoft.libs.common.config;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.Test;

import java.util.*;
import java.util.List;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the class 'ListProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListPropertyTest {

  static final ListProperty<Color> ColorsRequired        = new ListProperty<>( "color.required", new ColorAdapter(), true  ); 
  static final ListProperty<Color> ColorsOptional        = new ListProperty<>( "color.optional", new ColorAdapter(), false );
  static final ListProperty<Color> ColorsOptionalNotNull = new ListProperty<>( "color.notNull.optional", new ColorAdapter(), false )
                                                                .withDefault( Collections.<Color>emptyList() );
  
  ColorAdapter coloradapter = new ColorAdapter();
  
  @Test(groups="all")
  public void roundtripPropertiesForRequired() {
    
    Properties properties = new Properties();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    List<Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      assertThat( ex.getProperty(), is( ColorsRequired.getKey() ) );
    }
    
  }

  @Test(groups="all")
  public void roundtripMapForRequired() {
    
    Map<String,String> properties = new Hashtable<>();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    List<Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      assertThat( ex.getProperty(), is( ColorsRequired.getKey() ) );
    }
    
  }
  
  @Test(groups="all")
  public void roundtripPropertiesOptional() {
    
    Properties properties = new Properties();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    List<Color> read = ColorsOptional.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    assertNull( value );
    
  }

  @Test(groups="all")
  public void roundtripPropertiesOptionalNotNull() {
    
    Properties properties = new Properties();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptionalNotNull.setValue( properties, value );
    List<Color> read = ColorsOptionalNotNull.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptionalNotNull.setValue( properties, null );
    value = ColorsOptionalNotNull.getValue( properties );
    assertNotNull( value );
    assertTrue( value.isEmpty() );
    
  }

  @Test(groups="all")
  public void roundtripMapOptional() {
    
    Map<String,String> properties = new Hashtable<>();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    List<Color> read = ColorsOptional.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    assertNull( value );
    
  }

  @Test(groups="all")
  public void roundtripMapOptionalNotNull() {
    
    Map<String,String> properties = new Hashtable<>();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptionalNotNull.setValue( properties, value );
    List<Color> read = ColorsOptionalNotNull.getValue( properties );
    assertThat( read, is( value ) );
    
    // remove the color and wait for the complaint
    ColorsOptionalNotNull.setValue( properties, null );
    value = ColorsOptionalNotNull.getValue( properties );
    assertNotNull( value );
    assertTrue( value.isEmpty() );
    
  }

  @Test(groups="all")
  public void roundtripMapForRequiredWithEmptyValue() {
   
    Map<String,String> properties = new Hashtable<>();

    List<Color> value = createValues();
    for( int i = 0; i < value.size(); i++ ) {
      properties.put( String.format( "%s[%d]", ColorsRequired.getKey(), Integer.valueOf(i) ), coloradapter.marshal( value.get(i) ) );
    }
    properties.put( String.format( "%s[%d]", ColorsRequired.getKey(), Integer.valueOf( value.size() ) ), "" );
    
    List<Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
  }

  @Test(groups="all")
  public void roundtripPropertiesForRequiredWithEmptyValue() {
   
    Properties properties = new Properties();

    List<Color> value = createValues();
    for( int i = 0; i < value.size(); i++ ) {
      properties.setProperty( String.format( "%s[%d]", ColorsRequired.getKey(), Integer.valueOf(i) ), coloradapter.marshal( value.get(i) ) );
    }
    properties.setProperty( String.format( "%s[%d]", ColorsRequired.getKey(), Integer.valueOf( value.size() ) ), "" );
    
    List<Color> read = ColorsRequired.getValue( properties );
    assertThat( read, is( value ) );
    
  }

  private List<Color> createValues() {
    List<Color> result = new ArrayList<>();
    result.add( Color.green  );
    result.add( Color.blue   );
    result.add( Color.yellow );
    result.add( Color.cyan   );
    return result;
  }
  
} /* ENDCLASS */
