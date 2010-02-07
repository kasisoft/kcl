/**
 * Name........: EncodingTest
 * Description.: Tests for the constants 'Encoding'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.constants;

import com.kasisoft.lgpl.libs.common.constants.*;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the constants 'Encoding'.
 */
@Test(groups="all")
public class EncodingTest {

  @DataProvider(name="createData")
  public Object[][] createData() {
    return new Object[][] {
      { "Flöz", Encoding.UTF8, new byte[] { (byte) 0x46, (byte) 0x6C, (byte) 0xC3, (byte) 0xB6, (byte) 0x7A } },
      { "Flöz", Encoding.UTF16, new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00, (byte) 0x7A } },
      { "Flöz", Encoding.UTF16BE, new byte[] { (byte) 0x00, (byte) 0x46, (byte) 0x00, (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00, (byte) 0x7A } },
      { "Flöz", Encoding.UTF16LE, new byte[] { (byte) 0x46, (byte) 0x00, (byte) 0x6C, (byte) 0x00, (byte) 0xF6, (byte) 0x00, (byte) 0x7A, (byte) 0x00  } },
    };
  };
  
  @Test(dataProvider="createData")
  public void performEncoding( String literal, Encoding encoding, byte[] bytes ) {
    byte[] encoded = encoding.encode( literal );
    Assert.assertEquals( encoded, bytes );
  }
  
} /* ENDCLASS */
