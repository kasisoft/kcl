package com.kasisoft.libs.common.test.utils;

import static com.kasisoft.libs.common.test.testsupport.ExtendedAsserts.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.*;
import com.kasisoft.libs.common.utils.*;

import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Tests for the constants 'PrimitiveFunctions'.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class PrimitiveFunctionsTest {

    /* CHAR */

    @Test
    public void compare__char() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_CHARS, Empty.NO_CHARS));
        assertTrue(PrimitiveFunctions.compare(new char[] {'a'}, new char[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new char[] {'a', 'z'}, new char[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new char[] {'a', 'z'}, new char[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_CHARS, new char[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new char[] {'a'}, Empty.NO_CHARS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l'}, Empty.NO_CHARS, 3));
        assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a'}, new char[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a', 'z'}, new char[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a', 'z'}, new char[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l'}, new char[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a'}, Empty.NO_CHARS, 3));

    }

    @Test
    public void find__char() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_CHARS, 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new char[] {'a'}, 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new char[] {'a', 'z'}, 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_CHARS, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new char[] {'a'}, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new char[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

    }

    @Test
    public void indexOf__char() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_CHARS, new char[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a'}, new char[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z'}, new char[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'l', 'f'}, new char[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_CHARS, new char[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a'}, new char[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'a', 'z'}, new char[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new char[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__char() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_CHARS, new char[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'b', 'a'}, new char[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'z', 'a', 'z'}, new char[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new char[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__char() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_CHARS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new char[] {'a'}), is(Arrays.asList('a')));
        assertThat(PrimitiveFunctions.toList(new char[] {'a', 'b'}), is(Arrays.asList('a', 'b')));
        assertLists(PrimitiveFunctions.toList(new char[] {'a',
            'b'}), Arrays.asList(Character.valueOf('a'), Character.valueOf('b')));
    }

    @Test
    public void toObjectArray__char() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_CHARS), is(new Character[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a'}), is(new Character[] {'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a', 'b'}), is(new Character[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a', 'b'}), is(new Character[] {'a', 'b'}));
    }

    @Test
    public void toPrimitiveArray__char() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[0]), is(Empty.NO_CHARS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a'}), is(new char[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a', 'b'}), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a', 'b'}), is(new char[] {'a', 'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Collections.emptyList()), is(Empty.NO_CHARS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a')), is(new char[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a', 'b')), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a', 'b')), is(new char[] {'a', 'b'}));

    }

    @Test
    public void concat__char() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_CHARS), is(Empty.NO_CHARS));
        assertThat(PrimitiveFunctions.concat(new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_CHARS, new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_CHARS, new char[] {'a', 'b'}, new char[] {'c',
            'd'}), is(new char[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__char() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_CHARS, new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new char[] {'a', 'b'}, new char[] {'c', 'd'}), is(new char[] {'c', 'd',
            'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_CHARS, new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_CHARS, new char[] {'a', 'b'}, 0), is(new char[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new char[] {'a', 'b'}, new char[] {'c', 'd'}, 1), is(new char[] {'a', 'c',
            'd', 'b'}));

    }

    /* BYTE */

    @Test
    public void min__byte_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_BYTES);
        });
    }

    @Test
    public void max__byte_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_BYTES);
        });
    }

    @Test
    public void min__byte() {
        assertThat(PrimitiveFunctions.min(new byte[] {12}), is((byte) 12));
        assertThat(PrimitiveFunctions.min(new byte[] {23, 12}), is((byte) 12));
    }

    @Test
    public void max__byte() {
        assertThat(PrimitiveFunctions.max(new byte[] {12}), is((byte) 12));
        assertThat(PrimitiveFunctions.max(new byte[] {23, 12}), is((byte) 23));
    }

    @Test
    public void compare__byte() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_BYTES, Empty.NO_BYTES));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'a'}, new byte[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'a', 'z'}, new byte[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new byte[] {'a', 'z'}, new byte[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_BYTES, new byte[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'a'}, Empty.NO_BYTES));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l'}, Empty.NO_BYTES, 3));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a'}, new byte[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a', 'z'}, new byte[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a', 'z'}, new byte[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l'}, new byte[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a'}, Empty.NO_BYTES, 3));

    }

    @Test
    public void find__byte() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_BYTES, (byte) 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a'}, (byte) 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a', (byte) 'z'}, (byte) 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_BYTES, (byte) 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a'}, (byte) 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a', (byte) 'z', (byte) 'a',
            (byte) 'z'}, (byte) 'z', 2), is(3));

    }

    @Test
    public void indexOf__byte() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_BYTES, new byte[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a'}, new byte[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z'}, new byte[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'l', 'f'}, new byte[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_BYTES, new byte[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a'}, new byte[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'a', 'z'}, new byte[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new byte[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__byte() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_BYTES, new byte[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'b', 'a'}, new byte[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'z', 'a', 'z'}, new byte[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new byte[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__byte() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_BYTES), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new byte[] {(byte) 'a'}), is(Arrays.asList((byte) 'a')));
        assertThat(PrimitiveFunctions.toList(new byte[] {(byte) 'a',
            (byte) 'b'}), is(Arrays.asList((byte) 'a', (byte) 'b')));
        assertLists(PrimitiveFunctions.toList(new byte[] {(byte) 'a',
            (byte) 'b'}), Arrays.asList(Byte.valueOf((byte) 'a'), Byte.valueOf((byte) 'b')));
    }

    @Test
    public void toObjectArray__byte() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_BYTES), is(new Byte[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a'}), is(new Byte[] {'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a', 'b'}), is(new Byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a', 'b'}), is(new Byte[] {'a', 'b'}));
    }

    @Test
    public void toPrimitiveArray__byte() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[0]), is(Empty.NO_BYTES));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a'}), is(new byte[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Collections.emptyList()), is(Empty.NO_BYTES));
        assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a')), is(new byte[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a', (byte) 'b')), is(new byte[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a', (byte) 'b')), is(new byte[] {'a',
            'b'}));

    }

    @Test
    public void concat__byte() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_BYTES), is(Empty.NO_BYTES));
        assertThat(PrimitiveFunctions.concat(new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_BYTES, new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_BYTES, new byte[] {'a', 'b'}, new byte[] {'c',
            'd'}), is(new byte[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__byte() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_BYTES, new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new byte[] {'a', 'b'}, new byte[] {'c', 'd'}), is(new byte[] {'c', 'd',
            'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_BYTES, new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_BYTES, new byte[] {'a', 'b'}, 0), is(new byte[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new byte[] {'a', 'b'}, new byte[] {'c', 'd'}, 1), is(new byte[] {'a', 'c',
            'd', 'b'}));

    }

    /* SHORT */

    @Test
    public void min__short_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_SHORTS);
        });
    }

    @Test
    public void max__short_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_SHORTS);
        });
    }

    @Test
    public void min__short() {
        assertThat(PrimitiveFunctions.min(new short[] {12}), is((short) 12));
        assertThat(PrimitiveFunctions.min(new short[] {23, 12}), is((short) 12));
    }

    @Test
    public void max__short() {
        assertThat(PrimitiveFunctions.max(new short[] {12}), is((short) 12));
        assertThat(PrimitiveFunctions.max(new short[] {23, 12}), is((short) 23));
    }

    @Test
    public void compare__short() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_SHORTS, Empty.NO_SHORTS));
        assertTrue(PrimitiveFunctions.compare(new short[] {'a'}, new short[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new short[] {'a', 'z'}, new short[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new short[] {'a', 'z'}, new short[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_SHORTS, new short[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new short[] {'a'}, Empty.NO_SHORTS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l'}, Empty.NO_SHORTS, 3));
        assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a'}, new short[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a', 'z'}, new short[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a', 'z'}, new short[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l'}, new short[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a'}, Empty.NO_SHORTS, 3));

    }

    @Test
    public void find__short() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_SHORTS, (short) 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new short[] {(short) 'a'}, (short) 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new short[] {(short) 'a', (short) 'z'}, (short) 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_SHORTS, (short) 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new short[] {(short) 'a'}, (short) 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new short[] {(short) 'a', (short) 'z', (short) 'a',
            (short) 'z'}, (short) 'z', 2), is(3));

    }

    @Test
    public void indexOf__short() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_SHORTS, new short[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a'}, new short[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z'}, new short[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'l', 'f'}, new short[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_SHORTS, new short[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a'}, new short[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'a', 'z'}, new short[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new short[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__short() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_SHORTS, new short[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'b', 'a'}, new short[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'z', 'a', 'z'}, new short[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new short[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__short() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_SHORTS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new short[] {(short) 'a'}), is(Arrays.asList((short) 'a')));
        assertThat(PrimitiveFunctions.toList(new short[] {(short) 'a',
            (short) 'b'}), is(Arrays.asList((short) 'a', (short) 'b')));
        assertLists(PrimitiveFunctions.toList(new short[] {(short) 'a',
            (short) 'b'}), Arrays.asList(Short.valueOf((short) 'a'), Short.valueOf((short) 'b')));
    }

    @Test
    public void toObjectArray__short() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_SHORTS), is(new Short[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a'}), is(new Short[] {'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a', 'b'}), is(new Short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a', 'b'}), is(new Short[] {'a', 'b'}));
    }

    @Test
    public void toPrimitiveArray__short() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[0]), is(Empty.NO_SHORTS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a'}), is(new short[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a', 'b'}), is(new short[] {'a', 'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Collections.emptyList()), is(Empty.NO_SHORTS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a')), is(new short[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a', (short) 'b')), is(new short[] {
            'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a', (short) 'b')), is(new short[] {
            'a', 'b'}));

    }

    @Test
    public void concat__short() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_SHORTS), is(Empty.NO_SHORTS));
        assertThat(PrimitiveFunctions.concat(new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_SHORTS, new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_SHORTS, new short[] {'a', 'b'}, new short[] {'c',
            'd'}), is(new short[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__short() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_SHORTS, new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new short[] {'a', 'b'}, new short[] {'c', 'd'}), is(new short[] {'c', 'd',
            'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_SHORTS, new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_SHORTS, new short[] {'a', 'b'}, 0), is(new short[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new short[] {'a', 'b'}, new short[] {'c', 'd'}, 1), is(new short[] {'a',
            'c', 'd', 'b'}));

    }

    /* INTEGER */

    @Test
    public void min__int_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_INTS);
        });
    }

    @Test
    public void max__int_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_INTS);
        });
    }

    @Test
    public void min__int() {
        assertThat(PrimitiveFunctions.min(new int[] {12}), is(12));
        assertThat(PrimitiveFunctions.min(new int[] {23, 12}), is(12));
    }

    @Test
    public void max__int() {
        assertThat(PrimitiveFunctions.max(new int[] {12}), is(12));
        assertThat(PrimitiveFunctions.max(new int[] {23, 12}), is(23));
    }

    @Test
    public void compare__int() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_INTS, Empty.NO_INTS));
        assertTrue(PrimitiveFunctions.compare(new int[] {'a'}, new int[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new int[] {'a', 'z'}, new int[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new int[] {'a', 'z'}, new int[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_INTS, new int[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new int[] {'a'}, Empty.NO_INTS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l'}, Empty.NO_INTS, 3));
        assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a'}, new int[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a', 'z'}, new int[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a', 'z'}, new int[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l'}, new int[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a'}, Empty.NO_INTS, 3));

    }

    @Test
    public void find__int() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_INTS, 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new int[] {'a'}, 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new int[] {'a', 'z'}, 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_INTS, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new int[] {'a'}, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new int[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

    }

    @Test
    public void indexOf__int() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_INTS, new int[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a'}, new int[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z'}, new int[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'l', 'f'}, new int[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_INTS, new int[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a'}, new int[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'a', 'z'}, new int[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new int[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__int() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_INTS, new int[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'b', 'a'}, new int[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'z', 'a', 'z'}, new int[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new int[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__int() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_INTS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new int[] {'a'}), is(Arrays.asList((int) 'a')));
        assertThat(PrimitiveFunctions.toList(new int[] {'a', 'b'}), is(Arrays.asList((int) 'a', (int) 'b')));
        assertLists(PrimitiveFunctions.toList(new int[] {'a',
            'b'}), Arrays.asList(Integer.valueOf('a'), Integer.valueOf('b')));
    }

    @Test
    public void toObjectArray__int() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_INTS), is(new Integer[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a'}), is(new Integer[] {(int) 'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a', 'b'}), is(new Integer[] {(int) 'a', (int) 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a', 'b'}), is(new Integer[] {(int) 'a', (int) 'b'}));
    }

    @Test
    public void toPrimitiveArray__int() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[0]), is(Empty.NO_INTS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a'}), is(new int[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a', (int) 'b'}), is(new int[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a', (int) 'b'}), is(new int[] {'a', 'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Collections.emptyList()), is(Empty.NO_INTS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a')), is(new int[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a', (int) 'b')), is(new int[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a', (int) 'b')), is(new int[] {'a',
            'b'}));

    }

    @Test
    public void concat__int() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_INTS), is(new int[0]));
        assertThat(PrimitiveFunctions.concat(new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_INTS, new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_INTS, new int[] {'a', 'b'}, new int[] {'c', 'd'}), is(new int[] {
            'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__int() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_INTS, new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new int[] {'a', 'b'}, new int[] {'c', 'd'}), is(new int[] {'c', 'd', 'a',
            'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_INTS, new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_INTS, new int[] {'a', 'b'}, 0), is(new int[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new int[] {'a', 'b'}, new int[] {'c', 'd'}, 1), is(new int[] {'a', 'c',
            'd', 'b'}));

    }

    /* LONG */

    @Test
    public void min__long_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_LONGS);
        });
    }

    @Test
    public void max__long_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_LONGS);
        });
    }

    @Test
    public void min__long() {
        assertThat(PrimitiveFunctions.min(new long[] {12}), is(12L));
        assertThat(PrimitiveFunctions.min(new long[] {23, 12}), is(12L));
    }

    @Test
    public void max__long() {
        assertThat(PrimitiveFunctions.max(new long[] {12}), is(12L));
        assertThat(PrimitiveFunctions.max(new long[] {23, 12}), is(23L));
    }

    @Test
    public void compare__long() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_LONGS, Empty.NO_LONGS));
        assertTrue(PrimitiveFunctions.compare(new long[] {'a'}, new long[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new long[] {'a', 'z'}, new long[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new long[] {'a', 'z'}, new long[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_LONGS, new long[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new long[] {'a'}, Empty.NO_LONGS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l'}, Empty.NO_LONGS, 3));
        assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a'}, new long[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a', 'z'}, new long[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a', 'z'}, new long[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l'}, new long[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a'}, Empty.NO_LONGS, 3));

    }

    @Test
    public void find__long() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_LONGS, 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new long[] {'a'}, 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new long[] {'a', 'z'}, 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_LONGS, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new long[] {'a'}, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new long[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

    }

    @Test
    public void indexOf__long() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_LONGS, new long[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a'}, new long[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z'}, new long[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'l', 'f'}, new long[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_LONGS, new long[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a'}, new long[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'a', 'z'}, new long[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new long[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__long() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_LONGS, new long[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'b', 'a'}, new long[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'z', 'a', 'z'}, new long[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new long[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__long() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_LONGS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new long[] {'a'}), is(Arrays.asList((long) 'a')));
        assertThat(PrimitiveFunctions.toList(new long[] {'a', 'b'}), is(Arrays.asList((long) 'a', (long) 'b')));
        assertLists(PrimitiveFunctions.toList(new long[] {'a',
            'b'}), Arrays.asList(Long.valueOf('a'), Long.valueOf('b')));
    }

    @Test
    public void toObjectArray__long() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_LONGS), is(new Long[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a'}), is(new Long[] {(long) 'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a', 'b'}), is(new Long[] {(long) 'a', (long) 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a', 'b'}), is(new Long[] {(long) 'a', (long) 'b'}));
    }

    @Test
    public void toPrimitiveArray__long() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[0]), is(Empty.NO_LONGS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a'}), is(new long[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a', (long) 'b'}), is(new long[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a', (long) 'b'}), is(new long[] {'a', 'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Collections.emptyList()), is(Empty.NO_LONGS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a')), is(new long[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a', (long) 'b')), is(new long[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a', (long) 'b')), is(new long[] {'a',
            'b'}));

    }

    @Test
    public void concat__long() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_LONGS), is(Empty.NO_LONGS));
        assertThat(PrimitiveFunctions.concat(new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_LONGS, new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_LONGS, new long[] {'a', 'b'}, new long[] {'c',
            'd'}), is(new long[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__long() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_LONGS, new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new long[] {'a', 'b'}, new long[] {'c', 'd'}), is(new long[] {'c', 'd',
            'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_LONGS, new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_LONGS, new long[] {'a', 'b'}, 0), is(new long[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new long[] {'a', 'b'}, new long[] {'c', 'd'}, 1), is(new long[] {'a', 'c',
            'd', 'b'}));

    }

    /* FLOAT */

    @Test
    public void min__float_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_FLOATS);
        });
    }

    @Test
    public void max__float_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_FLOATS);
        });
    }

    @Test
    public void min__float() {
        assertThat(PrimitiveFunctions.min(new float[] {12}), is((float) 12));
        assertThat(PrimitiveFunctions.min(new float[] {23, 12}), is((float) 12));
    }

    @Test
    public void max__float() {
        assertThat(PrimitiveFunctions.max(new float[] {12}), is((float) 12));
        assertThat(PrimitiveFunctions.max(new float[] {23, 12}), is((float) 23));
    }

    @Test
    public void compare__float() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_FLOATS, Empty.NO_FLOATS));
        assertTrue(PrimitiveFunctions.compare(new float[] {'a'}, new float[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new float[] {'a', 'z'}, new float[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new float[] {'a', 'z'}, new float[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_FLOATS, new float[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new float[] {'a'}, Empty.NO_FLOATS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l'}, Empty.NO_FLOATS, 3));
        assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a'}, new float[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a', 'z'}, new float[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a', 'z'}, new float[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l'}, new float[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a'}, Empty.NO_FLOATS, 3));

    }

    @Test
    public void find__float() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_FLOATS, 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new float[] {'a'}, 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new float[] {'a', 'z'}, 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_FLOATS, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new float[] {'a'}, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new float[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

    }

    @Test
    public void indexOf__float() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_FLOATS, new float[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a'}, new float[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z'}, new float[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'l', 'f'}, new float[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_FLOATS, new float[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a'}, new float[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'a', 'z'}, new float[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new float[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__float() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_FLOATS, new float[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'b', 'a'}, new float[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'z', 'a', 'z'}, new float[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new float[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__float() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_FLOATS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new float[] {'a'}), is(Arrays.asList((float) 'a')));
        assertThat(PrimitiveFunctions.toList(new float[] {'a', 'b'}), is(Arrays.asList((float) 'a', (float) 'b')));
        assertLists(PrimitiveFunctions.toList(new float[] {'a',
            'b'}), Arrays.asList(Float.valueOf('a'), Float.valueOf('b')));
    }

    @Test
    public void toObjectArray__float() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_FLOATS), is(new Float[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a'}), is(new Float[] {(float) 'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a', 'b'}), is(new Float[] {(float) 'a',
            (float) 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a', 'b'}), is(new Float[] {(float) 'a',
            (float) 'b'}));
    }

    @Test
    public void toPrimitiveArray__float() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[0]), is(Empty.NO_FLOATS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a'}), is(new float[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a', (float) 'b'}), is(new float[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a', (float) 'b'}), is(new float[] {'a',
            'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Collections.emptyList()), is(Empty.NO_FLOATS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a')), is(new float[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a', (float) 'b')), is(new float[] {
            'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a', (float) 'b')), is(new float[] {
            'a', 'b'}));

    }

    @Test
    public void concat__float() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_FLOATS), is(Empty.NO_FLOATS));
        assertThat(PrimitiveFunctions.concat(new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_FLOATS, new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_FLOATS, new float[] {'a', 'b'}, new float[] {'c',
            'd'}), is(new float[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__float() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_FLOATS, new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new float[] {'a', 'b'}, new float[] {'c', 'd'}), is(new float[] {'c', 'd',
            'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_FLOATS, new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_FLOATS, new float[] {'a', 'b'}, 0), is(new float[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new float[] {'a', 'b'}, new float[] {'c', 'd'}, 1), is(new float[] {'a',
            'c', 'd', 'b'}));

    }

    /* DOUBLE */

    @Test
    public void min__double_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.min(Empty.NO_DOUBLES);
        });
    }

    @Test
    public void max__double_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.max(Empty.NO_DOUBLES);
        });
    }

    @Test
    public void min__double() {
        assertThat(PrimitiveFunctions.min(new double[] {12}), is((double) 12));
        assertThat(PrimitiveFunctions.min(new double[] {23, 12}), is((double) 12));
    }

    @Test
    public void max__double() {
        assertThat(PrimitiveFunctions.max(new double[] {12}), is((double) 12));
        assertThat(PrimitiveFunctions.max(new double[] {23, 12}), is((double) 23));
    }

    @Test
    public void compare__double() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_DOUBLES, Empty.NO_DOUBLES));
        assertTrue(PrimitiveFunctions.compare(new double[] {'a'}, new double[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new double[] {'a', 'z'}, new double[] {'a', 'z'}));
        assertFalse(PrimitiveFunctions.compare(new double[] {'a', 'z'}, new double[] {'a', 'y'}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_DOUBLES, new double[] {'a'}));
        assertTrue(PrimitiveFunctions.compare(new double[] {'a'}, Empty.NO_DOUBLES));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l'}, Empty.NO_DOUBLES, 3));
        assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a'}, new double[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a', 'z'}, new double[] {'a', 'z'}, 3));
        assertFalse(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a', 'z'}, new double[] {'a', 'y'}, 3));
        assertFalse(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l'}, new double[] {'a'}, 3));
        assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a'}, Empty.NO_DOUBLES, 3));

    }

    @Test
    public void find__double() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_DOUBLES, 'a'), is(-1));
        assertThat(PrimitiveFunctions.find(new double[] {'a'}, 'a'), is(0));
        assertThat(PrimitiveFunctions.find(new double[] {'a', 'z'}, 'z'), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_DOUBLES, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new double[] {'a'}, 'a', 2), is(-1));
        assertThat(PrimitiveFunctions.find(new double[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

    }

    @Test
    public void indexOf__double() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_DOUBLES, new double[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a'}, new double[] {'a'}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z'}, new double[] {'z'}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'l', 'f'}, new double[] {'z', 'l'}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_DOUBLES, new double[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a'}, new double[] {'a'}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'a', 'z'}, new double[] {'z'}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new double[] {'z',
            'l'}, 3), is(4));

    }

    @Test
    public void lastIndexOf__double() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_DOUBLES, new double[] {'a'}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'b', 'a'}, new double[] {'a'}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'z', 'a', 'z'}, new double[] {'z'}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new double[] {'z',
            'l'}), is(5));

    }

    @Test
    public void toList__double() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_DOUBLES), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new double[] {'a'}), is(Arrays.asList((double) 'a')));
        assertThat(PrimitiveFunctions.toList(new double[] {'a', 'b'}), is(Arrays.asList((double) 'a', (double) 'b')));
        assertLists(PrimitiveFunctions.toList(new double[] {'a',
            'b'}), Arrays.asList(Double.valueOf('a'), Double.valueOf('b')));
    }

    @Test
    public void toObjectArray__double() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_DOUBLES), is(new Double[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a'}), is(new Double[] {(double) 'a'}));
        assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a', 'b'}), is(new Double[] {(double) 'a',
            (double) 'b'}));
        assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a', 'b'}), is(new Double[] {(double) 'a',
            (double) 'b'}));
    }

    @Test
    public void toPrimitiveArray__double() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[0]), is(Empty.NO_DOUBLES));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a'}), is(new double[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a', (double) 'b'}), is(new double[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a', (double) 'b'}), is(new double[] {'a',
            'b'}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Collections.emptyList()), is(Empty.NO_DOUBLES));
        assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a')), is(new double[] {'a'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a', (double) 'b')), is(new double[] {
            'a', 'b'}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a', (double) 'b')), is(new double[] {
            'a', 'b'}));

    }

    @Test
    public void concat__double() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_DOUBLES), is(Empty.NO_DOUBLES));
        assertThat(PrimitiveFunctions.concat(new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_DOUBLES, new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_DOUBLES, new double[] {'a', 'b'}, new double[] {'c',
            'd'}), is(new double[] {'a', 'b', 'c', 'd'}));

    }

    @Test
    public void insert__double() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_DOUBLES, new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(new double[] {'a', 'b'}, new double[] {'c', 'd'}), is(new double[] {'c',
            'd', 'a', 'b'}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_DOUBLES, new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_DOUBLES, new double[] {'a', 'b'}, 0), is(new double[] {'a',
            'b'}));
        assertThat(PrimitiveFunctions.insert(new double[] {'a', 'b'}, new double[] {'c', 'd'}, 1), is(new double[] {'a',
            'c', 'd', 'b'}));

    }

    /* BOOLEAN */

    @Test
    public void or__boolean_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.or(Empty.NO_BOOLEANS);
        });
    }

    @Test
    public void and__boolean_error() {
        assertThrows(KclException.class, () -> {
            PrimitiveFunctions.and(Empty.NO_BOOLEANS);
        });
    }

    @Test
    public void or__boolean() {
        assertTrue(PrimitiveFunctions.or(new boolean[] {true}));
        assertTrue(PrimitiveFunctions.or(new boolean[] {false, true}));
        assertFalse(PrimitiveFunctions.or(new boolean[] {false, false, false}));
    }

    @Test
    public void and_boolean() {
        assertTrue(PrimitiveFunctions.and(new boolean[] {true}));
        assertFalse(PrimitiveFunctions.and(new boolean[] {false, true}));
        assertTrue(PrimitiveFunctions.and(new boolean[] {true, true, true}));
    }

    @Test
    public void compare__boolean() {

        // compare(char[], char[])
        assertTrue(PrimitiveFunctions.compare(Empty.NO_BOOLEANS, Empty.NO_BOOLEANS));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {false}, new boolean[] {false}));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {false, true}, new boolean[] {false, true}));
        assertFalse(PrimitiveFunctions.compare(new boolean[] {false, true}, new boolean[] {false, false}));
        assertFalse(PrimitiveFunctions.compare(Empty.NO_BOOLEANS, new boolean[] {false}));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {false}, Empty.NO_BOOLEANS));

        // compare(char[], char[], int)
        assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true}, Empty.NO_BOOLEANS, 3));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false}, new boolean[] {false}, 3));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false, true}, new boolean[] {false,
            true}, 3));
        assertFalse(PrimitiveFunctions.compare(new boolean[] {true, true, true, false, true}, new boolean[] {false,
            false}, 3));
        assertFalse(PrimitiveFunctions.compare(new boolean[] {true, true, true}, new boolean[] {false}, 3));
        assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false}, Empty.NO_BOOLEANS, 3));

    }

    @Test
    public void find__boolean() {

        // find(char[], char)
        assertThat(PrimitiveFunctions.find(Empty.NO_BOOLEANS, false), is(-1));
        assertThat(PrimitiveFunctions.find(new boolean[] {false}, false), is(0));
        assertThat(PrimitiveFunctions.find(new boolean[] {false, true}, true), is(1));

        // find(char[], char, int)
        assertThat(PrimitiveFunctions.find(Empty.NO_BOOLEANS, false, 2), is(-1));
        assertThat(PrimitiveFunctions.find(new boolean[] {false}, false, 2), is(-1));
        assertThat(PrimitiveFunctions.find(new boolean[] {false, true, false, true}, true, 2), is(3));

    }

    @Test
    public void indexOf__boolean() {

        // indexOf(char[], char)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_BOOLEANS, new boolean[] {false}), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false}, new boolean[] {false}), is(0));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true}, new boolean[] {true}), is(1));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, true, true}, new boolean[] {true,
            true}), is(1));

        // indexOf(char[], char, int)
        assertThat(PrimitiveFunctions.indexOf(Empty.NO_BOOLEANS, new boolean[] {false}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false}, new boolean[] {false}, 2), is(-1));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, false, true}, new boolean[] {
            true}, 2), is(3));
        assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, true, true, true, true, true}, new boolean[] {
            true, true}, 3), is(3));

    }

    @Test
    public void lastIndexOf__boolean() {

        // lastIndexOf(char[], char)
        assertThat(PrimitiveFunctions.lastIndexOf(Empty.NO_BOOLEANS, new boolean[] {false}), is(-1));
        assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, false}, new boolean[] {false}), is(2));
        assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, false, true}, new boolean[] {
            true}), is(3));
        assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, true, true, false, true,
            true}, new boolean[] {true, true}), is(5));

    }

    @Test
    public void toList__boolean() {
        assertThat(PrimitiveFunctions.toList(Empty.NO_BOOLEANS), is(Collections.emptyList()));
        assertThat(PrimitiveFunctions.toList(new boolean[] {false}), is(Arrays.asList(false)));
        assertThat(PrimitiveFunctions.toList(new boolean[] {false, true}), is(Arrays.asList(false, true)));
        assertLists(PrimitiveFunctions.toList(new boolean[] {false, true}), Arrays.asList(Boolean.FALSE, Boolean.TRUE));
    }

    @Test
    public void toObjectArray__boolean() {
        assertThat(PrimitiveFunctions.toObjectArray(Empty.NO_BOOLEANS), is(new Boolean[0]));
        assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false}), is(new Boolean[] {false}));
        assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false, true}), is(new Boolean[] {false,
            Boolean.TRUE}));
        assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false, true}), is(new Boolean[] {false,
            Boolean.TRUE}));
    }

    @Test
    public void toPrimitiveArray__boolean() {

        assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[0]), is(Empty.NO_BOOLEANS));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false}), is(new boolean[] {false}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false, true}), is(new boolean[] {false, true}));
        assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false, true}), is(new boolean[] {false, true}));

        assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Collections.emptyList()), is(Empty.NO_BOOLEANS));
        assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false)), is(new boolean[] {false}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false, true)), is(new boolean[] {false,
            true}));
        assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false, true)), is(new boolean[] {false,
            true}));

    }

    @Test
    public void concat__boolean() {

        assertThat(PrimitiveFunctions.concat(Empty.NO_BOOLEANS), is(Empty.NO_BOOLEANS));
        assertThat(PrimitiveFunctions.concat(new boolean[] {false, true}), is(new boolean[] {false, true}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_BOOLEANS, new boolean[] {false, true}), is(new boolean[] {false,
            true}));
        assertThat(PrimitiveFunctions.concat(Empty.NO_BOOLEANS, new boolean[] {false, true}, new boolean[] {true,
            true}), is(new boolean[] {false, true, true, true}));

    }

    @Test
    public void insert__boolean() {

        // insert(char[], char[])
        assertThat(PrimitiveFunctions.insert(Empty.NO_BOOLEANS, new boolean[] {false, true}), is(new boolean[] {false,
            true}));
        assertThat(PrimitiveFunctions.insert(new boolean[] {false, true}, new boolean[] {true,
            true}), is(new boolean[] {true, true, false, true}));
        assertThat(PrimitiveFunctions.insert(Empty.NO_BOOLEANS, new boolean[] {false, true}), is(new boolean[] {false,
            true}));

        // insert(char[], char[], int)
        assertThat(PrimitiveFunctions.insert(Empty.NO_BOOLEANS, new boolean[] {false, true}, 0), is(new boolean[] {
            false, true}));
        assertThat(PrimitiveFunctions.insert(new boolean[] {false, true}, new boolean[] {true,
            true}, 1), is(new boolean[] {false, true, true, true}));

    }

} /* ENDCLASS */
