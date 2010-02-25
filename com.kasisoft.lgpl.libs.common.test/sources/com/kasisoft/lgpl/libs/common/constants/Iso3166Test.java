/**
 * Name........: Iso3166Test
 * Description.: Test for the constants 'Iso3166'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import org.testng.annotations.*;

import org.testng.*;

/**
 * Test for the constants 'Iso3166'.
 */
@Test(groups="all")
public class Iso3166Test {

  @DataProvider(name="createValues")
  public Object[][] createValues() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i] };
    }
    return result;
  }

  @Test(dataProvider="createValues")
  public void validCode( Iso3166 value ) {
    Assert.assertNotNull( value.alpha2() );
    Assert.assertNotNull( value.alpha3() );
    Assert.assertEquals( value.alpha2().length(), 2 );
    Assert.assertEquals( value.alpha3().length(), 3 );
  }
  
} /* ENDCLASS */
