package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the class 'Triple'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TripleTest {

  private Triple<String, String, Boolean> newTriple(String key, String m, boolean value) {
    var result = new Triple<String, String, Boolean>();
    result.setValues(key, m, value);
    return result;
  }
  
  @DataProvider(name = "createTripleData")
  public Object[][] createTripleData() {
    return new Object[][] {
      {newTriple("A", "mid0", true)  , "A", "mid0", true},
      {newTriple("A", "mid1", false) , "A", "mid1", false},
      {newTriple("B", "mid2", true)  , "B", "mid2", true},
    };
  }
  
  @Test(dataProvider = "createTripleData", groups = "all")
  public void processPairs(Triple<String, String, Boolean> pair, String key, String mid, boolean value) {
    assertThat(pair.getFirst(), is(key));
    assertThat(pair.getLast(), is(value));
    assertThat(pair.getValue2(), is(mid));
  }
  
} /* ENDCLASS */
