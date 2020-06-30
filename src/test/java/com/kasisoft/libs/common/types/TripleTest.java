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

  @DataProvider(name = "data_processTriples")
  public Object[][] data_processTriples() {
    return new Object[][] {
      {new Triple<String, String, Boolean>("A", "mid0", true), "A", "mid0", true},
      {new Triple<String, String, Boolean>("A", "mid1", false), "A", "mid1", false},
      {new Triple<String, String, Boolean>("B", "mid2", true), "B", "mid2", true},
    };
  }
  
  @Test(dataProvider = "data_processTriples", groups = "all")
  public void processTriples(Triple<String, String, Boolean> pair, String key, String mid, boolean value) {
    assertThat(pair.getFirst(), is(key));
    assertThat(pair.getLast(), is(value));
    assertThat(pair.getValue2(), is(mid));
  }
  
} /* ENDCLASS */
