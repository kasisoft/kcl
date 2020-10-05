package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PairTest {

  @DataProvider(name = "data_processPairs")
  public Object[][] data_processPairs() {
    return new Object[][] {
      {new Pair<String, Boolean>("A", true), "A", true},
      {new Pair<String, Boolean>("A", false), "A", false},
      {new Pair<String, Boolean>("B", true), "B", true},
    };
  }
  
  @Test(dataProvider = "data_processPairs", groups = "all")
  public void processPairs(Pair<String, Boolean> pair, String key, boolean value) {
    assertThat(pair.getFirst(), is(key));
    assertThat(pair.getLast(), is(value));
    assertThat(pair.getKey(), is(key));
    assertThat(pair.getValue(), is(value));
  }
  
} /* ENDCLASS */
