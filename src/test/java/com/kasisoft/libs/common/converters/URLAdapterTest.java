package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.URL;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'URLAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class URLAdapterTest {

  URLAdapter adapter = new URLAdapter();

  @DataProvider(name = "dataDecode")
  public Object[][] dataDecode() throws Exception {
    return new Object[][] {
      { null                        , null                                   },
      { "http://www.amiga-news.de"  , new URL( "http://www.amiga-news.de" )  },
    };
  }

  @DataProvider(name = "dataEncode")
  public Object[][] dataEncode() throws Exception {
    return new Object[][] {
      { null                                  , null                        },
      { new URL( "http://www.amiga-news.de" ) , "http://www.amiga-news.de"  },
    };
  }

  @Test(dataProvider = "dataDecode", groups = "all")
  public void decode(String value, URL expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @Test(dataProvider = "dataEncode", groups = "all")
  public void encode(URL value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
