package com.kasisoft.libs.common.utils;

import static com.kasisoft.libs.common.testsupport.ExtendedAsserts.assertLists;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

/**
 * Tests for the constants 'PrimitiveFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PrimitiveFunctionsTest {
  
  /* CHAR */

  @Test
  public void compare__char() {
    
    // compare(char[], char[])
    assertTrue(PrimitiveFunctions.compare(new char[0], new char[0]));
    assertTrue(PrimitiveFunctions.compare(new char[] {'a'}, new char[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new char[] {'a', 'z'}, new char[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new char[0], new char[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new char[] {'a'}, new char[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l'}, new char[0], 3));
    assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a'}, new char[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a', 'z'}, new char[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l'}, new char[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new char[] {'l', 'l', 'l', 'a'}, new char[0], 3));
    
  }

  @Test
  public void find__char() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new char[0], 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new char[] {'a'}, 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new char[] {'a', 'z'}, 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new char[0], 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new char[] {'a'}, 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new char[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

  }

  @Test
  public void indexOf__char() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new char[0], new char[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a'}, new char[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z'}, new char[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'l', 'f'}, new char[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new char[0], new char[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a'}, new char[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'a', 'z'}, new char[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new char[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new char[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__char() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new char[0], new char[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'b', 'a'}, new char[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'z', 'a', 'z'}, new char[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new char[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new char[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__char() {
    assertThat(PrimitiveFunctions.toList(new char[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new char[] {'a'}), is(Arrays.asList('a')));
    assertThat(PrimitiveFunctions.toList(new char[] {'a', 'b'}), is(Arrays.asList('a', 'b')));
    assertLists(PrimitiveFunctions.toList(new char[] {'a', 'b'}), Arrays.asList(Character.valueOf('a'), Character.valueOf('b')));
  }

  @Test
  public void toObjectArray__char() {
    assertThat(PrimitiveFunctions.toObjectArray(new char[0]), is(new Character[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a'}), is(new Character[] {'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a', 'b'}), is(new Character[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new char[] {'a', 'b'}), is(new Character[] {'a', 'b'}));
  }

  @Test
  public void toPrimitiveArray__char() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[0]), is(new char[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a'}), is(new char[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a', 'b'}), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Character[] {'a', 'b'}), is(new char[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Collections.emptyList()), is(new char[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a')), is(new char[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a', 'b')), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayCharacter(Arrays.asList('a', 'b')), is(new char[] {'a', 'b'}));

  }

  @Test
  public void concat__char() {
    
    assertThat(PrimitiveFunctions.concat(new char[0]), is(new char[0]));
    assertThat(PrimitiveFunctions.concat(new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new char[0], new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new char[0], new char[] {'a', 'b'}, new char[] {'c', 'd'}), is(new char[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__char() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new char[0], new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new char[] {'a', 'b'}, new char[] {'c', 'd'}), is(new char[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new char[0], new char[] {'a', 'b'}), is(new char[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new char[0], new char[] {'a', 'b'}, 0), is(new char[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new char[] {'a', 'b'}, new char[] {'c', 'd'}, 1), is(new char[] {'a', 'c', 'd', 'b'}));

  }

  /* BYTE */
  
  @Test(expectedExceptions = KclException.class)
  public void min__byte_error() {
    PrimitiveFunctions.min(new byte[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__byte_error() {
    PrimitiveFunctions.max(new byte[0]);
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
    assertTrue(PrimitiveFunctions.compare(new byte[0], new byte[0]));
    assertTrue(PrimitiveFunctions.compare(new byte[] {'a'}, new byte[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new byte[] {'a', 'z'}, new byte[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new byte[0], new byte[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new byte[] {'a'}, new byte[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l'}, new byte[0], 3));
    assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a'}, new byte[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a', 'z'}, new byte[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l'}, new byte[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new byte[] {'l', 'l', 'l', 'a'}, new byte[0], 3));
    
  }

  @Test
  public void find__byte() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new byte[0], (byte) 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a'}, (byte) 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a', (byte) 'z'}, (byte) 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new byte[0], (byte) 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a'}, (byte) 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new byte[] {(byte) 'a', (byte) 'z', (byte) 'a', (byte) 'z'}, (byte) 'z', 2), is(3));

  }

  @Test
  public void indexOf__byte() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new byte[0], new byte[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a'}, new byte[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z'}, new byte[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'l', 'f'}, new byte[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new byte[0], new byte[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a'}, new byte[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'a', 'z'}, new byte[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new byte[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new byte[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__byte() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new byte[0], new byte[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'b', 'a'}, new byte[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'z', 'a', 'z'}, new byte[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new byte[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new byte[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__byte() {
    assertThat(PrimitiveFunctions.toList(new byte[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new byte[] {(byte) 'a'}), is(Arrays.asList((byte) 'a')));
    assertThat(PrimitiveFunctions.toList(new byte[] {(byte) 'a', (byte) 'b'}), is(Arrays.asList((byte) 'a', (byte) 'b')));
    assertLists(PrimitiveFunctions.toList(new byte[] {(byte) 'a', (byte) 'b'}), Arrays.asList(Byte.valueOf((byte) 'a'), Byte.valueOf((byte) 'b')));
  }

  @Test
  public void toObjectArray__byte() {
    assertThat(PrimitiveFunctions.toObjectArray(new byte[0]), is(new Byte[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a'}), is(new Byte[] {'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a', 'b'}), is(new Byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new byte[] {'a', 'b'}), is(new Byte[] {'a', 'b'}));
  }

  @Test
  public void toPrimitiveArray__byte() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[0]), is(new byte[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a'}), is(new byte[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Collections.emptyList()), is(new byte[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a')), is(new byte[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a', (byte) 'b')), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayByte(Arrays.asList((byte) 'a', (byte) 'b')), is(new byte[] {'a', 'b'}));

  }

  @Test
  public void concat__byte() {
    
    assertThat(PrimitiveFunctions.concat(new byte[0]), is(new byte[0]));
    assertThat(PrimitiveFunctions.concat(new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new byte[0], new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new byte[0], new byte[] {'a', 'b'}, new byte[] {'c', 'd'}), is(new byte[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__byte() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new byte[0], new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new byte[] {'a', 'b'}, new byte[] {'c', 'd'}), is(new byte[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new byte[0], new byte[] {'a', 'b'}), is(new byte[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new byte[0], new byte[] {'a', 'b'}, 0), is(new byte[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new byte[] {'a', 'b'}, new byte[] {'c', 'd'}, 1), is(new byte[] {'a', 'c', 'd', 'b'}));

  }

  /* SHORT */
  
  @Test(expectedExceptions = KclException.class)
  public void min__short_error() {
    PrimitiveFunctions.min(new short[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__short_error() {
    PrimitiveFunctions.max(new short[0]);
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
    assertTrue(PrimitiveFunctions.compare(new short[0], new short[0]));
    assertTrue(PrimitiveFunctions.compare(new short[] {'a'}, new short[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new short[] {'a', 'z'}, new short[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new short[0], new short[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new short[] {'a'}, new short[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l'}, new short[0], 3));
    assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a'}, new short[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a', 'z'}, new short[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l'}, new short[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new short[] {'l', 'l', 'l', 'a'}, new short[0], 3));
    
  }

  @Test
  public void find__short() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new short[0], (short) 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new short[] {(short) 'a'}, (short) 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new short[] {(short) 'a', (short) 'z'}, (short) 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new short[0], (short) 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new short[] {(short) 'a'}, (short) 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new short[] {(short) 'a', (short) 'z', (short) 'a', (short) 'z'}, (short) 'z', 2), is(3));

  }

  @Test
  public void indexOf__short() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new short[0], new short[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a'}, new short[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z'}, new short[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'l', 'f'}, new short[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new short[0], new short[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a'}, new short[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'a', 'z'}, new short[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new short[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new short[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__short() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new short[0], new short[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'b', 'a'}, new short[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'z', 'a', 'z'}, new short[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new short[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new short[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__short() {
    assertThat(PrimitiveFunctions.toList(new short[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new short[] {(short) 'a'}), is(Arrays.asList((short) 'a')));
    assertThat(PrimitiveFunctions.toList(new short[] {(short) 'a', (short) 'b'}), is(Arrays.asList((short) 'a', (short) 'b')));
    assertLists(PrimitiveFunctions.toList(new short[] {(short) 'a', (short) 'b'}), Arrays.asList(Short.valueOf((short) 'a'), Short.valueOf((short) 'b')));
  }

  @Test
  public void toObjectArray__short() {
    assertThat(PrimitiveFunctions.toObjectArray(new short[0]), is(new Short[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a'}), is(new Short[] {'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a', 'b'}), is(new Short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new short[] {'a', 'b'}), is(new Short[] {'a', 'b'}));
  }

  @Test
  public void toPrimitiveArray__short() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[0]), is(new short[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a'}), is(new short[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Short[] {'a', 'b'}), is(new short[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Collections.emptyList()), is(new short[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a')), is(new short[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a', (short) 'b')), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayShort(Arrays.asList((short) 'a', (short) 'b')), is(new short[] {'a', 'b'}));

  }

  @Test
  public void concat__short() {
    
    assertThat(PrimitiveFunctions.concat(new short[0]), is(new short[0]));
    assertThat(PrimitiveFunctions.concat(new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new short[0], new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new short[0], new short[] {'a', 'b'}, new short[] {'c', 'd'}), is(new short[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__short() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new short[0], new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new short[] {'a', 'b'}, new short[] {'c', 'd'}), is(new short[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new short[0], new short[] {'a', 'b'}), is(new short[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new short[0], new short[] {'a', 'b'}, 0), is(new short[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new short[] {'a', 'b'}, new short[] {'c', 'd'}, 1), is(new short[] {'a', 'c', 'd', 'b'}));

  }

  /* INTEGER */
  
  @Test(expectedExceptions = KclException.class)
  public void min__int_error() {
    PrimitiveFunctions.min(new int[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__int_error() {
    PrimitiveFunctions.max(new int[0]);
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
    assertTrue(PrimitiveFunctions.compare(new int[0], new int[0]));
    assertTrue(PrimitiveFunctions.compare(new int[] {'a'}, new int[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new int[] {'a', 'z'}, new int[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new int[0], new int[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new int[] {'a'}, new int[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l'}, new int[0], 3));
    assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a'}, new int[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a', 'z'}, new int[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l'}, new int[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new int[] {'l', 'l', 'l', 'a'}, new int[0], 3));
    
  }

  @Test
  public void find__int() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new int[0], 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new int[] {'a'}, 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new int[] {'a', 'z'}, 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new int[0], 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new int[] {'a'}, 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new int[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

  }

  @Test
  public void indexOf__int() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new int[0], new int[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a'}, new int[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z'}, new int[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'l', 'f'}, new int[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new int[0], new int[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a'}, new int[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'a', 'z'}, new int[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new int[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new int[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__int() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new int[0], new int[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'b', 'a'}, new int[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'z', 'a', 'z'}, new int[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new int[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new int[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__int() {
    assertThat(PrimitiveFunctions.toList(new int[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new int[] {'a'}), is(Arrays.asList((int) 'a')));
    assertThat(PrimitiveFunctions.toList(new int[] {'a', 'b'}), is(Arrays.asList((int) 'a', (int) 'b')));
    assertLists(PrimitiveFunctions.toList(new int[] {'a', 'b'}), Arrays.asList(Integer.valueOf('a'), Integer.valueOf('b')));
  }

  @Test
  public void toObjectArray__int() {
    assertThat(PrimitiveFunctions.toObjectArray(new int[0]), is(new Integer[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a'}), is(new Integer[] {(int) 'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a', 'b'}), is(new Integer[] {(int) 'a', (int) 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new int[] {'a', 'b'}), is(new Integer[] {(int) 'a', (int) 'b'}));
  }

  @Test
  public void toPrimitiveArray__int() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[0]), is(new int[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a'}), is(new int[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a', (int) 'b'}), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Integer[] {(int) 'a', (int) 'b'}), is(new int[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Collections.emptyList()), is(new int[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a')), is(new int[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a', (int) 'b')), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayInteger(Arrays.asList((int) 'a', (int) 'b')), is(new int[] {'a', 'b'}));

  }

  @Test
  public void concat__int() {
    
    assertThat(PrimitiveFunctions.concat(new int[0]), is(new int[0]));
    assertThat(PrimitiveFunctions.concat(new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new int[0], new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new int[0], new int[] {'a', 'b'}, new int[] {'c', 'd'}), is(new int[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__int() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new int[0], new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new int[] {'a', 'b'}, new int[] {'c', 'd'}), is(new int[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new int[0], new int[] {'a', 'b'}), is(new int[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new int[0], new int[] {'a', 'b'}, 0), is(new int[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new int[] {'a', 'b'}, new int[] {'c', 'd'}, 1), is(new int[] {'a', 'c', 'd', 'b'}));

  }

  /* LONG */
  
  @Test(expectedExceptions = KclException.class)
  public void min__long_error() {
    PrimitiveFunctions.min(new long[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__long_error() {
    PrimitiveFunctions.max(new long[0]);
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
    assertTrue(PrimitiveFunctions.compare(new long[0], new long[0]));
    assertTrue(PrimitiveFunctions.compare(new long[] {'a'}, new long[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new long[] {'a', 'z'}, new long[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new long[0], new long[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new long[] {'a'}, new long[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l'}, new long[0], 3));
    assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a'}, new long[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a', 'z'}, new long[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l'}, new long[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new long[] {'l', 'l', 'l', 'a'}, new long[0], 3));
    
  }

  @Test
  public void find__long() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new long[0], 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new long[] {'a'}, 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new long[] {'a', 'z'}, 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new long[0], 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new long[] {'a'}, 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new long[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

  }

  @Test
  public void indexOf__long() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new long[0], new long[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a'}, new long[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z'}, new long[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'l', 'f'}, new long[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new long[0], new long[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a'}, new long[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'a', 'z'}, new long[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new long[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new long[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__long() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new long[0], new long[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'b', 'a'}, new long[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'z', 'a', 'z'}, new long[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new long[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new long[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__long() {
    assertThat(PrimitiveFunctions.toList(new long[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new long[] {'a'}), is(Arrays.asList((long) 'a')));
    assertThat(PrimitiveFunctions.toList(new long[] {'a', 'b'}), is(Arrays.asList((long) 'a', (long) 'b')));
    assertLists(PrimitiveFunctions.toList(new long[] {'a', 'b'}), Arrays.asList(Long.valueOf('a'), Long.valueOf('b')));
  }

  @Test
  public void toObjectArray__long() {
    assertThat(PrimitiveFunctions.toObjectArray(new long[0]), is(new Long[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a'}), is(new Long[] {(long) 'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a', 'b'}), is(new Long[] {(long) 'a', (long) 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new long[] {'a', 'b'}), is(new Long[] {(long) 'a', (long) 'b'}));
  }

  @Test
  public void toPrimitiveArray__long() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[0]), is(new long[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a'}), is(new long[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a', (long) 'b'}), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Long[] {(long) 'a', (long) 'b'}), is(new long[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Collections.emptyList()), is(new long[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a')), is(new long[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a', (long) 'b')), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayLong(Arrays.asList((long) 'a', (long) 'b')), is(new long[] {'a', 'b'}));

  }

  @Test
  public void concat__long() {
    
    assertThat(PrimitiveFunctions.concat(new long[0]), is(new long[0]));
    assertThat(PrimitiveFunctions.concat(new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new long[0], new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new long[0], new long[] {'a', 'b'}, new long[] {'c', 'd'}), is(new long[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__long() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new long[0], new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new long[] {'a', 'b'}, new long[] {'c', 'd'}), is(new long[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new long[0], new long[] {'a', 'b'}), is(new long[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new long[0], new long[] {'a', 'b'}, 0), is(new long[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new long[] {'a', 'b'}, new long[] {'c', 'd'}, 1), is(new long[] {'a', 'c', 'd', 'b'}));

  }

  /* FLOAT */
  
  @Test(expectedExceptions = KclException.class)
  public void min__float_error() {
    PrimitiveFunctions.min(new float[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__float_error() {
    PrimitiveFunctions.max(new float[0]);
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
    assertTrue(PrimitiveFunctions.compare(new float[0], new float[0]));
    assertTrue(PrimitiveFunctions.compare(new float[] {'a'}, new float[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new float[] {'a', 'z'}, new float[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new float[0], new float[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new float[] {'a'}, new float[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l'}, new float[0], 3));
    assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a'}, new float[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a', 'z'}, new float[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l'}, new float[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new float[] {'l', 'l', 'l', 'a'}, new float[0], 3));
    
  }

  @Test
  public void find__float() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new float[0], 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new float[] {'a'}, 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new float[] {'a', 'z'}, 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new float[0], 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new float[] {'a'}, 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new float[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

  }

  @Test
  public void indexOf__float() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new float[0], new float[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a'}, new float[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z'}, new float[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'l', 'f'}, new float[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new float[0], new float[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a'}, new float[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'a', 'z'}, new float[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new float[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new float[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__float() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new float[0], new float[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'b', 'a'}, new float[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'z', 'a', 'z'}, new float[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new float[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new float[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__float() {
    assertThat(PrimitiveFunctions.toList(new float[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new float[] {'a'}), is(Arrays.asList((float) 'a')));
    assertThat(PrimitiveFunctions.toList(new float[] {'a', 'b'}), is(Arrays.asList((float) 'a', (float) 'b')));
    assertLists(PrimitiveFunctions.toList(new float[] {'a', 'b'}), Arrays.asList(Float.valueOf('a'), Float.valueOf('b')));
  }

  @Test
  public void toObjectArray__float() {
    assertThat(PrimitiveFunctions.toObjectArray(new float[0]), is(new Float[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a'}), is(new Float[] {(float) 'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a', 'b'}), is(new Float[] {(float) 'a', (float) 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new float[] {'a', 'b'}), is(new Float[] {(float) 'a', (float) 'b'}));
  }

  @Test
  public void toPrimitiveArray__float() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[0]), is(new float[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a'}), is(new float[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a', (float) 'b'}), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Float[] {(float) 'a', (float) 'b'}), is(new float[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Collections.emptyList()), is(new float[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a')), is(new float[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a', (float) 'b')), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayFloat(Arrays.asList((float) 'a', (float) 'b')), is(new float[] {'a', 'b'}));

  }

  @Test
  public void concat__float() {
    
    assertThat(PrimitiveFunctions.concat(new float[0]), is(new float[0]));
    assertThat(PrimitiveFunctions.concat(new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new float[0], new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new float[0], new float[] {'a', 'b'}, new float[] {'c', 'd'}), is(new float[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__float() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new float[0], new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new float[] {'a', 'b'}, new float[] {'c', 'd'}), is(new float[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new float[0], new float[] {'a', 'b'}), is(new float[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new float[0], new float[] {'a', 'b'}, 0), is(new float[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new float[] {'a', 'b'}, new float[] {'c', 'd'}, 1), is(new float[] {'a', 'c', 'd', 'b'}));

  }

  /* DOUBLE */
  
  @Test(expectedExceptions = KclException.class)
  public void min__double_error() {
    PrimitiveFunctions.min(new double[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void max__double_error() {
    PrimitiveFunctions.max(new double[0]);
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
    assertTrue(PrimitiveFunctions.compare(new double[0], new double[0]));
    assertTrue(PrimitiveFunctions.compare(new double[] {'a'}, new double[] {'a'}));
    assertTrue(PrimitiveFunctions.compare(new double[] {'a', 'z'}, new double[] {'a', 'z'}));
    assertFalse(PrimitiveFunctions.compare(new double[0], new double[] {'a'}));
    assertFalse(PrimitiveFunctions.compare(new double[] {'a'}, new double[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l'}, new double[0], 3));
    assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a'}, new double[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a', 'z'}, new double[] {'a', 'z'}, 3));
    assertFalse(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l'}, new double[] {'a'}, 3));
    assertTrue(PrimitiveFunctions.compare(new double[] {'l', 'l', 'l', 'a'}, new double[0], 3));
    
  }

  @Test
  public void find__double() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new double[0], 'a'), is(-1));
    assertThat(PrimitiveFunctions.find(new double[] {'a'}, 'a'), is(0));
    assertThat(PrimitiveFunctions.find(new double[] {'a', 'z'}, 'z'), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new double[0], 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new double[] {'a'}, 'a', 2), is(-1));
    assertThat(PrimitiveFunctions.find(new double[] {'a', 'z', 'a', 'z'}, 'z', 2), is(3));

  }

  @Test
  public void indexOf__double() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new double[0], new double[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a'}, new double[] {'a'}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z'}, new double[] {'z'}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'l', 'f'}, new double[] {'z', 'l'}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new double[0], new double[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a'}, new double[] {'a'}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'a', 'z'}, new double[] {'z'}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new double[] {'a', 'z', 'l', 'f', 'z', 'l', 'f'}, new double[] {'z', 'l'}, 3), is(4));
    
  }
  
  @Test
  public void lastIndexOf__double() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new double[0], new double[] {'a'}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'b', 'a'}, new double[] {'a'}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'z', 'a', 'z'}, new double[] {'z'}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new double[] {'a', 'z', 'l', 'f', 'a', 'z', 'l'}, new double[] {'z', 'l'}), is(5));
    
  }
  
  @Test
  public void toList__double() {
    assertThat(PrimitiveFunctions.toList(new double[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new double[] {'a'}), is(Arrays.asList((double) 'a')));
    assertThat(PrimitiveFunctions.toList(new double[] {'a', 'b'}), is(Arrays.asList((double) 'a', (double) 'b')));
    assertLists(PrimitiveFunctions.toList(new double[] {'a', 'b'}), Arrays.asList(Double.valueOf('a'), Double.valueOf('b')));
  }

  @Test
  public void toObjectArray__double() {
    assertThat(PrimitiveFunctions.toObjectArray(new double[0]), is(new Double[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a'}), is(new Double[] {(double) 'a'}));
    assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a', 'b'}), is(new Double[] {(double) 'a', (double) 'b'}));
    assertThat(PrimitiveFunctions.toObjectArray(new double[] {'a', 'b'}), is(new Double[] {(double) 'a', (double) 'b'}));
  }

  @Test
  public void toPrimitiveArray__double() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[0]), is(new double[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a'}), is(new double[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a', (double) 'b'}), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Double[] {(double) 'a', (double) 'b'}), is(new double[] {'a', 'b'}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Collections.emptyList()), is(new double[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a')), is(new double[] {'a'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a', (double) 'b')), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayDouble(Arrays.asList((double) 'a', (double) 'b')), is(new double[] {'a', 'b'}));

  }

  @Test
  public void concat__double() {
    
    assertThat(PrimitiveFunctions.concat(new double[0]), is(new double[0]));
    assertThat(PrimitiveFunctions.concat(new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new double[0], new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.concat(new double[0], new double[] {'a', 'b'}, new double[] {'c', 'd'}), is(new double[] {'a', 'b', 'c', 'd'}));
    
  }

  @Test
  public void insert__double() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new double[0], new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new double[] {'a', 'b'}, new double[] {'c', 'd'}), is(new double[] {'c', 'd', 'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new double[0], new double[] {'a', 'b'}), is(new double[] {'a', 'b'}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new double[0], new double[] {'a', 'b'}, 0), is(new double[] {'a', 'b'}));
    assertThat(PrimitiveFunctions.insert(new double[] {'a', 'b'}, new double[] {'c', 'd'}, 1), is(new double[] {'a', 'c', 'd', 'b'}));

  }

  /* BOOLEAN */
  
  @Test(expectedExceptions = KclException.class)
  public void or__boolean_error() {
    PrimitiveFunctions.or(new boolean[0]);
  }
  
  @Test(expectedExceptions = KclException.class)
  public void and__boolean_error() {
    PrimitiveFunctions.and(new boolean[0]);
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
    assertTrue(PrimitiveFunctions.compare(new boolean[0], new boolean[0]));
    assertTrue(PrimitiveFunctions.compare(new boolean[] {false}, new boolean[] {false}));
    assertTrue(PrimitiveFunctions.compare(new boolean[] {false, true}, new boolean[] {false, true}));
    assertFalse(PrimitiveFunctions.compare(new boolean[0], new boolean[] {false}));
    assertFalse(PrimitiveFunctions.compare(new boolean[] {false}, new boolean[0]));

    // compare(char[], char[], int)
    assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true}, new boolean[0], 3));
    assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false}, new boolean[] {false}, 3));
    assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false, true}, new boolean[] {false, true}, 3));
    assertFalse(PrimitiveFunctions.compare(new boolean[] {true, true, true}, new boolean[] {false}, 3));
    assertTrue(PrimitiveFunctions.compare(new boolean[] {true, true, true, false}, new boolean[0], 3));
    
  }

  @Test
  public void find__boolean() {

    // find(char[], char)
    assertThat(PrimitiveFunctions.find(new boolean[0], false), is(-1));
    assertThat(PrimitiveFunctions.find(new boolean[] {false}, false), is(0));
    assertThat(PrimitiveFunctions.find(new boolean[] {false, true}, true), is(1));

    // find(char[], char, int)
    assertThat(PrimitiveFunctions.find(new boolean[0], false, 2), is(-1));
    assertThat(PrimitiveFunctions.find(new boolean[] {false}, false, 2), is(-1));
    assertThat(PrimitiveFunctions.find(new boolean[] {false, true, false, true}, true, 2), is(3));

  }

  @Test
  public void indexOf__boolean() {
    
    // indexOf(char[], char)
    assertThat(PrimitiveFunctions.indexOf(new boolean[0], new boolean[] {false}), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false}, new boolean[] {false}), is(0));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true}, new boolean[] {true}), is(1));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, true, true}, new boolean[] {true, true}), is(1));

    // indexOf(char[], char, int)
    assertThat(PrimitiveFunctions.indexOf(new boolean[0], new boolean[] {false}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false}, new boolean[] {false}, 2), is(-1));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, false, true}, new boolean[] {true}, 2), is(3));
    assertThat(PrimitiveFunctions.indexOf(new boolean[] {false, true, true, true, true, true, true}, new boolean[] {true, true}, 3), is(3));
    
  }
  
  @Test
  public void lastIndexOf__boolean() {
    
    // lastIndexOf(char[], char)
    assertThat(PrimitiveFunctions.lastIndexOf(new boolean[0], new boolean[] {false}), is(-1));
    assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, false}, new boolean[] {false}), is(2));
    assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, false, true}, new boolean[] {true}), is(3));
    assertThat(PrimitiveFunctions.lastIndexOf(new boolean[] {false, true, true, true, false, true, true}, new boolean[] {true, true}), is(5));
    
  }
  
  @Test
  public void toList__boolean() {
    assertThat(PrimitiveFunctions.toList(new boolean[0]), is(Collections.emptyList()));
    assertThat(PrimitiveFunctions.toList(new boolean[] {false}), is(Arrays.asList(false)));
    assertThat(PrimitiveFunctions.toList(new boolean[] {false, true}), is(Arrays.asList(false, true)));
    assertLists(PrimitiveFunctions.toList(new boolean[] {false, true}), Arrays.asList(Boolean.FALSE, Boolean.TRUE));
  }

  @Test
  public void toObjectArray__boolean() {
    assertThat(PrimitiveFunctions.toObjectArray(new boolean[0]), is(new Boolean[0]));
    assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false}), is(new Boolean[] {false}));
    assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false, true}), is(new Boolean[] {false, Boolean.TRUE}));
    assertThat(PrimitiveFunctions.toObjectArray(new boolean[] {false, true}), is(new Boolean[] {false, Boolean.TRUE}));
  }

  @Test
  public void toPrimitiveArray__boolean() {
    
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[0]), is(new boolean[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false}), is(new boolean[] {false}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false, true}), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.toPrimitiveArray(new Boolean[] {false, true}), is(new boolean[] {false, true}));

    assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Collections.emptyList()), is(new boolean[0]));
    assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false)), is(new boolean[] {false}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false, true)), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.toPrimitiveArrayBoolean(Arrays.asList(false, true)), is(new boolean[] {false, true}));

  }

  @Test
  public void concat__boolean() {
    
    assertThat(PrimitiveFunctions.concat(new boolean[0]), is(new boolean[0]));
    assertThat(PrimitiveFunctions.concat(new boolean[] {false, true}), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.concat(new boolean[0], new boolean[] {false, true}), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.concat(new boolean[0], new boolean[] {false, true}, new boolean[] {true, true}), is(new boolean[] {false, true, true, true}));
    
  }

  @Test
  public void insert__boolean() {
  
    // insert(char[], char[])
    assertThat(PrimitiveFunctions.insert(new boolean[0], new boolean[] {false, true}), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.insert(new boolean[] {false, true}, new boolean[] {true, true}), is(new boolean[] {true, true, false, true}));
    assertThat(PrimitiveFunctions.insert(new boolean[0], new boolean[] {false, true}), is(new boolean[] {false, true}));

    // insert(char[], char[], int)
    assertThat(PrimitiveFunctions.insert(new boolean[0], new boolean[] {false, true}, 0), is(new boolean[] {false, true}));
    assertThat(PrimitiveFunctions.insert(new boolean[] {false, true}, new boolean[] {true, true}, 1), is(new boolean[] {false, true, true, true}));

  }

} /* ENDCLASS */
