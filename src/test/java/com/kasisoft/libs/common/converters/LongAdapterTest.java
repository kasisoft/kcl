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
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongAdapterTest {

  LongAdapter adapter = new LongAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      { null    , null                               },
      { "0"     , Long.valueOf( 0                  ) },
      { "13"    , Long.valueOf( 13                 ) },
      { "-23"   , Long.valueOf( -23                ) },
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Long expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      { null                            , null          },
      { Long.valueOf( 0               ) , "0"           },
      { Long.valueOf( 13              ) , "13"          },
      { Long.valueOf( -23             ) , "-23"         },
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Long value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] dataInvalidDecode() {
    return new Object[][] {
      { "3.7" },
    };
  }

  @Test(dataProvider = "data_invalidDecode", groups="all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }

} /* ENDCLASS */
