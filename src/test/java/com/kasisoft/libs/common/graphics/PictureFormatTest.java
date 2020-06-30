package com.kasisoft.libs.common.graphics;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PictureFormatTest {

  @Test(groups = "all")
  public void nullTestsAsFalse() {
    for (PictureFormat fmt : PictureFormat.values()) {
      assertFalse(fmt.test(null));
    }
  }

  @Test(groups = "all")
  public void testForFilename() {
    for (PictureFormat fmt : PictureFormat.values()) {
      var filename = String.format("example.%s", fmt.getSuffix());
      assertTrue(fmt.test(filename));
    }
  }

  @Test(groups = "all")
  public void fileNamesWithoutSuffix() {
    for (PictureFormat fmt : PictureFormat.values()) {
      var filename = String.format("example_%s", fmt.getSuffix());
      assertFalse(fmt.test(filename));
    }
  }

} /* ENDCLASS */
