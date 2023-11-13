package com.kasisoft.libs.common.test.functional;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KSupplierTest {

  private KSupplier<String> getName = () -> "name";

  @Test
  public void get() throws Exception {
    assertThat(getName.get(), is("name"));
  }

  @Test
  public void protect() {
    assertThat(getName.protect().get(), is("name"));
  }

} /* ENDCLASS */
