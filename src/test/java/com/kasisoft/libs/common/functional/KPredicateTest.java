package com.kasisoft.libs.common.functional;

import static org.junit.jupiter.api.Assertions.*;

import com.kasisoft.libs.common.*;
import org.junit.jupiter.api.*;

import java.util.function.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class KPredicateTest {

    private KPredicate<String> isName                   = $ -> $.equalsIgnoreCase("name");

    private KPredicate<String> isNameWithError          = $ -> { throw new DummyException("error"); };

    private KPredicate<String> isUppercaseAsKPredicate  = $ -> $.equals($.toUpperCase());

    private Predicate<String>  isUppercaseAsPredicate   = $ -> $.equals($.toUpperCase());

    @Test
    public void test() throws Exception {
        assertFalse(isName.test("A"));
        assertTrue(isName.test("NAME"));
        assertThrows(DummyException.class, () -> {
            isNameWithError.test("A");
        });
    }

    @Test
    public void negate() {
        assertTrue(isName.negate().test("A"));
        assertFalse(isName.negate().test("NAME"));
        assertThrows(KclException.class, () -> {
            isNameWithError.negate().test("A");
        });
    }

    @Test
    public void protect() {
        assertFalse(isName.protect().test("A"));
        assertTrue(isName.protect().test("NAME"));
        assertThrows(KclException.class, () -> {
            isNameWithError.protect().test("A");
        });
    }

    @Test
    public void and() throws Exception {
        assertFalse(isName.and(isUppercaseAsKPredicate).test("A"));
        assertFalse(isName.and(isUppercaseAsPredicate).test("A"));
        assertFalse(isName.and(isUppercaseAsKPredicate).test("a"));
        assertFalse(isName.and(isUppercaseAsPredicate).test("a"));
        assertFalse(isName.and(isUppercaseAsKPredicate).test("name"));
        assertTrue(isName.and(isUppercaseAsPredicate).test("NAME"));
    }

    @Test
    public void or() throws Exception {
        assertTrue(isName.or(isUppercaseAsKPredicate).test("A"));
        assertTrue(isName.or(isUppercaseAsPredicate).test("A"));
        assertFalse(isName.or(isUppercaseAsKPredicate).test("a"));
        assertFalse(isName.or(isUppercaseAsPredicate).test("a"));
        assertTrue(isName.or(isUppercaseAsKPredicate).test("name"));
        assertTrue(isName.or(isUppercaseAsPredicate).test("name"));
    }

} /* ENDCLASS */
