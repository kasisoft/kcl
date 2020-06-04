package com.kasisoft.libs.common.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the class 'Tupel'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TupelTest {

  private Tupel<String> newTupel(String ... args) {
    var result = new Tupel<String>();
    result.setValues(args);
    return result;
  }
  
  @DataProvider(name = "createTupelData")
  public Object[][] createTupelData() {
    return new Object[][] {
      { newTupel(), null, null },
      { newTupel( "A"           ), "A", "A" },
      { newTupel( "A", "B"      ), "A", "B" },
      { newTupel( "A", "B", "C" ), "A", "C" },
    };
  }
  
  @Test(dataProvider = "createTupelData", groups = "all")
  public void processTupels( Tupel<String> tupel, String first, String last ) {
    assertThat(tupel.getFirst(), is(first));
    assertThat(tupel.getLast(), is(last ));
  }
  
} /* ENDCLASS */