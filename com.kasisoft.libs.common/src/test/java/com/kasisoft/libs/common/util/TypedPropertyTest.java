/**
 * Name........: TypedPropertyTest
 * Description.: Tests for the class 'TypedProperty'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.xml.adapters.*;

import org.testng.annotations.*;
import org.testng.annotations.Test;

import java.util.*;

import java.awt.*;

import junit.framework.*;

/**
 * Tests for the class 'TypedProperty'.
 */
@Test(groups="all")
public class TypedPropertyTest {

  private static final TypedProperty<Color> MyRequiredColor = new TypedProperty<Color>( "required.color", new ColorAdapter(), true  ); 
  private static final TypedProperty<Color> MyOptionalColor = new TypedProperty<Color>( "optional.color", new ColorAdapter(), false );
  
  @DataProvider(name="roundtripData")
  public Object[][] roundtripData() {
    return new Object[][] {
      { Color.green   },
      { Color.blue    },
      { Color.yellow  },
      { Color.cyan    },
    };
  }
  
  @Test(dataProvider="roundtripData")
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

  @Test(dataProvider="roundtripData")
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

  @Test(dataProvider="roundtripData")
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

  @Test(dataProvider="roundtripData")
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
