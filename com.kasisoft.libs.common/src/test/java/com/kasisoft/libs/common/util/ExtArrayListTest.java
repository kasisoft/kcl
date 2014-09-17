package com.kasisoft.libs.common.util;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the type 'ExtArrayList'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ExtArrayListTest {

  @Test(groups="all")
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
  
  @Test(groups="all")
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
  
  @Test(groups="all")
  public void addSuppressNull() {
    
    ExtArrayList<String> list = new ExtArrayList<String>( true );
    list.addAll( Arrays.asList( "BLA", "BLUB" ) );
    list.add( null );

    Assert.assertEquals( list.size(), 2 );
    
  }
  
  @Test(groups="all")
  public void setSuppressNull() {
    
    ExtArrayList<String> list = new ExtArrayList<String>( true );
    list.addAll( Arrays.asList( "BLA", "BLUB" ) );
    list.set( 0, null );

    Assert.assertEquals( list.size(), 1 );
    
  }
  
  @Test(groups="all")
  public void sublist() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>( "BLA", "BLUB", "BLAU", "KRAUT", "FROG" );
    List<String>         list2 = list1.subList( 1, -1 );
    Assert.assertNotNull( list2 );
    Assert.assertEquals( list2.size(), 3 );
    Assert.assertEquals( list2.get(0), "BLUB" );
    Assert.assertEquals( list2.get(1), "BLAU" );
    Assert.assertEquals( list2.get(2), "KRAUT" );

  }
  
  @Test(groups="all")
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

  @Test(groups="all")
  public void newListSuppressNull() {
    
    ExtArrayList<String> list1 = new ExtArrayList<String>( true );
    Assert.assertEquals( list1.size(), 0 );
    
    ExtArrayList<String> list2 = new ExtArrayList<String>( true, Arrays.asList( "BLA", null, null, "BLUB", null ) );
    Assert.assertEquals( list2.size(), 2 );
    Assert.assertEquals( list2.get(0), "BLA" );
    Assert.assertEquals( list2.get(1), "BLUB" );
    
    ExtArrayList<String> list3 = new ExtArrayList<String>( true, "BLA", null, null, "BLUB", null );
    Assert.assertEquals( list3.size(), 2 );
    Assert.assertEquals( list3.get(0), "BLA" );
    Assert.assertEquals( list3.get(1), "BLUB" );
    
  }

} /* ENDCLASS */
