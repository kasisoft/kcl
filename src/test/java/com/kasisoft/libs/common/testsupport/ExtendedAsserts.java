package com.kasisoft.libs.common.testsupport;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import java.util.List;

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
      for (int i = 0; i < value.size(); i++) {
        assertThat("[" + i + "]", value.get(i), is(expected.get(i)));
      }
    }
  }
  
} /* ENDCLASS */
