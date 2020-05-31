package com.kasisoft.libs.common.old.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the constants 'HttpStatusCode'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
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
    assertThat( HttpStatusCode.valueByStatusCode( textualcode ), is( code ) );
  }
  
} /* ENDCLASS */
