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
 * Tests for the type 'ShortAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortAdapterTest {

  ShortAdapter adapter = new ShortAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      { null , null},
      { "0"  , Short.valueOf((short)0  )},
      { "13" , Short.valueOf((short)13 )},
      { "-23", Short.valueOf((short)-23)},
    };
  }

  @DataProvider(name = "dataInvalidDecode")
  public Object[][] dataInvalidDecode() {
    return new Object[][] {
      { "3.7" },
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      { null                       , null },
      { Short.valueOf( (short)0  ) , "0"  },
      { Short.valueOf( (short)13 ) , "13" },
      { Short.valueOf( (short)-23) , "-23"},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Short expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Short value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", groups="all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }

} /* ENDCLASS */
