package com.kasisoft.libs.common.workspace;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import java.net.*;

import java.awt.*;

import java.io.*;

/**
 * Tests for the class 'Workspace'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WorkspaceTest {

  @Test(groups = "all")
  public void valVersion() throws Exception {
    
    Workspace ws     = Workspace.getInstance();
    
    Version   object = new Version( 2, 3, 1 );
    Version   defval = new Version( 4, 1, 1 );
    ws.setVersion( "version", object );
    
    assertNull( ws.getVersion( "version_" ) );
    assertNull( ws.getVersion( "version_", null ) );
    assertThat( ws.getVersion( "version_", defval ), is( defval ) );
    
    assertThat( ws.getVersion( "version", null   ), is( object ) );
    assertThat( ws.getVersion( "version", defval ), is( object ) );
    
  }
  
  @Test(groups = "all")
  public void valURL() throws Exception {
    
    Workspace ws     = Workspace.getInstance();
    
    URL       object = new File( "/sample" ).toURI().toURL();
    URL       defval = new File( "/dodo"   ).toURI().toURL();
    ws.setURL( "url", object );
    
    assertNull( ws.getURL( "url_" ) );
    assertNull( ws.getURL( "url_", null ) );
    assertThat( ws.getURL( "url_", defval ), is( defval ) );
    
    assertThat( ws.getURL( "url", null   ), is( object ) );
    assertThat( ws.getURL( "url", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valURI() {
    
    Workspace ws     = Workspace.getInstance();
    
    URI       object = new File( "/sample" ).toURI();
    URI       defval = new File( "/dodo"   ).toURI();
    ws.setURI( "uri", object );
    
    assertNull( ws.getURI( "uri_" ) );
    assertNull( ws.getURI( "uri_", null ) );
    assertThat( ws.getURI( "uri_", defval ), is( defval ) );
    
    assertThat( ws.getURI( "uri", null   ), is( object ) );
    assertThat( ws.getURI( "uri", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valFile() {
    
    Workspace ws     = Workspace.getInstance();
    
    File      object = new File( "/sample" );
    File      defval = new File( "/dodo"   );
    ws.setFile( "file", object );
    
    assertNull( ws.getFile( "file_" ) );
    assertNull( ws.getFile( "file_", null ) );
    assertThat( ws.getFile( "file_", defval ), is( defval ) );
    
    assertThat( ws.getFile( "file", null   ), is( object ) );
    assertThat( ws.getFile( "file", defval ), is( object ) );
    
  }
  
  @Test(groups = "all")
  public void valPoint() {
    
    Workspace ws     = Workspace.getInstance();
    
    Point     object = new Point( 10, 10 );
    Point     defval = new Point( 27, 35 );
    ws.setPoint( "point", object );
    
    assertNull( ws.getPoint( "point_" ) );
    assertNull( ws.getPoint( "point_", null ) );
    assertThat( ws.getPoint( "point_", defval ), is( defval ) );
    
    assertThat( ws.getPoint( "point", null   ), is( object ) );
    assertThat( ws.getPoint( "point", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valInsets() {
    
    Workspace ws     = Workspace.getInstance();
    
    Insets    object = new Insets( 10, 10, 20, 20 );
    Insets    defval = new Insets( 27, 27, 35, 35 );
    ws.setInsets( "insets", object );
    
    assertNull( ws.getInsets( "insets_" ) );
    assertNull( ws.getInsets( "insets_", null ) );
    assertThat( ws.getInsets( "insets_", defval ), is( defval ) );
    
    assertThat( ws.getInsets( "insets", null   ), is( object ) );
    assertThat( ws.getInsets( "insets", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valString() {
    
    Workspace ws     = Workspace.getInstance();
    
    String    object = "alpha";
    String    defval = "beta";
    ws.setString( "string", object );
    
    assertNull( ws.getString( "string_" ) );
    assertNull( ws.getString( "string_", null ) );
    assertThat( ws.getString( "string_", defval ), is( defval ) );
    
    assertThat( ws.getString( "string", null   ), is( object ) );
    assertThat( ws.getString( "string", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valShort() {
    
    Workspace ws     = Workspace.getInstance();
    
    Short     object = Short.valueOf((short) 1);
    Short     defval = Short.valueOf((short) -12);
    ws.setShort( "short", object );
    
    assertNull( ws.getShort( "short_" ) );
    assertNull( ws.getShort( "short_", null ) );
    assertThat( ws.getShort( "short_", defval ), is( defval ) );
    
    assertThat( ws.getShort( "short", null   ), is( object ) );
    assertThat( ws.getShort( "short", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valFloat() {
    
    Workspace ws     = Workspace.getInstance();
    
    Float     object = Float.valueOf((float) 1.0);
    Float     defval = Float.valueOf((float) 17.3);
    ws.setFloat( "float", object );
    
    assertNull( ws.getFloat( "float_" ) );
    assertNull( ws.getFloat( "float_", null ) );
    assertThat( ws.getFloat( "float_", defval ), is( defval ) );
    
    assertThat( ws.getFloat( "float", null   ), is( object ) );
    assertThat( ws.getFloat( "float", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valDouble() {
    
    Workspace ws     = Workspace.getInstance();
    
    Double    object = Double.valueOf(1.0);
    Double    defval = Double.valueOf(17.3);
    ws.setDouble( "double", object );
    
    assertNull( ws.getDouble( "double_" ) );
    assertNull( ws.getDouble( "double_", null ) );
    assertThat( ws.getDouble( "double_", defval ), is( defval ) );
    
    assertThat( ws.getDouble( "double", null   ), is( object ) );
    assertThat( ws.getDouble( "double", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valColor() {
    
    Workspace ws     = Workspace.getInstance();
    
    Color     object = Color.red;
    Color     defval = Color.green;
    ws.setColor( "color", object );
    
    assertNull( ws.getColor( "color_" ) );
    assertNull( ws.getColor( "color_", null ) );
    assertThat( ws.getColor( "color_", defval ), is( defval ) );
    
    assertThat( ws.getColor( "color", null   ), is( object ) );
    assertThat( ws.getColor( "color", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valByte() {
    
    Workspace ws     = Workspace.getInstance();
    
    Byte      object = (byte) 1;
    Byte      defval = (byte) 12;
    ws.setByte( "byte", object );
    
    assertNull( ws.getByte( "byte_" ) );
    assertNull( ws.getByte( "byte_", null ) );
    assertThat( ws.getByte( "byte_", defval ), is( defval ) );
    
    assertThat( ws.getByte( "byte", null   ), is( object ) );
    assertThat( ws.getByte( "byte", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valBoolean() {
    
    Workspace ws     = Workspace.getInstance();
    
    Boolean   object = Boolean.FALSE;
    Boolean   defval = Boolean.TRUE;
    ws.setBoolean( "boolean", object );
    
    assertNull( ws.getBoolean( "boolean_" ) );
    assertNull( ws.getBoolean( "boolean_", null ) );
    assertThat( ws.getBoolean( "boolean_", defval ), is( defval ) );
    
    assertThat( ws.getBoolean( "boolean", null   ), is( object ) );
    assertThat( ws.getBoolean( "boolean", defval ), is( object ) );
    
  }

  @Test(groups = "all")
  public void valRectangle() {
    
    Workspace ws     = Workspace.getInstance();
    
    Rectangle object = new Rectangle( 40, 40, 300, 120 );
    Rectangle defval = new Rectangle( 30, 40, 300, 120 );
    ws.setRectangle( "rectangle", object );
    
    assertNull( ws.getRectangle( "rectangle_" ) );
    assertNull( ws.getRectangle( "rectangle_", null ) );
    assertThat( ws.getRectangle( "rectangle_", defval ), is( defval ) );
    
    assertThat( ws.getRectangle( "rectangle", null   ), is( object ) );
    assertThat( ws.getRectangle( "rectangle", defval ), is( object ) );
    
  }
  
} /* ENDCLASS */
