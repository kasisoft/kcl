package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Test for the constants 'Iso639'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Iso639Test {

  @DataProvider(name = "createValues")
  public Object[][] createValues() {
    var values = Iso639.values();
    var result = new Object[ values.length ][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i]};
    }
    return result;
  }

  @Test(dataProvider = "createValues", groups = "all")
  public void validCode(Iso639 value) {
    assertNotNull(value.getBibliography());
    assertNotNull(value.getTerminology() );
    assertThat(value.getBibliography().length(), is(3));
    assertThat(value.getTerminology().length(), is(3));
    if (value.getAlpha2() != null) {
      assertThat(value.getAlpha2().length(), is(2));
    }
  }
  
  @DataProvider(name = "createAlpha2")
  public Object[][] createAlpha2() {
    var values = Iso639.values();
    var count  = 0;
    for (var value : values ) {
      if (value.getAlpha2() != null) {
        count++;
      }
    }
    var result = new Object[count][];
    var j      = 0;
    for (var value : values) {
      if (value.getAlpha2() != null) {
        result[j++] = new Object[] {value, value.getAlpha2()};
      }
    }
    return result;
  }

  @Test(dataProvider = "createAlpha2", groups = "all")
  public void findByAlpha2(Iso639 expected, String alpha2) {
    var identified = Iso639.findByAlpha2(alpha2);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }

  @DataProvider(name = "createBibliography")
  public Object[][] createBibliography() {
    var values = Iso639.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getBibliography()};
    }
    return result;
  }
  
  @Test(dataProvider = "createBibliography", groups = "all")
  public void findByBibliography(Iso639 expected, String bibliography) {
    var identified = Iso639.findByBibliography(bibliography);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }
  
  @DataProvider(name = "createTerminology")
  public Object[][] createTerminology() {
    var values = Iso639.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getTerminology()};
    }
    return result;
  }
  
  @Test(dataProvider = "createTerminology",groups = "all")
  public void findByTerminology(Iso639 expected, String terminology) {
    var identified = Iso639.findByTerminology(terminology);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is( expected));
  }

} /* ENDCLASS */
