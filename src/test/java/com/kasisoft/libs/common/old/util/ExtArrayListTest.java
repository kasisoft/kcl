package com.kasisoft.libs.common.old.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the type 'ExtArrayList'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ExtArrayListTest {

  @Test(groups="all")
  public void newList() {
    
    ExtArrayList<String> list1 = new ExtArrayList<>();
    assertThat( list1.size(), is(0) );
    
    ExtArrayList<String> list2 = new ExtArrayList<>( Arrays.asList( "BLA", "BLUB" ) );
    assertThat( list2.size(), is(2) );
    assertThat( list2.get(0), is( "BLA"  ) );
    assertThat( list2.get(1), is( "BLUB" ) );
    
    ExtArrayList<String> list3 = new ExtArrayList<>( "BLA", "BLUB" );
    assertThat( list3.size(), is(2) );
    assertThat( list3.get(0), is( "BLA"  ) );
    assertThat( list3.get(1), is( "BLUB" ) );
    
  }
  
  @Test(groups="all")
  public void add() {
    
    ExtArrayList<String> list1 = new ExtArrayList<>();
    list1.add( "BLA" );
    
    list1.add( 0, "BLUB" );
    assertThat( list1.size(), is(2) );
    assertThat( list1.get(0), is( "BLUB" ) );
    assertThat( list1.get(1), is( "BLA"  ) );
    
    list1.add( -1, "FROG" );
    assertThat( list1.size(), is(3) );
    assertThat( list1.get(0), is( "BLUB" ) );
    assertThat( list1.get(1), is( "FROG" ) );
    assertThat( list1.get(2), is( "BLA"  ) );

    list1.addAll( -1, "BLAU", "KRAUT" );
    assertThat( list1.size(), is( 5 ) );
    assertThat( list1.get(0), is( "BLUB"  ) );
    assertThat( list1.get(1), is( "FROG"  ) );
    assertThat( list1.get(2), is( "BLAU"  ) );
    assertThat( list1.get(3), is( "KRAUT" ) );
    assertThat( list1.get(4), is( "BLA"   ) );

  }
  
  @Test(groups="all")
  public void addSuppressNull() {
    
    ExtArrayList<String> list = new ExtArrayList<>( true );
    list.addAll( Arrays.asList( "BLA", "BLUB" ) );
    list.add( null );

    assertThat( list.size(), is( 2 ) );
    
  }
  
  @Test(groups="all")
  public void setSuppressNull() {
    
    ExtArrayList<String> list = new ExtArrayList<>( true );
    list.addAll( Arrays.asList( "BLA", "BLUB" ) );
    list.set( 0, null );

    assertThat( list.size(), is( 1 ) );
    
  }
  
  @Test(groups="all")
  public void sublist() {
    
    ExtArrayList<String> list1 = new ExtArrayList<>( "BLA", "BLUB", "BLAU", "KRAUT", "FROG" );
    List<String>         list2 = list1.subList( 1, -1 );
    assertThat( list2, is( notNullValue() ) );
    assertThat( list2.size(), is( 3 ) );
    assertThat( list2.get(0), is( "BLUB"  ) );
    assertThat( list2.get(1), is( "BLAU"  ) );
    assertThat( list2.get(2), is( "KRAUT" ) );

  }
  
  @Test(groups="all")
  public void trim() {
    
    ExtArrayList<String> list1 = new ExtArrayList<>( null, null, null, "BLA", "BLUB", "BLAU", "KRAUT", "FROG", null, "", null );
    list1.trim();
    assertThat( list1.size(), is( 7 ) );
    assertThat( list1.get(0), is( "BLA"   ) );
    assertThat( list1.get(1), is( "BLUB"  ) );
    assertThat( list1.get(2), is( "BLAU"  ) );
    assertThat( list1.get(3), is( "KRAUT" ) );
    assertThat( list1.get(4), is( "FROG"  ) );
    assertThat( list1.get(5), is( nullValue() ) );
    assertThat( list1.get(6), is( "" ) );
    
  }

  @Test(groups="all")
  public void newListSuppressNull() {
    
    ExtArrayList<String> list1 = new ExtArrayList<>( true );
    assertThat( list1.size(), is( 0 ) );
    
    ExtArrayList<String> list2 = new ExtArrayList<>( true, Arrays.asList( "BLA", null, null, "BLUB", null ) );
    assertThat( list2.size(), is( 2 ) );
    assertThat( list2.get(0), is( "BLA"  ) );
    assertThat( list2.get(1), is( "BLUB" ) );
    
    ExtArrayList<String> list3 = new ExtArrayList<String>( true, "BLA", null, null, "BLUB", null );
    assertThat( list3.size(), is( 2 ) );
    assertThat( list3.get(0), is( "BLA"  ) );
    assertThat( list3.get(1), is( "BLUB" ) );
    
  }

} /* ENDCLASS */
