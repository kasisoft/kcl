package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.util.*;

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

  @DataProvider(name = "data_test")
  public Object[][] data_test() {
    var list = new ArrayList<Object[]>();
    for (var iso : Iso639.values()) {
      if (iso.getAlpha2() != null) {
        list.add(new Object[] {iso.getAlpha2(), iso});
      }
      list.add(new Object[] {iso.getBibliography(), iso});
      list.add(new Object[] {iso.getTerminology(), iso});
    }
    return list.toArray(new Object[list.size()][2]);
  }
  
  @Test(groups = "all", dataProvider = "data_test")
  public void test(String code, Iso639 iso639) {
    assertTrue(iso639.test(code));
  }

  @Test(groups = "all")
  public void test__NullValue() {
    for (var iso : Iso639.values()) {
      assertFalse(iso.test(null));
    }
  }

  @Test(groups = "all")
  public void findBy__NullValue() {
    
    for (var iso : Iso639.values()) {
      
      var opt1 = iso.findByAlpha2(null);
      assertNotNull(opt1);
      assertFalse(opt1.isPresent());
      
      var opt2 = iso.findByAlpha3(null);
      assertNotNull(opt2);
      assertFalse(opt2.isPresent());
      
    }
    
  }

} /* ENDCLASS */
