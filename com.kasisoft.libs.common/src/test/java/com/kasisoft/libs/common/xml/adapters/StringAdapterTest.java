package com.kasisoft.libs.common.xml.adapters;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the type 'StringAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, String expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( String value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

} /* ENDCLASS */
