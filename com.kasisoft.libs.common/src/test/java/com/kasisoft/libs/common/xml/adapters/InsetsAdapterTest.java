package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'InsetsAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InsetsAdapterTest {

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

  InsetsAdapter adapter = new InsetsAdapter( errhandler, null, null, ":" );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    return new Object[][] {
      { null            , null                                 },
      { "-13:42:10:20"  , new Insets( -13  , 42 ,  10 ,  20 )  },
      { "0:0:50:28"     , new Insets(   0  ,  0 ,  50 ,  28 )  },
      { "0:0:-50:-28"   , new Insets(   0  ,  0 , -50 , -28 )  },
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
      { null                               , null            },
      { new Insets( -13  , 42 , 30 , 70 )  , "-13:42:30:70"  },
      { new Insets(   0  ,  0 , 12 , 29 )  , "0:0:12:29"     },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, Insets expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions={FailureException.class,NumberFormatException.class}, groups="all")
  public void invalidUnmarshal( String value, Insets expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( Insets value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
