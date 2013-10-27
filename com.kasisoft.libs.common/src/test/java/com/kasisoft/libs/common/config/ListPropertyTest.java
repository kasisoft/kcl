/**
 * Name........: ListPropertyTest
 * Description.: Tests for the class 'ListProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.Test;

import java.util.*;
import java.util.List;

import java.awt.*;

import junit.framework.*;

/**
 * Tests for the class 'ListProperty'.
 */
@SuppressWarnings("deprecation")
public class ListPropertyTest {

  private static final ListProperty<Color> ColorsRequired = new ListProperty<Color>( "color.required", new ColorAdapter(), true  ); 
  private static final ListProperty<Color> ColorsOptional = new ListProperty<Color>( "color.optional", new ColorAdapter(), false );
  
  @Test(groups="all")
  public void roundtripPropertiesForRequired() {
    
    Properties properties = new Properties();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    List<Color> read = ColorsRequired.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( ColorsRequired.getKey(), ex.getProperty() );
    }
    
  }

  @Test(groups="all")
  public void roundtripMapForRequired() {
    
    Map<String,String> properties = new Hashtable<String,String>();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsRequired.setValue( properties, value );
    List<Color> read = ColorsRequired.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsRequired.setValue( properties, null );
    try {
      value = ColorsRequired.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( ColorsRequired.getKey(), ex.getProperty() );
    }
    
  }
  
  @Test(groups="all")
  public void roundtripPropertiesOptional() {
    
    Properties properties = new Properties();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    List<Color> read = ColorsOptional.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    Assert.assertTrue( value.isEmpty() );
    
  }

  @Test(groups="all")
  public void roundtripMapOptional() {
    
    Map<String,String> properties = new Hashtable<String,String>();

    List<Color> value = createValues();
    
    // set and get the color
    ColorsOptional.setValue( properties, value );
    List<Color> read = ColorsOptional.getValue( properties );
    Assert.assertEquals( read, value );
    
    // remove the color and wait for the complaint
    ColorsOptional.setValue( properties, null );
    value = ColorsOptional.getValue( properties );
    Assert.assertTrue( value.isEmpty() );
    
  }

  private List<Color> createValues() {
    List<Color> result = new ArrayList<Color>();
    result.add( Color.green  );
    result.add( Color.blue   );
    result.add( Color.yellow );
    result.add( Color.cyan   );
    return result;
  }
  
} /* ENDCLASS */
