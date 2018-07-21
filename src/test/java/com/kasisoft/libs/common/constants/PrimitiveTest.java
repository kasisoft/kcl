package com.kasisoft.libs.common.constants;

import static com.kasisoft.libs.common.base.LibConfig.*;
import static com.kasisoft.libs.common.constants.Primitive.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.util.*;

import java.lang.reflect.*;

/**
 * Tests for the constants 'Primitive'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PrimitiveTest {
  
  @DataProvider(name="createCharInsertion")
  public Object[][] createCharInsertion() {
    return new Object[][] {
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(0), "" },  
      { "".toCharArray(), "Hello World".toCharArray(), Integer.valueOf(5), "" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(0), " small Hello World" },  
      { "Hello World".toCharArray(), " small ".toCharArray(), Integer.valueOf(5), "Hello small  World" },  
    };
  }

  @Test(dataProvider="createCharInsertion")
  public void insertChars( char[] dest, char[] insert, int index, String expected ) {
    char[] combined = PChar.insert( dest, insert, index );
    assertThat( new String( combined ), is( expected ) );
  }
  
  @DataProvider(name="createByteInsertion")
  public Object[][] createByteInsertion() {
    return new Object[][] {
      { "".getBytes(), "Hello World".getBytes(), Integer.valueOf(0), "" },  
      { "".getBytes(), "Hello World".getBytes(), Integer.valueOf(5), "" },  
      { "Hello World".getBytes(), " small ".getBytes(), Integer.valueOf(0), " small Hello World" },  
      { "Hello World".getBytes(), " small ".getBytes(), Integer.valueOf(5), "Hello small  World" },  
    };
  }
  
  @Test(dataProvider="createByteInsertion")
  public void insertBytes( byte[] dest, byte[] insert, int index, String expected ) {
    byte[] combined = PByte.insert( dest, insert, index );
    assertThat( new String( combined ), is( expected ) );
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

  @DataProvider(name="createByteBuffers")
  public Object[][] createByteBuffers() {
    return new Object[][] {
      { toByteArray( "Hello" ), "Hello" },
      { toByteArray( "Hello", null, " ", "World" ), "Hello World" },
      { toByteArray( "Hello", " ", "World" ), "Hello World" },
    };
  }

  @Test(dataProvider="createByteBuffers")
  public void joinByteBuffers( byte[][] buffers, String expected ) {
    byte[] joined = PByte.concat( buffers );
    assertThat( new String( joined ), is( expected ) );
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

  @DataProvider(name="createCharBuffers")
  public Object[][] createCharBuffers() {
    return new Object[][] {
      { toCharArray( "Hello" ), "Hello" },
      { toCharArray( "Hello", null, " ", "World" ), "Hello World" },
      { toCharArray( "Hello", " ", "World" ), "Hello World" },
    };
  }

  @Test(dataProvider="createCharBuffers")
  public void joinCharBuffers( char[][] buffers, String expected ) {
    char[] joined = PChar.concat( buffers );
    assertThat( new String( joined ), is( expected ) );
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

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfChars( String buffer, String characters, int offset, int expectedindex ) {
    int index = PChar.lastIndexOf( buffer.toCharArray(), characters.toCharArray(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @Test(dataProvider="createLastIndexOf")
  public void indexLastOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = PByte.lastIndexOf( buffer.getBytes(), characters.getBytes(), offset );
    assertThat( index, is( expectedindex ) );
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

  @Test(dataProvider="createIndexOf")
  public void indexOfChars( String buffer, String characters, int offset, int expectedindex ) {
    int index = PChar.indexOf( buffer.toCharArray(), characters.toCharArray(), offset );
    assertThat( index, is( expectedindex ) );
  }

  @Test(dataProvider="createIndexOf")
  public void indexOfBytes( String buffer, String characters, int offset, int expectedindex ) {
    int index = PByte.indexOf( buffer.getBytes(), characters.getBytes(), offset );
    assertThat( index, is( expectedindex ) );
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
    boolean result = PByte.compare( buffer, sequence, offset );
    assertThat( result, is( expected ) );
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
    boolean result = PChar.compare( buffer, sequence, offset );
    assertThat( result, is( expected ) );
  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void min() {
    
    assertThat( PInt.min( new int[] { 12 } ), is( 12 ) );
    assertThat( PInt.min( new int[] { -477, 32 } ), is( -477 ) );
    assertThat( PInt.min( new int[] { -477, 32, 2778 } ), is( -477 ) );

    assertThat( PLong.min( new long[] { 12 } ), is( 12L ) );
    assertThat( PLong.min( new long[] { -477, 32 } ), is( -477L ) );
    assertThat( PLong.min( new long[] { -477, 32, 2778 } ), is( -477L ) );

    assertThat( (float) PFloat.min( new float[] { (float) 12.0 } ), is( (float) 12.0 ) );
    assertThat( (float) PFloat.min( new float[] { (float) -477, (float) 32 } ), is( (float) -477.0 ) );
    assertThat( (float) PFloat.min( new float[] { (float) -477, (float) 32, (float) 2778 } ), is( (float) -477.0 ) );

    assertThat( (double) PDouble.min( new double[] { 12.0 } ), is( 12.0 ) );
    assertThat( (double) PDouble.min( new double[] { -477.0, 32.0 } ), is( -477.0 ) );
    assertThat( (double) PDouble.min( new double[] { -477.0, 32.0, 2778.0 } ), is( -477.0 ) );

  }

  @SuppressWarnings({ "boxing", "cast" })
  @Test(groups="all")
  public void max() {
    
    assertThat( PInt.max( new int[] { 12 } ), is( 12 ) );
    assertThat( PInt.max( new int[] { -477, 32 } ), is( 32 ) );
    assertThat( PInt.max( new int[] { -477, 32, 2778 } ), is( 2778 ) );

    assertThat( PLong.max( new long[] { 12 } ), is( 12L ) );
    assertThat( PLong.max( new long[] { -477, 32 } ), is( 32L ) );
    assertThat( PLong.max( new long[] { -477, 32, 2778 } ), is( 2778L ) );

    assertThat( (float) PFloat.max( new float[] { (float) 12.0 } ), is( (float) 12.0 ) );
    assertThat( (float) PFloat.max( new float[] { (float) -477, (float) 32 } ), is( (float) 32.0 ) );
    assertThat( (float) PFloat.max( new float[] { (float) -477, (float) 32, (float) 2778 } ), is( (float) 2778.0 ) );

    assertThat( (double) PDouble.max( new double[] { 12.0 } ), is( 12.0 ) );
    assertThat( (double) PDouble.max( new double[] { -477.0, 32.0 } ), is( 32.0 ) );
    assertThat( (double) PDouble.max( new double[] { -477.0, 32.0, 2778.0 } ), is( 2778.0 ) );

  }
  
  private <T> List<T> list( T ... objects ) {
    return Arrays.asList( objects );
  }
  
  @Test(groups="all")
  public void toList() {
    
    // Boolean
    assertThat( PBoolean.toList( new boolean[] {} ), is( this.<Boolean>list() ) );
    assertThat( PBoolean.toList( new boolean[] { true } ), is( list( Boolean.TRUE ) ) );
    assertThat( PBoolean.toList( new boolean[] { false, true } ), is( list( Boolean.FALSE, Boolean.TRUE ) ) );

    // Char
    assertThat( PChar.toList( new char[] {} ), is( this.<Character>list() ) );
    assertThat( PChar.toList( new char[] { '\n' } ), is( list( Character.valueOf('\n') ) ) );
    assertThat( PChar.toList( new char[] { '\r', '\n' } ), is( list( Character.valueOf('\r'), Character.valueOf('\n') ) ) );

    // Byte
    assertEquals( PByte.toList( new byte[] {} ), new ArrayList<Byte>() );
    assertEquals( PByte.toList( new byte[] { (byte) 0 } ), list( Byte.valueOf((byte) 0) ) );
    assertEquals( PByte.toList( new byte[] { (byte) 1, (byte) 0 } ), list( Byte.valueOf( (byte) 1), Byte.valueOf( (byte) 0) ) );

    // Short
    assertThat( PShort.toList( new short[] {} ), is( this.<Short>list() ) );
    assertThat( PShort.toList( new short[] { (short) 0 } ), is( list( Short.valueOf( (short) 0) ) ) );
    assertThat( PShort.toList( new short[] { (short) 1, (short) 0 } ), is( list( Short.valueOf( (short) 1), Short.valueOf( (short) 0) ) ) );

    // Integer
    assertThat( PInt.toList( new int[] {} ), is( this.<Integer>list() ) );
    assertThat( PInt.toList( new int[] { 0 } ), is( list( Integer.valueOf(0) ) ) );
    assertThat( PInt.toList( new int[] { 1, 0 } ), is( list( Integer.valueOf(1), Integer.valueOf(0) ) ) );

    // Long
    assertThat( PLong.toList( new long[] {} ), is( this.<Long>list() ) );
    assertThat( PLong.toList( new long[] { 0 } ), is( list( Long.valueOf(0) ) ) );
    assertThat( PLong.toList( new long[] { 1, 0 } ), is( list( Long.valueOf(1), Long.valueOf(0) ) ) );

    // Float
    assertThat( PFloat.toList( new float[] {} ), is( this.<Float>list() ) );
    assertThat( PFloat.toList( new float[] { 0 } ), is( list( Float.valueOf(0) ) ) );
    assertThat( PFloat.toList( new float[] { 1, 0 } ), is( list( Float.valueOf(1), Float.valueOf(0) ) ) );

    // Double
    assertThat( PDouble.toList( new double[] {} ), is( this.<Double>list() ) );
    assertThat( PDouble.toList( new double[] { 0 } ), is( list( Double.valueOf(0) ) ) );
    assertThat( PDouble.toList( new double[] { 1, 0 } ), is( list( Double.valueOf(1), Double.valueOf(0) ) ) );

  }

  private double add( double a, double b ) {
    return a + b;
  }
  
  @Test(groups="all")
  public void sumLong() {
    assertThat( PLong.forValues( (long[]) null, 0L, Math::addExact ), is(0L) );
    assertThat( PLong.forValues( new long[0], 0L, Math::addExact ), is(0L) );
    assertThat( PLong.forValues( new long[] { 12L }, 0L, Math::addExact ), is(12L) );
    assertThat( PLong.forValues( new long[] { 12L, 23L }, 0L, Math::addExact ), is(35L) );
    assertThat( PLong.forValues( new long[] { -12L, 23L }, 0L, Math::addExact ), is(11L) );
  }

  @Test(groups="all")
  public void sumDouble() {
    assertThat( PDouble.forValues( (double[]) null, 0.0, this::add ), is(0.0) );
    assertThat( PDouble.forValues( new double[0], 0.0, this::add ), is(0.0) );
    assertThat( PDouble.forValues( new double[] { 12.0 }, 0.0, this::add ), is(12.0) );
    assertThat( PDouble.forValues( new double[] { 12.0, 23.0 }, 0.0, this::add ), is(35.0) );
    assertThat( PDouble.forValues( new double[] { -12.0, 23.0 }, 0.0, this::add ), is(11.0) );
  }

  @Test(groups="all")
  public void sumInt() {
    assertThat( PInt.forValues( (int[]) null, 0, Math::addExact ), is(0) );
    assertThat( PInt.forValues( new int[0], 0, Math::addExact ), is(0) );
    assertThat( PInt.forValues( new int[] { 12 }, 0, Math::addExact ), is(12) );
    assertThat( PInt.forValues( new int[] { 12, 23 }, 0, Math::addExact ), is(35) );
    assertThat( PInt.forValues( new int[] { -12, 23 }, 0, Math::addExact ), is(11) );
  }

  @Test(groups="all")
  public void or() {
    
    assertTrue( PBoolean.or( new boolean[] { true } ) );
    assertFalse( PBoolean.or( new boolean[] {  false } ) );
    assertTrue( PBoolean.or( new boolean[] { true, true } ) );
    assertTrue( PBoolean.or( new boolean[] { true, false } ) );
    assertFalse( PBoolean.or( new boolean[] { false, false } ) );
    assertTrue( PBoolean.or( new boolean[] { true, true, true } ) );
    assertTrue( PBoolean.or( new boolean[] { true, true, false } ) );
    assertFalse( PBoolean.or( new boolean[] { false, false, false } ) );

    assertTrue( PBoolean.or( Boolean.TRUE ).booleanValue() );
    assertFalse( PBoolean.or( Boolean.FALSE ).booleanValue() );
    assertTrue( PBoolean.or( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertTrue( PBoolean.or( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertFalse( PBoolean.or( Boolean.FALSE, Boolean.FALSE ).booleanValue() );
    assertTrue( PBoolean.or( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertTrue( PBoolean.or( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertFalse( PBoolean.or( Boolean.FALSE, Boolean.FALSE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void and() {
    
    assertTrue( PBoolean.and( new boolean[] { true } ) );
    assertFalse( PBoolean.and( new boolean[] { false } ) );
    assertTrue( PBoolean.and( new boolean[] { true, true } ) );
    assertFalse( PBoolean.and( new boolean[] { true, false } ) );
    assertTrue( PBoolean.and( new boolean[] { true, true, true } ) );
    assertFalse( PBoolean.and( new boolean[] { true, true, false } ) );

    assertTrue( PBoolean.and( Boolean.TRUE ).booleanValue() );
    assertFalse( PBoolean.and( Boolean.FALSE ).booleanValue() );
    assertTrue( PBoolean.and( Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertFalse( PBoolean.and( Boolean.TRUE, Boolean.FALSE ).booleanValue() );
    assertTrue( PBoolean.and( Boolean.TRUE, Boolean.TRUE, Boolean.TRUE ).booleanValue() );
    assertFalse( PBoolean.and( Boolean.TRUE, Boolean.TRUE, Boolean.FALSE ).booleanValue() );

  }

  @Test(groups="all")
  public void toPrimitiveArray() {
    
    // Boolean
    assertThat( PBoolean.toPrimitiveArray( (Boolean[]) null ), is( nullValue() ) );
    assertThat( PBoolean.toPrimitiveArray( new Boolean[0] ), is( notNullValue() ) );
    assertThat( PBoolean.toPrimitiveArray( new Boolean[] { Boolean.TRUE } ), is( new boolean[] { true } ) );
    assertThat( PBoolean.toPrimitiveArray( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), is( new boolean[] { false, true } ) );
    assertThat( PBoolean.toPrimitiveArray( new Boolean[] { Boolean.FALSE, null, Boolean.TRUE } ), is( new boolean[] { false, true } ) );

    // Char
    assertThat( PChar.toPrimitiveArray( (Character[]) null ), is( nullValue() ) );
    assertThat( PChar.toPrimitiveArray( new Character[0] ), is( notNullValue() ) );
    assertThat( PChar.toPrimitiveArray( new Character[] { Character.valueOf('\n') } ), is( new char[] { '\n' } ) );
    assertThat( PChar.toPrimitiveArray( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), is( new char[] { '\r', '\n' } ) );
    assertThat( PChar.toPrimitiveArray( new Character[] { Character.valueOf('\r'), null, Character.valueOf('\n') } ), is( new char[] { '\r', '\n' } ) );

    // Byte
    assertThat( PByte.toPrimitiveArray( (Byte[]) null ), is( nullValue() ) );
    assertThat( PByte.toPrimitiveArray( new Byte[] {} ), is( notNullValue() ) );
    assertThat( PByte.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 0 } ) );
    assertThat( PByte.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 1, (byte) 0 } ) );
    assertThat( PByte.toPrimitiveArray( new Byte[] { Byte.valueOf((byte) 1), null, Byte.valueOf((byte) 0) } ), is( new byte[] { (byte) 1, (byte) 0 } ) );

    // Short
    assertThat( PShort.toPrimitiveArray( (Short[]) null ), is( nullValue() ) );
    assertThat( PShort.toPrimitiveArray( new Short[] {} ), is( notNullValue() ) );
    assertThat( PShort.toPrimitiveArray( new Short[] { Short.valueOf((short) 0) } ), is( new short[] { (short) 0 } ) );
    assertThat( PShort.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), is( new short[] { (short) 1, (short) 0 } ) );
    assertThat( PShort.toPrimitiveArray( new Short[] { Short.valueOf((short) 1), null, Short.valueOf((short) 0) } ), is( new short[] { (short) 1, (short) 0 } ) );

    // Integer
    assertThat( PInt.toPrimitiveArray( (Integer[]) null ), is( nullValue() ) );
    assertThat( PInt.toPrimitiveArray( new Integer[] {} ), is( notNullValue() ) );
    assertThat( PInt.toPrimitiveArray( new Integer[] { Integer.valueOf(0) } ), is( new int[] { 0 } ) );
    assertThat( PInt.toPrimitiveArray( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), is( new int[] { 1, 0 } ) );
    assertThat( PInt.toPrimitiveArray( new Integer[] { Integer.valueOf(1), null, Integer.valueOf(0) } ), is( new int[] { 1, 0 } ) );

    // Long
    assertThat( PLong.toPrimitiveArray( (Long[]) null ), is( nullValue() ) );
    assertThat( PLong.toPrimitiveArray( new Long[] {} ), is( notNullValue() ) );
    assertThat( PLong.toPrimitiveArray( new Long[] { Long.valueOf(0) } ), is( new long[] { 0 } ) );
    assertThat( PLong.toPrimitiveArray( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), is( new long[] { 1, 0 } ) );
    assertThat( PLong.toPrimitiveArray( new Long[] { Long.valueOf(1), null, Long.valueOf(0) } ), is( new long[] { 1, 0 } ) );

    // Float
    assertThat( PFloat.toPrimitiveArray( (Float[]) null ), is( nullValue() ) );
    assertThat( PFloat.toPrimitiveArray( new Float[] {} ), is( notNullValue() ) );
    assertThat( PFloat.toPrimitiveArray( new Float[] { Float.valueOf(0) } ), is( new float[] { 0 } ) );
    assertThat( PFloat.toPrimitiveArray( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), is( new float[] { 1, 0 } ) );
    assertThat( PFloat.toPrimitiveArray( new Float[] { Float.valueOf(1), null, Float.valueOf(0) } ), is( new float[] { 1, 0 } ) );

    // Double
    assertThat( PDouble.toPrimitiveArray( (Double[]) null ), is( nullValue() ) );
    assertThat( PDouble.toPrimitiveArray( new Double[] {} ), is( notNullValue() ) );
    assertThat( PDouble.toPrimitiveArray( new Double[] { Double.valueOf(0) } ), is( new double[] { 0 } ) );
    assertThat( PDouble.toPrimitiveArray( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), is( new double[] { 1, 0 } ) );
    assertThat( PDouble.toPrimitiveArray( new Double[] { Double.valueOf(1), null, Double.valueOf(0) } ), is( new double[] { 1, 0 } ) );

  }

  @Test(groups="all")
  public void toObjectArray() {
    
    // Boolean
    assertThat( PBoolean.toObjectArray( (boolean[]) null ), is( nullValue() ) );
    assertThat( PBoolean.toObjectArray( new boolean[] {} ), is( notNullValue() ) );
    assertThat( PBoolean.toObjectArray( new boolean[] { true } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( PBoolean.toObjectArray( new boolean[] { false, true } ), is( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ) );

    // Char
    assertThat( PChar.toObjectArray( (char[]) null ), is( nullValue() ) );
    assertThat( PChar.toObjectArray( new char[] {} ), is( notNullValue() ) );
    assertThat( PChar.toObjectArray( new char[] { '\n' } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( PChar.toObjectArray( new char[] { '\r', '\n' } ), is( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ) );

    // Byte
    assertThat( PByte.toObjectArray( (byte[]) null ), is( nullValue() ) );
    assertThat( PByte.toObjectArray( new byte[] {} ), is( notNullValue() ) );
    assertThat( PByte.toObjectArray( new byte[] { (byte) 0 } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( PByte.toObjectArray( new byte[] { (byte) 1, (byte) 0 } ), is( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ) );

    // Short
    assertThat( PShort.toObjectArray( (short[]) null ), is( nullValue() ) );
    assertThat( PShort.toObjectArray( new short[] {} ), is( notNullValue() ) );
    assertThat( PShort.toObjectArray( new short[] { (short) 0 } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( PShort.toObjectArray( new short[] { (short) 1, (short) 0 } ), is( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ) );

    // Integer
    assertThat( PInt.toObjectArray( (int[]) null ), is( nullValue() ) );
    assertThat( PInt.toObjectArray( new int[] {} ), is( notNullValue() ) );
    assertThat( PInt.toObjectArray( new int[] { 0 } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( PInt.toObjectArray( new int[] { 1, 0 } ), is( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ) );

    // Long
    assertThat( PLong.toObjectArray( (long[]) null ), is( nullValue() ) );
    assertThat( PLong.toObjectArray( new long[] {} ), is( notNullValue() ) );
    assertThat( PLong.toObjectArray( new long[] { 0 } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( PLong.toObjectArray( new long[] { 1, 0 } ), is( new Long[] { Long.valueOf(1), Long.valueOf(0) } ) );

    // Float
    assertThat( PFloat.toObjectArray( (float[]) null ), is( nullValue() ) );
    assertThat( PFloat.toObjectArray( new float[] {} ), is( notNullValue() ) );
    assertThat( PFloat.toObjectArray( new float[] { 0 } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( PFloat.toObjectArray( new float[] { 1, 0 } ), is( new Float[] { Float.valueOf(1), Float.valueOf(0) } ) );

    // Double
    assertThat( PDouble.toObjectArray( (double[]) null ), is( nullValue() ) );
    assertThat( PDouble.toObjectArray( new double[] {} ), is( notNullValue() ) );
    assertThat( PDouble.toObjectArray( new double[] { 0 } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( PDouble.toObjectArray( new double[] { 1, 0 } ), is( new Double[] { Double.valueOf(1), Double.valueOf(0) } ) );

  }
  
  @Test(groups="all")
  public void cleanup() {
    
    // Boolean
    assertThat( PBoolean.cleanup( (Boolean[]) null ), is( nullValue() ) );
    assertThat( PBoolean.cleanup( new Boolean[] { null, null } ), is( nullValue() ) );
    assertThat( PBoolean.cleanup( new Boolean[] { null, Boolean.TRUE } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( PBoolean.cleanup( new Boolean[] { Boolean.TRUE, null } ), is( new Boolean[] { Boolean.TRUE } ) );
    assertThat( PBoolean.cleanup( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ), is( new Boolean[] { Boolean.FALSE, Boolean.TRUE } ) );

    // Char
    assertThat( PChar.cleanup( (Character[]) null ), is( nullValue() ) );
    assertThat( PChar.cleanup( new Character[] { null, null } ), is( nullValue() ) );
    assertThat( PChar.cleanup( new Character[] { null, Character.valueOf('\n') } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( PChar.cleanup( new Character[] { Character.valueOf('\n'), null } ), is( new Character[] { Character.valueOf('\n') } ) );
    assertThat( PChar.cleanup( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ), is( new Character[] { Character.valueOf('\r'), Character.valueOf('\n') } ) );

    // Byte
    assertThat( PByte.cleanup( (Byte[]) null ), is( nullValue() ) );
    assertThat( PByte.cleanup( new Byte[] { null, null } ), is( nullValue() ) );
    assertThat( PByte.cleanup( new Byte[] { null, Byte.valueOf((byte) 0) } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( PByte.cleanup( new Byte[] { Byte.valueOf((byte) 0), null } ), is( new Byte[] { Byte.valueOf((byte) 0) } ) );
    assertThat( PByte.cleanup( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ), is( new Byte[] { Byte.valueOf((byte) 1), Byte.valueOf((byte) 0) } ) );

    // Short
    assertThat( PShort.cleanup( (Short[]) null ), is( nullValue() ) );
    assertThat( PShort.cleanup( new Short[] { null, null } ), is( nullValue() ) );
    assertThat( PShort.cleanup( new Short[] { null, Short.valueOf((short) 0) } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( PShort.cleanup( new Short[] { Short.valueOf((short) 0), null } ), is( new Short[] { Short.valueOf((short) 0) } ) );
    assertThat( PShort.cleanup( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ), is( new Short[] { Short.valueOf((short) 1), Short.valueOf((short) 0) } ) );

    // Integer
    assertThat( PInt.cleanup( (Integer[]) null ), is( nullValue() ) );
    assertThat( PInt.cleanup( new Integer[] { null, null } ), is( nullValue() ) );
    assertThat( PInt.cleanup( new Integer[] { null, Integer.valueOf(0) } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( PInt.cleanup( new Integer[] { Integer.valueOf(0), null } ), is( new Integer[] { Integer.valueOf(0) } ) );
    assertThat( PInt.cleanup( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ), is( new Integer[] { Integer.valueOf(1), Integer.valueOf(0) } ) );

    // Long
    assertThat( PLong.cleanup( (Long[]) null ), is( nullValue() ) );
    assertThat( PLong.cleanup( new Long[] { null, null } ), is( nullValue() ) );
    assertThat( PLong.cleanup( new Long[] { null, Long.valueOf(0) } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( PLong.cleanup( new Long[] { Long.valueOf(0), null } ), is( new Long[] { Long.valueOf(0) } ) );
    assertThat( PLong.cleanup( new Long[] { Long.valueOf(1), Long.valueOf(0) } ), is( new Long[] { Long.valueOf(1), Long.valueOf(0) } ) );

    // Float
    assertThat( PFloat.cleanup( (Float) null ), is( nullValue() ) );
    assertThat( PFloat.cleanup( new Float[] { null, null } ), is( nullValue() ) );
    assertThat( PFloat.cleanup( new Float[] { null, Float.valueOf(0) } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( PFloat.cleanup( new Float[] { Float.valueOf(0), null } ), is( new Float[] { Float.valueOf(0) } ) );
    assertThat( PFloat.cleanup( new Float[] { Float.valueOf(1), Float.valueOf(0) } ), is( new Float[] { Float.valueOf(1), Float.valueOf(0) } ) );

    // Double
    assertThat( PDouble.cleanup( (Double) null ), is( nullValue() ) );
    assertThat( PDouble.cleanup( new Double[] { null, null } ), is( nullValue() ) );
    assertThat( PDouble.cleanup( new Double[] { null, Double.valueOf(0) } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( PDouble.cleanup( new Double[] { Double.valueOf(0), null } ), is( new Double[] { Double.valueOf(0) } ) );
    assertThat( PDouble.cleanup( new Double[] { Double.valueOf(1), Double.valueOf(0) } ), is( new Double[] { Double.valueOf(1), Double.valueOf(0) } ) );

  }
  
  @Test(groups="all")
  public void countSet() {
    assertThat( PInt.countSet(), is(0) );
    assertThat( PInt.countSet( (Integer) null ), is(0) );
    assertThat( PInt.countSet( (Integer) null, (Integer) null ), is(0) );
    assertThat( PInt.countSet( (Integer) null, 13 ), is(1) );
    assertThat( PInt.countSet( 15, null ), is(1) );
    assertThat( PInt.countSet( 16, 24 ), is(2) );
  }

  @DataProvider(name="createData")
  public Object[][] createData() {
    Primitive[] values = Primitive.values();
    Object[][]  result = new Object[ values.length ][];
    for( int i = 0; i < values.length; i++ ) {
      result[i] = new Object[] { values[i], Integer.valueOf( 20 ) };
    }
    return result;
  }

  @Test(dataProvider="createData",groups="all")
  public void arrayCreation( Primitive primitive, Integer size ) {
    assertThat( primitive.getArrayClass     (), is( notNullValue() ) );
    assertThat( primitive.getObjectClass    (), is( notNullValue() ) );
    assertThat( primitive.getPrimitiveClass (), is( notNullValue() ) );
    Object array = primitive.newArray( size.intValue() );
    assertThat( primitive.length( array ), is( size.intValue() ) );
  }
  
  @Test(dataProvider="createData",groups="all")
  public void arrayObjectCreation( Primitive primitive, Integer size ) {
    assertThat( primitive.getArrayClass     (), is( notNullValue() ) );
    assertThat( primitive.getObjectClass    (), is( notNullValue() ) );
    assertThat( primitive.getPrimitiveClass (), is( notNullValue() ) );
    Object array = primitive.newObjectArray( size.intValue() );
    assertThat( primitive.length( array ), is( size.intValue() ) );
  }

  @DataProvider(name="createTypes")
  public Object[][] createTypes() {
    Primitive[] primitives = Primitive.values();
    Object[][]  result     = new Object[ primitives.length + 1 ][];
    for( int i = 0; i < primitives.length; i++ ) {
      result[i] = new Object[] { primitives[i].newArray(0), primitives[i] };
    }
    result[ result.length - 1 ] = new Object[] { new String[0], null };
    return result;
  }
  
  @DataProvider(name="createObjectTypes")
  public Object[][] createObjectTypes() {
    return new Object[][] {
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      { ""                                          , null                 }
    };
  }

  @DataProvider(name="createAllTypes")
  public Object[][] createAllTypes() {
    return new Object[][] {
        
      // object types
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      
      // array of object types
      { new Boolean   [0], Primitive . PBoolean },
      { new Byte      [0], Primitive . PByte    },
      { new Character [0], Primitive . PChar    },
      { new Short     [0], Primitive . PShort   },
      { new Integer   [0], Primitive . PInt     },
      { new Long      [0], Primitive . PLong    },
      { new Float     [0], Primitive . PFloat   },
      { new Double    [0], Primitive . PDouble  },

      // array of primitive types
      { new boolean   [0], Primitive . PBoolean },
      { new byte      [0], Primitive . PByte    },
      { new char      [0], Primitive . PChar    },
      { new short     [0], Primitive . PShort   },
      { new int       [0], Primitive . PInt     },
      { new long      [0], Primitive . PLong    },
      { new float     [0], Primitive . PFloat   },
      { new double    [0], Primitive . PDouble  },

      // non-primitive related
      { ""   , null },
      
      // null argument
      { null , null }
      
    };
  }

  @Test(dataProvider="createAllTypes",groups="all")
  public void byType( Object array, Primitive expected ) {
    assertThat( Primitive.byType( array ), is( expected ) );
  }

  @DataProvider(name="createRunAllocations")
  public Object[][] createRunAllocations() {
    return new Object[][] {
        
      // object types
      { Boolean   . valueOf( true                  ), Primitive . PBoolean },
      { Byte      . valueOf( Byte      . MIN_VALUE ), Primitive . PByte    },
      { Character . valueOf( Character . MIN_VALUE ), Primitive . PChar    },
      { Short     . valueOf( Short     . MIN_VALUE ), Primitive . PShort   },
      { Integer   . valueOf( Integer   . MIN_VALUE ), Primitive . PInt     },
      { Long      . valueOf( Long      . MIN_VALUE ), Primitive . PLong    },
      { Float     . valueOf( Float     . MIN_VALUE ), Primitive . PFloat   },
      { Double    . valueOf( Double    . MIN_VALUE ), Primitive . PDouble  },
      
      // array of object types
      { new Boolean   [0], Primitive . PBoolean },
      { new Byte      [0], Primitive . PByte    },
      { new Character [0], Primitive . PChar    },
      { new Short     [0], Primitive . PShort   },
      { new Integer   [0], Primitive . PInt     },
      { new Long      [0], Primitive . PLong    },
      { new Float     [0], Primitive . PFloat   },
      { new Double    [0], Primitive . PDouble  },

      // array of primitive types
      { new boolean   [0], Primitive . PBoolean },
      { new byte      [0], Primitive . PByte    },
      { new char      [0], Primitive . PChar    },
      { new short     [0], Primitive . PShort   },
      { new int       [0], Primitive . PInt     },
      { new long      [0], Primitive . PLong    },
      { new float     [0], Primitive . PFloat   },
      { new double    [0], Primitive . PDouble  },

    };
  }
  
  @Test(dataProvider="createRunAllocations", groups="all")
  public void allocate( Object array, Primitive expected ) {
    
    Primitive  primitive  = Primitive.byType( array );
    assertThat( primitive, is( expected ) );
    
    Integer    count      = cfgBufferSize();
    
    Object     datablock1 = primitive.allocate();
    assertThat( datablock1, is( notNullValue() ) );
    assertTrue( primitive.length( datablock1 ) >= count.intValue() );
    
    Object     datablock2 = primitive.allocate( null );
    assertThat( datablock2, is( notNullValue() ) );
    assertTrue( primitive.length( datablock2 ) >= count.intValue() );
    
    Object     datablock3 = primitive.allocate( Integer.valueOf( 100 ) );
    assertThat( datablock3, is( notNullValue() ) );
    assertTrue( primitive.length( datablock3 ) >= 100 );
    assertTrue( primitive.length( datablock3 ) < count.intValue() );
    
  }

  @DataProvider(name="createCompareSimpleData")
  public Object[][] createCompareSimpleData() {
    List<Object[]> result = new ArrayList<>();
    for( Primitive p : Primitive.values() ) {
      
      // compare identical arrays
      Object array      = p.randomArray(13);
      Object exactcopy  = p.copy( array );
      result.add( new Object[] { array, exactcopy, true } );

      // compare different arrays
      Object failcopy   = p.copy( array );
      changeValue( failcopy, 7, p );
      result.add( new Object[] { array, failcopy, false } );
      
    }
    return result.toArray(new Object[result.size()][]);
  }
  
  @Test(dataProvider="createCompareSimpleData", groups="all")
  public void compareSimple( Object array1, Object array2, boolean expected ) {
    Primitive primitive = Primitive.byType( array1 );
    assertThat( primitive.compare( array1, array2 ), is( expected ) );
  }

  @DataProvider(name="createCompareWithOffsetData")
  public Object[][] createCompareWithOffsetData() {
    List<Object[]> result = new ArrayList<>();
    for( Primitive p : Primitive.values() ) {
      
      // compare identical arrays
      Object array      = p.randomArray(13);
      Object exactcopy  = p.copyOfRange( array, 4, 13 );
      result.add( new Object[] { array, exactcopy, true } );

      // compare different arrays
      Object failcopy   = p.copyOfRange( array, 4, 13 );
      changeValue( failcopy, 2, p );
      result.add( new Object[] { array, failcopy, false } );
      
      // break;
      
    }
    return result.toArray(new Object[result.size()][]);
  }
  
  private void changeValue( Object array, int idx, Primitive p ) {
    Object current = Array.get( array, idx );
    Object changed = p.randomValue();
    while( current.equals( changed ) ) {
      changed = p.randomValue();
    }
    Array.set( array, idx, changed );
  }

  @Test(dataProvider="createCompareWithOffsetData", groups="all")
  public void compareWithOffset( Object array1, Object array2, boolean expected ) {
    Primitive primitive = Primitive.byType( array1 );
    assertThat( primitive.compare( array1, array2, 4 ), is( expected ) );
  }

} /* ENDCLASS */
