package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

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
      { "3.7" },
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
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Short value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all")
  public void invalidUnmarshal( String value) throws Exception {
    assertNull( adapter.unmarshal( value ) );
  }
  
} /* ENDCLASS */
