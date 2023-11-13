package com.kasisoft.libs.common.test.graphics;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.graphics.*;

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
      var filename = "example.%s".formatted(fmt.getSuffix());
      assertTrue(fmt.test(filename));
    }
  }

  @Test
  public void fileNamesWithoutSuffix() {
    for (PictureFormat fmt : PictureFormat.values()) {
      var filename = "example_%s".formatted(fmt.getSuffix());
      assertFalse(fmt.test(filename));
    }
  }

} /* ENDCLASS */
