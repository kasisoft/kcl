package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

/**
 * Tests for the type 'ByteAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteAdapterTest {

  private ByteAdapter adapter = new ByteAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      { null, null},
      {"0"  , Byte.valueOf((byte) 0)},
      {"13" , Byte.valueOf((byte) 13)},
      {"-23", Byte.valueOf((byte) -23)},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Byte expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  

  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                    , null},
      {Byte.valueOf((byte) 0)  , "0"},
      {Byte.valueOf((byte) 13) , "13"},
      {Byte.valueOf((byte) -23), "-23"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Byte value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      {"3.7"},
    };
  }

  @Test(dataProvider = "data_invalidDecode", groups = "all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }
  
} /* ENDCLASS */
