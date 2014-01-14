/**
 * Name........: URIAdapterTest
 * Description.: Tests for the type 'URIAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.net.*;

/**
 * Tests for the type 'URIAdapter'.
 */
@Test(groups="all")
public class URIAdapterTest {

  private SimpleErrorHandler errhandler = new SimpleErrorHandler() {

    @Override
    public void failure( Object source, String message, Exception cause ) {
      if( cause instanceof RuntimeException ) {
        throw ((RuntimeException) cause);
      } else {
        throw new RuntimeException( cause );
      }
    }
    
  };

  private URIAdapter adapter = new URIAdapter( errhandler, null, null );

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

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, URI expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }

  @Test(dataProvider="createMarshalling")
  public void marshal( URI value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
