/**
 * Name........: ArrayFunctionsTest
 * Description.: Testcases for the class 'ArrayFunctions'. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.util;

import com.kasisoft.lgpl.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Testcases for the class 'ArrayFunctions'.
 */
@Test(groups="all")
public class ArrayFunctionsTest {

  @Test
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

  @Test
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

  @Test
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

  @Test
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

  @Test
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
  
  @Test
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

  @Test
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

} /* ENDCLASS */
