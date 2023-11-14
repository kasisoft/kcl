package com.kasisoft.libs.common.test.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.functional.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KPredicateTest {

    private KPredicate<String> isName      = $ -> $.equalsIgnoreCase("name");

    private KPredicate<String> isUppercase = $ -> $.equals($.toUpperCase());

    @Test
    public void predicate() throws Exception {
        assertFalse(isName.test("A"));
        assertTrue(isName.test("NAME"));
        assertTrue(isUppercase.test("A"));
    }

    @Test
    public void negate() throws Exception {
        assertTrue(isName.negate().test("A"));
        assertFalse(isUppercase.negate().test("A"));
        assertTrue(isUppercase.negate().test("b"));
    }

    @Test
    public void protect() {
        assertFalse(isName.protect().test("A"));
        assertTrue(isName.protect().test("NAME"));
        assertTrue(isUppercase.protect().test("A"));
    }

    @Test
    public void and() throws Exception {
        assertTrue(isName.and(isUppercase).test("NAME"));
        assertFalse(isName.and(isUppercase).test("A"));
    }

    @Test
    public void or() throws Exception {
        assertTrue(isName.or(isUppercase).test("name"));
        assertTrue(isName.or(isUppercase).test("A"));
        assertFalse(isName.or(isUppercase).test("a"));
    }

} /* ENDCLASS */
