package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'FloatAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FloatAdapterTest {

  SimpleErrorHandler errhandler = new SimpleErrorHandler() {

    @Override
    public void failure( Object source, String message, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };

  FloatAdapter adapter = new FloatAdapter( errhandler, null, null );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                                      },
      { "0.0"   , Float.valueOf( (float) 0.0              ) },
      { "1.3"   , Float.valueOf( (float) 1.3              ) },
      { "2.4e8" , Float.valueOf( (float) 2.4e8            ) },
      { "nan"   , Float.valueOf( Float.NaN                ) },
      { "NAN"   , Float.valueOf( Float.NaN                ) },
      { "inf"   , Float.valueOf( Float.POSITIVE_INFINITY  ) },
      { "INF"   , Float.valueOf( Float.POSITIVE_INFINITY  ) },
      { "+inf"  , Float.valueOf( Float.POSITIVE_INFINITY  ) },
      { "+INF"  , Float.valueOf( Float.POSITIVE_INFINITY  ) },
      { "-inf"  , Float.valueOf( Float.NEGATIVE_INFINITY  ) },
      { "-INF"  , Float.valueOf( Float.NEGATIVE_INFINITY  ) },
      { "max"   , Float.valueOf( Float.MAX_VALUE          ) },
      { "MAX"   , Float.valueOf( Float.MAX_VALUE          ) },
      { "min"   , Float.valueOf( Float.MIN_VALUE          ) },
      { "MIN"   , Float.valueOf( Float.MIN_VALUE          ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3,7"   , Float.valueOf( (float) 3.7 ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                      , null        },
      { Float.valueOf( (float) 0.0              ) , "0.0"       },
      { Float.valueOf( (float) 1.3              ) , "1.3"       },
      { Float.valueOf( (float) 2.4e8            ) , "2.4E8"     },
      { Float.valueOf( Float.NaN                ) , "NaN"       },
      { Float.valueOf( Float.POSITIVE_INFINITY  ) , "Infinity"  },
      { Float.valueOf( Float.NEGATIVE_INFINITY  ) , "-Infinity" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Float expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Float value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", groups="all", expectedExceptions=NumberFormatException.class)
  public void invalidUnmarshal( String value, Float expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

} /* ENDCLASS */
