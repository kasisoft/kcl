package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the constants 'Encoding'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
  
  @Test(dataProvider="createData", groups="all")
  public void performEncoding( String literal, Encoding encoding, byte[] bytes ) {
    byte[] encoded = encoding.encode( literal );
    assertThat( encoded, is( bytes ) );
  }
  
  @Test(groups="all")
  public void values() {
    assertThat( Encoding.values(), is( notNullValue() ) );
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

  @Test(dataProvider="valueByNameData", groups="all")
  public void valueByName( String name, Encoding expected ) {
    assertThat( Encoding.valueByName( name ), is( expected ) );
  }
  
} /* ENDCLASS */
