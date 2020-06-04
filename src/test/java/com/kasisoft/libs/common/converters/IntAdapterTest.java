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
 * Tests for the type 'IntAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntAdapterTest {

  IntAdapter adapter = new IntAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      { null    , null                                  },
      { "0"     , Integer.valueOf( 0                  ) },
      { "13"    , Integer.valueOf( 13                 ) },
      { "-23"   , Integer.valueOf( -23                ) },
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
      { null                                  , null          },
      { Integer.valueOf( 0                  ) , "0"           },
      { Integer.valueOf( 13                 ) , "13"          },
      { Integer.valueOf( -23                ) , "-23"         },
      { Integer.valueOf( Integer.MAX_VALUE  ) , "2147483647"  },
      { Integer.valueOf( Integer.MIN_VALUE  ) , "-2147483648" },
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Integer expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Integer value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", groups="all", expectedExceptions = KclException.class)
  public void invalidDecode(String value) throws Exception {
    assertNull(adapter.decode(value));
  }

} /* ENDCLASS */