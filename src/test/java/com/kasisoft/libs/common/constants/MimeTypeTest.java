package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tests for the enumeration 'MimeType'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MimeTypeTest {

  @Test(groups = "all")
  public void findBySuffix() {
    var result = MimeType.findBySuffix("tex");
    assertNotNull(result);
    assertThat(result.size(), is(2));
    assertTrue(result.contains(MimeType.LaTeX));
    assertTrue(result.contains(MimeType.TeX));
  }

  @Test(groups = "all")
  public void findBySuffix__UnknownSuffix() {
    var result = MimeType.findBySuffix("kiq");
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test(groups = "all")
  public void findByMimeType() {
    
    var result1 = MimeType.findByMimeType("text/html;charset=UTF-8");
    assertNotNull(result1);
    assertTrue(result1.isPresent());
    assertThat(result1.get(), is(MimeType.Html));

    var result2 = MimeType.findByMimeType("text/html");
    assertNotNull(result2);
    assertTrue(result2.isPresent());
    assertThat(result2.get(), is(MimeType.Html));

    var result3 = MimeType.findByMimeType(";text/html");
    assertNotNull(result3);
    assertFalse(result3.isPresent());

  }
  
  @DataProvider(name = "data_test")
  public Object[][] data_test() {
    var values = MimeType.values();
    var result = new Object[values.length][2];
    for (var i = 0; i < values.length; i++) {
      result[i][0] = values[i].getMimeType();
      result[i][1] = values[i];
    }
    return result;
  }
  
  
  @Test(groups = "all", dataProvider = "data_test")
  public void test(String type, MimeType mt) {
    assertTrue(mt.test(type));
  }

  
  @Test(groups = "all")
  public void test__NullValue() {
    for (var mt : MimeType.values()) {
      assertFalse(mt.test(null));
    }
  }

  @DataProvider(name = "data_supportsSuffix")
  public Object[][] data_supportsSuffix() {
    var values = MimeType.values();
    var result = new Object[values.length][2];
    for (var i = 0; i < values.length; i++) {
      result[i][0] = values[i].getPrimarySuffix();
      result[i][1] = values[i];
    }
    return result;
  }
  
  @Test(groups = "all", dataProvider = "data_supportsSuffix")
  public void supportsSuffix(String suffix, MimeType mt) {
    assertTrue(mt.supportsSuffix(suffix));
  }

} /* ENDCLASS */
