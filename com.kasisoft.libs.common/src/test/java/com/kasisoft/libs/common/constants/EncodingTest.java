/**
 * Name........: EncodingTest
 * Description.: Tests for the constants 'Encoding'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

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
  
  @Test
  public void values() {
    Assert.assertNotNull( Encoding.values() );
  }

  @Test
  public void getDefault() {
    Assert.assertNotNull( Encoding.getDefault() );
  }

  @DataProvider(name="valueByNameData")
  public Object[][] valueByNameData() {
    return new Object[][] {
      { Encoding . UTF8    . getEncoding(), Encoding . UTF8    },  
      { Encoding . UTF16   . getEncoding(), Encoding . UTF16   },
      { Encoding . UTF16BE . getEncoding(), Encoding . UTF16BE },
      { Encoding . UTF16LE . getEncoding(), Encoding . UTF16LE },
      { "Bibo"                            , null               },
    };
  }

  @Test(dataProvider="valueByNameData")
  public void valueByName( String name, Encoding expected ) {
    Assert.assertEquals( Encoding.valueByName( name ), expected );
  }
  
} /* ENDCLASS */
