package com.kasisoft.libs.common.testsupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

import java.awt.image.BufferedImage;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ExtendedAsserts {

  public static <T> void assertLists(List<T> value, List<T> expected) {
    if (expected == null) {
      assertNull(value);
    } else {
      assertNotNull(value);
      assertThat(value.size(), is(expected.size()));
      for (var i = 0; i < value.size(); i++) {
        assertThat("[" + i + "]", value.get(i), is(expected.get(i)));
      }
    }
  }
  
  public static void assertImages(BufferedImage value, BufferedImage expected) {
    if (expected == null) {
      assertNull(value);
    } else {
      assertNotNull(value);
      assertThat(value.getWidth(), is(expected.getWidth()));
      assertThat(value.getHeight(), is(expected.getHeight()));
      for (var x = 0; x < value.getWidth(); x++) {
        for (var y = 0; y < value.getHeight(); y++) {
          assertThat(String.format("x: %d, y: %d", x, y), value.getRGB(x, y), is(expected.getRGB(x, y)));
        }
      }
    }
  }
  
} /* ENDCLASS */
