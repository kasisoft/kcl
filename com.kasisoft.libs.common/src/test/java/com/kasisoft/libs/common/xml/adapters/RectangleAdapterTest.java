package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

/**
 * Tests for the type 'RectangleAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class RectangleAdapterTest {

  private SimpleErrorHandler errhandler = new SimpleErrorHandler() {

    @Override
    public void failure( Object source, String message, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };
  
  private RectangleAdapter adapter = new RectangleAdapter( errhandler, null, null, ":" );

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

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Rectangle expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class,NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Rectangle expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Rectangle value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
