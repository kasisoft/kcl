package com.kasisoft.libs.common.functional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KSupplierTest {

  private KSupplier<String> getName = () -> "name";
  
  @Test(groups = "all")
  public void get() throws Exception {
    assertThat(getName.get(), is("name"));
  }

  @Test(groups = "all")
  public void protect() {
    assertThat(getName.protect().get(), is("name"));
  }

} /* ENDCLASS */
