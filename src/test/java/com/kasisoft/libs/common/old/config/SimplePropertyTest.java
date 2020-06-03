package com.kasisoft.libs.common.old.config;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import com.kasisoft.libs.common.converters.ColorAdapter;
import com.kasisoft.libs.common.converters.StringAdapter;
import com.kasisoft.libs.common.old.constants.SysProperty;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import org.testng.Assert;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import java.awt.Color;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'SimpleProperty'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimplePropertyTest {

  static final SimpleProperty<Color> MyRequiredColor = new SimpleProperty<>( "required.color", new ColorAdapter(), true  ); 
  static final SimpleProperty<Color> MyOptionalColor = new SimpleProperty<>( "optional.color", new ColorAdapter(), false );
  
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
    assertThat( color, is( value ) );
    
    // remove the color and wait for the complaint
    MyRequiredColor.setValue( properties, null );
    try {
      value = MyRequiredColor.getValue( properties );
    } catch( MissingPropertyException ex ) {
      Assert.assertEquals( MyRequiredColor.getKey(), ex.getProperty() );
    }
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripMapForRequired( Color color ) {
    
    Map<String,String> properties = new Hashtable<>();

    // set and get the color
    MyRequiredColor.setValue( properties, color );
    Color value = MyRequiredColor.getValue( properties );
    assertThat( color, is( value ) );
    
    // remove the color and wait for the complaint
    MyRequiredColor.setValue( properties, null );
    try {
      value = MyRequiredColor.getValue( properties );
    } catch( MissingPropertyException ex ) {
      assertThat( MyRequiredColor.getKey(), is( ex.getProperty() ) );
    }
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripPropertiesForOptional( Color color ) {
    
    Properties properties = new Properties();

    // set and get the color
    MyOptionalColor.setValue( properties, color );
    Color value = MyOptionalColor.getValue( properties );
    assertThat( color, is( value ) );
    
    // remove the color and accept the null result
    MyOptionalColor.setValue( properties, null );
    value = MyOptionalColor.getValue( properties );
    assertThat( null, is( value ) );
    
  }

  @Test(dataProvider="roundtripData",groups="all")
  public void roundtripMapForOptional( Color color ) {
    
    Map<String,String> properties = new Hashtable<>();

    // set and get the color
    MyOptionalColor.setValue( properties, color );
    Color value = MyOptionalColor.getValue( properties );
    assertThat( color, is( value ) );
    
    // remove the color and accept the null result
    MyOptionalColor.setValue( properties, null );
    value = MyOptionalColor.getValue( properties );
    assertThat( null, is( value ) );
    
  }
  
  @Test(groups="all")
  public void withResolving() {
    
    PropertiesConfig        config   = new PropertiesConfig( "${%s}", true );
    SimpleProperty<String>  property = new SimpleProperty<>( "sample", new StringAdapter() ).withConfig( config );
    
    Map<String,String> properties = new Hashtable<>();
    properties.put( "sample", "_${sys:java.io.tmpdir}_" );

    String read    = property.getValue( properties );
    assertThat( read, is( notNullValue() ) );
    
    String tempdir = SysProperty.TempDir.getTextualValue( System.getProperties() );
    assertThat( read, is( String.format( "_%s_", tempdir ) ) );
    
  }
  
} /* ENDCLASS */
