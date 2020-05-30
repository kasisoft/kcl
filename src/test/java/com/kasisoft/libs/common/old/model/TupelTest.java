package com.kasisoft.libs.common.old.model;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Tests for the class 'Tupel'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TupelTest {

  private Tupel<String> newTupel( String ... args ) {
    Tupel<String> result = new Tupel<>();
    result.setValues( args );
    return result;
  }
  
  @DataProvider(name="createTupelData")
  public Object[][] createTupelData() {
    return new Object[][] {
      { newTupel(), null, null },
      { newTupel( "A"           ), "A", "A" },
      { newTupel( "A", "B"      ), "A", "B" },
      { newTupel( "A", "B", "C" ), "A", "C" },
    };
  }
  
  @Test(dataProvider="createTupelData", groups="all")
  public void processTupels( Tupel<String> tupel, String first, String last ) {
    assertThat( tupel.getValue(), is( first ) );
    assertThat( tupel.getFirst(), is( first ) );
    assertThat( tupel.getLast(), is( last ) );
  }
  
} /* ENDCLASS */
