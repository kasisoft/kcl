package com.kasisoft.libs.common.converters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.types.*;

import org.testng.annotations.*;

/**
 * Tests for the type 'VersionAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class VersionAdapterTest {

  private VersionAdapter adapter = new VersionAdapter().withMicro(true).withQualifier(true);
  
  @DataProvider(name = "data_decode")
  public Object[][] data_decode() {
    return new Object[][] {
      {null             , null                                       },
      {"1.1.1.qualifier", new Version( "1.1.1.qualifier", true, true)},
    };
  }

  @Test(dataProvider = "data_decode", groups = "all")
  public void decode( String value, Version expected ) throws Exception {
    assertThat(adapter.decode(value), is(expected));
  }
  
  @DataProvider(name = "data_encode")
  public Object[][] data_encode() {
    return new Object[][] {
      {null                                              , null             },
      {new Version(1, 1, Integer.valueOf(1), "qualifier"), "1.1.1.qualifier"},
    };
  }

  @Test(dataProvider = "data_encode", groups = "all")
  public void encode(Version value, String expected) throws Exception {
    assertThat(adapter.encode(value), is(expected));
  }
  
} /* ENDCLASS */
