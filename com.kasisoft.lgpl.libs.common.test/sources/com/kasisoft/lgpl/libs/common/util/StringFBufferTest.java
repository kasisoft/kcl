/**
 * Name........: StringFBufferTest
 * Description.: Testcase for the class 'StringFBuffer'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Testcase for the class 'StringFBuffer'.
 */
@Test(groups="all")
public class StringFBufferTest {

  @Test
  public void appendF() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }
  
  @Test
  public void charAt() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.charAt(0)  , 'M' );
    Assert.assertEquals( buffer.charAt(-1) , '!' );
  }

  @Test
  public void substring() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.substring( 0, 2 )  , "My" );
    Assert.assertEquals( buffer.substring( -10 ) , "Not 0x11 !" );
  }
  
  @Test
  public void trimLeading() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimLeading();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }

  @Test
  public void trimTrailing() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trimTrailing();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }

  @Test
  public void trim() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "\r\n   My test is this: %s ! Not 0x%02x !\r\n   ", "Hello World", Integer.valueOf( 17 ) );
    buffer.trim();
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }
  
  @Test
  public void endsWith() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.endsWith( true  , "here !" ) );
    Assert.assertFalse ( buffer.endsWith( true  , "HERE !" ) );
    Assert.assertTrue  ( buffer.endsWith( false , "HERE !" ) );
  }

  @Test
  public void startsWith() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.startsWith( true  , "The" ) );
    Assert.assertFalse ( buffer.startsWith( true  , "THE" ) );
    Assert.assertTrue  ( buffer.startsWith( false , "THE" ) );
  }

  @Test
  public void equals() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "The frog is here !" );
    Assert.assertTrue  ( buffer.startsWith( true  , "The frog is here !" ) );
    Assert.assertFalse ( buffer.startsWith( true  , "THE FROG IS HERE !" ) );
    Assert.assertTrue  ( buffer.startsWith( false , "THE FROG IS HERE !" ) );
  }

} /* ENDCLASS */
