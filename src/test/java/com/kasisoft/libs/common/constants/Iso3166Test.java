package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for the constants 'Iso3166'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Iso3166Test {

  @DataProvider(name = "createValues")
  public Object[][] createValues() {
    var values = Iso3166.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i]};
    }
    return result;
  }

  @DataProvider(name = "createAlpha2")
  public Object[][] createAlpha2() {
    var values = Iso3166.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getAlpha2()};
    }
    return result;
  }

  @DataProvider(name = "createAlpha3")
  public Object[][] createAlpha3() {
    var values = Iso3166.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getAlpha3()};
    }
    return result;
  }

  @DataProvider(name = "createNumerical")
  public Object[][] createNumerical() {
    var values = Iso3166.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], Integer.valueOf(values[i].getNumerical())};
    }
    return result;
  }

  @Test(dataProvider = "createValues", groups = "all")
  public void validCode(Iso3166 value) {
    assertNotNull(value.getAlpha2());
    assertNotNull(value.getAlpha3());
    assertThat(value.getAlpha2().length(), is(2) );
    assertThat(value.getAlpha3().length(), is(3) );
  }

  @Test(dataProvider = "createAlpha2", groups = "all")
  public void findByAlpha2(Iso3166 expected, String alpha2) {
    var identified = Iso3166.findByAlpha2(alpha2);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }

  @Test(dataProvider = "createAlpha3", groups = "all")
  public void findByAlpha3(Iso3166 expected, String alpha3) {
    var identified = Iso3166.findByAlpha3(alpha3);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }

  @Test(dataProvider = "createNumerical", groups = "all")
  public void findByNumerical(Iso3166 expected, Integer numerical) {
    var identified = Iso3166.findByNumerical(numerical.intValue());
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }

} /* ENDCLASS */
