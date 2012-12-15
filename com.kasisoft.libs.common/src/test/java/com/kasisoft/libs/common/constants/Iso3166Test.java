/**
 * Name........: Iso3166Test
 * Description.: Test for the constants 'Iso3166'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

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

  @DataProvider(name="createAlpha2")
  public Object[][] createAlpha2() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], values[i].alpha2() };
    }
    return result;
  }

  @DataProvider(name="createAlpha3")
  public Object[][] createAlpha3() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], values[i].alpha3() };
    }
    return result;
  }

  @DataProvider(name="createNumerical")
  public Object[][] createNumerical() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], Integer.valueOf( values[i].numerical() ) };
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

  @Test(dataProvider="createAlpha2")
  public void byAlpha2( Iso3166 expected, String alpha2 ) {
    Iso3166 identified = Iso3166.valueByAlpha2( alpha2 );
    Assert.assertNotNull( identified );
    Assert.assertEquals( identified, expected );
  }

  @Test(dataProvider="createAlpha3")
  public void byAlpha3( Iso3166 expected, String alpha3 ) {
    Iso3166 identified = Iso3166.valueByAlpha3( alpha3 );
    Assert.assertNotNull( identified );
    Assert.assertEquals( identified, expected );
  }

  @Test(dataProvider="createNumerical")
  public void byNumerical( Iso3166 expected, Integer numerical ) {
    Iso3166 identified = Iso3166.valueByNumerical( numerical.intValue() );
    Assert.assertNotNull( identified );
    Assert.assertEquals( identified, expected );
  }

} /* ENDCLASS */
