package com.kasisoft.libs.common.functional;

import com.kasisoft.libs.common.DummyException;
import com.kasisoft.libs.common.KclException;
import org.junit.jupiter.api.Test;

import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KTriPredicateTest {

    private KTriPredicate<String, String, String> isEqualIgnoreCase                  = ($a, $b, $c) -> $a.equalsIgnoreCase($b);

    private KTriPredicate<String, String, String> isEqualIgnoreCaseWithError         = ($a, $b, $c) -> { throw new DummyException("error"); };

    private KTriPredicate<String, String, String> areBothUppercaseAsKBiPredicate     = ($a, $b, $c) -> {
        var strA = $a.toUpperCase();
        var strB = $b.toUpperCase();
        return strA.equals($a) && strB.equals($b);
    };

    private TriPredicate<String, String, String> areBothUppercaseAsBiPredicate       = ($a, $b, $c) -> {
        var strA = $a.toUpperCase();
        var strB = $b.toUpperCase();
        return strA.equals($a) && strB.equals($b);
    };

    @Test
    public void test() throws Exception {
        assertFalse(isEqualIgnoreCase.test("A", "b", null));
        assertTrue(isEqualIgnoreCase.test("A", "a", null));
        assertThrows(DummyException.class, () -> {
            isEqualIgnoreCaseWithError.test("A", "A", null);
        });
    }

    @Test
    public void negate() {
        assertTrue(isEqualIgnoreCase.negate().test("A", "b", null));
        assertFalse(isEqualIgnoreCase.negate().test("A", "a", null));
        assertThrows(KclException.class, () -> {
            isEqualIgnoreCaseWithError.negate().test("A", "A", null);
        });
    }

    @Test
    public void protect() {
        assertFalse(isEqualIgnoreCase.protect().test("A", "b", null));
        assertTrue(isEqualIgnoreCase.protect().test("A", "a", null));
        assertThrows(KclException.class, () -> {
            isEqualIgnoreCaseWithError.protect().test("A", "A", null);
        });
    }

    @Test
    public void and() throws Exception {
        assertTrue(isEqualIgnoreCase.and(areBothUppercaseAsKBiPredicate).test("A", "A", null));
        assertFalse(isEqualIgnoreCase.and(areBothUppercaseAsKBiPredicate).test("A", "a", null));
        assertTrue(isEqualIgnoreCase.and(areBothUppercaseAsBiPredicate).test("A", "A", null));
        assertFalse(isEqualIgnoreCase.and(areBothUppercaseAsBiPredicate).test("A", "a", null));
    }

    @Test
    public void or() throws Exception {
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsKBiPredicate).test("A", "a", null));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsKBiPredicate).test("A", "B", null));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsBiPredicate).test("A", "a", null));
        assertTrue(isEqualIgnoreCase.or(areBothUppercaseAsBiPredicate).test("A", "B", null));
    }

} /* ENDCLASS */
