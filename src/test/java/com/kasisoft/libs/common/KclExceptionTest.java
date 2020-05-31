package com.kasisoft.libs.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KclExceptionTest {

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void wrap() {
    try {
      throw new RuntimeException("simple text");
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }
  
  @Test(groups = "all")
  public void unwrap() {
    try {
      wrap();
    } catch (KclException ex) {
      Exception cause = KclException.unwrap(ex);
      assertNotNull(cause);
      assertTrue(cause instanceof RuntimeException);
      assertThat(cause.getLocalizedMessage(), is("java.lang.RuntimeException: simple text"));
    }
  }
  
} /* ENDCLASS */
