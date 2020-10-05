package com.kasisoft.libs.common.functional;

import static org.testng.Assert.*;

import org.testng.annotations.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KBiPredicateTest {
  
  KBiPredicate<String, String> isEqualIgnoreCase = ($a, $b) -> $a.equalsIgnoreCase($b);
  
  KBiPredicate<String, String> areBothUppercase  = ($a, $b) -> {
    var strA = $a.toUpperCase();
    var strB = $b.toUpperCase();
    return strA.equals($a) && strB.equals($b);
  };
  
  @Test(groups = "all")
  public void predicate() throws Exception {
    assertFalse(isEqualIgnoreCase.test("A", "b"));
    assertTrue(isEqualIgnoreCase.test("A", "a"));
  }

  @Test(groups = "all")
  public void negate() throws Exception {
    assertTrue(isEqualIgnoreCase.negate().test("A", "b"));
    assertFalse(isEqualIgnoreCase.negate().test("A", "a"));
  }
  
  @Test(groups = "all")
  public void protect() {
    assertFalse(isEqualIgnoreCase.protect().test("A", "b"));
    assertTrue(isEqualIgnoreCase.protect().test("A", "a"));
  }

  @Test(groups = "all")
  public void and() throws Exception {
    assertTrue(isEqualIgnoreCase.and(areBothUppercase).test("A", "A"));
    assertFalse(isEqualIgnoreCase.and(areBothUppercase).test("A", "a"));
  }

  @Test(groups = "all")
  public void or() throws Exception {
    assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "A"));
    assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "a"));
    assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "B"));
  }

} /* ENDCLASS */
