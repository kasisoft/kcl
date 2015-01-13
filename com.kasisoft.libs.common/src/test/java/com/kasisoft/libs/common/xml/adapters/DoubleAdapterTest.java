package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'DoubleAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoubleAdapterTest {

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

  DoubleAdapter adapter = new DoubleAdapter( errhandler, null, null );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null    , null                                        },
      { "0.0"   , Double.valueOf( 0.0                       ) },
      { "1.3"   , Double.valueOf( 1.3                       ) },
      { "2.4e8" , Double.valueOf( 2.4e8                     ) },
      { "nan"   , Double.valueOf( Double.NaN                ) },
      { "NAN"   , Double.valueOf( Double.NaN                ) },
      { "inf"   , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "INF"   , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "+inf"  , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "+INF"  , Double.valueOf( Double.POSITIVE_INFINITY  ) },
      { "-inf"  , Double.valueOf( Double.NEGATIVE_INFINITY  ) },
      { "-INF"  , Double.valueOf( Double.NEGATIVE_INFINITY  ) },
      { "max"   , Double.valueOf( Double.MAX_VALUE          ) },
      { "MAX"   , Double.valueOf( Double.MAX_VALUE          ) },
      { "min"   , Double.valueOf( Double.MIN_VALUE          ) },
      { "MIN"   , Double.valueOf( Double.MIN_VALUE          ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "3,7"   , Double.valueOf( 3.7 ) },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                        , null        },
      { Double.valueOf( 0.0                       ) , "0.0"       },
      { Double.valueOf( 1.3                       ) , "1.3"       },
      { Double.valueOf( 2.4e8                     ) , "2.4E8"     },
      { Double.valueOf( Double.NaN                ) , "NaN"       },
      { Double.valueOf( Double.POSITIVE_INFINITY  ) , "Infinity"  },
      { Double.valueOf( Double.NEGATIVE_INFINITY  ) , "-Infinity" },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Double expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Double value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=NumberFormatException.class, groups="all")
  public void invalidUnmarshal( String value, Double expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

} /* ENDCLASS */
