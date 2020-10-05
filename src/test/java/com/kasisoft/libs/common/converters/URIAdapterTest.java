package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import java.net.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class URIAdapterTest {

  URIAdapter adapter = new URIAdapter();

  @DataProvider(name = "data_decode")
  public Object[][] data_decode() throws Exception {
    return new Object[][] {
      {null                      , null                               },
      {"http://www.amiga-news.de", new URI("http://www.amiga-news.de")},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode(String value, URI expected) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }

  @DataProvider(name = "data_encode")
  public Object[][] data_encode() throws Exception {
    return new Object[][] {
      {null                               , null                      },
      {new URI("http://www.amiga-news.de"), "http://www.amiga-news.de"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(URI value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
