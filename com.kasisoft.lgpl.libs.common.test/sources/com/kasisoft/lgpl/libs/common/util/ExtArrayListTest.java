/**
 * Name........: ExtArrayListTest
 * Description.: Tests for the type 'ExtArrayList'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the type 'ExtArrayList'.
 */
@Test(groups="all")
public class ExtArrayListTest {

  @Test
  public void newList() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>();
    Assert.assertEquals( list1.size(), 0 );
    
    ExtArrayList<String> list2 = new ExtArrayList<String>( Arrays.asList( "BLA", "BLUB" ) );
    Assert.assertEquals( list2.size(), 2 );
    Assert.assertEquals( list2.get(0), "BLA" );
    Assert.assertEquals( list2.get(1), "BLUB" );
    
    ExtArrayList<String> list3 = new ExtArrayList<String>( "BLA", "BLUB" );
    Assert.assertEquals( list3.size(), 2 );
    Assert.assertEquals( list3.get(0), "BLA" );
    Assert.assertEquals( list3.get(1), "BLUB" );
    
  }
  
  @Test
  public void add() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>();
    list1.add( "BLA" );
    
    list1.add( 0, "BLUB" );
    Assert.assertEquals( list1.size(), 2 );
    Assert.assertEquals( list1.get(0), "BLUB" );
    Assert.assertEquals( list1.get(1), "BLA" );
    
    list1.add( -1, "FROG" );
    Assert.assertEquals( list1.size(), 3 );
    Assert.assertEquals( list1.get(0), "BLUB" );
    Assert.assertEquals( list1.get(1), "FROG" );
    Assert.assertEquals( list1.get(2), "BLA" );

    list1.addAll( -1, "BLAU", "KRAUT" );
    Assert.assertEquals( list1.size(), 5 );
    Assert.assertEquals( list1.get(0), "BLUB" );
    Assert.assertEquals( list1.get(1), "FROG" );
    Assert.assertEquals( list1.get(2), "BLAU" );
    Assert.assertEquals( list1.get(3), "KRAUT" );
    Assert.assertEquals( list1.get(4), "BLA" );

  }
  
  @Test
  public void sublist() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>( "BLA", "BLUB", "BLAU", "KRAUT", "FROG" );
    List<String>         list2 = list1.subList( 1, -1 );
    Assert.assertNotNull( list2 );
    Assert.assertEquals( list2.size(), 3 );
    Assert.assertEquals( list2.get(0), "BLUB" );
    Assert.assertEquals( list2.get(1), "BLAU" );
    Assert.assertEquals( list2.get(2), "KRAUT" );

  }
  
  @Test
  public void trim() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>( null, null, null, "BLA", "BLUB", "BLAU", "KRAUT", "FROG", null, "", null );
    list1.trim();
    Assert.assertEquals( list1.size(), 7 );
    Assert.assertEquals( list1.get(0), "BLA" );
    Assert.assertEquals( list1.get(1), "BLUB" );
    Assert.assertEquals( list1.get(2), "BLAU" );
    Assert.assertEquals( list1.get(3), "KRAUT" );
    Assert.assertEquals( list1.get(4), "FROG" );
    Assert.assertNull( list1.get(5) );
    Assert.assertEquals( list1.get(6), "" );
    
  }
  
} /* ENDCLASS */
