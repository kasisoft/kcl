package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

/**
 * Tests for the type 'ColorAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ColorAdapterTest {

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

  private ColorAdapter adapter = new ColorAdapter( errhandler, null, null );

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

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Color expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class, NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Color expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Color value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
