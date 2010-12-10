/**
 * Name........: DoubleAdapterTest
 * Description.: Tests for the type 'DoubleAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.xml.adapters.*;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'DoubleAdapter'.
 */
@Test(groups="all")
public class DoubleAdapterTest {

  private DoubleAdapter adapter = new DoubleAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                                        },
      { "0.0"   , Double.valueOf( 0.0                       ) },
      { "1.3"   , Double.valueOf( 1.3                       ) },
      { "2.4e8" , Double.valueOf( 2.4e8                     ) },
      { "nan"   , Double.valueOf( Double.NaN                ) },
      { "NAN"   , Double.valueOf( Double.NaN                ) },
      { "inf"   , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "INF"   , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "+inf"  , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "+INF"  , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "-inf"  , Double.valueOf( Double.NEGATIVE_INFINITY  ) },
      { "-INF"  , Double.valueOf( Double.NEGATIVE_INFINITY  ) },
      { "max"   , Double.valueOf( Double.MAX_VALUE          ) },
      { "MAX"   , Double.valueOf( Double.MAX_VALUE          ) },
      { "min"   , Double.valueOf( Double.MIN_VALUE          ) },
      { "MIN"   , Double.valueOf( Double.MIN_VALUE          ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3,7"   , Double.valueOf( 3.7 ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                        , null        },
      { Double.valueOf( 0.0                       ) , "0.0"       },
      { Double.valueOf( 1.3                       ) , "1.3"       },
      { Double.valueOf( 2.4e8                     ) , "2.4E8"     },
      { Double.valueOf( Double.NaN                ) , "NaN"       },
      { Double.valueOf( Double.POSITIVE_INFINITY  ) , "Infinity"  },
      { Double.valueOf( Double.NEGATIVE_INFINITY  ) , "-Infinity" },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Double expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( Double value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=NumberFormatException.class)
  public void invalidUnmarshal( String value, Double expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

} /* ENDCLASS */
