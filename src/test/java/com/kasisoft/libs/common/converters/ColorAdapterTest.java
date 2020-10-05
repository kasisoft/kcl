package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.*;

import org.testng.annotations.*;

import java.awt.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the type 'ColorAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ColorAdapterTest {

  ColorAdapter adapter = new ColorAdapter();

  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null            , null        },
      {"yellow"        , Color.yellow},
      {"blue"          , Color.BLUE  },
      {"#ffffff00"     , Color.yellow},
      {"#ff0000ff"     , Color.BLUE  },
      {"#ffff00"       , Color.yellow},
      {"#0000ff"       , Color.BLUE  },
      {"rgb(255,255,0)", Color.yellow},
      {"rgb(0,0,255)"  , Color.BLUE  },
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, Color expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_invalidDecode")
  public Object[][] data_invalidDecode() {
    return new Object[][] {
      {"12:-13:42" , null},
      {"0:"        , null},
      {"a:b"       , null},
      {"#"         , null},
      {"#zzzzzz"   , null},
      {"rgb"       , null},
      {"rgb("      , null},
      {"rgb)"      , null},
      {"rgb()"     , null},
      {"rgb(a,b,c)", null},
    };
  }

  @Test(dataProvider = "data_invalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Color expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null        , null},
      {Color.yellow, "#ffffff00"},
      {Color.BLUE  , "#ff0000ff"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Color value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
