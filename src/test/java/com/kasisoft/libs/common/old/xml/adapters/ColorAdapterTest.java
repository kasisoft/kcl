package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.function.BiConsumer;

import java.awt.Color;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'ColorAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorAdapterTest {

  BiConsumer<Object,Exception> errhandler = new BiConsumer<Object,Exception>() {

    @Override
    public void accept( Object source, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };

  ColorAdapter adapter = new ColorAdapter( errhandler, null, null );

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
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={KclException.class, NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Color expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Color value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */