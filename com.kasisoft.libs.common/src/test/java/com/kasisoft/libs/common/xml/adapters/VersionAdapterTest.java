/**
 * Name........: VersionAdapterTest
 * Description.: Tests for the type 'VersionAdapter'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.text.*;

/**
 * Tests for the type 'VersionAdapter'.
 */
@Test(groups="all")
public class VersionAdapterTest {

  private VersionAdapter adapter = new VersionAdapter( true, true );
  
  @DataProvider(name="createUnmarshalling")
  public Object[][] createUnmarshalling() {
    try {
      return new Object[][] {
        { null              , null                                         },
        { "1.1.1.qualifier" , new Version( "1.1.1.qualifier", true, true ) },
      };
    } catch( ParseException ex ) {
      throw new RuntimeException(ex);
    }
  }

  @DataProvider(name="createMarshalling")
  public Object[][] createMarshalling() {
    return new Object[][] {
      { null                                                 , null               },
      { new Version( 1, 1, Integer.valueOf(1), "qualifier" ) , "1.1.1.qualifier"  },
    };
  }

  @Test(dataProvider="createUnmarshalling")
  public void unmarshal( String value, Version expected ) throws Exception {
    Assert.assertEquals( adapter.unmarshal( value ), expected );
  }
  
  @Test(dataProvider="createMarshalling")
  public void marshal( Version value, String expected ) throws Exception {
    Assert.assertEquals( adapter.marshal( value ), expected );
  }
  
} /* ENDCLASS */
