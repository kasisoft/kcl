package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the type 'LongAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LongAdapterTest {

  LongAdapter adapter = new LongAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                          },
      { "0"     , Long.valueOf( 0               ) },
      { "13"    , Long.valueOf( 13              ) },
      { "-23"   , Long.valueOf( -23             ) },
      { "max"   , Long.valueOf( Long.MAX_VALUE  ) },
      { "MAX"   , Long.valueOf( Long.MAX_VALUE  ) },
      { "min"   , Long.valueOf( Long.MIN_VALUE  ) },
      { "MIN"   , Long.valueOf( Long.MIN_VALUE  ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3.7"   , Double.valueOf( 3.7 ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                            , null                   },
      { Long.valueOf( 0               ) , "0"                    },
      { Long.valueOf( 13              ) , "13"                   },
      { Long.valueOf( -23             ) , "-23"                  },
      { Long.valueOf( Long.MAX_VALUE  ) , "9223372036854775807"  },
      { Long.valueOf( Long.MIN_VALUE  ) , "-9223372036854775808" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Long expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Long value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all", expectedExceptions=IllegalArgumentException.class)
  public void invalidUnmarshal( String value, Long expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
