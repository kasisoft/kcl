package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * Test for the constants 'Iso3166'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
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
      result[i] = new Object[] { values[i], values[i].getAlpha2() };
    }
    return result;
  }

  @DataProvider(name="createAlpha3")
  public Object[][] createAlpha3() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], values[i].getAlpha3() };
    }
    return result;
  }

  @DataProvider(name="createNumerical")
  public Object[][] createNumerical() {
    Iso3166[]   values = Iso3166.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], Integer.valueOf( values[i].getNumerical() ) };
    }
    return result;
  }

  @Test(dataProvider="createValues",groups="all")
  public void validCode( Iso3166 value ) {
    assertThat( value.getAlpha2(), is( notNullValue() ) );
    assertThat( value.getAlpha3(), is( notNullValue() ) );
    assertThat( value.getAlpha2().length(), is(2) );
    assertThat( value.getAlpha3().length(), is(3) );
  }

  @Test(dataProvider="createAlpha2",groups="all")
  public void byAlpha2( Iso3166 expected, String alpha2 ) {
    Iso3166 identified = Iso3166.valueByAlpha2( alpha2 );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }

  @Test(dataProvider="createAlpha3",groups="all")
  public void byAlpha3( Iso3166 expected, String alpha3 ) {
    Iso3166 identified = Iso3166.valueByAlpha3( alpha3 );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }

  @Test(dataProvider="createNumerical",groups="all")
  public void byNumerical( Iso3166 expected, Integer numerical ) {
    Iso3166 identified = Iso3166.valueByNumerical( numerical.intValue() );
    assertThat( identified, is( notNullValue() ) );
    assertThat( identified, is( expected ) );
  }

} /* ENDCLASS */