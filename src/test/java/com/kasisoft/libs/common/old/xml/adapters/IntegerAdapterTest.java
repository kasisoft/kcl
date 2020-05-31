package com.kasisoft.libs.common.old.xml.adapters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNull;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'IntegerAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntegerAdapterTest {

  IntegerAdapter adapter = new IntegerAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                                  },
      { "0"     , Integer.valueOf( 0                  ) },
      { "13"    , Integer.valueOf( 13                 ) },
      { "-23"   , Integer.valueOf( -23                ) },
      { "max"   , Integer.valueOf( Integer.MAX_VALUE  ) },
      { "MAX"   , Integer.valueOf( Integer.MAX_VALUE  ) },
      { "min"   , Integer.valueOf( Integer.MIN_VALUE  ) },
      { "MIN"   , Integer.valueOf( Integer.MIN_VALUE  ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3.7" },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                  , null          },
      { Integer.valueOf( 0                  ) , "0"           },
      { Integer.valueOf( 13                 ) , "13"          },
      { Integer.valueOf( -23                ) , "-23"         },
      { Integer.valueOf( Integer.MAX_VALUE  ) , "2147483647"  },
      { Integer.valueOf( Integer.MIN_VALUE  ) , "-2147483648" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Integer expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Integer value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all")
  public void invalidUnmarshal( String value ) throws Exception {
    assertNull( adapter.unmarshal( value ) );
  }

} /* ENDCLASS */
