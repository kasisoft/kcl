package com.kasisoft.libs.common.functional;

import static org.testng.Assert.*;

import org.testng.annotations.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KPredicateTest {
  
  KPredicate<String> isName         = $ -> $.equalsIgnoreCase("name");
  
  KPredicate<String> isUppercase    = $ -> $.equals($.toUpperCase());
  
  @Test(groups = "all")
  public void predicate() throws Exception {
    assertFalse(isName.test("A"));
    assertTrue(isName.test("NAME"));
    assertTrue(isUppercase.test("A"));
  }

  @Test(groups = "all")
  public void negate() throws Exception {
    assertTrue(isName.negate().test("A"));
    assertFalse(isUppercase.negate().test("A"));
    assertTrue(isUppercase.negate().test("b"));
  }
  
  @Test(groups = "all")
  public void protect() {
    assertFalse(isName.protect().test("A"));
    assertTrue(isName.protect().test("NAME"));
    assertTrue(isUppercase.protect().test("A"));
  }

  @Test(groups = "all")
  public void and() throws Exception {
    assertTrue(isName.and(isUppercase).test("NAME"));
    assertFalse(isName.and(isUppercase).test("A"));
  }

  @Test(groups = "all")
  public void or() throws Exception {
    assertTrue(isName.or(isUppercase).test("name"));
    assertTrue(isName.or(isUppercase).test("A"));
    assertFalse(isName.or(isUppercase).test("a"));
  }
  
} /* ENDCLASS */
