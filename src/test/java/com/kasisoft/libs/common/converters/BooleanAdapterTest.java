package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'BooleanAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BooleanAdapterTest {

  BooleanAdapter adapter = new BooleanAdapter();
  
  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
    return new Object[][] {
      { null    , null          },
      { "true"  , Boolean.TRUE  },
      { "false" , Boolean.FALSE },
      { "ja"    , Boolean.TRUE  },
      { "nein"  , Boolean.FALSE },
      { "an"    , Boolean.TRUE  },
      { "ein"   , Boolean.TRUE  },
      { "aus"   , Boolean.FALSE },
      { "on"    , Boolean.TRUE  },
      { "off"   , Boolean.FALSE },
      { "0"     , Boolean.FALSE },
      { "1"     , Boolean.TRUE  },
      { "-1"    , Boolean.TRUE  },
      { ""      , Boolean.FALSE },
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() {
    return new Object[][] {
      { null          , null    },
      { Boolean.TRUE  , "true"  },
      { Boolean.FALSE , "false" }, 
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Boolean expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Boolean value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
