package com.kasisoft.libs.common.constants;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.util.Optional;

/**
 * Tests for the enumeration 'MimeType'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MimeTypeTest {

  @Test(groups = "all")
  public void simple() {
    var result = MimeType.findBySuffix("tex");
    assertNotNull(result);
    assertThat(result.size(), is(2));
    assertTrue(result.contains(MimeType.LaTeX));
    assertTrue(result.contains(MimeType.TeX));
  }

  @Test(groups = "all")
  public void findByMimeType() {
    Optional<MimeType> result = MimeType.findByMimeType("text/html;charset=UTF-8");
    assertNotNull(result);
    assertTrue(result.isPresent());
    assertThat(result.get(), is(MimeType.Html));
  }

} /* ENDCLASS */
