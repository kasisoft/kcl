package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import java.net.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'URLAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class URLAdapterTest {

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

  URLAdapter adapter = new URLAdapter( errhandler, null, null );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() throws Exception {
    return new Object[][] {
      { null                        , null                                   },
      { "http://www.amiga-news.de"  , new URL( "http://www.amiga-news.de" )  },
    };
  }

  @DataProvider(name="createInvalidUnmarshalling")
  public Object[][] createInvalidUnmarshalling() {
    return new Object[][] {
      { "dodo" , null },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() throws Exception {
    return new Object[][] {
      { null                                  , null                        },
      { new URL( "http://www.amiga-news.de" ) , "http://www.amiga-news.de"  },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, URL expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createInvalidUnmarshalling", expectedExceptions=RuntimeException.class, groups="all")
  public void invalidUnmarshal( String value, URL expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( URL value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
