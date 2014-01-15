/**
 * Name........: ArrayFunctionsTest
 * Description.: Testcases for the class 'ArrayFunctions'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Testcases for the class 'ArrayFunctions'.
 */
@Test
public class ArrayFunctionsTest {

  @Test(groups="all")
  public void nonNullLength() {
    
    Assert.assertEquals( ArrayFunctions.nonNullLength(), 0 );
    Assert.assertEquals( ArrayFunctions.nonNullLength( (Object[]) null ), 0 );
    Assert.assertEquals( ArrayFunctions.nonNullLength( null, null ), 0 );
    Assert.assertEquals( ArrayFunctions.nonNullLength( null, "STR2" ), 1 );
    Assert.assertEquals( ArrayFunctions.nonNullLength( "STR1", null ), 1 );
    Assert.assertEquals( ArrayFunctions.nonNullLength( "STR1", "STR2" ), 2 );
    
  }

  @Test(groups="all")
  public void cleanup() {
    
    // Boolean
    Assert.assertEquals( ArrayFunctions.cleanup( (Boolean[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Boolean[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Boolean[] { null, Boolean.TRUE } ), new Boolean[] { Boolean.TRUE } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Boolean[] { Boolean.TRUE, null } ), new Boolean[] { Boolean.TRUE }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), new Boolean[] { Boolean.FALSE, Boolean.TRUE }  );

    // Char
    Assert.assertEquals( ArrayFunctions.cleanup( (Character[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Character[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Character[] { null, Character.valueOf('\n') } ), new Character[] { Character.valueOf('\n') } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Character[] { Character.valueOf('\n'), null } ), new Character[] { Character.valueOf('\n') }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), new Character[] { Character.valueOf('\r'), Character.valueOf('\n') }  );

    // Byte
    Assert.assertEquals( ArrayFunctions.cleanup( (Byte[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Byte[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Byte[] { null, Byte.valueOf((byte) 0) } ), new Byte[] { Byte.valueOf((byte) 0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Byte[] { Byte.valueOf((byte) 0), null } ), new Byte[] { Byte.valueOf((byte) 0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) }  );

    // Short
    Assert.assertEquals( ArrayFunctions.cleanup( (Short[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Short[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Short[] { null, Short.valueOf((short) 0) } ), new Short[] { Short.valueOf((short) 0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Short[] { Short.valueOf((short) 0), null } ), new Short[] { Short.valueOf((short) 0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) }  );

    // Integer
    Assert.assertEquals( ArrayFunctions.cleanup( (Integer[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Integer[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Integer[] { null, Integer.valueOf(0) } ), new Integer[] { Integer.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Integer[] { Integer.valueOf(0), null } ), new Integer[] { Integer.valueOf(0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), new Integer[] { Integer.valueOf(1), Integer.valueOf(0) }  );

    // Long
    Assert.assertEquals( ArrayFunctions.cleanup( (Long[]) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Long[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Long[] { null, Long.valueOf(0) } ), new Long[] { Long.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Long[] { Long.valueOf(0), null } ), new Long[] { Long.valueOf(0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), new Long[] { Long.valueOf(1), Long.valueOf(0) }  );

    // Float
    Assert.assertEquals( ArrayFunctions.cleanup( (Float) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Float[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Float[] { null, Float.valueOf(0) } ), new Float[] { Float.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Float[] { Float.valueOf(0), null } ), new Float[] { Float.valueOf(0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), new Float[] { Float.valueOf(1), Float.valueOf(0) }  );

    // Double
    Assert.assertEquals( ArrayFunctions.cleanup( (Double) null ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Double[] { null, null } ), null );
    Assert.assertEquals( ArrayFunctions.cleanup( new Double[] { null, Double.valueOf(0) } ), new Double[] { Double.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.cleanup( new Double[] { Double.valueOf(0), null } ), new Double[] { Double.valueOf(0) }  );
    Assert.assertEquals( ArrayFunctions.cleanup( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), new Double[] { Double.valueOf(1), Double.valueOf(0) }  );

  }

  @Test(groups="all")
  public void toObjectArray() {
    
    // Boolean
    Assert.assertEquals( ArrayFunctions.toObjectArray( (boolean[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new boolean[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new boolean[] { true } ), new Boolean[] { Boolean.TRUE } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new boolean[] { false, true } ), new Boolean[] { Boolean.FALSE, Boolean.TRUE } );

    // Char
    Assert.assertEquals( ArrayFunctions.toObjectArray( (char[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new char[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new char[] { '\n' } ), new Character[] { Character.valueOf('\n') } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new char[] { '\r', '\n' } ), new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } );

    // Byte
    Assert.assertEquals( ArrayFunctions.toObjectArray( (byte[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new byte[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new byte[] { (byte) 0 } ), new Byte[] { Byte.valueOf((byte) 0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new byte[] { (byte) 1, (byte) 0 } ), new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } );

    // Short
    Assert.assertEquals( ArrayFunctions.toObjectArray( (short[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new short[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new short[] { (short) 0 } ), new Short[] { Short.valueOf((short) 0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new short[] { (short) 1, (short) 0 } ), new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } );

    // Integer
    Assert.assertEquals( ArrayFunctions.toObjectArray( (int[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new int[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new int[] { 0 } ), new Integer[] { Integer.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new int[] { 1, 0 } ), new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } );

    // Long
    Assert.assertEquals( ArrayFunctions.toObjectArray( (long[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new long[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new long[] { 0 } ), new Long[] { Long.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new long[] { 1, 0 } ), new Long[] { Long.valueOf(1), Long.valueOf(0) } );

    // Float
    Assert.assertEquals( ArrayFunctions.toObjectArray( (float[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new float[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new float[] { 0 } ), new Float[] { Float.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new float[] { 1, 0 } ), new Float[] { Float.valueOf(1), Float.valueOf(0) } );

    // Double
    Assert.assertEquals( ArrayFunctions.toObjectArray( (double[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new double[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new double[] { 0 } ), new Double[] { Double.valueOf(0) } );
    Assert.assertEquals( ArrayFunctions.toObjectArray( new double[] { 1, 0 } ), new Double[] { Double.valueOf(1), Double.valueOf(0) } );

  }

  private <T> List<T> list( T ... objects ) {
    return Arrays.asList( objects );
  }
  
  @Test(groups="all")
  public void asList() {
    
    // Boolean
    Assert.assertEquals( ArrayFunctions.asList( new boolean[] {} ), new ArrayList<Boolean>() );
    Assert.assertEquals( ArrayFunctions.asList( new boolean[] { true } ), list( Boolean.TRUE ) );
    Assert.assertEquals( ArrayFunctions.asList( new boolean[] { false, true } ), list( Boolean.FALSE, Boolean.TRUE ) );

    // Char
    Assert.assertEquals( ArrayFunctions.asList( new char[] {} ), new ArrayList<Character>() );
    Assert.assertEquals( ArrayFunctions.asList( new char[] { '\n' } ), list( Character.valueOf('\n') ) );
    Assert.assertEquals( ArrayFunctions.asList( new char[] { '\r', '\n' } ), list( Character.valueOf('\r'), Character.valueOf('\n') ) );

    // Byte
    Assert.assertEquals( ArrayFunctions.asList( new byte[] {} ), new ArrayList<Byte>() );
    Assert.assertEquals( ArrayFunctions.asList( new byte[] { (byte) 0 } ), list( Byte.valueOf((byte) 0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new byte[] { (byte) 1, (byte) 0 } ), list( Byte.valueOf( (byte) 1), Byte.valueOf( (byte) 0) ) );

    // Short
    Assert.assertEquals( ArrayFunctions.asList( new short[] {} ), new ArrayList<Short>() );
    Assert.assertEquals( ArrayFunctions.asList( new short[] { (short) 0 } ), list( Short.valueOf( (short) 0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new short[] { (short) 1, (short) 0 } ), list( Short.valueOf( (short) 1), Short.valueOf( (short) 0) ) );

    // Integer
    Assert.assertEquals( ArrayFunctions.asList( new int[] {} ), new ArrayList<Integer>() );
    Assert.assertEquals( ArrayFunctions.asList( new int[] { 0 } ), list( Integer.valueOf(0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new int[] { 1, 0 } ), list( Integer.valueOf(1), Integer.valueOf(0) ) );

    // Long
    Assert.assertEquals( ArrayFunctions.asList( new long[] {} ), new ArrayList<Long>() );
    Assert.assertEquals( ArrayFunctions.asList( new long[] { 0 } ), list( Long.valueOf(0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new long[] { 1, 0 } ), list( Long.valueOf(1), Long.valueOf(0) ) );

    // Float
    Assert.assertEquals( ArrayFunctions.asList( new float[] {} ), new ArrayList<Float>() );
    Assert.assertEquals( ArrayFunctions.asList( new float[] { 0 } ), list( Float.valueOf(0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new float[] { 1, 0 } ), list( Float.valueOf(1), Float.valueOf(0) ) );

    // Double
    Assert.assertEquals( ArrayFunctions.asList( new double[] {} ), new ArrayList<Double>() );
    Assert.assertEquals( ArrayFunctions.asList( new double[] { 0 } ), list( Double.valueOf(0) ) );
    Assert.assertEquals( ArrayFunctions.asList( new double[] { 1, 0 } ), list( Double.valueOf(1), Double.valueOf(0) ) );

  }

  @Test(groups="all")
  public void toPrimitiveArray() {
    
    // Boolean
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Boolean[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Boolean[0] ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.TRUE } ), new boolean[] { true } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), new boolean[] { false, true } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Boolean[] { Boolean.FALSE, null, Boolean.TRUE } ), new boolean[] { false, true } );

    // Char
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Character[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Character[0] ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\n') } ), new char[] { '\n' } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), new char[] { '\r', '\n' } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Character[] { Character.valueOf('\r'), null, Character.valueOf('\n') } ), new char[] { '\r', '\n' } );

    // Byte
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Byte[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Byte[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 0) } ), new byte[] { (byte) 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), new byte[] { (byte) 1, (byte) 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), null, Byte.valueOf((byte) 0) } ), new byte[] { (byte) 1, (byte) 0 } );

    // Short
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Short[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Short[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 0) } ), new short[] { (short) 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), new short[] { (short) 1, (short) 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), null, Short.valueOf((short) 0) } ), new short[] { (short) 1, (short) 0 } );

    // Integer
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Integer[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Integer[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(0) } ), new int[] { 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), new int[] { 1, 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Integer[] { Integer.valueOf(1), null, Integer.valueOf(0) } ), new int[] { 1, 0 } );

    // Long
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Long[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Long[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(0) } ), new long[] { 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), new long[] { 1, 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Long[] { Long.valueOf(1), null, Long.valueOf(0) } ), new long[] { 1, 0 } );

    // Float
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Float[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Float[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(0) } ), new float[] { 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), new float[] { 1, 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Float[] { Float.valueOf(1), null, Float.valueOf(0) } ), new float[] { 1, 0 } );

    // Double
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( (Double[]) null ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Double[] {} ), null );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(0) } ), new double[] { 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), new double[] { 1, 0 } );
    Assert.assertEquals( ArrayFunctions.toPrimitiveArray( new Double[] { Double.valueOf(1), null, Double.valueOf(0) } ), new double[] { 1, 0 } );

  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void max() {
    
    Assert.assertEquals( ArrayFunctions.maxInt( 12 ), 12 );
    Assert.assertEquals( ArrayFunctions.maxInt( -477, 32 ), 32 );
    Assert.assertEquals( ArrayFunctions.maxInt( -477, 32, 2778 ), 2778 );

    Assert.assertEquals( ArrayFunctions.maxLong( 12 ), 12 );
    Assert.assertEquals( ArrayFunctions.maxLong( -477, 32 ), 32 );
    Assert.assertEquals( ArrayFunctions.maxLong( -477, 32, 2778 ), 2778 );

    Assert.assertEquals( (float) ArrayFunctions.maxFloat( (float) 12.0 ), (float) 12.0 );
    Assert.assertEquals( (float) ArrayFunctions.maxFloat( (float) -477, (float) 32 ), (float) 32.0 );
    Assert.assertEquals( (float) ArrayFunctions.maxFloat( (float) -477, (float) 32, (float) 2778 ), (float) 2778.0 );

    Assert.assertEquals( (double) ArrayFunctions.maxDouble( 12.0 ), 12.0 );
    Assert.assertEquals( (double) ArrayFunctions.maxDouble( -477.0, 32.0 ), 32.0 );
    Assert.assertEquals( (double) ArrayFunctions.maxDouble( -477.0, 32.0, 2778.0 ), 2778.0 );

  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void min() {
    
    Assert.assertEquals( ArrayFunctions.minInt( 12 ), 12 );
    Assert.assertEquals( ArrayFunctions.minInt( -477, 32 ), -477 );
    Assert.assertEquals( ArrayFunctions.minInt( -477, 32, 2778 ), -477 );

    Assert.assertEquals( ArrayFunctions.minLong( 12 ), 12 );
    Assert.assertEquals( ArrayFunctions.minLong( -477, 32 ), -477 );
    Assert.assertEquals( ArrayFunctions.minLong( -477, 32, 2778 ), -477 );

    Assert.assertEquals( (float) ArrayFunctions.minFloat( (float) 12.0 ), (float) 12.0 );
    Assert.assertEquals( (float) ArrayFunctions.minFloat( (float) -477, (float) 32 ), (float) -477.0 );
    Assert.assertEquals( (float) ArrayFunctions.minFloat( (float) -477, (float) 32, (float) 2778 ), (float) -477.0 );

    Assert.assertEquals( (double) ArrayFunctions.minDouble( 12.0 ), 12.0 );
    Assert.assertEquals( (double) ArrayFunctions.minDouble( -477.0, 32.0 ), -477.0 );
    Assert.assertEquals( (double) ArrayFunctions.minDouble( -477.0, 32.0, 2778.0 ), -477.0 );

  }

  @Test(groups="all")
  public void and() {
    
    Assert.assertTrue( ArrayFunctions.and( true ) );
    Assert.assertFalse( ArrayFunctions.and( false ) );
    Assert.assertTrue( ArrayFunctions.and( true, true ) );
    Assert.assertFalse( ArrayFunctions.and( true, false ) );
    Assert.assertTrue( ArrayFunctions.and( true, true, true ) );
    Assert.assertFalse( ArrayFunctions.and( true, true, false ) );

    Assert.assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectAnd( Boolean.FALSE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectAnd( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void or() {
    
    Assert.assertTrue( ArrayFunctions.or( true ) );
    Assert.assertFalse( ArrayFunctions.or( false ) );
    Assert.assertTrue( ArrayFunctions.or( true, true ) );
    Assert.assertTrue( ArrayFunctions.or( true, false ) );
    Assert.assertFalse( ArrayFunctions.or( false, false ) );
    Assert.assertTrue( ArrayFunctions.or( true, true, true ) );
    Assert.assertTrue( ArrayFunctions.or( true, true, false ) );
    Assert.assertFalse( ArrayFunctions.or( false, false, false ) );

    Assert.assertTrue( ArrayFunctions.objectOr( Boolean.TRUE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectOr( Boolean.FALSE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectOr( Boolean.FALSE, Boolean.FALSE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    Assert.assertTrue( ArrayFunctions.objectOr( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    Assert.assertFalse( ArrayFunctions.objectOr( Boolean.FALSE, Boolean.FALSE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void addAll() {
    
    List<Boolean> receiver = new ArrayList<Boolean>();
    ArrayFunctions.addAll( receiver, Boolean.TRUE );
    
    Assert.assertEquals( receiver.size(), 1 );
    Assert.assertEquals( receiver.get(0), Boolean.TRUE );
    
    ArrayFunctions.addAll( receiver, Boolean.TRUE, Boolean.FALSE, Boolean.FALSE );
    Assert.assertEquals( receiver.size(), 4 );
    Assert.assertEquals( receiver.get(0), Boolean.TRUE );
    Assert.assertEquals( receiver.get(1), Boolean.TRUE );
    Assert.assertEquals( receiver.get(2), Boolean.FALSE );
    Assert.assertEquals( receiver.get(3), Boolean.FALSE );
    
  }
  
  @Test(groups="all")
  public void enumeration() {

    String        str   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    List<String>  list  = new ArrayList<String>();
    for( int i = 0; i < str.length(); i++ ) {
      list.add( String.valueOf( str.charAt(i) ) );
    }
    
    int                 count       = 0;
    Enumeration<String> enumeration = ArrayFunctions.enumeration( list.toArray( new String[ list.size() ] ) );
    while( enumeration.hasMoreElements() ) {
      String current = enumeration.nextElement();
      Assert.assertEquals( current.charAt(0), str.charAt( count ) );
      count++;
    }
    Assert.assertEquals( count, str.length() );
    
  }

  @Test(groups="all")
  public void iterator() {

    String        str   = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    List<String>  list  = new ArrayList<String>();
    for( int i = 0; i < str.length(); i++ ) {
      list.add( String.valueOf( str.charAt(i) ) );
    }
    
    int              count    = 0;
    Iterator<String> iterator = ArrayFunctions.iterator( list.toArray( new String[ list.size() ] ) );
    while( iterator.hasNext() ) {
      String current = iterator.next();
      Assert.assertEquals( current.charAt(0), str.charAt( count ) );
      count++;
    }
    Assert.assertEquals( count, str.length() );
    
  }
  
  @Test(groups="all")
  public void sumInt() {
    Assert.assertEquals( ArrayFunctions.sumInt( (int[]) null ), 0 );
    Assert.assertEquals( ArrayFunctions.sumInt( new int[0] ), 0 );
    Assert.assertEquals( ArrayFunctions.sumInt( 12 ), 12 );
    Assert.assertEquals( ArrayFunctions.sumInt( 12, 23 ), 35 );
    Assert.assertEquals( ArrayFunctions.sumInt( -12, 23 ), 11 );
  }

  @Test(groups="all")
  public void sumLong() {
    Assert.assertEquals( ArrayFunctions.sumLong( (long[]) null ), 0L );
    Assert.assertEquals( ArrayFunctions.sumLong( new long[0] ), 0L );
    Assert.assertEquals( ArrayFunctions.sumLong( 12L ), 12L );
    Assert.assertEquals( ArrayFunctions.sumLong( 12L, 23L ), 35L );
    Assert.assertEquals( ArrayFunctions.sumLong( -12L, 23L ), 11L );
  }

  @Test(groups="all")
  public void sumDouble() {
    Assert.assertEquals( ArrayFunctions.sumDouble( (double[]) null ), 0.0 );
    Assert.assertEquals( ArrayFunctions.sumDouble( new double[0] ), 0.0 );
    Assert.assertEquals( ArrayFunctions.sumDouble( 12.0 ), 12.0 );
    Assert.assertEquals( ArrayFunctions.sumDouble( 12.0, 23.0 ), 35.0 );
    Assert.assertEquals( ArrayFunctions.sumDouble( -12.0, 23.0 ), 11.0 );
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
    Assert.assertEquals( result, expected );
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
    Assert.assertEquals( result, expected );
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
    Assert.assertEquals( index, expectedindex );
  }

  @Test(dataProvider="createIndexOf")
  public void indexOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.indexOf( buffer.getBytes(), characters.getBytes(), offset );
    Assert.assertEquals( index, expectedindex );
  }

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfChars( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.lastIndexOf( buffer.toCharArray(), characters.toCharArray(), offset );
    Assert.assertEquals( index, expectedindex );
  }

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = ArrayFunctions.lastIndexOf( buffer.getBytes(), characters.getBytes(), offset );
    Assert.assertEquals( index, expectedindex );
  }

  @DataProvider(name="joinData")
  public Object[][] joinData() {
    return new Object[][] {
      { new String[][] {}, new String[0] },  
      { new String[][] { new String[0] }, new String[0] },
      { new String[][] { new String[0], new String[0] }, new String[0] },
      { new String[][] { new String[2], new String[0] }, new String[2] },
      { new String[][] { new String[0], new String[2] }, new String[2] },
      { new String[][] { new String[] { null }, new String[] { "Hello", null, "World" } }, new String[] { null, "Hello", null, "World" } },
    };
  }
  
  @Test(dataProvider="joinData", groups="all")
  public void join( String[][] input, String[] expected ) {
    Assert.assertEquals( ArrayFunctions.join( input ), expected );
  }
  
} /* ENDCLASS */
