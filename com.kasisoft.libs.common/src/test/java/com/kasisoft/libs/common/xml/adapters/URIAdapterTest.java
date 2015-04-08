package com.kasisoft.libs.common.xml.adapters;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import java.net.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Tests for the type 'URIAdapter'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class URIAdapterTest {

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

  URIAdapter adapter = new URIAdapter( errhandler, null, null );

  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() throws Exception {
    return new Object[][] {
      { null                        , null                                   },
      { "http://www.amiga-news.de"  , new URI( "http://www.amiga-news.de" )  },
    };
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() throws Exception {
    return new Object[][] {
      { null                                  , null                        },
      { new URI( "http://www.amiga-news.de" ) , "http://www.amiga-news.de"  },
    };
  }

  @Test(dataProvider="createUnmarshalling", groups="all")
  public void unmarshal( String value, URI expected ) throws Exception {
    assertThat( adapter.unmarshal( value ), is( expected ) );
  }

  @Test(dataProvider="createMarshalling", groups="all")
  public void marshal( URI value, String expected ) throws Exception {
    assertThat( adapter.marshal( value ), is( expected ) );
  }
  
} /* ENDCLASS */
