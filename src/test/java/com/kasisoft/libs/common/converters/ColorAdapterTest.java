package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

  ColorAdapter adapter = new ColorAdapter();

  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() {
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

  @DataProvider(name = "dataInvalidDecode")
  public Object[][] dataInvalidDecode() {
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

  @DataProvider(name = "dataEncode")
  public Object[][] createMarshalling() {
    return new Object[][] {
      {null        , null},
      {Color.yellow, "#ffffff00"},
      {Color.BLUE  , "#ff0000ff"},
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, Color expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @Test(dataProvider = "dataInvalidDecode", expectedExceptions = KclException.class, groups = "all")
  public void invalidDecode(String value, Color expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(Color value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
