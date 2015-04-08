package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'PointAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PointAdapterTest {

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

  PointAdapter adapter = new PointAdapter( errhandler, null, null, ":" );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null      , null                   },
      { "-13:42"  , new Point( -13  , 42 ) },
      { "0:0"     , new Point(   0  ,  0 ) },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "12:-13:42" , null },
      { "0:"        , null },
      { "a:b"       , null },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                    , null      },
      { new Point( -13  , 42 )  , "-13:42"  },
      { new Point(   0  ,  0 )  , "0:0"     },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Point expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class,NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Point expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Point value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
