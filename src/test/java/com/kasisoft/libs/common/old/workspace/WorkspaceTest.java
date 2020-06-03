package com.kasisoft.libs.common.old.workspace;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNull;

import com.kasisoft.libs.common.converters.BooleanAdapter;
import com.kasisoft.libs.common.converters.ByteAdapter;
import com.kasisoft.libs.common.converters.ColorAdapter;
import com.kasisoft.libs.common.converters.DoubleAdapter;
import com.kasisoft.libs.common.converters.FileAdapter;
import com.kasisoft.libs.common.converters.FloatAdapter;
import com.kasisoft.libs.common.converters.InsetsAdapter;
import com.kasisoft.libs.common.converters.PointAdapter;
import com.kasisoft.libs.common.converters.RectangleAdapter;
import com.kasisoft.libs.common.converters.ShortAdapter;
import com.kasisoft.libs.common.converters.StringAdapter;
import com.kasisoft.libs.common.converters.TypeAdapter;
import com.kasisoft.libs.common.converters.URIAdapter;
import com.kasisoft.libs.common.converters.URLAdapter;
import com.kasisoft.libs.common.converters.VersionAdapter;
import com.kasisoft.libs.common.old.config.SimpleProperty;
import com.kasisoft.libs.common.types.Version;

import org.testng.annotations.Test;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import java.io.File;

/**
 * Tests for the class 'Workspace'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WorkspaceTest {

  private <T> void valueTest( T object, T defval, String key, TypeAdapter<String,T> adapter ) throws Exception {
    
    Workspace ws   = Workspace.getInstance();
    String    key1 = key + "_";
    String    key2 = key;
    
    SimpleProperty<T> property1 = new SimpleProperty<>( key1, adapter );
    SimpleProperty<T> property2 = new SimpleProperty<>( key1, adapter ).withDefault( defval );
    SimpleProperty<T> property3 = new SimpleProperty<>( key2, adapter );
    SimpleProperty<T> property4 = new SimpleProperty<>( key2, adapter ).withDefault( defval );
    
    property3.setValue( ws.getProperties(), object );
    
    assertNull( property1.getValue( ws.getProperties() ) );
    assertThat( property2.getValue( ws.getProperties() ), is( defval ) );
    assertThat( property3.getValue( ws.getProperties() ), is( object ) );
    assertThat( property4.getValue( ws.getProperties() ), is( object ) );
    
  }
  
  @Test(groups = "all")
  public void valVersion() throws Exception {
    valueTest(
      new Version( 2, 3, 1 ),
      new Version( 4, 1, 1 ),
      "version",
      new VersionAdapter( true, false )
    );
  }
  
  @Test(groups = "all")
  public void valURL() throws Exception {
    valueTest(
      new File( "/sample" ).toURI().toURL(),
      new File( "/dodo"   ).toURI().toURL(),
      "url",
      new URLAdapter()
    );
  }

  @Test(groups = "all")
  public void valURI() throws Exception {
    valueTest(
      new File( "/sample" ).toURI(),
      new File( "/dodo"   ).toURI(),
      "uri",
      new URIAdapter()
    );
  }

  @Test(groups = "all")
  public void valFile() throws Exception {
    valueTest(
      new File( "/sample" ),
      new File( "/dodo"   ),
      "file",
      new FileAdapter()
    );
  }
  
  @Test(groups = "all")
  public void valPoint() throws Exception {
    valueTest(
      new Point( 10, 10 ),
      new Point( 27, 35 ),
      "point",
      new PointAdapter()
    );
  }

  @Test(groups = "all")
  public void valInsets() throws Exception {
    valueTest(
      new Insets( 10, 10, 20, 20 ),
      new Insets( 27, 27, 35, 35 ),
      "insets",
      new InsetsAdapter()
    );
  }

  @Test(groups = "all")
  public void valString() throws Exception {
    valueTest(
      "alpha",
      "beta",
      "string",
      new StringAdapter()
    );
  }

  @Test(groups = "all")
  public void valShort() throws Exception {
    valueTest(
      Short.valueOf((short) 1),
      Short.valueOf((short) -12),
      "short",
      new ShortAdapter()
    );
  }

  @Test(groups = "all")
  public void valFloat() throws Exception {
    valueTest(
      Float.valueOf((float) 1.0),
      Float.valueOf((float) 17.3),
      "float",
      new FloatAdapter()
    );
  }

  @Test(groups = "all")
  public void valDouble() throws Exception {
    valueTest(
      Double.valueOf(1.0),
      Double.valueOf(17.3),
      "double",
      new DoubleAdapter()
    );
  }

  @Test(groups = "all")
  public void valColor() throws Exception {
    valueTest(
      Color.red,
      Color.green,
      "color",
      new ColorAdapter()
    );
  }

  @Test(groups = "all")
  public void valByte() throws Exception {
    valueTest(
      Byte.valueOf((byte) 1),
      Byte.valueOf((byte) 12),
      "byte",
      new ByteAdapter()
    );
  }

  @Test(groups = "all")
  public void valBoolean() throws Exception {
    valueTest(
      Boolean.FALSE,
      Boolean.TRUE,
      "boolean",
      new BooleanAdapter()
    );
  }

  @Test(groups = "all")
  public void valRectangle() throws Exception {
    valueTest(
      new Rectangle( 40, 40, 300, 120 ),
      new Rectangle( 30, 20, 100, 520 ),
      "rectangle",
      new RectangleAdapter()
    );
  }
  
} /* ENDCLASS */
