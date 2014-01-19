/**
 * Name........: HttpStatusCodeTest
 * Description.: Tests for the constants 'HttpStatusCode'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the constants 'HttpStatusCode'.
 */
public class HttpStatusCodeTest {

  @DataProvider(name="createData")
  public Object[][] createData() {
    HttpStatusCode[] codes  = HttpStatusCode.values();
    Object[][]       result = new Object[codes.length][2];
    for( int i = 0; i < codes.length; i++ ) {
      result[i][0] = codes[i].getTextualCode();
      result[i][1] = codes[i];
    }
    return result;
  };
  
  @Test(dataProvider="createData", groups="all")
  public void valueByText( String textualcode, HttpStatusCode code ) {
    Assert.assertEquals( HttpStatusCode.valueByStatusCode( textualcode ), code );
  }
  
} /* ENDCLASS */
