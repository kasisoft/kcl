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
  public void append() {
    StringFBuffer buffer = new StringFBuffer();
    buffer.appendF( "My test is this: %s ! Not 0x%02x !", "Hello World", Integer.valueOf( 17 ) );
    Assert.assertEquals( buffer.toString(), "My test is this: Hello World ! Not 0x11 !" );
  }
  
} /* ENDCLASS */
