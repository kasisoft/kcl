/**
 * Name........: TupelTest
 * Description.: Tests for the class 'Tupel'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Tests for the class 'Tupel'.
 */
@Test(groups="all")
public class TupelTest {

  private Tupel<String> newTupel( String ... args ) {
    Tupel<String> result = new Tupel<String>();
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
  
  @Test(dataProvider="createTupelData")
  public void processTupels( Tupel<String> tupel, String first, String last ) {
    Assert.assertEquals( tupel.getValue(), first );
    Assert.assertEquals( tupel.getFirst(), first );
    Assert.assertEquals( tupel.getLast(), last );
  }
  
} /* ENDCLASS */
