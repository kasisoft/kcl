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
    Assert.assertEquals( 0, ArrayFunctions.sum( (int[]) null ) );
    Assert.assertEquals( 0, ArrayFunctions.sum( new int[0] ) );
    Assert.assertEquals( 12, ArrayFunctions.sum( 12 ) );
    Assert.assertEquals( 35, ArrayFunctions.sum( 12, 23 ) );
  }

  @Test(groups="all")
  public void sumLong() {
    Assert.assertEquals( 0, ArrayFunctions.sum( (long[]) null ) );
    Assert.assertEquals( 0, ArrayFunctions.sum( new long[0] ) );
    Assert.assertEquals( 12L, ArrayFunctions.sum( 12L ) );
    Assert.assertEquals( 35L, ArrayFunctions.sum( 12L, 23L ) );
  }

  @Test(groups="all")
  public void sumDouble() {
    Assert.assertEquals( 0.0, ArrayFunctions.sum( (double[]) null ) );
    Assert.assertEquals( 0.0, ArrayFunctions.sum( new double[0] ) );
    Assert.assertEquals( 12.0, ArrayFunctions.sum( 12.0 ) );
    Assert.assertEquals( 35.0, ArrayFunctions.sum( 12.0, 23.0 ) );
  }

} /* ENDCLASS */
