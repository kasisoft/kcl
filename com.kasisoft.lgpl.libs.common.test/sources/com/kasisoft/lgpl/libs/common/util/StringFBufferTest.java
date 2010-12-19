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

} /* ENDCLASS */
