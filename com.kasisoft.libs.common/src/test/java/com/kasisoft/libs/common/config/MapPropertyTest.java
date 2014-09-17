package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.Test;

import org.testng.*;

import java.util.*;

import java.awt.*;

/**
 * Tests for the class 'MapProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MapPropertyTest {

  private static final MapProperty<Color> ColorsRequired = new MapProperty<Color>( "color.required", new ColorAdapter(), true  ); 
  private static final MapProperty<Color> ColorsOptional = new MapProperty<Color>( "color.optional", new ColorAdapter(), false );
  
  @Test(groups="all")
  public void roundtripPropertiesForRequired() {
    
    Properties properties = new Properties();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    Map<String,Color> read = ColorsRequired.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( ColorsRequired.getKey(), ex.getProperty() );
    }
    
    // get the color while providing a default value
    read = ColorsRequired.getValue( properties, value );
    Assert.assertEquals( read, value );
    
  }

  @Test(groups="all")
  public void roundtripMapForRequired() {
    
    Map<String,String> properties = new Hashtable<String,String>();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    Map<String,Color> read = ColorsRequired.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( ColorsRequired.getKey(), ex.getProperty() );
    }
    
    // get the color while providing a default value
    read = ColorsRequired.getValue( properties, value );
    Assert.assertEquals( read, value );
    
  }
  
  @Test(groups="all")
  public void roundtripPropertiesOptional() {
    
    Properties properties = new Properties();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    Map<String,Color> read = ColorsOptional.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    Assert.assertTrue( value.isEmpty() );
    
    // get the color while providing a default value
    read = ColorsOptional.getValue( properties, value );
    Assert.assertEquals( read, value );
    
  }

  @Test(groups="all")
  public void roundtripMapOptional() {
    
    Map<String,String> properties = new Hashtable<String,String>();

    Map<String,Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    Map<String,Color> read = ColorsOptional.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    Assert.assertTrue( value.isEmpty() );
    
    // get the color while providing a default value
    read = ColorsOptional.getValue( properties, value );
    Assert.assertEquals( read, value );
    
  }

  private Map<String,Color> createValues() {
    Map<String,Color> result = new Hashtable<String,Color>();
    result.put( "green"  , Color.green  );
    result.put( "blue"   , Color.blue   );
    result.put( "yellow" , Color.yellow );
    result.put( "cyan"   , Color.cyan   );
    return result;
  }
  
} /* ENDCLASS */
