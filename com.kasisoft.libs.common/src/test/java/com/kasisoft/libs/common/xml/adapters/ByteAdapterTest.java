package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'ByteAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ByteAdapterTest {

  ByteAdapter adapter = new ByteAdapter();
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                          },
      { "0"     , Byte.valueOf( (byte) 0        ) },
      { "13"    , Byte.valueOf( (byte) 13       ) },
      { "-23"   , Byte.valueOf( (byte) -23      ) },
      { "max"   , Byte.valueOf( Byte.MAX_VALUE  ) },
      { "MAX"   , Byte.valueOf( Byte.MAX_VALUE  ) },
      { "min"   , Byte.valueOf( Byte.MIN_VALUE  ) },
      { "MIN"   , Byte.valueOf( Byte.MIN_VALUE  ) },
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
      { null                            , null   },
      { Byte.valueOf( (byte) 0        ) , "0"    },
      { Byte.valueOf( (byte) 13       ) , "13"   },
      { Byte.valueOf( (byte) -23      ) , "-23"  },
      { Byte.valueOf( Byte.MAX_VALUE  ) , "127"  },
      { Byte.valueOf( Byte.MIN_VALUE  ) , "-128" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Byte expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Byte value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all", expectedExceptions=IllegalArgumentException.class)
  public void invalidUnmarshal( String value, Long expected ) throws Exception {
    assertThat( (long) adapter.unmarshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
