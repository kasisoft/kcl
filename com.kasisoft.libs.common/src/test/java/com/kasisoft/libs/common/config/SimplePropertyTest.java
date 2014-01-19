/**
 * Name........: SimplePropertyTest
 * Description.: Tests for the class 'SimpleProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.*;

import java.awt.*;

import junit.framework.*;

/**
 * Tests for the class 'SimpleProperty'.
 */
public class SimplePropertyTest {

  private static final SimpleProperty<Color> MyRequiredColor = new SimpleProperty<Color>( "required.color", new ColorAdapter(), true  ); 
  private static final SimpleProperty<Color> MyOptionalColor = new SimpleProperty<Color>( "optional.color", new ColorAdapter(), false );
  
  @DataProvider(name="roundtripData")
  public Object[][] roundtripData() {
    return new Object[][] {
      { Color.green   },
      { Color.blue    },
      { Color.yellow  },
      { Color.cyan    },
    };
  }
  
  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripPropertiesForRequired( Color color ) {
    
    Properties properties = new Properties();

    // set and get the color
    MyRequiredColor.setValue( properties, color );
    Color value = MyRequiredColor.getValue( properties );
    Assert.assertEquals( color, value );
    
    // remove the color and wait for the complaint
    MyRequiredColor.setValue( properties, null );
    try {
      value = MyRequiredColor.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( MyRequiredColor.getKey(), ex.getProperty() );
    }
    
    // get the color while providing a default value
    value = MyRequiredColor.getValue( properties, Color.white );
    Assert.assertEquals( Color.white, value );
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripMapForRequired( Color color ) {
    
    Map<String,String> properties = new Hashtable<String,String>();

    // set and get the color
    MyRequiredColor.setValue( properties, color );
    Color value = MyRequiredColor.getValue( properties );
    Assert.assertEquals( color, value );
    
    // remove the color and wait for the complaint
    MyRequiredColor.setValue( properties, null );
    try {
      value = MyRequiredColor.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( MyRequiredColor.getKey(), ex.getProperty() );
    }
    
    // get the color while providing a default value
    value = MyRequiredColor.getValue( properties, Color.white );
    Assert.assertEquals( Color.white, value );
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripPropertiesForOptional( Color color ) {
    
    Properties properties = new Properties();

    // set and get the color
    MyOptionalColor.setValue( properties, color );
    Color value = MyOptionalColor.getValue( properties );
    Assert.assertEquals( color, value );
    
    // remove the color and accept the null result
    MyOptionalColor.setValue( properties, null );
    value = MyOptionalColor.getValue( properties );
    Assert.assertEquals( null, value );
    
    // get the color while providing a default value
    value = MyOptionalColor.getValue( properties, Color.white );
    Assert.assertEquals( Color.white, value );
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripMapForOptional( Color color ) {
    
    Map<String,String> properties = new Hashtable<String,String>();

    // set and get the color
    MyOptionalColor.setValue( properties, color );
    Color value = MyOptionalColor.getValue( properties );
    Assert.assertEquals( color, value );
    
    // remove the color and accept the null result
    MyOptionalColor.setValue( properties, null );
    value = MyOptionalColor.getValue( properties );
    Assert.assertEquals( null, value );
    
    // get the color while providing a default value
    value = MyOptionalColor.getValue( properties, Color.white );
    Assert.assertEquals( Color.white, value );
    
  }
  
} /* ENDCLASS */
