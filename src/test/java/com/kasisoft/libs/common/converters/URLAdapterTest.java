package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import java.net.*;

/**
 * Tests for the type 'URLAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class URLAdapterTest {

  private URLAdapter adapter = new URLAdapter();

  @DataProvider(name = "data_decode")
  public Object[][] data_decode() throws Exception {
    return new Object[][] {
      { null                        , null                                   },
      { "http://www.amiga-news.de"  , new URL( "http://www.amiga-news.de" )  },
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, URL expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_encode")
  public Object[][] data_encode() throws Exception {
    return new Object[][] {
      { null                                  , null                        },
      { new URL( "http://www.amiga-news.de" ) , "http://www.amiga-news.de"  },
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(URL value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
