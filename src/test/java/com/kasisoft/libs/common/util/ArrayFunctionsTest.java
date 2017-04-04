package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.util.*;

/**
 * Testcases for the class 'ArrayFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ArrayFunctionsTest {

  @Test(groups="all")
  public void nonNullLength() {
    assertThat( ArrayFunctions.nonNullLength(), is(0) );
    assertThat( ArrayFunctions.nonNullLength( (Object[]) null ), is(0) );
    assertThat( ArrayFunctions.nonNullLength( null, null ), is(0) );
    assertThat( ArrayFunctions.nonNullLength( null, "STR2" ), is(1) );
    assertThat( ArrayFunctions.nonNullLength( "STR1", null ), is(1) );
    assertThat( ArrayFunctions.nonNullLength( "STR1", "STR2" ), is(2) );
  }

  @Test(groups="all")
  public void cleanup() {
    
    // Boolean
    assertThat( ArrayFunctions.cleanup( (Boolean[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Boolean[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Boolean[] { null, Boolean.TRUE } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( ArrayFunctions.cleanup( new Boolean[] { Boolean.TRUE, null } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( ArrayFunctions.cleanup( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), is( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ) );

    // Char
    assertThat( ArrayFunctions.cleanup( (Character[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Character[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Character[] { null, Character.valueOf('\n') } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( ArrayFunctions.cleanup( new Character[] { Character.valueOf('\n'), null } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( ArrayFunctions.cleanup( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), is( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ) );

    // Byte
    assertThat( ArrayFunctions.cleanup( (Byte[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Byte[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Byte[] { null, Byte.valueOf((byte) 0) } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( ArrayFunctions.cleanup( new Byte[] { Byte.valueOf((byte) 0), null } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( ArrayFunctions.cleanup( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), is( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ) );

    // Short
    assertThat( ArrayFunctions.cleanup( (Short[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Short[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Short[] { null, Short.valueOf((short) 0) } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( ArrayFunctions.cleanup( new Short[] { Short.valueOf((short) 0), null } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( ArrayFunctions.cleanup( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), is( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ) );

    // Integer
    assertThat( ArrayFunctions.cleanup( (Integer[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Integer[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Integer[] { null, Integer.valueOf(0) } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Integer[] { Integer.valueOf(0), null } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), is( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ) );

    // Long
    assertThat( ArrayFunctions.cleanup( (Long[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Long[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Long[] { null, Long.valueOf(0) } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Long[] { Long.valueOf(0), null } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), is( new Long[] { Long.valueOf(1), Long.valueOf(0) } ) );

    // Float
    assertThat( ArrayFunctions.cleanup( (Float) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Float[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Float[] { null, Float.valueOf(0) } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Float[] { Float.valueOf(0), null } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), is( new Float[] { Float.valueOf(1), Float.valueOf(0) } ) );

    // Double
    assertThat( ArrayFunctions.cleanup( (Double) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Double[] { null, null } ), is( nullValue() ) );
    assertThat( ArrayFunctions.cleanup( new Double[] { null, Double.valueOf(0) } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Double[] { Double.valueOf(0), null } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( ArrayFunctions.cleanup( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), is( new Double[] { Double.valueOf(1), Double.valueOf(0) } ) );

  }

  @Test(groups="all")
  public void toObjectArray() {
    
    // Boolean
    assertThat( ArrayFunctions.toObjectArray( (boolean[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new boolean[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new boolean[] { true } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( ArrayFunctions.toObjectArray( new boolean[] { false, true } ), is( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ) );

    // Char
    assertThat( ArrayFunctions.toObjectArray( (char[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new char[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new char[] { '\n' } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( ArrayFunctions.toObjectArray( new char[] { '\r', '\n' } ), is( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ) );

    // Byte
    assertThat( ArrayFunctions.toObjectArray( (byte[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new byte[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new byte[] { (byte) 0 } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new byte[] { (byte) 1, (byte) 0 } ), is( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ) );

    // Short
    assertThat( ArrayFunctions.toObjectArray( (short[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new short[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new short[] { (short) 0 } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new short[] { (short) 1, (short) 0 } ), is( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ) );

    // Integer
    assertThat( ArrayFunctions.toObjectArray( (int[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new int[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new int[] { 0 } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new int[] { 1, 0 } ), is( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ) );

    // Long
    assertThat( ArrayFunctions.toObjectArray( (long[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new long[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new long[] { 0 } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new long[] { 1, 0 } ), is( new Long[] { Long.valueOf(1), Long.valueOf(0) } ) );

    // Float
    assertThat( ArrayFunctions.toObjectArray( (float[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new float[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new float[] { 0 } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new float[] { 1, 0 } ), is( new Float[] { Float.valueOf(1), Float.valueOf(0) } ) );

    // Double
    assertThat( ArrayFunctions.toObjectArray( (double[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new double[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toObjectArray( new double[] { 0 } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( ArrayFunctions.toObjectArray( new double[] { 1, 0 } ), is( new Double[] { Double.valueOf(1), Double.valueOf(0) } ) );

  }

  private <T> List<T> list( T ... objects ) {
    return Arrays.asList( objects );
  }
  
  @Test(groups="all")
  public void asList() {
    
    // Boolean
    assertThat( ArrayFunctions.asList( new boolean[] {} ), is( this.<Boolean>list() ) );
    assertThat( ArrayFunctions.asList( new boolean[] { true } ), is( list( Boolean.TRUE ) ) );
    assertThat( ArrayFunctions.asList( new boolean[] { false, true } ), is( list( Boolean.FALSE, Boolean.TRUE ) ) );

    // Char
    assertThat( ArrayFunctions.asList( new char[] {} ), is( this.<Character>list() ) );
    assertThat( ArrayFunctions.asList( new char[] { '\n' } ), is( list( Character.valueOf('\n') ) ) );
    assertThat( ArrayFunctions.asList( new char[] { '\r', '\n' } ), is( list( Character.valueOf('\r'), Character.valueOf('\n') ) ) );

    // Byte
    assertEquals( ArrayFunctions.asList( new byte[] {} ), new ArrayList<Byte>() );
    assertEquals( ArrayFunctions.asList( new byte[] { (byte) 0 } ), list( Byte.valueOf((byte) 0) ) );
    assertEquals( ArrayFunctions.asList( new byte[] { (byte) 1, (byte) 0 } ), list( Byte.valueOf( (byte) 1), Byte.valueOf( (byte) 0) ) );

    // Short
    assertThat( ArrayFunctions.asList( new short[] {} ), is( this.<Short>list() ) );
    assertThat( ArrayFunctions.asList( new short[] { (short) 0 } ), is( list( Short.valueOf( (short) 0) ) ) );
    assertThat( ArrayFunctions.asList( new short[] { (short) 1, (short) 0 } ), is( list( Short.valueOf( (short) 1), Short.valueOf( (short) 0) ) ) );

    // Integer
    assertThat( ArrayFunctions.asList( new int[] {} ), is( this.<Integer>list() ) );
    assertThat( ArrayFunctions.asList( new int[] { 0 } ), is( list( Integer.valueOf(0) ) ) );
    assertThat( ArrayFunctions.asList( new int[] { 1, 0 } ), is( list( Integer.valueOf(1), Integer.valueOf(0) ) ) );

    // Long
    assertThat( ArrayFunctions.asList( new long[] {} ), is( this.<Long>list() ) );
    assertThat( ArrayFunctions.asList( new long[] { 0 } ), is( list( Long.valueOf(0) ) ) );
    assertThat( ArrayFunctions.asList( new long[] { 1, 0 } ), is( list( Long.valueOf(1), Long.valueOf(0) ) ) );

    // Float
    assertThat( ArrayFunctions.asList( new float[] {} ), is( this.<Float>list() ) );
    assertThat( ArrayFunctions.asList( new float[] { 0 } ), is( list( Float.valueOf(0) ) ) );
    assertThat( ArrayFunctions.asList( new float[] { 1, 0 } ), is( list( Float.valueOf(1), Float.valueOf(0) ) ) );

    // Double
    assertThat( ArrayFunctions.asList( new double[] {} ), is( this.<Double>list() ) );
    assertThat( ArrayFunctions.asList( new double[] { 0 } ), is( list( Double.valueOf(0) ) ) );
    assertThat( ArrayFunctions.asList( new double[] { 1, 0 } ), is( list( Double.valueOf(1), Double.valueOf(0) ) ) );

  }

  @Test(groups="all")
  public void toPrimitiveArray() {
    
    // Boolean
    assertThat( ArrayFunctions.toPrimitiveArray( (Boolean[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Boolean[0] ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.TRUE } ), is( new boolean[] { true } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), is( new boolean[] { false, true } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.FALSE, null, Boolean.TRUE } ), is( new boolean[] { false, true } ) );

    // Char
    assertThat( ArrayFunctions.toPrimitiveArray( (Character[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Character[0] ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\n') } ), is( new char[] { '\n' } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), is( new char[] { '\r', '\n' } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\r'), null, Character.valueOf('\n') } ), is( new char[] { '\r', '\n' } ) );

    // Byte
    assertThat( ArrayFunctions.toPrimitiveArray( (Byte[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Byte[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 1, (byte) 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), null, Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 1, (byte) 0 } ) );

    // Short
    assertThat( ArrayFunctions.toPrimitiveArray( (Short[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Short[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 0) } ), is( new short[] { (short) 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), is( new short[] { (short) 1, (short) 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), null, Short.valueOf((short) 0) } ), is( new short[] { (short) 1, (short) 0 } ) );

    // Integer
    assertThat( ArrayFunctions.toPrimitiveArray( (Integer[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Integer[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(0) } ), is( new int[] { 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), is( new int[] { 1, 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(1), null, Integer.valueOf(0) } ), is( new int[] { 1, 0 } ) );

    // Long
    assertThat( ArrayFunctions.toPrimitiveArray( (Long[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Long[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(0) } ), is( new long[] { 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), is( new long[] { 1, 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(1), null, Long.valueOf(0) } ), is( new long[] { 1, 0 } ) );

    // Float
    assertThat( ArrayFunctions.toPrimitiveArray( (Float[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Float[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(0) } ), is( new float[] { 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), is( new float[] { 1, 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(1), null, Float.valueOf(0) } ), is( new float[] { 1, 0 } ) );

    // Double
    assertThat( ArrayFunctions.toPrimitiveArray( (Double[]) null ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Double[] {} ), is( nullValue() ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(0) } ), is( new double[] { 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), is( new double[] { 1, 0 } ) );
    assertThat( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(1), null, Double.valueOf(0) } ), is( new double[] { 1, 0 } ) );

  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void max() {
    
    assertThat( ArrayFunctions.maxInt( 12 ), is( 12 ) );
    assertThat( ArrayFunctions.maxInt( -477, 32 ), is( 32 ) );
    assertThat( ArrayFunctions.maxInt( -477, 32, 2778 ), is( 2778 ) );

    assertThat( ArrayFunctions.maxLong( 12 ), is( 12L ) );
    assertThat( ArrayFunctions.maxLong( -477, 32 ), is( 32L ) );
    assertThat( ArrayFunctions.maxLong( -477, 32, 2778 ), is( 2778L ) );

    assertThat( (float) ArrayFunctions.maxFloat( (float) 12.0 ), is( (float) 12.0 ) );
    assertThat( (float) ArrayFunctions.maxFloat( (float) -477, (float) 32 ), is( (float) 32.0 ) );
    assertThat( (float) ArrayFunctions.maxFloat( (float) -477, (float) 32, (float) 2778 ), is( (float) 2778.0 ) );

    assertThat( (double) ArrayFunctions.maxDouble( 12.0 ), is( 12.0 ) );
    assertThat( (double) ArrayFunctions.maxDouble( -477.0, 32.0 ), is( 32.0 ) );
    assertThat( (double) ArrayFunctions.maxDouble( -477.0, 32.0, 2778.0 ), is( 2778.0 ) );

  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void min() {
    
    assertThat( ArrayFunctions.minInt( 12 ), is( 12 ) );
    assertThat( ArrayFunctions.minInt( -477, 32 ), is( -477 ) );
    assertThat( ArrayFunctions.minInt( -477, 32, 2778 ), is( -477 ) );

    assertThat( ArrayFunctions.minLong( 12 ), is( 12L ) );
    assertThat( ArrayFunctions.minLong( -477, 32 ), is( -477L ) );
    assertThat( ArrayFunctions.minLong( -477, 32, 2778 ), is( -477L ) );

    assertThat( (float) ArrayFunctions.minFloat( (float) 12.0 ), is( (float) 12.0 ) );
    assertThat( (float) ArrayFunctions.minFloat( (float) -477, (float) 32 ), is( (float) -477.0 ) );
    assertThat( (float) ArrayFunctions.minFloat( (float) -477, (float) 32, (float) 2778 ), is( (float) -477.0 ) );

    assertThat( (double) ArrayFunctions.minDouble( 12.0 ), is( 12.0 ) );
    assertThat( (double) ArrayFunctions.minDouble( -477.0, 32.0 ), is( -477.0 ) );
    assertThat( (double) ArrayFunctions.minDouble( -477.0, 32.0, 2778.0 ), is( -477.0 ) );

  }

  @Test(groups="all")
  public void and() {
    
    assertTrue( ArrayFunctions.and( true ) );
    assertFalse( ArrayFunctions.and( false ) );
    assertTrue( ArrayFunctions.and( true, true ) );
    assertFalse( ArrayFunctions.and( true, false ) );
    assertTrue( ArrayFunctions.and( true, true, true ) );
    assertFalse( ArrayFunctions.and( true, true, false ) );

    assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE ).booleanValue() );
    assertFalse( ArrayFunctions.objectAnd( Boolean.FALSE ).booleanValue() );
    assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertFalse( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertFalse( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void or() {
    
    assertTrue( ArrayFunctions.or( true ) );
    assertFalse( ArrayFunctions.or( false ) );
    assertTrue( ArrayFunctions.or( true, true ) );
    assertTrue( ArrayFunctions.or( true, false ) );
    assertFalse( ArrayFunctions.or( false, false ) );
    assertTrue( ArrayFunctions.or( true, true, true ) );
    assertTrue( ArrayFunctions.or( true, true, false ) );
    assertFalse( ArrayFunctions.or( false, false, false ) );

    assertTrue( ArrayFunctions.objectOr( Boolean.TRUE ).booleanValue() );
    assertFalse( ArrayFunctions.objectOr( Boolean.FALSE ).booleanValue() );
    assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertFalse( ArrayFunctions.objectOr( Boolean.FALSE, Boolean.FALSE ).booleanValue() );
    assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertFalse( ArrayFunctions.objectOr( Boolean.FALSE, Boolean.FALSE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void addAll() {
    
    List<Boolean> receiver = new ArrayList<>();
    ArrayFunctions.addAll( receiver, Boolean.TRUE );
    
    assertThat( receiver.size(), is(1) );
    assertThat( receiver.get(0), is( Boolean.TRUE ) );
    
    ArrayFunctions.addAll( receiver, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE );
    assertThat( receiver.size(), is(4) );
    assertThat( receiver.get(0), is( Boolean.TRUE  ) );
    assertThat( receiver.get(1), is( Boolean.TRUE  ) );
    assertThat( receiver.get(2), is( Boolean.FALSE ) );
    assertThat( receiver.get(3), is( Boolean.FALSE ) );
    
  }
  
  @Test(groups="all")
  public void enumeration() {

    String        str   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    List<String>  list  = new ArrayList<>();
    for( int i = 0; i < str.length(); i++ ) {
      list.add( String.valueOf( str.charAt(i) ) );
    }
    
    int                 count       = 0;
    Enumeration<String> enumeration = ArrayFunctions.enumeration( list.toArray( new String[ list.size() ] ) );
    while( enumeration.hasMoreElements() ) {
      String current = enumeration.nextElement();
      assertThat( current.charAt(0), is( str.charAt( count ) ) );
      count++;
    }
    assertThat( count, is( str.length() ) );
    
  }

  @Test(groups="all")
  public void iterator() {

    String        str   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    List<String>  list  = new ArrayList<>();
    for( int i = 0; i < str.length(); i++ ) {
      list.add( String.valueOf( str.charAt(i) ) );
    }
    
    int              count    = 0;
    Iterator<String> iterator = ArrayFunctions.iterator( list.toArray( new String[ list.size() ] ) );
    while( iterator.hasNext() ) {
      String current = iterator.next();
      assertThat( current.charAt(0), is( str.charAt( count ) ) );
      count++;
    }
    assertThat( count, is( str.length() ) );
    
  }
  
  @Test(groups="all")
  public void sumInt() {
    assertThat( ArrayFunctions.sumInt( (int[]) null ), is(0) );
    assertThat( ArrayFunctions.sumInt( new int[0] ), is(0) );
    assertThat( ArrayFunctions.sumInt( 12 ), is(12) );
    assertThat( ArrayFunctions.sumInt( 12, 23 ), is(35) );
    assertThat( ArrayFunctions.sumInt( -12, 23 ), is(11) );
  }

  @Test(groups="all")
  public void sumLong() {
    assertThat( ArrayFunctions.sumLong( (long[]) null ), is(0L) );
    assertThat( ArrayFunctions.sumLong( new long[0] ), is(0L) );
    assertThat( ArrayFunctions.sumLong( 12L ), is(12L) );
    assertThat( ArrayFunctions.sumLong( 12L, 23L ), is(35L) );
    assertThat( ArrayFunctions.sumLong( -12L, 23L ), is(11L) );
  }

  @Test(groups="all")
  public void sumDouble() {
    assertThat( ArrayFunctions.sumDouble( (double[]) null ), is(0.0) );
    assertThat( ArrayFunctions.sumDouble( new double[0] ), is(0.0) );
    assertThat( ArrayFunctions.sumDouble( 12.0 ), is(12.0) );
    assertThat( ArrayFunctions.sumDouble( 12.0, 23.0 ), is(35.0) );
    assertThat( ArrayFunctions.sumDouble( -12.0, 23.0 ), is(11.0) );
  }

  @DataProvider(name="createCharComparisons")
  public Object[][] createCharComparisons() {
    return new Object[][] {
      { "Frog finds the dog".toCharArray(), "Frog"   . toCharArray(), Integer.valueOf(0), Boolean.TRUE  },  
      { "Frog finds the dog".toCharArray(), "finds"  . toCharArray(), Integer.valueOf(5), Boolean.TRUE  },  
      { "Frog finds the dog".toCharArray(), "Fr og"  . toCharArray(), Integer.valueOf(0), Boolean.FALSE },  
      { "Frog finds the dog".toCharArray(), "fin ds" . toCharArray(), Integer.valueOf(5), Boolean.FALSE },  
    };
  }

  @Test(dataProvider="createCharComparisons")
  public void compareChars( char[] buffer, char[] sequence, int offset, boolean expected ) {
    boolean result = ArrayFunctions.compare( buffer, sequence, offset );
    assertThat( result, is( expected ) );
  }

  @DataProvider(name="createByteComparisons")
  public Object[][] createByteComparisons() {
    return new Object[][] {
      { "Frog finds the dog".getBytes(), "Frog"   . getBytes(), Integer.valueOf(0), Boolean.TRUE  },  
      { "Frog finds the dog".getBytes(), "finds"  . getBytes(), Integer.valueOf(5), Boolean.TRUE  },  
      { "Frog finds the dog".getBytes(), "Fr og"  . getBytes(), Integer.valueOf(0), Boolean.FALSE },  
      { "Frog finds the dog".getBytes(), "fin ds" . getBytes(), Integer.valueOf(5), Boolean.FALSE },  
    };
  }

  @Test(dataProvider="createByteComparisons")
  public void compareBytes( byte[] buffer, byte[] sequence, int offset, boolean expected ) {
    boolean result = ArrayFunctions.compare( buffer, sequence, offset );
    assertThat( result, is( expected ) );
  }
  
  @DataProvider(name="createIndexOf")
  public Object[][] createIndexOf() {
    return new Object[][] {
      { "Frog finds the dog"       , "ind"                 , Integer.valueOf(0)   , Integer.valueOf(6)  },
      { "Frog finds the dog"       , "xyz"                 , Integer.valueOf(0)   , Integer.valueOf(-1) },
      { "Frog finds the dog"       , "ro "                 , Integer.valueOf(100) , Integer.valueOf(-1) },
      { "Frog finds the dog"       , "Frog finds the dog " , Integer.valueOf(0)   , Integer.valueOf(-1) },
      { "Frog finds finds the dog" , "ind"                 , Integer.valueOf(7)   , Integer.valueOf(12) },
    };
  }

  @DataProvider(name="createLastIndexOf")
  public Object[][] createLastIndexOf() {
    return new Object[][] {
      { "Frog finds the dog"       , "ind"                 , Integer.valueOf(0)   , Integer.valueOf(6)  },
      { "Frog finds the dog"       , "xyz"                 , Integer.valueOf(0)   , Integer.valueOf(-1) },
      { "Frog finds the dog"       , "ro "                 , Integer.valueOf(100) , Integer.valueOf(-1) },
      { "Frog finds the dog"       , "Frog finds the dog " , Integer.valueOf(0)   , Integer.valueOf(-1) },
      { "Frog finds finds the dog" , "ind"                 , Integer.valueOf(0)   , Integer.valueOf(12) },
    };
  }

  @Test(dataProvider="createIndexOf")
  public void indexOfChars( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.indexOf( buffer.toCharArray(), characters.toCharArray(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @Test(dataProvider="createIndexOf")
  public void indexOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.indexOf( buffer.getBytes(), characters.getBytes(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfChars( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.lastIndexOf( buffer.toCharArray(), characters.toCharArray(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.lastIndexOf( buffer.getBytes(), characters.getBytes(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @DataProvider(name="joinArraysData")
  public Object[][] joinArraysData() {
    return new Object[][] {
      { new String[][] {}, new String[0] },  
      { new String[][] { new String[0] }, new String[0] },
      { new String[][] { new String[0], new String[0] }, new String[0] },
      { new String[][] { new String[2], new String[0] }, new String[2] },
      { new String[][] { new String[0], new String[2] }, new String[2] },
      { new String[][] { new String[] { null }, new String[] { "Hello", null, "World" } }, new String[] { null, "Hello", null, "World" } },
    };
  }
  
  @Test(dataProvider="joinArraysData", groups="all")
  public void joinArray( String[][] input, String[] expected ) {
    assertThat( ArrayFunctions.joinArrays( new String[0], input ), is( expected ) );
  }
  
  private char[][] toCharArray( String ... str ) {
    if( str == null ) {
      return null;
    }
    char[][] result = new char[ str.length ][];
    for( int i = 0; i < result.length; i++ ) {
      if( str[i] != null ) {
        result[i] = str[i].toCharArray();
      }
    }
    return result;
  }

  private byte[][] toByteArray( String ... str ) {
    if( str == null ) {
      return null;
    }
    byte[][] result = new byte[ str.length ][];
    for( int i = 0; i < result.length; i++ ) {
      if( str[i] != null ) {
        result[i] = str[i].getBytes();
      }
    }
    return result;
  }

  @DataProvider(name="createCharBuffers")
  public Object[][] createCharBuffers() {
    return new Object[][] {
      { toCharArray( "Hello" ), "Hello" },
      { toCharArray( "Hello", null, " ", "World" ), "Hello World" },
      { toCharArray( "Hello", " ", "World" ), "Hello World" },
    };
  }

  @DataProvider(name="createByteBuffers")
  public Object[][] createByteBuffers() {
    return new Object[][] {
      { toByteArray( "Hello" ), "Hello" },
      { toByteArray( "Hello", null, " ", "World" ), "Hello World" },
      { toByteArray( "Hello", " ", "World" ), "Hello World" },
    };
  }


  @Test(dataProvider="createCharBuffers")
  public void joinCharBuffers( char[][] buffers, String expected ) {
    char[] joined = ArrayFunctions.joinBuffers( buffers );
    assertThat( new String( joined ), is( expected ) );
  }

  @Test(dataProvider="createByteBuffers")
  public void joinByteBuffers( byte[][] buffers, String expected ) {
    byte[] joined = ArrayFunctions.joinBuffers( buffers );
    assertThat( new String( joined ), is( expected ) );
  }
  
  @DataProvider(name="createCharInsertion")
  public Object[][] createCharInsertion() {
    return new Object[][] {
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(0), "" },  
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(5), "" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(0), " small Hello World" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(5), "Hello small  World" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(50), "Hello World" },  
    };
  }

  @DataProvider(name="createByteInsertion")
  public Object[][] createByteInsertion() {
    return new Object[][] {
      { "".getBytes(), "Hello World".getBytes(), Integer.valueOf(0), "" },  
      { "".getBytes(), "Hello World".getBytes(), Integer.valueOf(5), "" },  
      { "Hello World".getBytes(), " small ".getBytes(), Integer.valueOf(0), " small Hello World" },  
      { "Hello World".getBytes(), " small ".getBytes(), Integer.valueOf(5), "Hello small  World" },  
      { "Hello World".getBytes(), " small ".getBytes(), Integer.valueOf(50), "Hello World" },  
    };
  }
  
  @Test(dataProvider="createCharInsertion")
  public void insertChars( char[] dest, char[] insert, int index, String expected ) {
    char[] combined = ArrayFunctions.insert( dest, insert, index );
    assertThat( new String( combined ), is( expected ) );
  }

  @Test(dataProvider="createByteInsertion")
  public void insertBytes( byte[] dest, byte[] insert, int index, String expected ) {
    byte[] combined = ArrayFunctions.insert( dest, insert, index );
    assertThat( new String( combined ), is( expected ) );
  }
  
} /* ENDCLASS */
