package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the class 'Pair'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PairTest {

  private Pair<String, Boolean> newPair(String key, boolean value) {
    var result = new Pair<String, Boolean>();
    result.setValues(key, value);
    return result;
  }
  
  @DataProvider(name = "createPairData")
  public Object[][] createPairData() {
    return new Object[][] {
      {newPair("A", true)  , "A", true},
      {newPair("A", false) , "A", false},
      {newPair("B", true)  , "B", true},
    };
  }
  
  @Test(dataProvider = "createPairData", groups = "all")
  public void processPairs(Pair<String, Boolean> pair, String key, boolean value) {
    assertThat(pair.getFirst(), is(key));
    assertThat(pair.getLast(), is(value));
    assertThat(pair.getKey(), is(key));
    assertThat(pair.getValue(), is(value));
  }
  
} /* ENDCLASS */
