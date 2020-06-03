package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNull;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'ByteAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ByteAdapterTest {

  ByteAdapter adapter = new ByteAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      { null, null},
      {"0"  , Byte.valueOf((byte) 0)},
      {"13" , Byte.valueOf((byte) 13)},
      {"-23", Byte.valueOf((byte) -23)},
    };
  }

  @DataProvider(name = "dataInvalidEncode")
  public Object[][] dataInvalidEncode() {
    return new Object[][] {
      {"3.7"},
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      {null                     , null},
      {Byte.valueOf((byte) 0)   , "0"},
      {Byte.valueOf((byte) 13)  , "13"},
      {Byte.valueOf((byte) -23) , "-23"},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Byte expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Byte value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidEncode", groups = "all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }
  
} /* ENDCLASS */
