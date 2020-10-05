package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * Tests for the constants 'HttpStatusCode'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class HttpStatusCodeTest {

  @DataProvider(name = "data_findByStatusCode")
  public Object[][] data_findByStatusCode() {
    var codes  = HttpStatusCode.values();
    var result = new Object[codes.length][2];
    for (var i = 0; i < codes.length; i++) {
      result[i][0] = codes[i].getTextualCode();
      result[i][1] = codes[i];
    }
    return result;
  };
  
  @Test(dataProvider = "data_findByStatusCode", groups = "all")
  public void findByStatusCode(String textualcode, HttpStatusCode code) {
    var statusCode = HttpStatusCode.findByStatusCode(textualcode);
    assertNotNull(statusCode);
    assertTrue(statusCode.isPresent());
    assertThat(statusCode.get(), is(code));
  }
  
  @Test(groups = "all")
  public void findByStatusCode__Unknown() {
    assertFalse(HttpStatusCode.findByStatusCode(-1).isPresent());
    assertFalse(HttpStatusCode.findByStatusCode("bibo").isPresent());
    assertFalse(HttpStatusCode.findByStatusCode(null).isPresent());
  }
  
  @Test(groups = "all")
  public void predicate() {
    for (var sc : HttpStatusCode.values()) {
      assertFalse(sc.test(null));
      assertTrue(sc.test(sc.getCode()));
      assertFalse(sc.test(sc.getCode() + 1));
    }
  }
  
} /* ENDCLASS */
