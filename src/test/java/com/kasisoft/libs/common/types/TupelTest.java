package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TupelTest {

  @DataProvider(name = "data_processTupels")
  public Object[][] data_processTupels() {
    return new Object[][] {
      {new Tupel<String>(), null, null},
      {new Tupel<String>("A"), "A", "A"},
      {new Tupel<String>("A", "B"), "A", "B"},
      {new Tupel<String>("A", "B", "C"), "A", "C"},
    };
  }
  
  @Test(dataProvider = "data_processTupels", groups = "all")
  public void processTupels(Tupel<String> tupel, String first, String last) {
    assertThat(tupel.getFirst(), is(first));
    assertThat(tupel.getLast(), is(last ));
  }
  
} /* ENDCLASS */
