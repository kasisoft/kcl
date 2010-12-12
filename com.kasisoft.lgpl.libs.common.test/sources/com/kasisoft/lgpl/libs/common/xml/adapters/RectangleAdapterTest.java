/**
 * Name........: RectangleAdapterTest
 * Description.: Tests for the type 'RectangleAdapter'.
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
 * Tests for the type 'RectangleAdapter'.
 */
@Test(groups="all")
public class RectangleAdapterTest {

  private RectangleAdapter adapter = new RectangleAdapter( ":" );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null            , null                                   },
      { "-13:42:10:20"  , new Rectangle( -13  , 42 ,  10 ,  20 )  },
      { "0:0:50:28"     , new Rectangle(   0  ,  0 ,  50 ,  28 )  },
      { "0:0:-50:-28"   , new Rectangle(   0  ,  0 , -50 , -28 )  },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "12:-13:42" , null },
      { "0:"        , null },
      { "a:b"       , null },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                  , null            },
      { new Rectangle( -13  , 42 , 30 , 70 )  , "-13:42:30:70"  },
      { new Rectangle(   0  ,  0 , 12 , 29 )  , "0:0:12:29"     },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Rectangle expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class,NumberFormatException.class})
  public void invalidUnmarshal( String value, Rectangle expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling")
  public void marshal( Rectangle value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
