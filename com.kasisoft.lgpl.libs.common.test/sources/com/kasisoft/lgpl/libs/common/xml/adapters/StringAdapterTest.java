/**
 * Name........: StringAdapterTest
 * Description.: Tests for the type 'StringAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'StringAdapter'.
 */
@Test(groups="all")
public class StringAdapterTest {

  private StringAdapter adapter = new StringAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null  },
      { ""      , ""    },
      { "bla"   , "bla" },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
        { null    , null  },
        { ""      , ""    },
        { "bla"   , "bla" },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, String expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( String value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

} /* ENDCLASS */
