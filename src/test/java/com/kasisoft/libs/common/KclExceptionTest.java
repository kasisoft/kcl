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

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void wrap__KclException() {
    try {
      throw new KclException("simple text");
    } catch (Exception ex) {
      throw KclException.wrap(ex);
    }
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void wrap__KclExceptionWithMessage() {
    try {
      throw new KclException("simple text");
    } catch (Exception ex) {
      throw KclException.wrap(ex, "Error Message: %s", ex.getLocalizedMessage());
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

  @Test(groups = "all")
  public void unwrap__NotAKclException() {
    try {
      try {
        throw new KclException("simple text");
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    } catch (Exception ex) {
      var unwrapped = KclException.unwrap(ex);
      assertThat(unwrapped.getLocalizedMessage(), is("simple text"));
    }
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void defaultConstructor() {
    throw new KclException();
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void constructor__Formatting() {
    throw new KclException("Message: %s", "Value");
  }

  @Test(groups = "all", expectedExceptions = KclException.class)
  public void constructor__Message() {
    throw new KclException("Message");
  }

} /* ENDCLASS */
