package com.kasisoft.libs.common.graphics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PictureFormatTest {

  @Test
  public void nullTestsAsFalse() {
    for (PictureFormat fmt : PictureFormat.values()) {
      assertFalse(fmt.test(null));
    }
  }

  @Test
  public void testForFilename() {
    for (PictureFormat fmt : PictureFormat.values()) {
      var filename = String.format("example.%s", fmt.getSuffix());
      assertTrue(fmt.test(filename));
    }
  }

  @Test
  public void fileNamesWithoutSuffix() {
    for (PictureFormat fmt : PictureFormat.values()) {
      var filename = String.format("example_%s", fmt.getSuffix());
      assertFalse(fmt.test(filename));
    }
  }

} /* ENDCLASS */
