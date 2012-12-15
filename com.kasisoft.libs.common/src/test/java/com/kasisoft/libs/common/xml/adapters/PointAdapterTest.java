/**
 * Name........: PointAdapterTest
 * Description.: Tests for the type 'PointAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

/**
 * Tests for the type 'PointAdapter'.
 */
@Test(groups="all")
public class PointAdapterTest {

  private PointAdapter adapter = new PointAdapter( ":" );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null      , null                   },
      { "-13:42"  , new Point( -13  , 42 ) },
      { "0:0"     , new Point(   0  ,  0 ) },
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
      { null                    , null      },
      { new Point( -13  , 42 )  , "-13:42"  },
      { new Point(   0  ,  0 )  , "0:0"     },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Point expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class,NumberFormatException.class})
  public void invalidUnmarshal( String value, Point expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling")
  public void marshal( Point value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
