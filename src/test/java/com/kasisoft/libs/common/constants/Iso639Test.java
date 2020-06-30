package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Iso639Test {

  @DataProvider(name = "data_validCode")
  public Object[][] data_validCode() {
    var values = Iso639.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i]};
    }
    return result;
  }

  @Test(dataProvider = "data_validCode", groups = "all")
  public void validCode(Iso639 value) {
    assertNotNull(value.getBibliography());
    assertNotNull(value.getTerminology() );
    assertThat(value.getBibliography().length(), is(3));
    assertThat(value.getTerminology().length(), is(3));
    if (value.getAlpha2() != null) {
      assertThat(value.getAlpha2().length(), is(2));
    }
  }
  
  @DataProvider(name = "data_findByAlpha2")
  public Object[][] data_findByAlpha2() {
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

  @Test(dataProvider = "data_findByAlpha2", groups = "all")
  public void findByAlpha2(Iso639 expected, String alpha2) {
    var identified = Iso639.findByAlpha2(alpha2);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }

  @DataProvider(name = "data_findByBibliography")
  public Object[][] data_findByBibliography() {
    var values = Iso639.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getBibliography()};
    }
    return result;
  }
  
  @Test(dataProvider = "data_findByBibliography", groups = "all")
  public void findByBibliography(Iso639 expected, String bibliography) {
    var identified = Iso639.findByBibliography(bibliography);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is(expected));
  }
  
  @DataProvider(name = "data_findByTerminology")
  public Object[][] data_findByTerminology() {
    var values = Iso639.values();
    var result = new Object[values.length][];
    for (var i = 0; i < values.length; i++) {
      result[i] = new Object[] {values[i], values[i].getTerminology()};
    }
    return result;
  }
  
  @Test(dataProvider = "data_findByTerminology",groups = "all")
  public void findByTerminology(Iso639 expected, String terminology) {
    var identified = Iso639.findByTerminology(terminology);
    assertNotNull(identified);
    assertTrue(identified.isPresent());
    assertThat(identified.get(), is( expected));
  }

} /* ENDCLASS */
