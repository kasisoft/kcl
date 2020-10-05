package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntAdapterTest {

  IntAdapter adapter = new IntAdapter();
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      { null    , null                                  },
      { "0"     , Integer.valueOf( 0                  ) },
      { "13"    , Integer.valueOf( 13                 ) },
      { "-23"   , Integer.valueOf( -23                ) },
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Integer expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      { null                                  , null          },
      { Integer.valueOf( 0                  ) , "0"           },
      { Integer.valueOf( 13                 ) , "13"          },
      { Integer.valueOf( -23                ) , "-23"         },
      { Integer.valueOf( Integer.MAX_VALUE  ) , "2147483647"  },
      { Integer.valueOf( Integer.MIN_VALUE  ) , "-2147483648" },
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Integer value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      { "3.7" },
    };
  }

  @Test(dataProvider = "data_invalidDecode", groups="all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }

} /* ENDCLASS */
