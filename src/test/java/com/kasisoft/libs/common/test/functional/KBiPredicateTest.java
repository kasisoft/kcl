package com.kasisoft.libs.common.test.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KBiPredicateTest {

    private KBiPredicate<String, String> isEqualIgnoreCase = ($a, $b) -> $a.equalsIgnoreCase($b);

    private KBiPredicate<String, String> areBothUppercase  = ($a, $b) -> {
                                                               var strA = $a.toUpperCase();
                                                               var strB = $b.toUpperCase();
                                                               return strA.equals($a) && strB.equals($b);
                                                           };

    @Test
    public void predicate() throws Exception {
        assertFalse(isEqualIgnoreCase.test("A", "b"));
        assertTrue(isEqualIgnoreCase.test("A", "a"));
    }

    @Test
    public void negate() throws Exception {
        assertTrue(isEqualIgnoreCase.negate().test("A", "b"));
        assertFalse(isEqualIgnoreCase.negate().test("A", "a"));
    }

    @Test
    public void protect() {
        assertFalse(isEqualIgnoreCase.protect().test("A", "b"));
        assertTrue(isEqualIgnoreCase.protect().test("A", "a"));
    }

    @Test
    public void and() throws Exception {
        assertTrue(isEqualIgnoreCase.and(areBothUppercase).test("A", "A"));
        assertFalse(isEqualIgnoreCase.and(areBothUppercase).test("A", "a"));
    }

    @Test
    public void or() throws Exception {
        assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "A"));
        assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "a"));
        assertTrue(isEqualIgnoreCase.or(areBothUppercase).test("A", "B"));
    }

} /* ENDCLASS */
