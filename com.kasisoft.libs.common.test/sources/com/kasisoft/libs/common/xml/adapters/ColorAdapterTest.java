/**
 * Name........: ColorAdapterTest
 * Description.: Tests for the type 'ColorAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.xml.adapters;

import com.kasisoft.lgpl.libs.common.base.*;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

/**
 * Tests for the type 'ColorAdapter'.
 */
@Test(groups="all")
public class ColorAdapterTest {

  private ColorAdapter adapter = new ColorAdapter();

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null              , null          },
      { "yellow"          , Color.yellow  },
      { "blue"            , Color.BLUE    },
      { "#ffffff00"       , Color.yellow  },
      { "#ff0000ff"       , Color.BLUE    },
      { "#ffff00"         , Color.yellow  },
      { "#0000ff"         , Color.BLUE    },
      { "rgb(255,255,0)"  , Color.yellow  },
      { "rgb(0,0,255)"    , Color.BLUE    },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "12:-13:42"     , null },
      { "0:"            , null },
      { "a:b"           , null },
      { "#"             , null },
      { "#zzzzzz"       , null },
      { "rgb"           , null },
      { "rgb("          , null },
      { "rgb)"          , null },
      { "rgb()"         , null },
      { "rgb(a,b,c)"    , null },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null          , null            },
      { Color.yellow  , "#ffffff00"     },
      { Color.BLUE    , "#ff0000ff"     },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Color expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class, NumberFormatException.class})
  public void invalidUnmarshal( String value, Color expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling")
  public void marshal( Color value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
