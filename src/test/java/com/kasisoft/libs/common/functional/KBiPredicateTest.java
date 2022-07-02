package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;
 import org.junit.jupiter.api.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KBiPredicateTest {

    private KBiPredicate<String, String> isEqualIgnoreCase                  = ($a, $b) -> $a.equalsIgnoreCase($b);

    private KBiPredicate<String, String> isEqualIgnoreCaseWithError         = ($a, $b) -> { throw new DummyException("error"); };

    private KBiPredicate<String, String> areBothUppercaseAsKBiPredicate     = ($a, $b) -> {
        var strA = $a.toUpperCase();
        var strB = $b.toUpperCase();
        return strA.equals($a) && strB.equals($b);
    };

    private BiPredicate<String, String> areBothUppercaseAsBiPredicate       = ($a, $b) -> {
        var strA = $a.toUpperCase();
        var strB = $b.toUpperCase();
        return strA.equals($a) && strB.equals($b);
    };

    @Test
    public void test() throws Exception {
        assertFalse(isEqualIgnoreCase.test("A", "b"));
        assertTrue(isEqualIgnoreCase.test("A", "a"));
        assertThrows(DummyException.class, () -> {
            isEqualIgnoreCaseWithError.test("A", "A");
        });
    }

    @Test
    public void negate() {
        assertTrue(isEqualIgnoreCase.negate().test("A", "b"));
        assertFalse(isEqualIgnoreCase.negate().test("A", "a"));
        assertThrows(KclException.class, () -> {
            isEqualIgnoreCaseWithError.negate().test("A", "A");
        });
    }

    @Test
    public void protect() {
        assertFalse(isEqualIgnoreCase.protect().test("A", "b"));
        assertTrue(isEqualIgnoreCase.protect().test("A", "a"));
        assertThrows(KclException.class, () -> {
            isEqualIgnoreCaseWithError.protect().test("A", "A");
        });
    }

    @Test
    public void and() throws Exception {
        assertTrue(isEqualIgnoreCase.and(areBothUppercaseAsKBiPredicate).test("A", "A"));
        assertFalse(isEqualIgnoreCase.and(areBothUppercaseAsKBiPredicate).test("A", "a"));
        assertTrue(isEqualIgnoreCase.and(areBothUppercaseAsBiPredicate).test("A", "A"));
        assertFalse(isEqualIgnoreCase.and(areBothUppercaseAsBiPredicate).test("A", "a"));
    }

    @Test
    public void or() throws Exception {
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsKBiPredicate).test("A", "a"));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsKBiPredicate).test("A", "B"));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsBiPredicate).test("A", "a"));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsBiPredicate).test("A", "B"));
    }

} /* ENDCLASS */
