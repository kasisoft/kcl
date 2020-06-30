package com.kasisoft.libs.common.functional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.annotations.Test;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KSupplierTest {

  KSupplier<String> getName = () -> "name";
  
  @Test(groups = "all")
  public void get() throws Exception {
    assertThat(getName.get(), is("name"));
  }

  @Test(groups = "all")
  public void protect() {
    assertThat(getName.protect().get(), is("name"));
  }

} /* ENDCLASS */
