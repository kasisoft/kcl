package com.kasisoft.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'ShortAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShortAdapterTest {

  ShortAdapter adapter = new ShortAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                              },
      { "0"     , Short.valueOf( (short) 0        ) },
      { "13"    , Short.valueOf( (short) 13       ) },
      { "-23"   , Short.valueOf( (short) -23      ) },
      { "max"   , Short.valueOf( Short.MAX_VALUE  ) },
      { "MAX"   , Short.valueOf( Short.MAX_VALUE  ) },
      { "min"   , Short.valueOf( Short.MIN_VALUE  ) },
      { "MIN"   , Short.valueOf( Short.MIN_VALUE  ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3.7"   , Double.valueOf( 3.7 ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                              , null     },
      { Short.valueOf( (short) 0        ) , "0"      },
      { Short.valueOf( (short) 13       ) , "13"     },
      { Short.valueOf( (short) -23      ) , "-23"    },
      { Short.valueOf( Short.MAX_VALUE  ) , "32767"  },
      { Short.valueOf( Short.MIN_VALUE  ) , "-32768" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Short expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Short value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all", expectedExceptions=IllegalArgumentException.class)
  public void invalidUnmarshal( String value, Short expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
} /* ENDCLASS */
