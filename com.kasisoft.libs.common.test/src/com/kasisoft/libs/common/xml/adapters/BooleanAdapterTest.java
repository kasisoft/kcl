/**
 * Name........: BooleanAdapterTest
 * Description.: Tests for the type 'BooleanAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'BooleanAdapter'.
 */
@Test(groups="all")
public class BooleanAdapterTest {

  private BooleanAdapter adapter = new BooleanAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
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

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null          , null    },
      { Boolean.TRUE  , "true"  },
      { Boolean.FALSE , "false" }, 
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Boolean expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( Boolean value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
