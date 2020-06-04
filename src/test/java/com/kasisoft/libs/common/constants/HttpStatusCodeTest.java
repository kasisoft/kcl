package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

/**
 * Tests for the constants 'HttpStatusCode'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class HttpStatusCodeTest {

  @DataProvider(name = "createData")
  public Object[][] createData() {
    var        codes  = HttpStatusCode.values();
    Object[][] result = new Object[codes.length][2];
    for (var i = 0; i < codes.length; i++) {
      result[i][0] = codes[i].getTextualCode();
      result[i][1] = codes[i];
    }
    return result;
  };
  
  @Test(dataProvider = "createData", groups = "all")
  public void valueByText(String textualcode, HttpStatusCode code) {
    Optional<HttpStatusCode> statusCode = HttpStatusCode.findByStatusCode(textualcode);
    assertNotNull(statusCode);
    assertTrue(statusCode.isPresent());
    assertThat(statusCode.get(), is(code));
  }
  
} /* ENDCLASS */
