/**
 * Name........: IntegerAdapterTest
 * Description.: Tests for the type 'IntegerAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'IntegerAdapter'.
 */
@Test(groups="all")
public class IntegerAdapterTest {

  private IntegerAdapter adapter = new IntegerAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                                  },
      { "0"     , Integer.valueOf( 0                  ) },
      { "13"    , Integer.valueOf( 13                 ) },
      { "-23"   , Integer.valueOf( -23                ) },
      { "max"   , Integer.valueOf( Integer.MAX_VALUE  ) },
      { "MAX"   , Integer.valueOf( Integer.MAX_VALUE  ) },
      { "min"   , Integer.valueOf( Integer.MIN_VALUE  ) },
      { "MIN"   , Integer.valueOf( Integer.MIN_VALUE  ) },
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
      { null                                  , null          },
      { Integer.valueOf( 0                  ) , "0"           },
      { Integer.valueOf( 13                 ) , "13"          },
      { Integer.valueOf( -23                ) , "-23"         },
      { Integer.valueOf( Integer.MAX_VALUE  ) , "2147483647"  },
      { Integer.valueOf( Integer.MIN_VALUE  ) , "-2147483648" },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Integer expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( Integer value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=IllegalArgumentException.class)
  public void invalidUnmarshal( String value, Integer expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

} /* ENDCLASS */
